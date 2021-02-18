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

#version 330 core

out vec4 outColor;

in vec3 passTextureCoordinates;

uniform sampler2DArray blockTextureArray;

void main() {
    vec4 texColor = texture(blockTextureArray, passTextureCoordinates);
    if (texColor.a == 0) { // ToDo: This only works for alpha == 0. What about semi transparency? We would need to sort the faces, etc. See: https://learnopengl.com/Advanced-OpenGL/Blending
        discard;
    }
    outColor = texColor;
}
