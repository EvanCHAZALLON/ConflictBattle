package net.evan.conflictbattle.runnables;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import fr.versamc.game.GameAPI;
import fr.versamc.game.games.runnable.LobbyRunnable;
import fr.versamc.game.teams.TeamManager;
import fr.versamc.game.teams.Teams;
import net.evan.conflictbattle.ConflictBattle;
import net.evan.conflictbattle.ConflictPlayer;
import net.evan.conflictbattle.game.ConflictPhase;
import net.evan.conflictbattle.game.ConflictState;
import net.evan.conflictbattle.utils.inventory.ItemBuilder;
import net.spigotmc.tagapi.api.TagAPI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class LobbyCheckRunnable extends BukkitRunnable {

    private final ConflictBattle conflictBattle;
    private final TeamManager teamManager = GameAPI.getVersaGameAPI().getTeamManager();

    public LobbyCheckRunnable(ConflictBattle conflictBattle) {
        this.conflictBattle = conflictBattle;
    }

    @Override
    public void run() {
        if (conflictBattle.getConflictManager().getState() != ConflictState.WAITING) {
            this.cancel();
            return;
        }
        if (LobbyRunnable.isFinish()) {
            checkForTeams();
            conflictBattle.getRoleManager().shuffleRoles();
            conflictBattle.getConflictManager().setState(ConflictState.PLAYING);
            conflictBattle.getConflictManager().setPhase(ConflictPhase.FIRST_ROUND);
            conflictBattle.getConflictManager().setStartedAt(System.currentTimeMillis());
            conflictBattle.getConflictManager().teleportPlayers();
            conflictBattle.getConflictManager().getConflictTotemManager().setupTotems();

            setupHologram();
            setupPlayers();
            setupTags();

            launchTasks();
        }
    }

    private void setupPlayers() {
        for (ConflictPlayer conflictPlayer : conflictBattle.getConflictManager().getPlayers()) {
            conflictPlayer.getBukkitPlayer().getInventory().clear();
            conflictPlayer.getKit().sendKits(conflictPlayer.getBukkitPlayer());
            sendDefaultKit(conflictPlayer.getBukkitPlayer());
            conflictPlayer.getBukkitPlayer().performCommand("kit");
        }
    }

    private void sendDefaultKit(Player player) {
        player.getInventory().setHelmet(new ItemBuilder(Material.LEATHER_HELMET).armorColor(conflictBattle.getConflictManager().getPlayer(player).getConflictTeam().getTeams().getColor()).build());
        player.getInventory().setChestplate(new ItemBuilder(Material.CHAINMAIL_CHESTPLATE).build());
        player.getInventory().setLeggings(new ItemBuilder(Material.LEATHER_LEGGINGS).armorColor(conflictBattle.getConflictManager().getPlayer(player).getConflictTeam().getTeams().getColor()).build());
        player.getInventory().setBoots(new ItemBuilder(Material.LEATHER_BOOTS).armorColor(conflictBattle.getConflictManager().getPlayer(player).getConflictTeam().getTeams().getColor()).build());
        player.getInventory().addItem(new ItemStack(Material.IRON_PICKAXE));
        player.getInventory().addItem(new ItemStack(Material.IRON_AXE));
        player.getInventory().addItem(new ItemStack(Material.COBBLESTONE, 64));
        player.getInventory().addItem(new ItemStack(Material.WOOD, 64));
    }

    private void launchTasks() {
        BossbarRunnable bossbarRunnable = new BossbarRunnable(conflictBattle);
        bossbarRunnable.runTaskTimer(conflictBattle, 0, 20);

        TotemAliveRunnable totemAliveRunnable = new TotemAliveRunnable(conflictBattle);
        totemAliveRunnable.runTaskTimer(conflictBattle, 0, 20);

        HologramRunnable hologramRunnable = new HologramRunnable(conflictBattle);
        hologramRunnable.runTaskTimer(conflictBattle, 0, 20 * 5);
    }

    private void setupHologram() {
        for (ConflictPlayer conflictPlayer : conflictBattle.getConflictManager().getPlayers()) {
            Hologram hologram = HologramsAPI.createHologram(conflictBattle, conflictPlayer.getConflictTeam().getHologramLocation());
            hologram.getVisibilityManager().showTo(conflictPlayer.getBukkitPlayer());
            hologram.getVisibilityManager().setVisibleByDefault(false);

            for (int i = 0; i <= 11; i++) {
                hologram.insertTextLine(i, "§c");
            }

            conflictPlayer.setHologram(hologram);
        }
    }

    private void setupTags() {
        for (ConflictPlayer conflictPlayer : conflictBattle.getConflictManager().getPlayers()) {
            TagAPI.getInstance().setTag(conflictPlayer.getBukkitPlayer(), conflictPlayer.getConflictTeam().getTeams().getPrefix() + " ", " §e(" + conflictPlayer.getRole().getName().charAt(0) + ")", getPriorityForTeam(conflictPlayer), GameAPI.getVersaGameAPI().getTeamManager().getTeamPlayerList(conflictPlayer.getConflictTeam().getTeams()));
            TagAPI.getInstance().setTag(conflictPlayer.getBukkitPlayer(), conflictPlayer.getConflictTeam().getTeams().getPrefix() + " ", "", getPriorityForTeam(conflictPlayer), GameAPI.getVersaGameAPI().getTeamManager().getTeamPlayerList(getOtherTeam(conflictPlayer)));
        }
    }

    private int getPriorityForTeam(ConflictPlayer conflictPlayer) {
        if (conflictPlayer.getConflictTeam().getTeams() == Teams.RED) return 200;
        return 100;
    }

    private Teams getOtherTeam(ConflictPlayer conflictPlayer) {
        if (conflictPlayer.getConflictTeam().getTeams() == Teams.RED) return Teams.BLUE;
        return Teams.RED;
    }

    private void checkForTeams() {
        for (ConflictPlayer player : conflictBattle.getConflictManager().getPlayers()) {
            if (!hasTeamPlayer(player.getBukkitPlayer())) {
                Teams team = getLowestTeam();
                teamManager.addPlayerTeam(player.getBukkitPlayer(), team);
                player.setConflictTeam(conflictBattle.getConflictManager().getFromTeam(team));
            }
        }
    }

    private Teams getLowestTeam() {
        Teams lowestTeam = null;
        for (Teams team : teamManager.getTeamList()) {
            if (lowestTeam == null) lowestTeam = team;
            if (teamManager.countTeam(lowestTeam) > teamManager.countTeam(team)) {
                lowestTeam = team;
            }
        }
        return lowestTeam;
    }

    private boolean hasTeamPlayer(Player player) {
        for (Teams team : teamManager.getTeamList())
            if (teamManager.isInTeamPlayer(player, team)) {
                return true;
            }
        return false;
    }

}
