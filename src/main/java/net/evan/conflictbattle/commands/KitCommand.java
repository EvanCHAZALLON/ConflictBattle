package net.evan.conflictbattle.commands;

import fr.versamc.game.kits.KitAPI;
import net.evan.conflictbattle.ConflictBattle;
import net.evan.conflictbattle.game.ConflictState;
import net.evan.conflictbattle.utils.inventory.FastInv;
import net.evan.conflictbattle.utils.inventory.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class KitCommand implements CommandExecutor {

    private final ConflictBattle conflictBattle;

    public KitCommand(ConflictBattle conflictBattle) {
        this.conflictBattle = conflictBattle;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (conflictBattle.getConflictManager().getState() != ConflictState.PLAYING) return false;

        Player player = (Player) sender;
        FastInv kitInventory = new FastInv(3*9, "Kits");

        for (KitAPI kit : KitAPI.kitAPIList)
            kitInventory.addItem(kit.getItemIcons(player), e -> {
                player.getInventory().clear();
                player.getInventory().setHelmet(new ItemStack(Material.AIR));
                player.getInventory().setChestplate(new ItemStack(Material.AIR));
                player.getInventory().setLeggings(new ItemStack(Material.AIR));
                player.getInventory().setBoots(new ItemStack(Material.AIR));
                kit.sendKits((Player) e.getWhoClicked());
                sendDefaultKit(player);
                player.sendMessage(conflictBattle.getConflictManager().getMessages().getConflictBattlePrefix() + "§7Vous avez choisi le kit §e" + e.getCurrentItem().getItemMeta().getDisplayName());
                e.getWhoClicked().closeInventory();
            });

        kitInventory.open(player);
        return false;
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

}
