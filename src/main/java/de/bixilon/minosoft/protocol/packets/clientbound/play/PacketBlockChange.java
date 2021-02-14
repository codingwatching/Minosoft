/*
 * Minosoft
 * Copyright (C) 2020 Moritz Zwerger
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program.If not, see <https://www.gnu.org/licenses/>.
 *
 * This software is not affiliated with Mojang AB, the original developer of Minecraft.
 */

package de.bixilon.minosoft.protocol.packets.clientbound.play;

import de.bixilon.minosoft.data.mappings.blocks.Block;
import de.bixilon.minosoft.data.mappings.tweaker.VersionTweaker;
import de.bixilon.minosoft.data.world.BlockPosition;
import de.bixilon.minosoft.data.world.Chunk;
import de.bixilon.minosoft.data.world.ChunkSection;
import de.bixilon.minosoft.modding.event.events.BlockChangeEvent;
import de.bixilon.minosoft.protocol.network.Connection;
import de.bixilon.minosoft.protocol.packets.ClientboundPacket;
import de.bixilon.minosoft.protocol.protocol.InByteBuffer;
import de.bixilon.minosoft.util.logging.Log;

import static de.bixilon.minosoft.protocol.protocol.ProtocolVersions.V_14W03B;

public class PacketBlockChange extends ClientboundPacket {
    BlockPosition position;
    Block block;

    @Override
    public boolean read(InByteBuffer buffer) {
        if (buffer.getVersionId() < V_14W03B) {
            this.position = buffer.readBlockPositionByte();
            this.block = buffer.getConnection().getMapping().getBlock((buffer.readVarInt() << 4) | buffer.readByte()); // ToDo: When was the meta data "compacted"? (between 1.7.10 - 1.8)
            return true;
        }
        this.position = buffer.readPosition();
        this.block = buffer.getConnection().getMapping().getBlock(buffer.readVarInt());
        return true;

    }

    @Override
    public void handle(Connection connection) {
        Chunk chunk = connection.getPlayer().getWorld().getChunk(getPosition().getChunkLocation());
        if (chunk == null) {
            // thanks mojang
            return;
        }
        connection.fireEvent(new BlockChangeEvent(connection, this));

        int sectionHeight = getPosition().getSectionHeight();
        ChunkSection section = chunk.getSectionOrCreate(sectionHeight);

        // tweak
        if (!connection.getVersion().isFlattened()) {
            Block block = VersionTweaker.transformBlock(getBlock(), chunk, getPosition().getInChunkLocation());
            section.setBlock(getPosition().getInChunkLocation().getInChunkSectionLocation(), block);
        } else {
            Log.debug("Replacing %s with %s", section.getBlock(getPosition().getInChunkLocation().getInChunkSectionLocation()), this.block);
            section.setBlock(getPosition().getInChunkLocation().getInChunkSectionLocation(), getBlock());
        }

        connection.getRenderer().getRenderWindow().getChunkRenderer().prepareChunkSection(getPosition().getChunkLocation(), sectionHeight, section);
    }

    @Override
    public void log() {
        Log.protocol(String.format("[IN] Block change received at %s (block=%s)", this.position, this.block));
    }

    public BlockPosition getPosition() {
        return this.position;
    }

    public Block getBlock() {
        return this.block;
    }
}
