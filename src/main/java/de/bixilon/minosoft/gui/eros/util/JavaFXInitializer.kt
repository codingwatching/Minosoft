/*
 * Minosoft
 * Copyright (C) 2020-2024 Moritz Zwerger
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, see <https://www.gnu.org/licenses/>.
 *
 * This software is not affiliated with Mojang AB, the original developer of Minecraft.
 */

package de.bixilon.minosoft.gui.eros.util

import de.bixilon.kutil.concurrent.pool.DefaultThreadPool
import de.bixilon.kutil.concurrent.pool.runnable.ForcePooledRunnable
import de.bixilon.kutil.exception.ExceptionUtil.catchAll
import de.bixilon.kutil.latch.SimpleLatch
import de.bixilon.kutil.shutdown.ShutdownManager
import de.bixilon.minosoft.assets.IntegratedAssets
import de.bixilon.minosoft.assets.util.FileUtil.readImage
import de.bixilon.minosoft.gui.eros.crash.ErosCrashReport.Companion.crash
import de.bixilon.minosoft.util.logging.Log
import de.bixilon.minosoft.util.logging.LogLevels
import de.bixilon.minosoft.util.logging.LogMessageType
import de.bixilon.minosoft.util.system.SystemUtil
import javafx.application.Application
import javafx.application.Platform
import javafx.stage.Stage


class JavaFXInitializer internal constructor() : Application() {

    override fun start(stage: Stage) {
        Platform.setImplicitExit(false)

        JavaFXUtil.JAVA_FX_THREAD = Thread.currentThread()
        JavaFXUtil.HOST_SERVICES = hostServices
        SystemUtil.api = JavaFXSystemAPI()

        JavaFXUtil.MINOSOFT_LOGO = IntegratedAssets.DEFAULT[SystemUtil.ICON].readImage()

        Log.log(LogMessageType.JAVAFX, LogLevels.VERBOSE) { "Initialized JavaFX Toolkit!" }
        LATCH.dec()
        DefaultThreadPool += ForcePooledRunnable(priority = Int.MIN_VALUE) { JavaFXUtil.BIXILON_LOGO } // lazy value
    }

    companion object {
        private val LATCH = SimpleLatch(2)

        val initialized: Boolean
            get() = LATCH.count == 0

        val initializing: Boolean
            get() = LATCH.count >= 1

        @JvmStatic
        @Synchronized
        fun start() {
            check(initializing) { "Already initialized!" }
            Thread.setDefaultUncaughtExceptionHandler { _, exception ->
                if (ShutdownManager.shuttingDown) {
                    return@setDefaultUncaughtExceptionHandler
                }
                exception.printStackTrace(Log.FATAL_PRINT_STREAM)
                exception.crash()
            }

            Log.log(LogMessageType.JAVAFX, LogLevels.VERBOSE) { "Initializing JavaFX Toolkit..." }
            Thread({ launch(JavaFXInitializer::class.java) }, "JavaFX Toolkit Initializing Thread").start()
            LATCH.dec()
            catchAll { await() }
        }

        fun await() {
            LATCH.await()
        }
    }
}
