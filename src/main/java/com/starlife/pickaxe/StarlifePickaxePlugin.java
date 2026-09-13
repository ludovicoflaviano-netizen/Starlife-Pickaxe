package com.starlife.pickaxe;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public final class StarlifePickaxePlugin extends JavaPlugin implements Listener {
    private NamespacedKey itemKey;
    private NamespacedKey recipeKey;

    @Override
    public void onEnable() {
        itemKey = new NamespacedKey(this, "starlife_pickaxe");
        recipeKey = new NamespacedKey(this, "starlife_pickaxe_recipe");
        registerRecipe();
        Bukkit.getPluginManager().registerEvents(this, this);
        getLogger().info("Starlife Pickaxe enabled.");
    }

    private void registerRecipe() {
        Bukkit.removeRecipe(recipeKey);
        ShapedRecipe recipe = new ShapedRecipe(recipeKey, createStarlifePickaxe());
        recipe.shape("SSS", " I ", " I ");
        recipe.setIngredient('S', new RecipeChoice.MaterialChoice(Material.NETHER_STAR));
        recipe.setIngredient('I', Material.STICK);
        if (!Bukkit.addRecipe(recipe)) getLogger().warning("Could not register the Starlife Pickaxe recipe.");
    }

    private ItemStack createStarlifePickaxe() {
        ItemStack pickaxe = new ItemStack(Material.IRON_PICKAXE);
        ItemMeta meta = pickaxe.getItemMeta();
        meta.setDisplayName(ChatColor.AQUA + "Starlife Pickaxe");
        meta.setLore(List.of(
                ChatColor.GRAY + "Forged from three Nether Stars.",
                ChatColor.WHITE + "3x3 square mining",
                ChatColor.WHITE + "Netherite speed & durability"
        ));
        if (meta instanceof Damageable damageable) {
            damageable.setMaxDamage(2031);
        }
        meta.getPersistentDataContainer().set(itemKey, PersistentDataType.BYTE, (byte) 1);
        pickaxe.setItemMeta(meta);
        return pickaxe;
    }

    private boolean isStarlifePickaxe(ItemStack item) {
        if (item == null || item.getType() != Material.IRON_PICKAXE || !item.hasItemMeta()) return false;
        Byte value = item.getItemMeta().getPersistentDataContainer().get(itemKey, PersistentDataType.BYTE);
        return value != null && value == 1;
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        ItemStack tool = player.getInventory().getItemInMainHand();
        if (!isStarlifePickaxe(tool)) return;

        Block center = event.getBlock();
        BlockFace face = player.getTargetBlockFace(5);
        if (face == null) face = player.getFacing();

        BlockFace first;
        BlockFace second;
        switch (face) {
            case NORTH, SOUTH -> { first = BlockFace.EAST; second = BlockFace.UP; }
            case EAST, WEST -> { first = BlockFace.SOUTH; second = BlockFace.UP; }
            case UP, DOWN -> { first = BlockFace.EAST; second = BlockFace.SOUTH; }
            default -> { first = BlockFace.EAST; second = BlockFace.UP; }
        }

        // Always break exactly the 3x3 square centered on the original block.
        for (int a = -1; a <= 1; a++) {
            for (int b = -1; b <= 1; b++) {
                if (a == 0 && b == 0) continue;
                Block block = center.getRelative(first, a).getRelative(second, b);
                if (block.getType().isAir() || !block.getType().isBlock()) continue;
                if (block.getType().getHardness() < 0) continue;
                if (!block.breakNaturally(tool)) continue;
                block.getWorld().spawnParticle(Particle.WHITE_ASH,
                        block.getLocation().add(0.5, 0.5, 0.5), 10,
                        0.25, 0.25, 0.25, 0.01);
            }
        }

        center.getWorld().spawnParticle(Particle.WHITE_ASH,
                center.getLocation().add(0.5, 0.5, 0.5), 16,
                0.3, 0.3, 0.3, 0.01);
        center.getWorld().playSound(center.getLocation(), Sound.BLOCK_STONE_BREAK, 0.7f, 1.15f);
    }
}
