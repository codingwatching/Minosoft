/*
 * Minosoft
 * Copyright (C) 2020-2023 Moritz Zwerger
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, see <https://www.gnu.org/licenses/>.
 *
 * This software is not affiliated with Mojang AB, the original developer of Minecraft.
 */

package de.bixilon.minosoft.gui.rendering.models

import de.bixilon.kutil.concurrent.pool.DefaultThreadPool
import de.bixilon.kutil.latch.CountUpAndDownLatch
import de.bixilon.minosoft.data.registries.fluid.Fluid
import de.bixilon.minosoft.data.registries.identified.ResourceLocation
import de.bixilon.minosoft.data.registries.registries.Registries
import de.bixilon.minosoft.gui.rendering.RenderContext
import de.bixilon.minosoft.gui.rendering.world.entities.DefaultEntityModels
import de.bixilon.minosoft.gui.rendering.world.entities.EntityModels
import de.bixilon.minosoft.util.logging.Log
import de.bixilon.minosoft.util.logging.LogLevels
import de.bixilon.minosoft.util.logging.LogMessageType

class ModelLoader(
    val context: RenderContext,
) {
    val entities = EntityModels(context)

    private val registry: Registries = context.connection.registries


    private fun loadFluid(fluid: Fluid) {
        if (fluid.model != null) {
            return
        }
        val model = fluid.createModel() ?: return
        fluid.model = model
        model.load(context)
    }


    private fun loadFluidModels() {
        Log.log(LogMessageType.VERSION_LOADING, LogLevels.VERBOSE) { "Loading fluid models..." }

        for (fluid in registry.fluid) {
            loadFluid(fluid)
        }
    }

    private fun loadEntityModels(latch: CountUpAndDownLatch) {
        Log.log(LogMessageType.VERSION_LOADING, LogLevels.VERBOSE) { "Loading entity models..." }
        val innerLatch = CountUpAndDownLatch(DefaultEntityModels.MODELS.size, latch)

        for (register in DefaultEntityModels.MODELS) {
            DefaultThreadPool += { register.register(context, this); innerLatch.dec() }
        }
        innerLatch.await()
    }

    fun load(latch: CountUpAndDownLatch) {
        loadFluidModels()

        loadEntityModels(latch)

        Log.log(LogMessageType.VERSION_LOADING, LogLevels.VERBOSE) { "Done loading models!" }

    }

    companion object {

        fun ResourceLocation.model(prefix: String? = null): ResourceLocation {
            var path = this.path
            if (prefix != null && !path.startsWith(prefix)) {
                path = prefix + path
            }
            return ResourceLocation(this.namespace, "models/$path.json")
        }

        fun ResourceLocation.blockState(): ResourceLocation {
            return ResourceLocation(this.namespace, "blockstates/" + this.path + ".json")
        }

        fun ResourceLocation.bbModel(): ResourceLocation {
            return ResourceLocation(this.namespace, "models/" + this.path + ".bbmodel")
        }
    }
}
