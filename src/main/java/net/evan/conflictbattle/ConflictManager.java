package net.evan.conflictbattle;

import fr.versamc.game.GameAPI;
import fr.versamc.game.teams.TeamManager;
import fr.versamc.game.teams.Teams;
import fr.versamc.game.utils.Cuboid;
import fr.versamc.game.utils.TitleManager;
import net.evan.conflictbattle.game.ConflictPhase;
import net.evan.conflictbattle.game.ConflictState;
import net.evan.conflictbattle.runnables.CooldownRoundRunnable;
import net.evan.conflictbattle.teams.ConflictTeam;
import net.evan.conflictbattle.totem.ConflictTotem;
import net.evan.conflictbattle.totem.ConflictTotemManager;
import net.evan.conflictbattle.utils.inventory.ItemBuilder;
import net.evan.conflictbattle.utils.messages.Messages;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ConflictManager {

    private final ConflictBattle conflictBattle;
    private final TeamManager teamManager;
    private final ConflictTotemManager conflictTotemManager;

    private final Messages messages;

    private ConflictState state = ConflictState.WAITING;
    private ConflictPhase phase = ConflictPhase.NONE;

    private List<ConflictTeam> teams = new ArrayList<>();
    private List<ConflictPlayer> players = new ArrayList<>();
    private List<Location> blocks = new ArrayList<>();

    private long startedAt = 0L;

    public ConflictManager(ConflictBattle conflictBattle) {
        this.conflictBattle = conflictBattle;
        this.teamManager = GameAPI.getVersaGameAPI().getTeamManager();
        this.conflictTotemManager = new ConflictTotemManager(conflictBattle);
        this.messages = new Messages();

        teams.add(new ConflictTeam("Rouge", Teams.RED, new Location(Bukkit.getWorlds().get(0), -12.5, 104, 120.5, -180f, -0.5f),
                new Location(Bukkit.getWorlds().get(0), -8.5, 106, 102.5),
                new ConflictTotem(Teams.RED, new Location(Bukkit.getWorlds().get(0), -12, 102, 104))));
        teams.add(new ConflictTeam("Bleu", Teams.BLUE, new Location(Bukkit.getWorlds().get(0), -12.5, 104, -7.5, 0f, -0.5f),
                new Location(Bukkit.getWorlds().get(0), -15.5, 106, 8.5),
                new ConflictTotem(Teams.BLUE, new Location(Bukkit.getWorlds().get(0), -12, 102, 8))));


    }

    public void updateTeamPoints(Teams team) {
        int points = 0;
        for (Player player : GameAPI.getVersaGameAPI().getTeamManager().getTeamPlayerList(team)) {
            ConflictPlayer conflictPlayer = this.getPlayer(player);
            System.out.println("Points: " + conflictPlayer.getPoints());

            points += conflictPlayer.getPoints();
        }
        this.getFromTeam(team).setPoints(points);

        System.out.println(points);
        if (points >= 1000) {

            this.resetPoints();
            this.conflictBattle.getConflictManager().getFromTeam(team).addWonRound();
            this.conflictBattle.getConflictManager().getPhase().setWinner(team);

            clearAndGiveKits();
            for (ConflictPlayer conflictPlayer : conflictBattle.getConflictManager().getPlayers()) {
                conflictPlayer.getBukkitPlayer().performCommand("kit");
            }

            new CooldownRoundRunnable(conflictBattle).runTaskTimer(conflictBattle, 0, 20);

            for (ConflictTeam conflictTeam : conflictBattle.getConflictManager().getTeams()) {
                if (conflictTeam.getWonRounds() >= 2) {
                    this.conflictBattle.getConflictManager().setPhase(ConflictPhase.NONE);
                    checkForWin();
                    return;
                }
            }

            this.conflictBattle.getConflictManager().setPhase(ConflictPhase.getById((conflictBattle.getConflictManager().getPhase().getId() + 1)));
            this.teleportPlayers();
        }

    }

    private void clearAndGiveKits() {
        for (ConflictPlayer conflictPlayer : conflictBattle.getConflictManager().getPlayers()) {
            conflictPlayer.getBukkitPlayer().getInventory().clear();
            conflictPlayer.getBukkitPlayer().getInventory().setHelmet(new ItemStack(Material.AIR));
            conflictPlayer.getBukkitPlayer().getInventory().setChestplate(new ItemStack(Material.AIR));
            conflictPlayer.getBukkitPlayer().getInventory().setLeggings(new ItemStack(Material.AIR));
            conflictPlayer.getBukkitPlayer().getInventory().setBoots(new ItemStack(Material.AIR));
            conflictPlayer.getKit().sendKits(conflictPlayer.getBukkitPlayer());
            sendDefaultKit(conflictPlayer.getBukkitPlayer());

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

    private void checkForWin() {
        ConflictTeam current = null;
        for (ConflictTeam conflictTeam : conflictBattle.getConflictManager().getTeams()) {
            if (current == null) {
                current = conflictTeam;
                continue;
            }

            if (conflictTeam.getWonRounds() > current.getWonRounds()) {
                current = conflictTeam;
            }
        }

        assert current != null;
        this.conflictBattle.getConflictManager().setState(ConflictState.FINISHING);
        Bukkit.broadcastMessage(conflictBattle.getConflictManager().getMessages().getConflictBattlePrefix() + "§7Victoire de l'équipe " + current.getTeams().getPrefix() + current.getTeams().getName() + "§7.");
        Bukkit.broadcastMessage(conflictBattle.getConflictManager().getMessages().getConflictBattlePrefix() + "§7Nettoyage de la §bcarte §7en cours... ");
        for (Location location : getBlocks()) {
            location.getBlock().setType(Material.AIR);
        }

        Bukkit.broadcastMessage("§6-------- Récapitulatif --------");
        Bukkit.broadcastMessage("§1");
        for (ConflictPhase phase : ConflictPhase.values()) {
            if (phase == ConflictPhase.NONE) continue;
            if (phase.getWinner() == null) continue;

            Bukkit.broadcastMessage("§e- " + phase.getName() + " remportée par " + phase.getWinner().getPrefix() + phase.getWinner().getName());
        }

        Bukkit.broadcastMessage("§1");
        Bukkit.broadcastMessage("§6-------------------------------");


    }

    public void resetPoints() {
        for (Teams team : teamManager.getTeamList())
            this.getFromTeam(team).setPoints(0);


        for (ConflictPlayer player : conflictBattle.getConflictManager().getPlayers())
            player.setPoints(0);

    }

    public void teleportPlayers() {
        for (Teams teams : teamManager.getTeamList()) {
            teamManager.getTeamPlayerList(teams).forEach(player -> {
                player.setLevel(0);
                player.teleport(conflictBattle.getConflictManager().getFromTeam(teams).getLocation());
            });
        }
    }

    public ConflictPlayer getPlayer(Player player) {
        return players.stream().filter(conflictPlayer -> conflictPlayer.getBukkitPlayer().getName().equals(player.getName())).findAny().get();
    }

    public ConflictTotemManager getConflictTotemManager() {
        return conflictTotemManager;
    }

    public Messages getMessages() {
        return messages;
    }

    public ConflictState getState() {
        return state;
    }

    public void setState(ConflictState state) {
        this.state = state;
    }

    public ConflictPhase getPhase() {
        return phase;
    }

    public void setPhase(ConflictPhase phase) {
        this.phase = phase;
    }

    public List<ConflictTeam> getTeams() {
        return teams;
    }

    public ConflictTeam getFromTeam(Teams team) {
        return teams.stream().filter(conflictTeam -> conflictTeam.getTeams().getName().equals(team.getName())).findAny().get();
    }

    public List<ConflictPlayer> getPlayers() {
        return players;
    }

    public void registerPlayer(Player player) {
        players.add(new ConflictPlayer(player));
    }

    public void destroyPlayer(Player player) {
        players.remove(players.stream().filter(conflictPlayer -> conflictPlayer.getBukkitPlayer().getName().equals(player.getName())).findAny().get());
    }

    public long getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(long startedAt) {
        this.startedAt = startedAt;
    }

    public List<Location> getBlocks() {
        return blocks;
    }

}
