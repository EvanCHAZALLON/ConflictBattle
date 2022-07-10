package net.evan.conflictbattle.totem;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import fr.versamc.game.teams.Teams;
import fr.versamc.game.utils.Cuboid;
import org.bukkit.Location;

public class ConflictTotem {

    private Teams team;
    private Location location;
    private double health;

    private boolean destroyed;
    private long timeDestroyed;

    private Hologram hologram;
    private Cuboid cuboid;

    public ConflictTotem(Teams team, Location location) {
        this.team = team;
        this.location = location;
        this.health = 100;
        this.destroyed = false;
        this.timeDestroyed = 0;
        this.hologram = null;
        this.cuboid = new Cuboid(location.add(3, 3, 3), location.subtract(3, 3, 3));
    }

    public Teams getTeam() {
        return team;
    }

    public void setTeam(Teams team) {
        this.team = team;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public double getHealth() {
        return health;
    }

    public void setHealth(double health) {
        this.health = health;
    }

    public void damage() {
        this.health -= 1;
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public void setDestroyed(boolean destroyed) {
        this.destroyed = destroyed;
    }

    public long getTimeDestroyed() {
        return timeDestroyed;
    }

    public void setTimeDestroyed(long timeDestroyed) {
        this.timeDestroyed = timeDestroyed;
    }

    public Hologram getHologram() {
        return hologram;
    }

    public void setHologram(Hologram hologram) {
        this.hologram = hologram;
    }

    public Cuboid getCuboid() {
        return cuboid;
    }

    public void setCuboid(Cuboid cuboid) {
        this.cuboid = cuboid;
    }

}
