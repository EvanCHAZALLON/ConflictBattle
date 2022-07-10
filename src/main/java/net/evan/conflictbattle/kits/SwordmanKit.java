package net.evan.conflictbattle.kits;

import fr.versamc.game.kits.KitAPI;
import net.evan.conflictbattle.utils.inventory.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SwordmanKit extends KitAPI {

    @Override
    public void sendKits(Player player) {
        player.getInventory().addItem(new ItemStack(Material.STONE_SWORD));
    }

    @Override
    public ItemStack getItemIcons(Player player) {
        return new ItemBuilder(Material.IRON_SWORD).name(ChatColor.BLUE + "Epeiste").build();
    }

}
