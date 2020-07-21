/*
 * Codename Minosoft
 * Copyright (C) 2020 Moritz Zwerger
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 *  This software is not affiliated with Mojang AB, the original developer of Minecraft.
 */

package de.bixilon.minosoft.protocol.protocol.versions;


import de.bixilon.minosoft.protocol.protocol.Packets;
import de.bixilon.minosoft.protocol.protocol.Protocol;

public class Protocol_1_14_4 extends Protocol {

    public Protocol_1_14_4() {
        super();
        clientboundPacketMapping.put(Packets.Clientbound.LOGIN_SET_COMPRESSION, 0x03);
        clientboundPacketMapping.put(Packets.Clientbound.LOGIN_PLUGIN_REQUEST, 0x04);

        clientboundPacketMapping.put(Packets.Clientbound.PLAY_SPAWN_OBJECT, 0x00);
        clientboundPacketMapping.put(Packets.Clientbound.PLAY_SPAWN_EXPERIENCE_ORB, 0x01);
        clientboundPacketMapping.put(Packets.Clientbound.PLAY_SPAWN_WEATHER_ENTITY, 0x02);
        clientboundPacketMapping.put(Packets.Clientbound.PLAY_SPAWN_MOB, 0x03);
        clientboundPacketMapping.put(Packets.Clientbound.PLAY_SPAWN_PAINTING, 0x04);
        clientboundPacketMapping.put(Packets.Clientbound.PLAY_SPAWN_PLAYER, 0x05);
        clientboundPacketMapping.put(Packets.Clientbound.PLAY_ENTITY_ANIMATION, 0x06);
        clientboundPacketMapping.put(Packets.Clientbound.PLAY_STATISTICS, 0x07);
        clientboundPacketMapping.put(Packets.Clientbound.PLAY_BLOCK_BREAK_ANIMATION, 0x08);
        clientboundPacketMapping.put(Packets.Clientbound.PLAY_BLOCK_ENTITY_DATA, 0x09);
        clientboundPacketMapping.put(Packets.Clientbound.PLAY_BLOCK_ACTION, 0x0A);
        clientboundPacketMapping.put(Packets.Clientbound.PLAY_BLOCK_CHANGE, 0x0B);
        clientboundPacketMapping.put(Packets.Clientbound.PLAY_BOSS_BAR, 0x0C);
        clientboundPacketMapping.put(Packets.Clientbound.PLAY_SERVER_DIFFICULTY, 0x0D);
        clientboundPacketMapping.put(Packets.Clientbound.PLAY_CHAT_MESSAGE, 0x0E);
        clientboundPacketMapping.put(Packets.Clientbound.PLAY_MULTIBLOCK_CHANGE, 0x0F);
        clientboundPacketMapping.put(Packets.Clientbound.PLAY_TAB_COMPLETE, 0x10);
        clientboundPacketMapping.put(Packets.Clientbound.PLAY_DECLARE_COMMANDS, 0x11);
        clientboundPacketMapping.put(Packets.Clientbound.PLAY_WINDOW_CONFIRMATION, 0x12);
        clientboundPacketMapping.put(Packets.Clientbound.PLAY_CLOSE_WINDOW, 0x13);
        clientboundPacketMapping.put(Packets.Clientbound.PLAY_WINDOW_ITEMS, 0x14);
        clientboundPacketMapping.put(Packets.Clientbound.PLAY_WINDOW_PROPERTY, 0x15);
        clientboundPacketMapping.put(Packets.Clientbound.PLAY_SET_SLOT, 0x16);
        clientboundPacketMapping.put(Packets.Clientbound.PLAY_SET_COOLDOWN, 0x17);
        clientboundPacketMapping.put(Packets.Clientbound.PLAY_PLUGIN_MESSAGE, 0x18);
        clientboundPacketMapping.put(Packets.Clientbound.PLAY_NAMED_SOUND_EFFECT, 0x19);
        clientboundPacketMapping.put(Packets.Clientbound.PLAY_DISCONNECT, 0x1AA);
        clientboundPacketMapping.put(Packets.Clientbound.PLAY_ENTITY_STATUS, 0x1B);
        clientboundPacketMapping.put(Packets.Clientbound.PLAY_EXPLOSION, 0x1C);
        clientboundPacketMapping.put(Packets.Clientbound.PLAY_UNLOAD_CHUNK, 0x1D);
        clientboundPacketMapping.put(Packets.Clientbound.PLAY_CHANGE_GAME_STATE, 0x1E);
        clientboundPacketMapping.put(Packets.Clientbound.PLAY_OPEN_HORSE_WINDOW, 0x1F);
        clientboundPacketMapping.put(Packets.Clientbound.PLAY_KEEP_ALIVE, 0x20);
        clientboundPacketMapping.put(Packets.Clientbound.PLAY_CHUNK_DATA, 0x21);
        clientboundPacketMapping.put(Packets.Clientbound.PLAY_EFFECT, 0x22);
        clientboundPacketMapping.put(Packets.Clientbound.PLAY_PARTICLE, 0x23);
        clientboundPacketMapping.put(Packets.Clientbound.PLAY_UPDATE_LIGHT, 0x24);
        clientboundPacketMapping.put(Packets.Clientbound.PLAY_JOIN_GAME, 0x25);
        clientboundPacketMapping.put(Packets.Clientbound.PLAY_MAP_DATA, 0x26);
        clientboundPacketMapping.put(Packets.Clientbound.PLAY_TRADE_LIST, 0x27);
        clientboundPacketMapping.put(Packets.Clientbound.PLAY_ENTITY_MOVEMENT, 0x28);
        clientboundPacketMapping.put(Packets.Clientbound.PLAY_ENTITY_MOVEMENT_AND_ROTATION, 0x29);
        clientboundPacketMapping.put(Packets.Clientbound.PLAY_ENTITY_ROTATION, 0x2A);
        clientboundPacketMapping.put(Packets.Clientbound.PLAY_VEHICLE_MOVEMENT, 0x2C);
        clientboundPacketMapping.put(Packets.Clientbound.PLAY_OPEN_BOOK, 0x2D);
        clientboundPacketMapping.put(Packets.Clientbound.PLAY_OPEN_WINDOW, 0x2E);
        clientboundPacketMapping.put(Packets.Clientbound.PLAY_OPEN_SIGN_EDITOR, 0x2F);
        clientboundPacketMapping.put(Packets.Clientbound.PLAY_CRAFT_RECIPE_RESPONSE, 0x30);
        clientboundPacketMapping.put(Packets.Clientbound.PLAY_PLAYER_ABILITIES, 0x31);
        clientboundPacketMapping.put(Packets.Clientbound.PLAY_COMBAT_EVENT, 0x32);
        clientboundPacketMapping.put(Packets.Clientbound.PLAY_PLAYER_INFO, 0x33);
        clientboundPacketMapping.put(Packets.Clientbound.PLAY_FACE_PLAYER, 0x34);
        clientboundPacketMapping.put(Packets.Clientbound.PLAY_PLAYER_POSITION_AND_ROTATION, 0x35);
        clientboundPacketMapping.put(Packets.Clientbound.PLAY_UNLOCK_RECIPES, 0x36);
        clientboundPacketMapping.put(Packets.Clientbound.PLAY_DESTROY_ENTITIES, 0x37);
        clientboundPacketMapping.put(Packets.Clientbound.PLAY_REMOVE_ENTITY_EFFECT, 0x38);
        clientboundPacketMapping.put(Packets.Clientbound.PLAY_RESOURCE_PACK_SEND, 0x39);
        clientboundPacketMapping.put(Packets.Clientbound.PLAY_RESPAWN, 0x3A);
        clientboundPacketMapping.put(Packets.Clientbound.PLAY_ENTITY_HEAD_ROTATION, 0x3B);
        clientboundPacketMapping.put(Packets.Clientbound.PLAY_SELECT_ADVANCEMENT_TAB, 0x3C);
        clientboundPacketMapping.put(Packets.Clientbound.PLAY_WORLD_BORDER, 0x3D);
        clientboundPacketMapping.put(Packets.Clientbound.PLAY_CAMERA, 0x3E);
        clientboundPacketMapping.put(Packets.Clientbound.PLAY_HELD_ITEM_CHANGE, 0x3F);
        clientboundPacketMapping.put(Packets.Clientbound.PLAY_UPDATE_VIEW_POSITION, 0x40);
        clientboundPacketMapping.put(Packets.Clientbound.PLAY_UPDATE_VIEW_DISTANCE, 0x41);
        clientboundPacketMapping.put(Packets.Clientbound.PLAY_DISPLAY_SCOREBOARD, 0x42);
        clientboundPacketMapping.put(Packets.Clientbound.PLAY_ENTITY_METADATA, 0x43);
        clientboundPacketMapping.put(Packets.Clientbound.PLAY_ATTACH_ENTITY, 0x44);
        clientboundPacketMapping.put(Packets.Clientbound.PLAY_ENTITY_VELOCITY, 0x45);
        clientboundPacketMapping.put(Packets.Clientbound.PLAY_ENTITY_EQUIPMENT, 0x46);
        clientboundPacketMapping.put(Packets.Clientbound.PLAY_SET_EXPERIENCE, 0x47);
        clientboundPacketMapping.put(Packets.Clientbound.PLAY_UPDATE_HEALTH, 0x48);
        clientboundPacketMapping.put(Packets.Clientbound.PLAY_SCOREBOARD_OBJECTIVE, 0x49);
        clientboundPacketMapping.put(Packets.Clientbound.PLAY_SET_PASSENGERS, 0x4A);
        clientboundPacketMapping.put(Packets.Clientbound.PLAY_TEAMS, 0x4B);
        clientboundPacketMapping.put(Packets.Clientbound.PLAY_UPDATE_SCORE, 0x4C);
        clientboundPacketMapping.put(Packets.Clientbound.PLAY_SPAWN_POSITION, 0x4D);
        clientboundPacketMapping.put(Packets.Clientbound.PLAY_TITLE, 0x4F);
        clientboundPacketMapping.put(Packets.Clientbound.PLAY_ENTITY_SOUND_EFFECT, 0x50);
        clientboundPacketMapping.put(Packets.Clientbound.PLAY_SOUND_EFFECT, 0x51);
        clientboundPacketMapping.put(Packets.Clientbound.PLAY_STOP_SOUND, 0x52);
        clientboundPacketMapping.put(Packets.Clientbound.PLAY_LIST_HEADER_AND_FOOTER, 0x53);
        clientboundPacketMapping.put(Packets.Clientbound.PLAY_NBT_QUERY_RESPONSE, 0x54);
        clientboundPacketMapping.put(Packets.Clientbound.PLAY_ENTITY_TELEPORT, 0x56);
        clientboundPacketMapping.put(Packets.Clientbound.PLAY_ADVANCEMENTS, 0x57);
        clientboundPacketMapping.put(Packets.Clientbound.PLAY_ENTITY_PROPERTIES, 0x58);
        clientboundPacketMapping.put(Packets.Clientbound.PLAY_ENTITY_EFFECT, 0x59);
        clientboundPacketMapping.put(Packets.Clientbound.PLAY_DECLARE_RECIPES, 0x5A);
        clientboundPacketMapping.put(Packets.Clientbound.PLAY_TAGS, 0x5B);
        clientboundPacketMapping.put(Packets.Clientbound.PLAY_ACKNOWLEDGE_PLAYER_DIGGING, 0x5C);


        serverboundPacketMapping.put(Packets.Serverbound.LOGIN_PLUGIN_RESPONSE, 0x02);

        serverboundPacketMapping.put(Packets.Serverbound.PLAY_TELEPORT_CONFIRM, 0x00);
        serverboundPacketMapping.put(Packets.Serverbound.PLAY_QUERY_BLOCK_NBT, 0x01);
        serverboundPacketMapping.put(Packets.Serverbound.PLAY_SET_DIFFICULTY, 0x02);
        serverboundPacketMapping.put(Packets.Serverbound.PLAY_CHAT_MESSAGE, 0x03);
        serverboundPacketMapping.put(Packets.Serverbound.PLAY_CLIENT_STATUS, 0x04);
        serverboundPacketMapping.put(Packets.Serverbound.PLAY_CLIENT_SETTINGS, 0x05);
        serverboundPacketMapping.put(Packets.Serverbound.PLAY_TAB_COMPLETE, 0x06);
        serverboundPacketMapping.put(Packets.Serverbound.PLAY_WINDOW_CONFIRMATION, 0x07);
        serverboundPacketMapping.put(Packets.Serverbound.PLAY_CLICK_WINDOW_BUTTON, 0x08);
        serverboundPacketMapping.put(Packets.Serverbound.PLAY_CLICK_WINDOW, 0x09);
        serverboundPacketMapping.put(Packets.Serverbound.PLAY_CLOSE_WINDOW, 0x0A);
        serverboundPacketMapping.put(Packets.Serverbound.PLAY_PLUGIN_MESSAGE, 0x0B);
        serverboundPacketMapping.put(Packets.Serverbound.PLAY_EDIT_BOOK, 0x0C);
        serverboundPacketMapping.put(Packets.Serverbound.PLAY_ENTITY_NBT_REQUEST, 0x0D);
        serverboundPacketMapping.put(Packets.Serverbound.PLAY_INTERACT_ENTITY, 0x0E);
        serverboundPacketMapping.put(Packets.Serverbound.PLAY_KEEP_ALIVE, 0x0F);
        serverboundPacketMapping.put(Packets.Serverbound.PLAY_LOCK_DIFFICULTY, 0x10);
        serverboundPacketMapping.put(Packets.Serverbound.PLAY_PLAYER_POSITION, 0x11);
        serverboundPacketMapping.put(Packets.Serverbound.PLAY_PLAYER_POSITION_AND_ROTATION, 0x12);
        serverboundPacketMapping.put(Packets.Serverbound.PLAY_PLAYER_ROTATION, 0x13);
        serverboundPacketMapping.put(Packets.Serverbound.PLAY_VEHICLE_MOVE, 0x15);
        serverboundPacketMapping.put(Packets.Serverbound.PLAY_STEER_BOAT, 0x16);
        serverboundPacketMapping.put(Packets.Serverbound.PLAY_PICK_ITEM, 0x17);
        serverboundPacketMapping.put(Packets.Serverbound.PLAY_CRAFT_RECIPE_REQUEST, 0x18);
        serverboundPacketMapping.put(Packets.Serverbound.PLAY_PLAYER_ABILITIES, 0x19);
        serverboundPacketMapping.put(Packets.Serverbound.PLAY_PLAYER_DIGGING, 0x1A);
        serverboundPacketMapping.put(Packets.Serverbound.PLAY_ENTITY_ACTION, 0x1B);
        serverboundPacketMapping.put(Packets.Serverbound.PLAY_STEER_VEHICLE, 0x1C);
        serverboundPacketMapping.put(Packets.Serverbound.PLAY_RECIPE_BOOK_DATA, 0x1D);
        serverboundPacketMapping.put(Packets.Serverbound.PLAY_NAME_ITEM, 0x1E);
        serverboundPacketMapping.put(Packets.Serverbound.PLAY_RESOURCE_PACK_STATUS, 0x1F);
        serverboundPacketMapping.put(Packets.Serverbound.PLAY_ADVANCEMENT_TAB, 0x20);
        serverboundPacketMapping.put(Packets.Serverbound.PLAY_SELECT_TRADE, 0x21);
        serverboundPacketMapping.put(Packets.Serverbound.PLAY_SET_BEACON_EFFECT, 0x22);
        serverboundPacketMapping.put(Packets.Serverbound.PLAY_HELD_ITEM_CHANGE, 0x23);
        serverboundPacketMapping.put(Packets.Serverbound.PLAY_UPDATE_COMMAND_BLOCK, 0x24);
        serverboundPacketMapping.put(Packets.Serverbound.PLAY_UPDATE_COMMAND_BLOCK_MINECART, 0x25);
        serverboundPacketMapping.put(Packets.Serverbound.PLAY_CREATIVE_INVENTORY_ACTION, 0x26);
        serverboundPacketMapping.put(Packets.Serverbound.PLAY_UPDATE_JIGSAW_BLOCK, 0x27);
        serverboundPacketMapping.put(Packets.Serverbound.PLAY_UPDATE_STRUCTURE_BLOCK, 0x28);
        serverboundPacketMapping.put(Packets.Serverbound.PLAY_UPDATE_SIGN, 0x29);
        serverboundPacketMapping.put(Packets.Serverbound.PLAY_ANIMATION, 0x2A);
        serverboundPacketMapping.put(Packets.Serverbound.PLAY_SPECTATE, 0x2B);
        serverboundPacketMapping.put(Packets.Serverbound.PLAY_PLAYER_BLOCK_PLACEMENT, 0x2C);
        serverboundPacketMapping.put(Packets.Serverbound.PLAY_USE_ITEM, 0x2D);

    }

    public int getProtocolVersionNumber() {
        return 498;
    }

    @Override
    public String getVersionString() {
        return "1.14.4";
    }

    @Override
    public String getReleaseName() {
        return "Village and Pillage";
    }
}
