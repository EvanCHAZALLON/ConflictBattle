package net.evan.conflictbattle.listeners;

import fr.versamc.game.utils.TitleManager;
import net.evan.conflictbattle.ConflictBattle;
import net.evan.conflictbattle.teams.ConflictTeam;
import net.evan.conflictbattle.totem.ConflictTotem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TotemListener implements Listener {

    private final ConflictBattle conflictBattle;

    private final int COOLDOWN_TIME = 250; // Time in milliseconds
    private Map<Player, Long> cooldowns = new HashMap<>();

    public TotemListener(ConflictBattle conflictBattle) {
        this.conflictBattle = conflictBattle;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getClickedBlock() == null) return;
        if (event.getAction() == null) return;

        if (event.getClickedBlock().getType() == Material.BEACON && existsTotem(event.getClickedBlock().getLocation())) {
            if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
                ConflictTotem totem = getTotemAtLocation(event.getClickedBlock().getLocation());

                if (totem.getTeam().getName().equals(conflictBattle.getConflictManager().getPlayer(event.getPlayer()).getConflictTeam().getTeams().getName())) return;

                if (totem.isDestroyed()) {
                    TitleManager.sendTitle(event.getPlayer(), "§1", "§cCe totem est actuellement détruit", 20);
                    return;
                }

                if (totem.getHealth() <= 0) {
                    Bukkit.broadcastMessage(conflictBattle.getConflictManager().getMessages().getConflictBattlePrefix() + "§7Le totem de l'équipe " + totem.getTeam().getPrefix() + totem.getTeam().getName() + " §7a été §cdétruit§7.");
                    totem.setDestroyed(true);
                    totem.setTimeDestroyed(System.currentTimeMillis());
                    return;
                }

                if (checkCooldown(event.getPlayer())) {
                    cooldowns.put(event.getPlayer(), System.currentTimeMillis());
                    totem.damage();
                    conflictBattle.getConflictManager().getPlayer(event.getPlayer()).increaseTotemDamage();
                    totem.getHologram().clearLines();
                    totem.getHologram().insertTextLine(0, "§a" + conflictBattle.getConflictManager().getFromTeam(totem.getTeam()).getConflictTotem().getHealth() + "§7/§e100");
                }
            }
        }
    }

    private boolean checkCooldown(Player player) {
        if (!cooldowns.containsKey(player)) return true;

        if ((System.currentTimeMillis() - cooldowns.get(player)) > COOLDOWN_TIME) {
            cooldowns.remove(player);
            return true;
        }
        return false;
    }

    private boolean existsTotem(Location location) {
        boolean exists = false;
        for (ConflictTeam conflictTeam : conflictBattle.getConflictManager().getTeams()) {
            if (conflictTeam.getConflictTotem().getLocation().equals(location)) {
                exists = true;
            }
        }
        return exists;
    }

    private ConflictTotem getTotemAtLocation(Location location) {
        for (ConflictTeam conflictTeam : conflictBattle.getConflictManager().getTeams()) {
            if (conflictTeam.getConflictTotem().getLocation().equals(location)) {
                return conflictTeam.getConflictTotem();
            }
        }
        return null;
    }

}
