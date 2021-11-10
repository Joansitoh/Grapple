package me.dragonsteam.grapple.utils;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter @Setter
public class PlayerGrappleEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private final Player player;
    private final Entity hook;
    private final GrappleAction action;

    private boolean cancelled;

    public PlayerGrappleEvent(Player player, Entity hook, GrappleAction action) {
        this.player = player;
        this.hook = hook;
        this.action = action;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public boolean call() {
        Bukkit.getPluginManager().callEvent(this);
        return !isCancelled();
    }

}
