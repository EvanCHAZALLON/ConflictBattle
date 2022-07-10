package net.evan.conflictbattle.runnables;

import fr.versamc.game.GameAPI;
import net.evan.conflictbattle.ConflictBattle;
import net.evan.conflictbattle.roles.Role;
import net.evan.conflictbattle.teams.ConflictTeam;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class TotemAliveRunnable extends BukkitRunnable {

    private final ConflictBattle conflictBattle;

    public TotemAliveRunnable(ConflictBattle conflictBattle) {
        this.conflictBattle = conflictBattle;
    }

    @Override
    public void run() {
        for (ConflictTeam conflictTeam : conflictBattle.getConflictManager().getTeams()) {
            if (conflictTeam.getConflictTotem().isDestroyed()) {
                if (conflictTeam.getConflictTotem().getHealth() >= 100) {
                    conflictTeam.getConflictTotem().setDestroyed(false);
                    conflictTeam.getConflictTotem().setTimeDestroyed(0);
                    conflictTeam.getConflictTotem().setHealth(100);
                    Bukkit.broadcastMessage(conflictBattle.getConflictManager().getMessages().getConflictBattlePrefix() + "§7Le totem de l'équipe " + conflictTeam.getConflictTotem().getTeam().getPrefix() + conflictTeam.getConflictTotem().getTeam().getName() + " §7a été §arégénéré§7.");
                    conflictTeam.getConflictTotem().getHologram().clearLines();
                    conflictTeam.getConflictTotem().getHologram().insertTextLine(0, "§a" + conflictTeam.getConflictTotem().getHealth() + "§7/§e100");
                    return;
                }

                double booster = 1;
                for (Player player : GameAPI.getVersaGameAPI().getTeamManager().getTeamPlayerList(conflictTeam.getTeams())) {
                    if (conflictBattle.getConflictManager().getPlayer(player).getRole() == Role.SOLDIER) {
                        if (conflictTeam.getConflictTotem().getCuboid().isInArea(player.getLocation())) {
                            booster += 0.25;
                        }
                    }
                }

                conflictTeam.getConflictTotem().setHealth(conflictTeam.getConflictTotem().getHealth() + 1 * booster);
                conflictTeam.getConflictTotem().getHologram().clearLines();
                conflictTeam.getConflictTotem().getHologram().insertTextLine(0, "§a" + conflictTeam.getConflictTotem().getHealth() + "§7/§e100");

            }
        }
    }

}
