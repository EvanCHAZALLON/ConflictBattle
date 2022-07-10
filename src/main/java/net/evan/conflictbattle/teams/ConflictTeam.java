package net.evan.conflictbattle.teams;

import fr.versamc.game.teams.Teams;
import net.evan.conflictbattle.totem.ConflictTotem;
import org.bukkit.Location;

public class ConflictTeam {

    private String name;
    private Teams teams;

    private Location location;
    private Location hologramLocation;

    private int points;
    private int wonRounds;

    private ConflictTotem conflictTotem;

    public ConflictTeam(String name, Teams teams, Location location, Location hologramLocation, ConflictTotem conflictTotem) {
        this.name = name;
        this.teams = teams;
        this.location = location;
        this.hologramLocation = hologramLocation;
        this.points = 0;
        this.wonRounds = 0;
        this.conflictTotem = conflictTotem;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Teams getTeams() {
        return teams;
    }

    public void setTeams(Teams teams) {
        this.teams = teams;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Location getHologramLocation() {
        return hologramLocation;
    }

    public void setHologramLocation(Location hologramLocation) {
        this.hologramLocation = hologramLocation;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void addWonRound() {
        this.wonRounds += 1;
    }

    public int getWonRounds() {
        return wonRounds;
    }

    public void setWonRounds(int wonRounds) {
        this.wonRounds = wonRounds;
    }

    public ConflictTotem getConflictTotem() {
        return conflictTotem;
    }

    public void setConflictTotem(ConflictTotem conflictTotem) {
        this.conflictTotem = conflictTotem;
    }
}
