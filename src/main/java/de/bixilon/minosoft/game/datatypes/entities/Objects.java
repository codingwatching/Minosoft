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

package de.bixilon.minosoft.game.datatypes.entities;

import de.bixilon.minosoft.game.datatypes.entities.objects.*;

public enum Objects implements EntityEnumInterface {
    BOAT(1, Boat.class),
    ITEM_STACK(2, ItemStack.class),
    MINECART(10, Minecart.class),
    PRIMED_TNT(50, PrimedTNT.class),
    ENDER_CRYSTAL(51, EnderCrystal.class),
    ARROW(60, Arrow.class),
    SNOWBALL(61, Snowball.class),
    EGG(62, Egg.class),
    FIRE_BALL(63, FireBall.class),
    FIRE_CHARGE(64, FireCharge.class),
    ENDER_PEARL(65, Enderpearl.class),
    WITHER_SKULL(66, WitherSkull.class),
    FALLING_BLOCK(70, FallingBlock.class),
    ITEM_FRAME(71, ItemFrame.class),
    EYE_OF_ENDER(72, EyeOfEnder.class),
    THROWN_POTION(73, ThrownPotion.class),
    FALLING_DRAGON_EGG(74, FallingDragonEgg.class),
    THROWN_EXP_BOTTLE(75, ThrownExpBottle.class),
    FIREWORK(76, Firework.class),
    LEASH_KNOT(77, LeashKnot.class),
    ARMOR_STAND(78, ArmorStand.class),
    FISHING_FLOAT(90, FishingFloat.class);

    final int type;
    final Class<? extends EntityObject> clazz;

    Objects(int type, Class<? extends EntityObject> clazz) {
        this.type = type;
        this.clazz = clazz;
    }


    public static Objects byType(int type) {
        for (Objects b : values()) {
            if (b.getType() == type) {
                return b;
            }
        }
        return null;
    }

    @Override
    public int getType() {
        return type;
    }

    @Override
    public Class<? extends EntityObject> getClazz() {
        return clazz;
    }
}
