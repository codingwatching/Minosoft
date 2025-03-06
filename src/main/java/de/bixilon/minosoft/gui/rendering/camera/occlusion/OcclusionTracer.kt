/*
 * Minosoft
 * Copyright (C) 2020-2025 Moritz Zwerger
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, see <https://www.gnu.org/licenses/>.
 *
 * This software is not affiliated with Mojang AB, the original developer of Minecraft.
 */

package de.bixilon.minosoft.gui.rendering.camera.occlusion

import de.bixilon.kotlinglm.vec2.Vec2i
import de.bixilon.minosoft.data.direction.DirectionVector
import de.bixilon.minosoft.data.direction.Directions
import de.bixilon.minosoft.data.registries.dimension.DimensionProperties
import de.bixilon.minosoft.data.world.chunk.chunk.Chunk
import de.bixilon.minosoft.data.world.chunk.neighbours.ChunkNeighbourArray
import de.bixilon.minosoft.data.world.container.block.SectionOcclusion
import de.bixilon.minosoft.data.world.positions.SectionHeight
import de.bixilon.minosoft.data.world.positions.SectionPosition
import de.bixilon.minosoft.gui.rendering.camera.Camera
import de.bixilon.minosoft.protocol.packets.s2c.play.block.chunk.ChunkUtil.isInViewDistance

class OcclusionTracer(
    val position: SectionPosition,
    size: Vec2i,
    dimension: DimensionProperties,
    camera: Camera,
) {
    val chunkPosition = position.chunkPosition
    private val frustum = camera.frustum
    private val viewDistance = camera.context.session.world.view.viewDistance

    private val minSection = dimension.minSection
    private val maxSection = dimension.maxSection

    private val skip = SectionPositionSet(chunkPosition, size, minSection, dimension.sections) // TODO: reuse
    private val visible = SectionPositionSet(chunkPosition, size, minSection, dimension.sections)


    private fun trace(chunk: Chunk, height: SectionHeight, direction: Directions?, vector: DirectionVector) {
        if (!chunk.position.isInViewDistance(this.viewDistance, this.chunkPosition)) return
        if (height < minSection || height > maxSection) return

        val position = SectionPosition.of(chunk.position, height)

        if (position in visible) return
        if (position in skip) return

        val section = chunk[height]
        if (section != null && section !in frustum) {
            skip += position
            return
        }
        visible += position


        val inverted = direction?.inverted
        val neighbours = chunk.neighbours.neighbours
        val occlusion = section?.blocks?.occlusion // TODO: empty section bypass?


        if (vector.x <= 0) trace(occlusion, neighbours, height, inverted, Directions.WEST, vector)
        if (vector.x >= 0) trace(occlusion, neighbours, height, inverted, Directions.EAST, vector)

        if (vector.z <= 0) trace(occlusion, neighbours, height, inverted, Directions.NORTH, vector)
        if (vector.z >= 0) trace(occlusion, neighbours, height, inverted, Directions.SOUTH, vector)

        if (vector.y <= 0) trace(occlusion, chunk, height, inverted, Directions.DOWN, vector)
        if (vector.y >= 0) trace(occlusion, chunk, height, inverted, Directions.UP, vector)
    }

    private inline fun trace(occlusion: SectionOcclusion?, neighbours: ChunkNeighbourArray, height: SectionHeight, source: Directions?, destination: Directions, vector: DirectionVector) {
        if (occlusion != null && source != null && occlusion.isOccluded(source, destination)) return
        val next = neighbours[destination] ?: return
        trace(next, height, destination, vector.with(destination))
    }

    private inline fun trace(occlusion: SectionOcclusion?, chunk: Chunk, height: SectionHeight, source: Directions?, destination: Directions, vector: DirectionVector) {
        if (occlusion != null && source != null && occlusion.isOccluded(source, destination)) return
        trace(chunk, height + destination.vector.y, destination, vector.with(destination))
    }

    fun trace(chunk: Chunk): OcclusionGraph {
        for (direction in Directions) {
            trace(chunk, position.y, null, direction.vector)
        }

        visible += position

        return OcclusionGraph(visible)
    }
}
