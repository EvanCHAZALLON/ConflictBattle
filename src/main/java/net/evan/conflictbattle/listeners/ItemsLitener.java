package net.evan.conflictbattle.listeners;

import fr.versamc.game.GameAPI;
import fr.versamc.game.teams.TeamManager;
import fr.versamc.game.teams.Teams;
import net.evan.conflictbattle.ConflictBattle;
import net.evan.conflictbattle.utils.inventory.FastInv;
import net.evan.conflictbattle.utils.inventory.ItemBuilder;
import net.spigotmc.tagapi.api.TagAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;
import java.util.List;

public class ItemsLitener implements Listener {

    private final ConflictBattle conflictBattle;

    public ItemsLitener(ConflictBattle conflictBattle) {
        this.conflictBattle = conflictBattle;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getItem() == null) return;

        Player player = event.getPlayer();
        TeamManager teamManager = GameAPI.getVersaGameAPI().getTeamManager();

        if (event.getItem().getType() == Material.NETHER_STAR) {
            FastInv fastInv = new FastInv(9, event.getItem().getItemMeta().getDisplayName());


            fastInv.setItem(8, new ItemBuilder(Material.BONE).name("§fAléatoire").build(), e -> {
                Player p = (Player) e.getWhoClicked();
                GameAPI.getVersaGameAPI().getTeamManager().removePlayerFromAll(p);
                p.closeInventory();
                p.sendMessage(conflictBattle.getConflictManager().getMessages().getConflictBattlePrefix() + "§7Vous avez quitté votre équipe.");
            });

            int i = 0;
            for (Teams team : teamManager.getTeamList()) {
                fastInv.setItem(i, new ItemBuilder(Material.WOOL).data(team.getDyeColor().getData()).name(team.getPrefix() + team.getName()).lore(getLoreForTeam(team)).build(), inventoryClickEvent -> {
                    if (inventoryClickEvent.getCurrentItem().getType() == Material.AIR) return;
                    inventoryClickEvent.setCancelled(true);

                    String teamName = inventoryClickEvent.getCurrentItem().getItemMeta().getDisplayName().substring(2);
                    GameAPI.getVersaGameAPI().getTeamManager().removePlayerFromAll(player);
                    teamManager.addPlayerTeam(player, Teams.getTeamByName(teamName));
                    conflictBattle.getConflictManager().getPlayer(player).setConflictTeam(conflictBattle.getConflictManager().getFromTeam(Teams.getTeamByName(teamName)));

                    player.sendMessage(conflictBattle.getConflictManager().getMessages().getConflictBattlePrefix() + "§7Vous avez rejoint l'équipe " + Teams.getTeamByName(teamName).getPrefix() + Teams.getTeamByName(teamName).getName());
                    player.closeInventory();
                });
                i++;

            }

            fastInv.open(player);
        }

    }

    private List<String> getLoreForTeam(Teams team) {
        List<String> lore = new ArrayList<>();

        TeamManager teamManager = GameAPI.getVersaGameAPI().getTeamManager();

        if (teamManager.getTeamPlayerList(team).size() == 0) {
            for (int i = 0; i < (GameAPI.getVersaGameAPI().getGameSettings().getMaxPlayers() / 2); i++) {
                lore.add("§7Emplacement vide");
            }
        }

        if (teamManager.getTeamPlayerList(team).size() != 0) {
            int i = 0;
            for (Player player : teamManager.getTeamPlayerList(team)) {
                lore.add(team.getPrefix() + player.getName());
                i++;
            }

            int diff = (GameAPI.getVersaGameAPI().getGameSettings().getMaxPlayers() / 2) - i;
            for (int j = 0; j < diff; j++) {
                lore.add("§7Emplacement vide");
            }

        }

        return lore;
    }

}
