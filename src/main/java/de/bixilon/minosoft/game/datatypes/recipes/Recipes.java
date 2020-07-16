package de.bixilon.minosoft.game.datatypes.recipes;
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

import com.google.common.collect.HashBiMap;
import de.bixilon.minosoft.game.datatypes.inventory.Slot;
import de.bixilon.minosoft.protocol.protocol.ProtocolVersion;

import java.util.ArrayList;
import java.util.HashMap;

public class Recipes {
    static ArrayList<Recipe> recipeList = new ArrayList<>();
    static HashBiMap<Integer, Recipe> recipeIdMap = HashBiMap.create(); // ids for version <= VERSION_1_12_2
    static HashMap<ProtocolVersion, HashBiMap<String, Recipe>> recipeNameMap = new HashMap<>(); // names for version >= VERSION_1_13_2

    static {
        for (ProtocolVersion version : ProtocolVersion.versionMappingArray) {
            if (version.getVersionNumber() >= ProtocolVersion.VERSION_1_13_2.getVersionNumber()) {
                // add to list
                recipeNameMap.put(version, HashBiMap.create());
            }
        }
    }


    public static Recipe getRecipeById(int id) {
        return recipeIdMap.get(id);
    }

    public static Recipe getRecipe(String identifier, ProtocolVersion version) {
        return recipeNameMap.get(version).get(identifier);
    }

    public static Recipe getRecipe(RecipeProperties property, Slot result, String group, Ingredient[] ingredients) {
        for (Recipe recipe : recipeList) {
            if (recipe.getProperty() == property && recipe.getResult().equals(result) && recipe.getGroup().equals(group) && ingredientsEquals(recipe.getIngredients(), ingredients)) {
                return recipe;
            }
        }
        return null;
    }

    public static boolean ingredientsEquals(Ingredient[] one, Ingredient[] two) {
        if (one.length != two.length) {
            return false;
        }
        for (Ingredient ingredient : one) {
            if (!containsElement(two, ingredient)) {
                return false;
            }
        }
        return true;
    }

    public static boolean containsElement(Ingredient[] arr, Ingredient value) {
        for (Ingredient property : arr) {
            if (property.equals(value)) {
                return true;
            }
        }
        return false;
    }

    // we don't want that recipes from 1 server will appear on an other. You must call this function before reconnecting do avoid issues
    public static void removeCustomRecipes(ProtocolVersion version) {
        recipeNameMap.get(version).clear();
    }

    public static void registerCustomRecipe(ProtocolVersion version, Recipe recipe, String identifier) {
        recipeNameMap.get(version).put(identifier, recipe);
    }

}
