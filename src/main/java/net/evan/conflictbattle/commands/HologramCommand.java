package net.evan.conflictbattle.commands;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import net.evan.conflictbattle.ConflictBattle;
import net.evan.conflictbattle.ConflictPlayer;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class HologramCommand implements CommandExecutor {

    private final ConflictBattle conflictBattle;

    public HologramCommand(ConflictBattle conflictBattle) {
        this.conflictBattle = conflictBattle;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        ConflictPlayer conflictPlayer = conflictBattle.getConflictManager().getPlayer(player);

        Hologram hologram = HologramsAPI.createHologram(conflictBattle, player.getLocation().add(0, 5, 0));
        hologram.getVisibilityManager().showTo(player);
        hologram.getVisibilityManager().setVisibleByDefault(false);

        hologram.insertItemLine(0, new ItemStack(Material.DIAMOND_SWORD));
        hologram.insertTextLine(1, "§9§lStatistiques");
        hologram.insertTextLine(2, "");
        hologram.insertTextLine(3, "Rôle: " + conflictPlayer.getRole().getName());
        hologram.insertTextLine(4, "");
        hologram.insertTextLine(5, "Points personnels: " + conflictPlayer.getTotalPoints());
        hologram.insertTextLine(6, "Dégâts au totem: " + conflictPlayer.getTotemDamage());
        hologram.insertTextLine(7, "");
        hologram.insertTextLine(8, "§eKills: " + conflictPlayer.getKills());
        hologram.insertTextLine(9, "§eMorts: " + conflictPlayer.getDeaths());
        hologram.insertTextLine(10, "§eRatio: §cSOON");
        hologram.insertTextLine(11, "");

        conflictPlayer.setHologram(hologram);

        return false;
    }
}
