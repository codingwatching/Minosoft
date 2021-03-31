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

package de.bixilon.minosoft.data.entities.entities;

import de.bixilon.minosoft.data.entities.EntityMetaDataFields;
import de.bixilon.minosoft.data.entities.EntityRotation;
import de.bixilon.minosoft.data.player.Hands;
import de.bixilon.minosoft.protocol.network.Connection;
import glm_.vec3.Vec3;
import glm_.vec3.Vec3i;

import javax.annotation.Nullable;

public abstract class LivingEntity extends Entity {

    public LivingEntity(Connection connection, Vec3 position, EntityRotation rotation) {
        super(connection, position, rotation);
    }

    private boolean getLivingEntityFlag(int bitMask) {
        return this.metaData.getSets().getBitMask(EntityMetaDataFields.LIVING_ENTITY_FLAGS, bitMask);
    }

    // = isUsingItem
    @EntityMetaDataFunction(name = "Is hand active")
    public boolean isHandActive() {
        return getLivingEntityFlag(0x01);
    }

    @EntityMetaDataFunction(name = "Main hand")
    public Hands getMainHand() {
        return getLivingEntityFlag(0x04) ? Hands.OFF_HAND : Hands.MAIN_HAND;
    }

    @EntityMetaDataFunction(name = "Is auto spin attack")
    public boolean isAutoSpinAttack() {
        return getLivingEntityFlag(0x04);
    }

    @EntityMetaDataFunction(name = "Health")
    public float getHealth() {
        return this.metaData.getSets().getFloat(EntityMetaDataFields.LIVING_ENTITY_HEALTH);
    }

    @EntityMetaDataFunction(name = "Effect color")
    public int getEffectColor() {
        return this.metaData.getSets().getInt(EntityMetaDataFields.LIVING_ENTITY_EFFECT_COLOR);
    }

    @EntityMetaDataFunction(name = "Is effect ambient")
    public boolean getEffectAmbient() {
        return this.metaData.getSets().getBoolean(EntityMetaDataFields.LIVING_ENTITY_EFFECT_AMBIENCE);
    }

    @EntityMetaDataFunction(name = "Arrows in entity")
    public int getArrowCount() {
        return this.metaData.getSets().getInt(EntityMetaDataFields.LIVING_ENTITY_ARROW_COUNT);
    }

    @EntityMetaDataFunction(name = "Absorption hearts")
    public int getAbsorptionHearts() {
        return this.metaData.getSets().getInt(EntityMetaDataFields.LIVING_ENTITY_ABSORPTION_HEARTS);
    }

    @EntityMetaDataFunction(name = "Bed location")
    @Nullable
    public Vec3i getBedPosition() {
        return this.metaData.getSets().getBlockPosition(EntityMetaDataFields.LIVING_ENTITY_BED_POSITION);
    }

}
