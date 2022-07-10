package net.evan.conflictbattle;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import fr.versamc.game.GameAPI;
import fr.versamc.game.kits.KitAPI;
import fr.versamc.game.utils.TitleManager;
import net.evan.conflictbattle.kits.SwordmanKit;
import net.evan.conflictbattle.roles.Role;
import net.evan.conflictbattle.teams.ConflictTeam;
import net.evan.conflictbattle.utils.inventory.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ConflictPlayer {

    private Player bukkitPlayer;

    private ConflictTeam conflictTeam;
    private Role role;

    private int points;
    private int totalPoints;
    private int totemDamage;

    private int kills;
    private int deaths;

    private KitAPI kit;
    private Hologram hologram;

    public ConflictPlayer(Player bukkitPlayer) {
        this.bukkitPlayer = bukkitPlayer;
        this.conflictTeam = null;
        this.role = Role.NONE;
        this.points = 0;
        this.totalPoints = 0;
        this.totemDamage = 0;
        this.kills = 0;
        this.deaths = 0;
        this.kit = new SwordmanKit();
        this.hologram = null;
    }

    public Player getBukkitPlayer() {
        return bukkitPlayer;
    }

    public void setBukkitPlayer(Player bukkitPlayer) {
        this.bukkitPlayer = bukkitPlayer;
    }

    public ConflictTeam getConflictTeam() {
        return conflictTeam;
    }

    public void setConflictTeam(ConflictTeam conflictTeam) {
        this.conflictTeam = conflictTeam;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(int totalPoints) {
        this.totalPoints = totalPoints;
    }

    public void addPoints(int points) {
        this.points += points;
    }

    public void increasePoints() {
        if (getConflictTeam().getConflictTotem().isDestroyed()) {
            this.points += 10;
            this.totalPoints += 10;
            return;
        }

        this.points += role.getPointsPerKill();
        this.totalPoints += role.getPointsPerKill();
    }

    public int getTotemDamage() {
        return totemDamage;
    }

    public void setTotemDamage(int totemDamage) {
        this.totemDamage = totemDamage;
    }

    public void increaseTotemDamage() {
        this.totemDamage += 1;
    }


    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
        this.bukkitPlayer.sendMessage(ChatColor.YELLOW + "Votre rôle est désormais " + role.getName());
    }

    public int getKills() {
        return kills;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public int getDeaths() {
        return deaths;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public KitAPI getKit() {
        return kit;
    }

    public void setKit(KitAPI kit) {
        this.kit = kit;
    }

    public Hologram getHologram() {
        return hologram;
    }

    public void setHologram(Hologram hologram) {
        this.hologram = hologram;
    }

    public void respawn() {
        Player player = this.getBukkitPlayer();
        player.setHealth(player.getMaxHealth());
        player.setFoodLevel(20);

        player.getInventory().clear();
        player.getInventory().setHelmet(new ItemStack(Material.AIR));
        player.getInventory().setChestplate(new ItemStack(Material.AIR));
        player.getInventory().setLeggings(new ItemStack(Material.AIR));
        player.getInventory().setBoots(new ItemStack(Material.AIR));

        getKit().sendKits(player);
        sendDefaultKit(player);

        this.setDeaths(getDeaths() + 1);
        player.setGameMode(GameAPI.getVersaGameAPI().getGameSettings().getSpectatorMode());
        Bukkit.getScheduler().runTaskLater(ConflictBattle.getInstance(), () -> {
            player.teleport(getConflictTeam().getLocation());
            player.setGameMode(GameAPI.getVersaGameAPI().getGameSettings().getGamemode());
            TitleManager.sendTitle(player, "", "§aC'est reparti !", 20);
        }, 20*5);

    }

    private void sendDefaultKit(Player player) {
        player.getInventory().setHelmet(new ItemBuilder(Material.LEATHER_HELMET).armorColor(conflictTeam.getTeams().getColor()).build());
        player.getInventory().setChestplate(new ItemBuilder(Material.CHAINMAIL_CHESTPLATE).build());
        player.getInventory().setLeggings(new ItemBuilder(Material.LEATHER_LEGGINGS).armorColor(conflictTeam.getTeams().getColor()).build());
        player.getInventory().setBoots(new ItemBuilder(Material.LEATHER_BOOTS).armorColor(conflictTeam.getTeams().getColor()).build());
        player.getInventory().addItem(new ItemStack(Material.IRON_PICKAXE));
        player.getInventory().addItem(new ItemStack(Material.IRON_AXE));
        player.getInventory().addItem(new ItemStack(Material.COBBLESTONE, 64));
        player.getInventory().addItem(new ItemStack(Material.WOOD, 64));
    }

}
