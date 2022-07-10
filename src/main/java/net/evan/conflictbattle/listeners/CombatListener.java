package net.evan.conflictbattle.listeners;

import fr.versamc.game.utils.TitleManager;
import net.evan.conflictbattle.ConflictBattle;
import net.evan.conflictbattle.ConflictPlayer;
import net.evan.conflictbattle.game.ConflictPhase;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class CombatListener implements Listener {

    private final ConflictBattle conflictBattle;

    public CombatListener(ConflictBattle conflictBattle) {
        this.conflictBattle = conflictBattle;
    }

    @EventHandler
    public void onKill(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        if (!(event.getDamager() instanceof Player)) return;
        if (conflictBattle.getConflictManager().getPhase() == ConflictPhase.NONE) return;

        ConflictPlayer conflictVictim = conflictBattle.getConflictManager().getPlayer((Player) event.getEntity());

        if (event.getDamage() >= conflictVictim.getBukkitPlayer().getHealth()) {
            event.setDamage(0);

            conflictVictim.respawn();

            ConflictPlayer conflictDamager = conflictBattle.getConflictManager().getPlayer((Player) event.getDamager());
            Bukkit.broadcastMessage("§c☠ §8§l» " + conflictVictim.getConflictTeam().getTeams().getPrefix() + conflictVictim.getBukkitPlayer().getName() + " §7a été tué par " + conflictDamager.getConflictTeam().getTeams().getPrefix() + conflictDamager.getBukkitPlayer().getName());
            conflictDamager.setKills(conflictDamager.getKills() + 1);
            conflictDamager.getBukkitPlayer().setHealth(conflictDamager.getBukkitPlayer().getMaxHealth());
            conflictDamager.increasePoints();
            conflictBattle.getConflictManager().updateTeamPoints(conflictDamager.getConflictTeam().getTeams());
            TitleManager.sendTitle(conflictDamager.getBukkitPlayer(), "§1", "§e+" + conflictDamager.getRole().getPointsPerKill(), 20*2);
        }

    }

}
