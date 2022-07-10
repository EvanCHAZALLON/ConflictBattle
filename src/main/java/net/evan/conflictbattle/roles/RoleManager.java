package net.evan.conflictbattle.roles;

import fr.versamc.game.GameAPI;
import fr.versamc.game.teams.TeamManager;
import fr.versamc.game.teams.Teams;
import fr.versamc.game.utils.TitleManager;
import net.evan.conflictbattle.ConflictBattle;
import net.evan.conflictbattle.ConflictManager;
import net.evan.conflictbattle.ConflictPlayer;
import net.evan.conflictbattle.teams.ConflictTeam;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RoleManager {

    private final ConflictBattle conflictBattle;
    private final ConflictManager conflictManager;

    public RoleManager(ConflictBattle conflictBattle) {
        this.conflictBattle = conflictBattle;
        this.conflictManager = conflictBattle.getConflictManager();
    }

    public void shuffleRoles() {
        Random random = new Random();
        TeamManager teamManager = GameAPI.getVersaGameAPI().getTeamManager();

        for (Teams team : GameAPI.getVersaGameAPI().getTeamManager().getTeamList()) {
            List<ConflictPlayer> selectedPlayers = new ArrayList<>();

            for (int i = 0; i < 1; i++) { // Remplacer 1 par 3
                Player player = teamManager.getTeamPlayerList(team).get(random.nextInt(teamManager.countTeam(team)));
                ConflictPlayer conflictPlayer = conflictManager.getPlayer(player);

                while (selectedPlayers.contains(conflictPlayer)) {
                    player = teamManager.getTeamPlayerList(team).get(random.nextInt(teamManager.countTeam(team)));
                    conflictPlayer = conflictManager.getPlayer(player);
                }
                selectedPlayers.add(conflictPlayer);

            }

            selectedPlayers.get(0).setRole(Role.COMMANDING_OFFICER);
            /*selectedPlayers.get(1).setRole(Role.OFFICER);
            selectedPlayers.get(2).setRole(Role.OFFICER);*/

            for (Player player : teamManager.getTeamPlayerList(team)) {
                ConflictPlayer conflictPlayer = conflictManager.getPlayer(player);
                if (conflictPlayer.getRole() == Role.NONE)  conflictPlayer.setRole(Role.SOLDIER);
            }


        }
        conflictBattle.getConflictManager().getPlayers().forEach(conflictPlayer -> TitleManager.sendTitle(conflictPlayer.getBukkitPlayer(), "§9§lConflictBattle", "§7Vous êtes §e" + conflictPlayer.getRole().getName(), 20));

    }

}
