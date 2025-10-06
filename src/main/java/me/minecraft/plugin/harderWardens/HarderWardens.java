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

import java.util.Collections;
import java.util.Random;

public final class HarderWardens extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_GREEN + "HarderWardens >> Plugin has been enabled!");
        this.getServer().getPluginManager().registerEvents(this, this);
        this.saveDefaultConfig();

        FileConfiguration config = this.getConfig();
        config.addDefault("warden_difficulty", "NORMAL");
        config.addDefault("warden_health", 100);
        config.addDefault("warden_damage", 1.0);
        config.addDefault("warden_loot_option_1", 1);
        config.addDefault("warden_loot_option_2", 2);
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
    public void wardenDeathEvent(EntityDeathEvent e) {
/*
        LivingEntity ent = e.getEntity();
        Random ran = new Random();
        int num = ran.nextInt(100);

        String warden_difficulty = this.getConfig().getString("warden_difficulty", "NORMAL");
        if (ent.getType() == EntityType.WARDEN) {
            e.getDrops().clear();
            if (warden_difficulty.equals("EASY")) { // Loot from wardens on easy difficulty (Tier 1 drops)

                e.getDrops().add(new ItemStack(Material.IRON_INGOT, 12)); // 12 Iron Ingots
                e.getDrops().add(new ItemStack(Material.GOLD_INGOT, 12)); // 12 Gold Ingots

                ItemStack low_level_book1 = new ItemStack(Material.ENCHANTED_BOOK, 1);
                EnchantmentStorageMeta meta1 = (EnchantmentStorageMeta)low_level_book1.getItemMeta();
                meta1.addStoredEnchant(Enchantment.SWIFT_SNEAK, 1, true);
                low_level_book1.setItemMeta(meta1);
                e.getDrops().add(low_level_book1); // Swift Sneak 1 Book

                ItemStack low_level_book2 = new ItemStack(Material.ENCHANTED_BOOK, 1);
                EnchantmentStorageMeta meta2 = (EnchantmentStorageMeta)low_level_book2.getItemMeta();
                meta2.addStoredEnchant(Enchantment.UNBREAKING, 1, true);
                low_level_book2.setItemMeta(meta2);
                e.getDrops().add(low_level_book2); // Unbreaking 1 Book

                ItemStack fragment = new ItemStack(Material.CHARCOAL, 1);
                ItemMeta itemStackMeta = fragment.getItemMeta();
                itemStackMeta.setDisplayName(ChatColor.DARK_PURPLE + "Warden's Fragment");
                itemStackMeta.setLore(Collections.singletonList(ChatColor.DARK_GREEN + "A custom drop from Warden. Doesn't have any uses yet."));
                itemStackMeta.addEnchant(Enchantment.INFINITY, 1, true);
                itemStackMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                fragment.setItemMeta(itemStackMeta);
                e.getDrops().add(fragment); // Custom item for easy difficulty wardens - Warden's Fragment

            } else if (warden_difficulty.equals("NORMAL")) { //  Loot from wardens on normal difficulty (Tier 2 drops)

                e.getDrops().add(new ItemStack(Material.NETHERITE_SCRAP, 2)); // 2 Netherite Scraps
                e.getDrops().add(new ItemStack(Material.DIAMOND, 5)); // 5 Diamonds

                ItemStack mid_level_book1 = new ItemStack(Material.ENCHANTED_BOOK, 1);
                EnchantmentStorageMeta meta1 = (EnchantmentStorageMeta)mid_level_book1.getItemMeta();
                meta1.addStoredEnchant(Enchantment.SWIFT_SNEAK, 2, true);
                mid_level_book1.setItemMeta(meta1);
                e.getDrops().add(mid_level_book1); // Swift Sneak 2 Book

                ItemStack mid_level_book2 = new ItemStack(Material.ENCHANTED_BOOK, 1);
                EnchantmentStorageMeta meta2 = (EnchantmentStorageMeta)mid_level_book2.getItemMeta();
                meta2.addStoredEnchant(Enchantment.PROTECTION, 2, true);
                mid_level_book2.setItemMeta(meta2);
                e.getDrops().add(mid_level_book2); // Protection 2 Book

                ItemStack fragment = new ItemStack(Material.ECHO_SHARD, 1);
                ItemMeta itemStackMeta = fragment.getItemMeta();
                itemStackMeta.setDisplayName(ChatColor.DARK_PURPLE + "Dark Essence");
                itemStackMeta.setLore(Collections.singletonList(ChatColor.DARK_GREEN + "A custom drop from Warden. Doesn't have any uses yet."));
                itemStackMeta.addEnchant(Enchantment.INFINITY, 1, true);
                itemStackMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                fragment.setItemMeta(itemStackMeta);
                e.getDrops().add(fragment); // Custom item for normal difficulty wardens - Dark Essence

            } else if (warden_difficulty.equals("HARD")) { // Loot from wardens on hard difficulty (Tier 3 drops)

                ItemStack enc_dia_chestplate = new ItemStack(Material.DIAMOND_CHESTPLATE, 1);
                ItemMeta itemStackMeta1 = enc_dia_chestplate.getItemMeta();
                itemStackMeta1.addEnchant(Enchantment.PROTECTION, 4, true);
                itemStackMeta1.addEnchant(Enchantment.UNBREAKING, 3, true);
                enc_dia_chestplate.setItemMeta(itemStackMeta1);
                e.getDrops().add(enc_dia_chestplate); // Enchanted Diamond Chestplate

                ItemStack high_level_book1 = new ItemStack(Material.ENCHANTED_BOOK, 1);
                EnchantmentStorageMeta meta1 = (EnchantmentStorageMeta)high_level_book1.getItemMeta();
                meta1.addStoredEnchant(Enchantment.SWIFT_SNEAK, 3, true);
                high_level_book1.setItemMeta(meta1);
                e.getDrops().add(high_level_book1); // Swift Sneak 3 Book

                ItemStack high_level_book2 = new ItemStack(Material.ENCHANTED_BOOK, 1);
                EnchantmentStorageMeta meta2 = (EnchantmentStorageMeta)high_level_book2.getItemMeta();
                meta2.addStoredEnchant(Enchantment.SHARPNESS, 4, true);
                high_level_book2.setItemMeta(meta2);
                e.getDrops().add(high_level_book2); // Sharpness 4 Book

                ItemStack fragment2 = new ItemStack(Material.AMETHYST_SHARD, 1);
                ItemMeta itemStackMeta2 = fragment2.getItemMeta();
                itemStackMeta2.setDisplayName(ChatColor.DARK_PURPLE + "Void Fragment");
                itemStackMeta2.setLore(Collections.singletonList(ChatColor.DARK_GREEN + "A custom drop from Warden. Doesn't have any uses yet."));
                itemStackMeta2.addEnchant(Enchantment.INFINITY, 1, true);
                itemStackMeta2.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                fragment2.setItemMeta(itemStackMeta2);
                e.getDrops().add(fragment2); // Custom item for hard difficulty wardens - Void Fragment

            } else { // if difficulty not set correctly, default difficulty (normal) will be used

                e.getDrops().add(new ItemStack(Material.NETHERITE_SCRAP, 2)); // 2 Netherite Scraps
                e.getDrops().add(new ItemStack(Material.DIAMOND, 5)); // 5 Diamonds

                ItemStack mid_level_book1 = new ItemStack(Material.ENCHANTED_BOOK, 1);
                EnchantmentStorageMeta meta1 = (EnchantmentStorageMeta)mid_level_book1.getItemMeta();
                meta1.addStoredEnchant(Enchantment.SWIFT_SNEAK, 2, true);
                mid_level_book1.setItemMeta(meta1);
                e.getDrops().add(mid_level_book1); // Swift Sneak 2 Book

                ItemStack mid_level_book2 = new ItemStack(Material.ENCHANTED_BOOK, 1);
                EnchantmentStorageMeta meta2 = (EnchantmentStorageMeta)mid_level_book2.getItemMeta();
                meta2.addStoredEnchant(Enchantment.PROTECTION, 2, true);
                mid_level_book2.setItemMeta(meta2);
                e.getDrops().add(mid_level_book2); // Protection 2 Book

                ItemStack fragment = new ItemStack(Material.ECHO_SHARD, 1);
                ItemMeta itemStackMeta = fragment.getItemMeta();
                itemStackMeta.setDisplayName(ChatColor.DARK_PURPLE + "Dark Essence");
                itemStackMeta.setLore(Collections.singletonList(ChatColor.DARK_GREEN + "A custom drop from Warden. Doesn't have any uses yet."));
                itemStackMeta.addEnchant(Enchantment.INFINITY, 1, true);
                itemStackMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                fragment.setItemMeta(itemStackMeta);
                e.getDrops().add(fragment); // Custom item for normal difficulty wardens - Dark Essence

            }
        }*/
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
}
