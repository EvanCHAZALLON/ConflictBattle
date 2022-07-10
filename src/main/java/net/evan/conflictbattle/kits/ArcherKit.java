package net.evan.conflictbattle.kits;

import fr.versamc.game.kits.KitAPI;
import net.evan.conflictbattle.utils.inventory.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ArcherKit extends KitAPI {

    @Override
    public void sendKits(Player player) {
        player.getInventory().addItem(new ItemStack(Material.WOOD_SWORD));
        player.getInventory().addItem(new ItemStack(Material.BOW));
        player.getInventory().addItem(new ItemStack(Material.ARROW, 16));
    }

    @Override
    public ItemStack getItemIcons(Player player) {
        return new ItemBuilder(Material.BOW).name(ChatColor.BLUE + "Archer").build();
    }

}
