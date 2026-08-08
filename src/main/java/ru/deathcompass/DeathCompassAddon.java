package ru.deathcompass;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DeathCompassAddon extends JavaPlugin implements Listener {
    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player p = e.getEntity();
        Location loc = p.getLocation();

        ItemStack compass = new ItemStack(Material.RECOVERY_COMPASS);
        ItemMeta meta = compass.getItemMeta();
        meta.setDisplayName(ChatColor.RED + "Компас смерти");
        meta.setEnchantmentGlintOverride(true); // эффект зачарования
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.WHITE + "Никнейм: " + ChatColor.LIGHT_PURPLE + p.getName());
        lore.add(ChatColor.WHITE + "Мир: " + ChatColor.GREEN + worldName(loc));
        lore.add(ChatColor.WHITE + "Координаты: " + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ());
        lore.add(ChatColor.WHITE + "Причина: " + cause(p.getLastDamageCause()));
        lore.add(ChatColor.WHITE + "Время: " + new SimpleDateFormat("dd.MM.yyyy HH:mm").format(new Date()));
        meta.setLore(lore);
        compass.setItemMeta(meta);

        getServer().getScheduler().runTask(this, () -> p.getInventory().addItem(compass));
    }

    private String worldName(Location loc) {
        switch (loc.getWorld().getName()) {
            case "world_nether": return "Нижний мир";
            case "world_the_end": return "Энд";
            default: return "Обычный мир";
        }
    }

    private String cause(EntityDamageEvent d) {
        if (d == null) return "Неизвестна";
        switch (d.getCause()) {
            case FALL: return "Упал с высоты";
            case DROWNING: return "Утонул";
            case LAVA: case FIRE: case FIRE_TICK: return "Сгорел";
            case VOID: return "Упал в пустоту";
            case ENTITY_ATTACK: return "Убит мобом";
            case PROJECTILE: return "Застрелен";
            default: return d.getCause().name();
        }
    }
}
