package uk.co.tmdavies.prisoncore.listeners.custom;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class PlayerAcquiredItemEvent extends PlayerEvent {

    private final ItemStack itemStack;
    private static final HandlerList handlerList = new HandlerList();

    public PlayerAcquiredItemEvent(@NotNull Player who, ItemStack itemStack) {
        super(who);
        this.itemStack = itemStack;
    }

    public ItemStack getItem() {

        return this.itemStack;
    }

    public static HandlerList getHandlerList() {

        return handlerList;

    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

}
