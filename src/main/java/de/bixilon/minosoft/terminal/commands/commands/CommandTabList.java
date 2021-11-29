/*
 * Minosoft
 * Copyright (C) 2020 Moritz Zwerger
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, see <https://www.gnu.org/licenses/>.
 *
 * This software is not affiliated with Mojang AB, the original developer of Minecraft.
 */

package de.bixilon.minosoft.terminal.commands.commands;

import com.github.freva.asciitable.AsciiTable;
import de.bixilon.minosoft.data.commands.CommandLiteralNode;
import de.bixilon.minosoft.data.commands.CommandNode;
import de.bixilon.minosoft.data.entities.entities.player.PlayerEntity;
import de.bixilon.minosoft.data.player.tab.TabListItem;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public class CommandTabList extends Command {
    @Override
    public CommandNode build(CommandNode parent) {
        parent.addChildren(
                new CommandLiteralNode("tab",
                        new CommandLiteralNode("list", (connection, stack) -> {
                            print(connection.getTabList().getHeader().getAnsiColoredMessage());

                            int entries = connection.getTabList().getTabListItemsByUUID().size();
                            int columns = (entries / 20) + 1;
                            if (columns > 4) {
                                columns = 4;
                            }
                            int rows = (entries / columns);
                            if (rows > 20) {
                                rows = 20;
                            }

                            ArrayList<Object[]> tableData = new ArrayList<>();

                            Iterator<TabListItem> tabListItemIterator = connection.getTabList().getTabListItemsByUUID().values().iterator();
                            for (int row = 0; row < rows; row++) {
                                ArrayList<Object> current = new ArrayList<>();
                                for (int column = 0; column < columns; column++) {
                                    if (tabListItemIterator.hasNext()) {
                                        current.add(tabListItemIterator.next().getDisplayName());
                                    } else {
                                        current.add(null);
                                    }
                                }
                                tableData.add(current.toArray());
                            }

                            // ToDo: we need to sort this, look at net.minecraft.client.gui.components.PlayerTabOverlay


                            print(AsciiTable.getTable(tableData.toArray(new Object[0][0])));

                            print(connection.getTabList().getFooter().getAnsiColoredMessage());

                        }, new CommandLiteralNode("all", (connection, stack) -> {
                            print(connection.getTabList().getHeader().getAnsiColoredMessage());

                            ArrayList<Object[]> tableData = new ArrayList<>();

                            for (Map.Entry<UUID, TabListItem> entry : connection.getTabList().getTabListItemsByUUID().entrySet()) {
                                PlayerEntity playerEntity = (PlayerEntity) connection.getWorld().getEntities().get(entry.getKey());
                                Integer entityId = playerEntity != null ? connection.getWorld().getEntities().getId(playerEntity) : null;
                                tableData.add(new Object[]{entry.getKey(), entityId, entry.getValue().getName(), entry.getValue().getDisplayName(), entry.getValue().getGamemode(), entry.getValue().getPing() + "ms"});
                            }

                            print(AsciiTable.getTable(new String[]{"UUID", "ENTITY ID", "PLAYER NAME", "DISPLAY NAME", "GAMEMODE", "PING"}, tableData.toArray(new Object[0][0])));

                            print(connection.getTabList().getFooter().getAnsiColoredMessage());
                        }))));
        return parent;
    }
}