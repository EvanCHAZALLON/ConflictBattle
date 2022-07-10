package net.evan.conflictbattle.totem;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import fr.versamc.game.GameAPI;
import fr.versamc.game.utils.Cuboid;
import net.evan.conflictbattle.ConflictBattle;
import net.evan.conflictbattle.ConflictPlayer;
import net.evan.conflictbattle.teams.ConflictTeam;
import net.evan.conflictbattle.utils.bar.BarUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class ConflictTotemManager {

    private final ConflictBattle conflictBattle;

    public ConflictTotemManager(ConflictBattle conflictBattle) {
        this.conflictBattle = conflictBattle;
    }

    public void setupTotems() {
        for (ConflictTeam team : conflictBattle.getConflictManager().getTeams()) {
            Bukkit.getWorlds().get(0).getBlockAt(team.getConflictTotem().getLocation()).setType(Material.BEACON);
            Hologram teamHologram = HologramsAPI.createHologram(conflictBattle, team.getConflictTotem().getLocation());
            teamHologram.insertTextLine(0, "§a" + team.getConflictTotem().getHealth() + "§7/§e100");
            team.getConflictTotem().setHologram(teamHologram);

            setupCuboid(team);

            for (ConflictPlayer conflictPlayer : conflictBattle.getConflictManager().getPlayers()) {
                BarUtil.setBar(conflictPlayer.getBukkitPlayer(), "§cChargement...", 100);
                BarUtil.removeBar(conflictPlayer.getBukkitPlayer());
            }

        }
    }

    private void setupCuboid(ConflictTeam team) {
        ArrayList<Location> locations = getCorners(team.getConflictTotem().getLocation(), 3, 1);
        Location corner1 = locations.get(0).add(0,3,0);
        Location corner2 = locations.get(3);

        Cuboid cuboid = new Cuboid(corner1, corner2);
        team.getConflictTotem().setCuboid(cuboid);
    }

    private ArrayList<Location> getCorners(Location center, double radius, int amount) {
        World world = center.getWorld();
        ArrayList<Location> locations = new ArrayList<Location>();

        final double mainX = center.getX();
        final double mainY = center.getY();
        final double mainZ = center.getZ();

        for (int z = 0; z < 2; z++) {
            double newZ = z == 0 ? (mainZ - radius) : (mainZ + radius);
            Location corner = new Location(world, mainX - radius, mainY, newZ);
            Location corner2 = new Location(world, mainX + radius, mainY, newZ);
            double increment = (corner.distance(corner2) / amount);
            for (int i = 0; i < (amount + 1); i++) {
                locations.add(corner.clone().add(increment * i, 0.0D, 0.0D));
            }
        }

        for (int x = 0; x < 2; x++) {
            double newX = x == 0 ? (mainX - radius) : (mainX + radius);
            Location corner = new Location(world, newX, mainY, mainZ - radius);
            Location corner2 = new Location(world, newX, mainY, mainZ + radius);
            double increment = (corner.distance(corner2) / amount);
            for (int i = 0; i < (amount + 1); i++) {
                locations.add(corner.clone().add(0.0D, 0.0D, increment * i));
            }
        }
        return locations;
    }

    public void updateBossBar() {
        for (ConflictTeam conflictTeam : conflictBattle.getConflictManager().getTeams()) {
            for (Player player : GameAPI.getVersaGameAPI().getTeamManager().getTeamPlayerList(conflictTeam.getTeams())) {
                BarUtil.updateBar(player, formatBossBar(conflictTeam), 100);
            }
        }
    }

    private String formatBossBar(ConflictTeam team) {
        int health = (int) team.getConflictTotem().getHealth();

        int percentage = (health / 100) * 100;

        if (percentage <= 10 && percentage > 0) {
            return "§a■§7■■■■■■■■■ §e(" + percentage + "%)";
        }
        if (percentage <= 20 && percentage > 10) {
            return "§a■■§7■■■■■■■■ §e(" + percentage + "%)";
        }
        if (percentage <= 30 && percentage > 20) {
            return "§a■■■§7■■■■■■■ §e(" + percentage + "%)";
        }
        if (percentage <= 40 && percentage > 30) {
            return "§a■■■■§7■■■■■■ §e(" + percentage + "%)";
        }
        if (percentage <= 50 && percentage > 40) {
            return "§a■■■■■§7■■■■■ §e(" + percentage + "%)";
        }
        if (percentage <= 60 && percentage > 50) {
            return "§a■■■■■■§7■■■■ §e(" + percentage + "%)";
        }
        if (percentage <= 70 && percentage > 60) {
            return "§a■■■■■■■§7■■■ §e(" + percentage + "%)";
        }
        if (percentage <= 80 && percentage > 70) {
            return "§a■■■■■■■■§7■■ §e(" + percentage + "%)";
        }
        if (percentage <= 90 && percentage > 80) {
            return "§a■■■■■■■■■§7■ §e(" + percentage + "%)";
        }
        if (percentage <= 100 && percentage > 90) {
            return "§a■■■■■■■■■■ §e(" + percentage + "%)";
        }
        return "";

    }

}
