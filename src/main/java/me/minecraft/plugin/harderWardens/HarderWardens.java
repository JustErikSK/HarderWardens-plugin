package me.minecraft.plugin.harderWardens;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Warden;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public final class HarderWardens extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_GREEN + "Harder Wardens >> Plugin has been enabled!");
        this.getServer().getPluginManager().registerEvents(this, this);
        this.saveDefaultConfig();

        getLogger().info("Harder Wardens is loading loot tables...");
        WardenLootManager.init();
        getLogger().info("Loot tables initialized!");

        FileConfiguration config = this.getConfig();
        config.addDefault("warden_difficulty", "NORMAL");
        config.addDefault("warden_health", 100);
        config.addDefault("warden_damage", 1.0);
        config.addDefault("warden_loot_option_1", 1);
        config.addDefault("warden_loot_option_2", 2);
    }

    public class WardenLootManager {
        // Easy difficulty loot array
        public static final List<ItemStack> easyCommon = new ArrayList<>();
        public static final List<ItemStack> easyRare = new ArrayList<>();
        // Normal difficulty loot array
        public static final List<ItemStack> normalCommon = new ArrayList<>();
        public static final List<ItemStack> normalRare = new ArrayList<>();
        // Hard difficulty loot array
        public static final List<ItemStack> hardCommon = new ArrayList<>();
        public static final List<ItemStack> hardRare = new ArrayList<>();
        // Nightmare difficulty loot array
        public static final List<ItemStack> nightmareCommon = new ArrayList<>();
        public static final List<ItemStack> nightmareRare = new ArrayList<>();
        // Insane difficulty loot array
        public static final List<ItemStack> insaneCommon = new ArrayList<>();
        public static final List<ItemStack> insaneRare = new ArrayList<>();

        public static void init() {
            // ==== EASY ====
            easyCommon.add(new ItemStack(Material.IRON_INGOT, 3));
            easyCommon.add(new ItemStack(Material.AMETHYST_SHARD, 4));
            easyCommon.add(new ItemStack(Material.LAPIS_LAZULI, 5));

            easyRare.add(new ItemStack(Material.DIAMOND, 1));
            easyRare.add(new ItemStack(Material.GOLDEN_APPLE, 1));

            // ==== NORMAL ====
            normalCommon.add(new ItemStack(Material.GOLD_INGOT, 4));
            normalCommon.add(new ItemStack(Material.REDSTONE, 6));
            normalCommon.add(new ItemStack(Material.EMERALD, 2));

            normalRare.add(new ItemStack(Material.NETHERITE_SCRAP, 1));
            normalRare.add(new ItemStack(Material.ENDER_PEARL, 2));

            // ==== HARD ====
            hardCommon.add(new ItemStack(Material.QUARTZ, 5));
            hardCommon.add(new ItemStack(Material.BLAZE_ROD, 2));
            hardCommon.add(new ItemStack(Material.DIAMOND, 2));

            hardRare.add(new ItemStack(Material.TOTEM_OF_UNDYING, 1));

            // ==== NIGHTMARE ====
            nightmareCommon.add(new ItemStack(Material.DIAMOND, 3));
            nightmareCommon.add(new ItemStack(Material.ENDER_PEARL, 3));
            nightmareCommon.add(new ItemStack(Material.BLAZE_POWDER, 2));

            nightmareRare.add(new ItemStack(Material.NETHERITE_SCRAP, 2));
            nightmareRare.add(new ItemStack(Material.ENCHANTED_GOLDEN_APPLE, 1));

            // ==== INSANE ====
            insaneCommon.add(new ItemStack(Material.NETHERITE_SCRAP, 3));
            insaneCommon.add(new ItemStack(Material.END_CRYSTAL, 1));
            insaneCommon.add(new ItemStack(Material.GHAST_TEAR, 2));

            insaneRare.add(new ItemStack(Material.NETHERITE_INGOT, 1));
            insaneRare.add(new ItemStack(Material.TOTEM_OF_UNDYING, 1));

            // ==== ENCHANTED BOOKS, ARMOR AND WEAPONS ====
            // Unbreaking 1 Book
            ItemStack bookUnbreaking1 = new ItemStack(Material.ENCHANTED_BOOK, 1);
            EnchantmentStorageMeta metaUnbreaking1 = (EnchantmentStorageMeta) bookUnbreaking1.getItemMeta();
            if (metaUnbreaking1 != null) {
                metaUnbreaking1.addStoredEnchant(Enchantment.UNBREAKING, 1, true);
                bookUnbreaking1.setItemMeta(metaUnbreaking1);
            }
            easyRare.add(bookUnbreaking1);

            // Protection 1 Book
            ItemStack bookProtection1 = new ItemStack(Material.ENCHANTED_BOOK, 1);
            EnchantmentStorageMeta metaProtection1 = (EnchantmentStorageMeta) bookProtection1.getItemMeta();
            if (metaProtection1 != null) {
                metaProtection1.addStoredEnchant(Enchantment.PROTECTION, 1, true);
                bookProtection1.setItemMeta(metaProtection1);
            }
            easyRare.add(bookProtection1);
        }
    }

    @EventHandler
    public void wardenSpawnEvent(EntitySpawnEvent e) {

        String warden_difficulty = this.getConfig().getString("warden_difficulty", "NORMAL");
        if (e.getEntity() instanceof LivingEntity ent) {
            if (ent.getType() == EntityType.WARDEN) {
                if (warden_difficulty.equals("EASY")) { // 100HP (50 hearts) on easy difficulty
                    ent.setCustomName("Echo Lurker"); // Warden's name on easy difficulty is Echo Lurker
                    ent.setCustomNameVisible(false);
                    ent.setPersistent(ent.isCustomNameVisible());
                    ent.setMaxHealth(100);
                    ent.setHealth(100);
                } else if (warden_difficulty.equals("NORMAL")) { // 250HP (125 hearts) on normal difficulty
                    ent.setCustomName("Abyss Killer"); // Warden's name on normal difficulty is Abyss Killer
                    ent.setCustomNameVisible(false);
                    ent.setPersistent(ent.isCustomNameVisible());
                    ent.setMaxHealth(250);
                    ent.setHealth(250);
                } else if (warden_difficulty.equals("HARD")) { // 500HP (250 hearts) on hard difficulty
                    ent.setCustomName("Void Reaper"); // Warden's name on hard difficulty is Void Reaper
                    ent.setCustomNameVisible(false);
                    ent.setPersistent(ent.isCustomNameVisible());
                    ent.setMaxHealth(500);
                    ent.setHealth(500);
                } else if (warden_difficulty.equals("NIGHTMARE")) { // 900HP (450 hearts) on nightmare difficulty
                    ent.setCustomName("Nightmare Sentinel"); // Warden's name on hard difficulty is Nightmare Sentinel
                    ent.setCustomNameVisible(false);
                    ent.setPersistent(ent.isCustomNameVisible());
                    ent.setMaxHealth(900);
                    ent.setHealth(900);
                } else if (warden_difficulty.equals("INSANE")) { // 1200HP (600 hearts) on insane difficulty
                    ent.setCustomName("Abyssal Devourer"); // Warden's name on hard difficulty is Abyssal Devourer
                    ent.setCustomNameVisible(false);
                    ent.setPersistent(ent.isCustomNameVisible());
                    ent.setMaxHealth(1200);
                    ent.setHealth(1200);
                } else { // if difficulty not set correctly, default difficulty (normal) will be used
                    ent.setCustomName("Abyss Killer");
                    ent.setCustomNameVisible(false);
                    ent.setPersistent(ent.isCustomNameVisible());
                    ent.setMaxHealth(250);
                    ent.setHealth(250);
                }
            }
        }
    }

    @EventHandler
    public void wardenAttackEvent(EntityDamageByEntityEvent e) {
        String warden_difficulty = this.getConfig().getString("warden_difficulty", "NORMAL");
        if (e.getDamager() instanceof Warden) {
            if (warden_difficulty.equals("EASY")) { // 0.5x damage on easy difficulty
                double originalDamage = e.getDamage();
                double newDamage = originalDamage * 0.5;
                e.setDamage(newDamage);
            } else if (warden_difficulty.equals("NORMAL")) { // 1.5x damage on normal difficulty
                double originalDamage = e.getDamage();
                double newDamage = originalDamage * 1.5;
                e.setDamage(newDamage);
            } else if (warden_difficulty.equals("HARD")) { // 2.5x damage on hard difficulty
                double originalDamage = e.getDamage();
                double newDamage = originalDamage * 2.5;
                e.setDamage(newDamage);
            } else if (warden_difficulty.equals("NIGHTMARE")) { // 3.5x damage on nightmare difficulty
                double originalDamage = e.getDamage();
                double newDamage = originalDamage * 3.5;
                e.setDamage(newDamage);
            } else if (warden_difficulty.equals("INSANE")) { // 4.5x damage on insane difficulty
                double originalDamage = e.getDamage();
                double newDamage = originalDamage * 4.5;
                e.setDamage(newDamage);
            } else { // if difficulty not set correctly, default difficulty (normal) will be used
                double originalDamage = e.getDamage();
                double newDamage = originalDamage * 1.5;
                e.setDamage(newDamage);
            }
        }
    }

    @EventHandler
    public void wardenDeathEvent(EntityDeathEvent e) {

    }
}
