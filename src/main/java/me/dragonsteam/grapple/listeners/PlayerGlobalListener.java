package me.dragonsteam.grapple.listeners;

import me.dragonsteam.grapple.Grapple;
import me.dragonsteam.grapple.utils.GrappleAction;
import me.dragonsteam.grapple.utils.GrappleUtils;
import me.dragonsteam.grapple.utils.PlayerGrappleEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerGlobalListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        if (!Grapple.INSTANCE.getConfig().getBoolean("GRAPPLE.JOIN-GRAPPLE"))
            return;

        GrappleUtils.giveGrappleItem(e.getPlayer());

        if (e.getPlayer().hasPermission(GrappleUtils.getGrapplePermission("staff"))) {
            if (Grapple.INSTANCE.getConfig().getBoolean("CHECK-UPDATES"))
                Grapple.INSTANCE.getUpdateChecker().checkNow(e.getPlayer());
        }
    }

    @EventHandler
    public void onPlayerFishEvent(PlayerFishEvent e) {
        Player p = e.getPlayer();
        if (!p.getItemInHand().hasItemMeta() || !p.getItemInHand().getItemMeta().hasDisplayName())
            return;

        String name = p.getItemInHand().getItemMeta().getDisplayName();

        if (name.equals(GrappleUtils.getGrappleName())) {
            GrappleAction action = GrappleUtils.getGrappleAction();
            e.getPlayer().getItemInHand().setDurability((short) -5);
            p.updateInventory();

            if (p.hasPermission(GrappleUtils.getGrapplePermission("use"))) {
                if (e.getState() == PlayerFishEvent.State.CAUGHT_ENTITY && (
                        action.equals(GrappleAction.PLAYERS) || action.equals(GrappleAction.BOTH))) {
                    Bukkit.getPluginManager().callEvent(new PlayerGrappleEvent(p, e.getCaught(), GrappleAction.PLAYERS));
                    return;
                }

                if (e.getState() != PlayerFishEvent.State.FISHING && (
                        action.equals(GrappleAction.HOOK) || action.equals(GrappleAction.BOTH))) {
                    Bukkit.getPluginManager().callEvent(new PlayerGrappleEvent(p, e.getHook(), GrappleAction.HOOK));
                }
            } else e.setCancelled(true);
        }
    }

}