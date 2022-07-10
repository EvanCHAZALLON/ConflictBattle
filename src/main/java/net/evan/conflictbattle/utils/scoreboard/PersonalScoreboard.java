package net.evan.conflictbattle.utils.scoreboard;

import fr.versamc.game.GameAPI;
import fr.versamc.game.teams.TeamManager;
import fr.versamc.game.teams.Teams;
import net.evan.conflictbattle.ConflictBattle;
import net.evan.conflictbattle.ConflictPlayer;
import net.evan.conflictbattle.game.ConflictState;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.UUID;

/*
 * This file is part of SamaGamesAPI.
 *
 * SamaGamesAPI is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * SamaGamesAPI is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with SamaGamesAPI.  If not, see <http://www.gnu.org/licenses/>.
 */
public class PersonalScoreboard {
    private ConflictBattle conflictBattle;
    private TeamManager teamManager;

    private Player player;
    private ConflictPlayer conflictPlayer;

    private final UUID uuid;
    private final ObjectiveSign objectiveSign;

    private DateTimeFormatter dateTimeFormatter;
    private LocalDateTime localDateTime;

    PersonalScoreboard(ConflictBattle conflictBattle, Player player){
        this.conflictBattle = conflictBattle;
        this.teamManager = GameAPI.getVersaGameAPI().getTeamManager();

        this.player = player;
        this.conflictPlayer = conflictBattle.getConflictManager().getPlayer(player);

        uuid = player.getUniqueId();
        objectiveSign = new ObjectiveSign("sidebar", "ConflictBattle");

        reloadData();
        objectiveSign.addReceiver(player);

        dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        localDateTime = LocalDateTime.now();
    }

    public void reloadData(){}

    public void setLines(String ip){
        objectiveSign.setDisplayName("§7» §9§lConflictBattle §7«");

        if (conflictBattle.getConflictManager().getState() == ConflictState.PLAYING) {
            objectiveSign.clearScores();
            objectiveSign.setLine(0, "      §8§l› §7" + dateTimeFormatter.format(localDateTime) + " §8§l‹ ");
            objectiveSign.setLine(1, "§1");
            objectiveSign.setLine(2, "§8§l▶ §7Durée: §c" + formatTime());
            objectiveSign.setLine(3, "§8§l▶ §7Manche(s): §9" + conflictBattle.getConflictManager().getFromTeam(Teams.BLUE).getWonRounds() + "§8/§c" + conflictBattle.getConflictManager().getFromTeam(Teams.RED).getWonRounds());
            objectiveSign.setLine(4, "§2");
            objectiveSign.setLine(5, "§8§l▶ §c⬛ §8: §f" + teamManager.countTeam(Teams.RED) + " §8┃ §a" + conflictBattle.getConflictManager().getFromTeam(Teams.RED).getPoints() + " §7points");
            objectiveSign.setLine(6, "§8§l▶ §9⬛ §8: §f" + teamManager.countTeam(Teams.BLUE) + " §8┃ §a" + conflictBattle.getConflictManager().getFromTeam(Teams.BLUE).getPoints() + " §7points");
            objectiveSign.setLine(7, "§3");
            objectiveSign.setLine(8, "§8§l▶ §7Role: §e" + conflictPlayer.getRole().getName());
            objectiveSign.setLine(9, "§8§l▶ §7K§8/§7D: §e" + conflictPlayer.getKills() + "§8/§e" + conflictPlayer.getDeaths());
            objectiveSign.setLine(10, "§4");
            objectiveSign.setLine(11, ip);
            objectiveSign.updateLines();
            return;
        } else if (conflictBattle.getConflictManager().getState() == ConflictState.WAITING) {
            objectiveSign.clearScores();
            objectiveSign.setLine(0, "      §8§l› §7" + dateTimeFormatter.format(localDateTime) + " §8§l‹ ");
            objectiveSign.setLine(1, "§1");
            objectiveSign.setLine(2, "§8§l▶ §7Carte : §e" + Bukkit.getWorlds().get(0).getName());
            objectiveSign.setLine(3, "§8§l▶ §7Joueurs : §c" + conflictBattle.getConflictManager().getPlayers().size() + "§8/§c" + GameAPI.getVersaGameAPI().getGameSettings().getMaxPlayers());
            objectiveSign.setLine(4, "§2");
            objectiveSign.setLine(5, "§8§l▶ §7Attente §8(§e" + formatPlayers() + "§8)");
            objectiveSign.setLine(6, "§3");
            objectiveSign.setLine(7, ip);
            objectiveSign.updateLines();
            return;
        } else {
            objectiveSign.clearScores();
            objectiveSign.setLine(0, "§1");
            objectiveSign.setLine(1, ip);
            objectiveSign.updateLines();
            return;
        }
    }

    private String formatPlayers() {
        int missing = GameAPI.getVersaGameAPI().getGameSettings().getMinPlayers() - conflictBattle.getConflictManager().getPlayers().size();

        if (missing == 1) return missing + " manquant";
        if (missing <= 0) return 0 + " manquant";
        return missing + " manquants";
    }

    private Teams getOtherTeam(ConflictPlayer conflictPlayer) {
        if (conflictPlayer.getConflictTeam().getTeams() == Teams.RED) return Teams.BLUE;
        return Teams.RED;
    }

    private String formatTime() {
        return new SimpleDateFormat("mm:ss").format(new Date((System.currentTimeMillis() - conflictBattle.getConflictManager().getStartedAt())));
    }

    public void onLogout(){
        objectiveSign.removeReceiver(Bukkit.getServer().getOfflinePlayer(uuid));
    }
}