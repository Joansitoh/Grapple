package me.dragonsteam.grapple.utils;

import me.dragonsteam.grapple.Grapple;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class GrappleUtils {

    private static final FileConfiguration config = Grapple.INSTANCE.getConfig();

    public static boolean isLegacy() {
        String a = Grapple.INSTANCE.getServer().getClass().getPackage().getName();
        String version = a.substring(a.lastIndexOf('.') + 1);


        List<String> vers = Arrays.asList("_13", "_14", "_15", "_16", "_17", "_18");
        for (String s : vers) if (version.contains(s)) return false;
        return true;
    }

    public static String getGrappleName() {
        return Grapple.translate(config.getString("GRAPPLE.ITEM-NAME"));
    }

    public static GrappleAction getGrappleAction() {
        try {
            return GrappleAction.valueOf(config.getString("METHOD"));
        } catch (Exception e) {
            return GrappleAction.HOOK;
        }
    }

    public static double getActionMultiplier(GrappleAction action, String multiplier) {
        String hook = action.equals(GrappleAction.PLAYERS) ? "PLAYERS" : "DEFAULT";
        return config.getDouble("HOOKS." + hook + "." + multiplier + "-MULTIPLIER");
    }

    public static Sound getActionSound(GrappleAction action) {
        Sound sound;
        if (isLegacy()) sound = Sound.valueOf("BAT_TAKEOFF");
        else sound = Sound.valueOf("ENTITY_BAT_TAKEOFF");

        try {
            if (action == GrappleAction.PLAYERS)
                return Sound.valueOf(config.getString("HOOKS.PLAYERS.SOUND"));
            return Sound.valueOf(config.getString("HOOKS.DEFAULT.SOUND"));
        } catch (Exception e) {
            return sound;
        }
    }

    /*public static Object getActionEffect(GrappleAction action) {
        if (isLegacy()) {
            org.bukkit.Effect effect = org.bukkit.Effect.valueOf("CLOUD");

            try {
                if (action == GrappleAction.PLAYERS)
                    return org.bukkit.Effect.valueOf(config.getString("HOOKS.PLAYERS.EFFECT"));
                return org.bukkit.Effect.valueOf(config.getString("HOOKS.DEFAULT.EFFECT"));
            } catch (Exception e) {
                return effect;
            }
        }

        org.bukkit.Particle effect = org.bukkit.Particle.valueOf("CLOUD");

        try {
            if (action == GrappleAction.PLAYERS)
                return org.bukkit.Particle.valueOf(config.getString("HOOKS.PLAYERS.EFFECT"));
            return org.bukkit.Particle.valueOf(config.getString("HOOKS.DEFAULT.EFFECT"));
        } catch (Exception e) {
            return effect;
        }
    }
*/
    public static void giveGrappleItem(Player player) {
        if (player.getInventory().contains(getGrappleItem())) player.getInventory().remove(getGrappleItem());
        if (config.get("GRAPPLE.SLOT") != null)
            player.getInventory().setItem(config.getInt("GRAPPLE.SLOT"), getGrappleItem());
        else player.getInventory().addItem(getGrappleItem());
        player.updateInventory();
    }

    public static ItemStack getGrappleItem() {
        ItemStack item = new ItemStack(Material.FISHING_ROD, 1);
        List<String> lore = new ArrayList<>();
        ItemMeta m = item.getItemMeta();

        m.setDisplayName(getGrappleName());
        for (String s : config.getStringList("GRAPPLE.FORMAT"))
            lore.add(Grapple.translate(s));
        m.setLore(lore);

        item.setItemMeta(m);
        return item;
    }

    public static String getGrapplePermission(String use) {
        if (use.equalsIgnoreCase("staff"))
            return config.getString("PERMISSIONS.RELOAD-PLUGIN");
        if (use.equalsIgnoreCase("help"))
            return config.getString("PERMISSIONS.PLUGIN-HELP");
        if (use.equalsIgnoreCase("give"))
            return config.getString("PERMISSIONS.GIVE-GRAPPLE");
        if (use.equalsIgnoreCase("use")) {
            return config.getString("PERMISSIONS.USE-GRAPPLE");
        }
        return "";
    }
}