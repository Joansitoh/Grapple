package me.dragonsteam.grapple;

import de.jeff_media.updatechecker.UpdateChecker;
import lombok.Getter;
import me.dragonsteam.grapple.commands.FishCommand;
import me.dragonsteam.grapple.listeners.PlayerGlobalListener;
import me.dragonsteam.grapple.listeners.PlayerGrappleListener;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class Grapple extends JavaPlugin {

    public static Grapple INSTANCE;

    ///////////////////////////////////////////////

    private UpdateChecker updateChecker;

    public void onEnable() {
        INSTANCE = this;

        ///////////////////////////////////////////////

        saveDefaultConfig();
        updateChecker = UpdateChecker.init(this, 60466).checkEveryXHours(1).checkNow();

        ///////////////////////////////////////////////

        getServer().getPluginManager().registerEvents(new PlayerGlobalListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerGrappleListener(), this);
        getCommand("grapple").setExecutor(new FishCommand());

        ///////////////////////////////////////////////
    }


    public void onDisable() {
    }

    public static String translate(String input) {
        return ChatColor.translateAlternateColorCodes('&', input);
    }
}
