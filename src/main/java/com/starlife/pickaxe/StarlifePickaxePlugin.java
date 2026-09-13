package com.starlife.pickaxe;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public final class StarlifePickaxePlugin extends JavaPlugin {

    private NamespacedKey itemKey;
    private NamespacedKey recipeKey;

    @Override
    public void onEnable() {
        itemKey = new NamespacedKey(this, "starlife_pickaxe");
        recipeKey = new NamespacedKey(this, "starlife_pickaxe_recipe");
        registerRecipe();
        getLogger().info("Starlife Pickaxe enabled.");
    }

    @Override
    public void onDisable() {
        getLogger().info("Starlife Pickaxe disabled.");
    }

    private void registerRecipe() {
        Bukkit.removeRecipe(recipeKey);

        ItemStack result = createStarlifePickaxe();
        ShapedRecipe recipe = new ShapedRecipe(recipeKey, result);
        recipe.shape("SSS", " I ", " I ");
        recipe.setIngredient('S', new RecipeChoice.MaterialChoice(Material.NETHER_STAR));
        recipe.setIngredient('I', Material.STICK);

        if (!Bukkit.addRecipe(recipe)) {
            getLogger().warning("Could not register the Starlife Pickaxe recipe.");
        }
    }

    private ItemStack createStarlifePickaxe() {
        ItemStack pickaxe = new ItemStack(Material.NETHERITE_PICKAXE);
        ItemMeta meta = pickaxe.getItemMeta();

        meta.setDisplayName(ChatColor.AQUA + "Starlife Pickaxe");
        meta.setLore(List.of(
                ChatColor.GRAY + "Forged from three Nether Stars.",
                ChatColor.DARK_AQUA + "A pickaxe worthy of the stars."
        ));
        meta.getPersistentDataContainer().set(itemKey, PersistentDataType.BYTE, (byte) 1);
        pickaxe.setItemMeta(meta);
        return pickaxe;
    }
}
