package net.evan.conflictbattle.kits;

import fr.versamc.game.kits.KitAPI;
import net.evan.conflictbattle.utils.inventory.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class ScoutKit extends KitAPI {

    @Override
    public void sendKits(Player player) {
        player.getInventory().addItem(new ItemStack(Material.GOLD_SWORD));
        player.getInventory().addItem(new ItemBuilder(Material.STICK).name("Recul 1").enchant(Enchantment.KNOCKBACK, 1).build());
        player.getInventory().addItem(new ItemStack(Material.STONE_AXE));
        player.getInventory().addItem(new ItemStack(Material.STONE_PICKAXE));
        //TODO: Item Potion célérité 1
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, false), false);
    }

    @Override
    public ItemStack getItemIcons(Player player) {
        return new ItemBuilder(Material.FEATHER).name(ChatColor.BLUE + "Eclaireur").build();
    }

}
