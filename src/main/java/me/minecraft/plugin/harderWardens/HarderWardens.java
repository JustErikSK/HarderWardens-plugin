package me.minecraft.plugin.harderWardens;

import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Warden;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import static me.minecraft.plugin.harderWardens.HarderWardens.WardenDifficulty.*;

public final class HarderWardens extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(this, this);
        this.saveDefaultConfig();
        validateCustomLootOptions();

        getLogger().info("Harder Wardens is loading loot tables...");
        WardenLootManager.init();
        getLogger().info("Loot tables initialized!");
        Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_GREEN + "Harder Wardens >> Plugin has been enabled!");

        FileConfiguration config = this.getConfig();
        config.addDefault("warden_difficulty", "NORMAL");
        config.addDefault("warden_health", 100);
        config.addDefault("warden_damage", 1.0);
        config.addDefault("warden_loot_option_1", 1);
        config.addDefault("warden_loot_option_2", 2);

        registerCommand(
                "harderwardens",
                "Command to reload config for Harder Wardens plugin",
                List.of("hw"),
                new HarderWardensCommand(this)
        );
        getLogger().info("Harder Wardens enabled!");
    }

    public final class HarderWardensCommand implements BasicCommand {

        private final HarderWardens plugin;

        public HarderWardensCommand(HarderWardens plugin) {
            this.plugin = plugin;
        }

        @Override
        public void execute(CommandSourceStack source, String[] args) {
            CommandSender sender = source.getSender();

            // Permission
            if (!sender.hasPermission("harderwardens.admin")) {
                sender.sendMessage("§cYou don't have permission to use this command.");
                return;
            }

            if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
                plugin.reloadPluginConfig(); // your method that calls reloadConfig() + refreshes cached values if you use them
                sender.sendMessage("§aHarder Wardens config reloaded!");
                return;
            }

            sender.sendMessage("§cUsage: /harderwardens reload");
        }

        @Override
        public Collection<String> suggest(CommandSourceStack source, String[] args) {
            if (args.length == 1) return List.of("reload");
            return List.of();
        }
    }

    public void reloadPluginConfig() {
        reloadConfig();
        getLogger().info("Harder Wardens config reloaded.");
    }

    public static class WardenLootManager {
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
            hardRare.add(new ItemStack(Material.DIAMOND, 5));

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
        }
    }

    private static final List<List<ItemStack>> LOOT_POOLS = List.of(
            WardenLootManager.easyCommon,
            WardenLootManager.easyRare,
            WardenLootManager.normalCommon,
            WardenLootManager.normalRare,
            WardenLootManager.hardCommon,
            WardenLootManager.hardRare,
            WardenLootManager.nightmareCommon,
            WardenLootManager.nightmareRare,
            WardenLootManager.insaneCommon,
            WardenLootManager.insaneRare
    );

    private List<ItemStack> poolById(int id) {
        if (id < 1 || id > LOOT_POOLS.size()) return Collections.emptyList();
        return LOOT_POOLS.get(id - 1);
    }

    private static final double HEALTH_CAP = 1024.0;

    private void applyHealth(LivingEntity ent, double requestedMaxHealth, boolean healToFull) {
        double finalMax = Math.min(requestedMaxHealth, HEALTH_CAP);

        AttributeInstance attr = ent.getAttribute(Attribute.MAX_HEALTH);
        if (attr != null) {
            attr.setBaseValue(finalMax);
        }
        if (healToFull) {
            ent.setHealth(finalMax);
        } else {
            ent.setHealth(Math.min(ent.getHealth(),  finalMax));
        }
    }

    @EventHandler
    public void wardenSpawnEvent(CreatureSpawnEvent e) {
        if (e.getEntityType() != EntityType.WARDEN) return;
        Warden war = (Warden) e.getEntity();
        WardenDifficulty diff = getActiveDifficulty();

        switch (diff) {
            case EASY:
                applyHealth(war, 300, true);
                break;
            case NORMAL:
                applyHealth(war, 500, true);
                break;
            case HARD:
                applyHealth(war, 700, true);
                break;
            case NIGHTMARE:
                applyHealth(war, 900, true);
                break;
            case INSANE:
                applyHealth(war, 1024, true);
                break;
            case CUSTOM:
                applyCustomStats(war);
                break;
        }
        applyWardenName(war, diff);
    }

    private void applyWardenName(Warden w, WardenDifficulty diff) {
        Component name = switch (diff) {
            case EASY -> Component.text("Echo Lurker", NamedTextColor.DARK_AQUA);
            case NORMAL -> Component.text("Abyss Watcher", NamedTextColor.DARK_BLUE);
            case HARD -> Component.text("Void Reaper", NamedTextColor.DARK_PURPLE);
            case NIGHTMARE -> Component.text("Nightmare Sentinel", NamedTextColor.RED);
            case INSANE -> Component.text("Abyssal Devourer", NamedTextColor.DARK_RED);
            case CUSTOM -> Component.text("Custom Warden", NamedTextColor.GOLD);
        };
        w.customName(name);
        w.setCustomNameVisible(true);
    }

    @EventHandler
    public void wardenAttackEvent(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Warden)) return;
        e.setDamage(e.getDamage() * getDamageMultiplier(getActiveDifficulty()));
    }

    private double getDamageMultiplier(WardenDifficulty diff) {
        switch (diff) {
            case EASY: return 0.5;
            case NORMAL: return 1.5;
            case HARD: return 2.5;
            case NIGHTMARE: return 3.5;
            case INSANE: return 4.5;
            case CUSTOM:
                return Math.max(0.1,
                        Math.min(100.0, getConfig().getDouble("warden_damage", 1.0)));
            default:
                return 1.5;
        }
    }

    public enum WardenDifficulty {
        EASY, NORMAL, HARD, NIGHTMARE, INSANE, CUSTOM
    }

    private static final Map<WardenDifficulty, Double> RARE_CHANCE = Map.of(
            EASY, 0.25,
            NORMAL, 0.35,
            HARD, 0.45,
            WardenDifficulty.NIGHTMARE, 0.55,
            WardenDifficulty.INSANE, 0.65,
            WardenDifficulty.CUSTOM, 0.50
    );

    private static final Map<String, WardenDifficulty> DIFF_ALIASES = new HashMap<>();
    static {
        DIFF_ALIASES.put("easy", EASY);
        DIFF_ALIASES.put("normal", NORMAL);
        DIFF_ALIASES.put("hard", HARD);
        DIFF_ALIASES.put("nightmare", WardenDifficulty.NIGHTMARE);
        DIFF_ALIASES.put("insane", WardenDifficulty.INSANE);
        DIFF_ALIASES.put("custom", WardenDifficulty.CUSTOM);
    }

    private WardenDifficulty getActiveDifficulty() {
        String raw = this.getConfig().getString("warden_difficulty", "NORMAL");
        if (raw == null) return NORMAL;

        String key = raw.trim().toLowerCase(Locale.ROOT);
        WardenDifficulty mapped = DIFF_ALIASES.get(key);
        if (mapped != null) return mapped;

        try {
            String norm = raw.trim().replace('-', '_').replace(' ', '_').toUpperCase(Locale.ROOT);
            return WardenDifficulty.valueOf(norm);
        } catch (IllegalArgumentException ex) {
            getLogger().warning("Invalid difficulty in config: " + raw + " (defaulting to NORMAL)");
            return NORMAL;
        }
    }

    private static final double HEALTH_MIN = 1.0;
    private static final double HEALTH_MAX = 1024.0;
    private static final double DMG_MIN = 0.1;
    private static final double DMG_MAX = 100.0;

    private void applyCustomStats(LivingEntity ent) {
        double cfgHP = Math.max(HEALTH_MIN, Math.min(HEALTH_MAX, getConfig().getDouble("warden_health", 100.0)));
        double cfgDMG = Math.max(DMG_MIN, Math.min(DMG_MAX, getConfig().getDouble("warden_damage", 1.0)));

        AttributeInstance hpAttr = ent.getAttribute(Attribute.MAX_HEALTH);
        if (hpAttr != null) hpAttr.setBaseValue(cfgHP);
        ent.setHealth(Math.min(cfgHP, HEALTH_MAX));

        AttributeInstance dmgAttr = ent.getAttribute(Attribute.ATTACK_DAMAGE);
        if (dmgAttr != null) dmgAttr.setBaseValue(cfgDMG);
    }

    private List<ItemStack> getCommonPool(WardenDifficulty d) {
        switch (d) {
            case EASY: return WardenLootManager.easyCommon;
            case NORMAL: return WardenLootManager.normalCommon;
            case HARD: return WardenLootManager.hardCommon;
            case NIGHTMARE: return WardenLootManager.nightmareCommon; // or veryHardCommon if you kept old name
            case INSANE: return WardenLootManager.insaneCommon;
            default: return Collections.emptyList();
        }
    }
    private List<ItemStack> getRarePool(WardenDifficulty d) {
        switch (d) {
            case EASY: return WardenLootManager.easyRare;
            case NORMAL: return WardenLootManager.normalRare;
            case HARD: return WardenLootManager.hardRare;
            case NIGHTMARE: return WardenLootManager.nightmareRare;
            case INSANE: return WardenLootManager.insaneRare;
            default: return Collections.emptyList();
        }
    }

    @EventHandler
    public void wardenDeathEvent(EntityDeathEvent e) {
        if (e.getEntityType() != EntityType.WARDEN) return;

        int xp = e.getDroppedExp();
        e.getDrops().clear();

        WardenDifficulty diff = getActiveDifficulty();

        List<ItemStack> chosenList;

        if (diff == WardenDifficulty.CUSTOM) {
            int opt1 = getConfig().getInt("warden_loot_option_1", 1);
            int opt2 = getConfig().getInt("warden_loot_option_2", 2);

            List<ItemStack> pool1 = poolById(opt1);
            List<ItemStack> pool2 = poolById(opt2);

            boolean pickFirst = ThreadLocalRandom.current().nextBoolean();
            chosenList = pickFirst ? pool1 : pool2;
            if (chosenList.isEmpty()) chosenList = pickFirst ? pool2 : pool1;
            if (chosenList.isEmpty()) {
                e.setDroppedExp(xp);
                return;
            }
        } else {
            List<ItemStack> common = getCommonPool(diff);
            List<ItemStack> rare = getRarePool(diff);
            double rareChance = RARE_CHANCE.getOrDefault(diff, 0.35);
            boolean pickRare = ThreadLocalRandom.current().nextDouble() < rareChance;
            chosenList = pickRare ? rare : common;
            if (chosenList.isEmpty()) chosenList = pickRare ? common : rare;
            if (chosenList.isEmpty()) {
                e.setDroppedExp(xp);
                return;
            }
        }

        for (ItemStack template : chosenList) {
            if (template == null || template.getType().isAir()) continue;
            e.getEntity().getWorld().dropItemNaturally(
                    e.getEntity().getLocation(),
                    template.clone()
            );
        }

        e.setDroppedExp(xp);
    }

    private void validateCustomLootOptions() {
        int o1 = getConfig().getInt("warden_loot_option_1", 1);
        int o2 = getConfig().getInt("warden_loot_option_2", 2);
        if (o1 < 1 || o1 > 10) getLogger().warning("warden_loot_option_1 must be 1..10 (was " + o1 + ")");
        if (o2 < 1 || o2 > 10) getLogger().warning("warden_loot_option_2 must be 1..10 (was " + o2 + ")");
    }
}
