package net.evan.conflictbattle.listeners;

import net.evan.conflictbattle.ConflictBattle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class CancelListener implements Listener {

    private final ConflictBattle conflictBattle;

    public CancelListener(ConflictBattle conflictBattle) {
        this.conflictBattle = conflictBattle;
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (event.getBlockPlaced().getLocation().getY() >= 102) {
            event.setCancelled(true);
            return;
        }
        conflictBattle.getConflictManager().getBlocks().add(event.getBlockPlaced().getLocation());
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        boolean isNotMap = conflictBattle.getConflictManager().getBlocks().stream().anyMatch(location -> location.equals(event.getBlock().getLocation()));
        if (!isNotMap) event.setCancelled(true);
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (event.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK) return;
        if (event.getCause() == EntityDamageEvent.DamageCause.PROJECTILE) return;
        if (event.getCause() == EntityDamageEvent.DamageCause.CONTACT) return;
        if (event.getCause() == EntityDamageEvent.DamageCause.MAGIC) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onAnyMove(PlayerMoveEvent event) {

    }

}
