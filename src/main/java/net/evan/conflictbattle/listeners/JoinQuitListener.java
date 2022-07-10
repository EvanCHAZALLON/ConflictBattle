package net.evan.conflictbattle.listeners;

import fr.versamc.game.GameAPI;
import fr.versamc.game.games.GameState;
import net.evan.conflictbattle.ConflictBattle;
import net.evan.conflictbattle.utils.inventory.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinQuitListener implements Listener {

    private final ConflictBattle conflictBattle;

    public JoinQuitListener(ConflictBattle conflictBattle) {
        this.conflictBattle = conflictBattle;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        GameAPI.getVersaGameAPI().getGameSettings().setState(GameState.GAME);
        Player player = event.getPlayer();
        conflictBattle.getConflictManager().registerPlayer(player);
        conflictBattle.getScoreboardManager().onLogin(player);
        player.getInventory().setItem(4, new ItemBuilder(Material.NETHER_STAR).name(ChatColor.RED + "Equipes").build());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        conflictBattle.getConflictManager().destroyPlayer(player);
        conflictBattle.getScoreboardManager().onLogout(player);
    }

}
