package net.evan.conflictbattle.kits;

import fr.versamc.game.kits.KitAPI;
import net.evan.conflictbattle.utils.inventory.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SnowmanKit extends KitAPI {

    @Override
    public void sendKits(Player player) {
        player.getInventory().addItem(new ItemStack(Material.GOLD_SWORD));
        player.getInventory().addItem(new ItemStack(Material.SNOW_BALL, 32));
    }

    @Override
    public ItemStack getItemIcons(Player player) {
        return new ItemBuilder(Material.SNOW_BALL).name(ChatColor.BLUE + "Snowman").build();
    }

}
