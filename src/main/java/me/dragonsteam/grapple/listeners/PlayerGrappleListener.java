package me.dragonsteam.grapple.listeners;

import me.dragonsteam.grapple.Grapple;
import me.dragonsteam.grapple.utils.GrappleAction;
import me.dragonsteam.grapple.utils.GrappleUtils;
import me.dragonsteam.grapple.utils.PlayerGrappleEvent;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.util.Vector;
import xyz.xenondevs.particle.ParticleBuilder;
import xyz.xenondevs.particle.ParticleEffect;

public class PlayerGrappleListener implements Listener {

    @EventHandler
    public void onGrapplePlayer(PlayerGrappleEvent e) {
        e.getPlayer().playSound(e.getPlayer().getLocation(), GrappleUtils.getActionSound(e.getAction()), 1.0F, -5.0F);
        new ParticleBuilder(ParticleEffect.CLOUD, e.getPlayer().getLocation())
                .setOffsetY(0.5f).setOffsetX(0.5f).setOffsetZ(0.5f).setSpeed(0)
                .setAmount(20).display();

    /*    if (GrappleUtils.isLegacy()) {
            org.bukkit.Effect effect = (org.bukkit.Effect) GrappleUtils.getActionEffect(e.getAction());
            e.getPlayer().playEffect(e.getPlayer().getLocation(), effect, 0);
        } else {
            org.bukkit.Particle effect = (org.bukkit.Particle) GrappleUtils.getActionEffect(e.getAction());
            e.getPlayer().spawnParticle(effect, e.getPlayer().getLocation(), 0);
        }
*/
        if (e.getAction().equals(GrappleAction.HOOK)) {
            pullEntityToLocation(GrappleAction.HOOK, e.getPlayer(), e.getHook().getLocation());
            return;
        }

        if (e.getAction().equals(GrappleAction.PLAYERS))
            pullEntityToLocation(GrappleAction.PLAYERS, e.getHook(), e.getPlayer().getLocation());
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        if (!Grapple.INSTANCE.getConfig().getBoolean("GRAPPLE.UNDROP"))
            return;

        if (e.getItemDrop().getItemStack().equals(GrappleUtils.getGrappleItem())) e.setCancelled(true);
    }

    @EventHandler
    public void onMoveGrapple(InventoryMoveItemEvent e) {
        if (!Grapple.INSTANCE.getConfig().getBoolean("GRAPPLE.HOOKED"))
            return;

        if (e.getDestination() == null)
            return;

        if (e.getItem() != null && e.getItem().equals(GrappleUtils.getGrappleItem()))
            e.setCancelled(true);
    }

    @EventHandler
    public void onMoveGrapple(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (!Grapple.INSTANCE.getConfig().getBoolean("GRAPPLE.HOOKED"))
            return;

        if (e.getClick().isKeyboardClick() &&
                e.getInventory() != null && e.getInventory().getItem(e.getHotbarButton()) != null &&
                e.getInventory().getItem(e.getHotbarButton()).equals(GrappleUtils.getGrappleItem())) {
            e.setCancelled(true);
            p.updateInventory();
            return;
        }

        if (e.getCursor() != null && e.getCursor().equals(GrappleUtils.getGrappleItem())) {
            e.setCancelled(true);
            p.updateInventory();
            return;
        }

        if (e.getCurrentItem() != null && e.getCurrentItem().equals(GrappleUtils.getGrappleItem())) {
            e.setCancelled(true);
            p.updateInventory();
        }
    }

    private void pullEntityToLocation(GrappleAction action, Entity e, Location loc) {
        Location entityLoc = e.getLocation();
        entityLoc.setY(entityLoc.getY() + 0.5D);
        e.teleport(entityLoc);

        double g = -0.08D;
        double t = loc.distance(entityLoc);
        double v_x = (1.0D + GrappleUtils.getActionMultiplier(action, "X") * t) * (loc.getX() - entityLoc.getX()) / t;
        double v_y = (1.0D + GrappleUtils.getActionMultiplier(action, "Y") * t) * (loc.getY() - entityLoc.getY()) / t - 0.5D * g * t;
        double v_z = (1.0D + GrappleUtils.getActionMultiplier(action, "Z") * t) * (loc.getZ() - entityLoc.getZ()) / t;

        Vector v = e.getVelocity();
        v.setX(v_x);
        v.setY(v_y);
        v.setZ(v_z);
        e.setVelocity(v);
    }

}