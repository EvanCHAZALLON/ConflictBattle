package net.evan.conflictbattle;

import fr.versamc.game.kits.KitAPI;
import net.evan.conflictbattle.commands.HologramCommand;
import net.evan.conflictbattle.commands.KitCommand;
import net.evan.conflictbattle.kits.*;
import net.evan.conflictbattle.listeners.*;
import net.evan.conflictbattle.roles.RoleManager;
import net.evan.conflictbattle.runnables.LobbyCheckRunnable;
import net.evan.conflictbattle.utils.inventory.FastInvManager;
import net.evan.conflictbattle.utils.scoreboard.ScoreboardManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class ConflictBattle extends JavaPlugin {

    public static ConflictBattle INSTANCE;

    private ConflictManager conflictManager;
    private RoleManager roleManager;

    private ScoreboardManager scoreboardManager;
    private ScheduledExecutorService executorMonoThread;
    private ScheduledExecutorService scheduledExecutorService;

    @Override
    public void onEnable() {
        INSTANCE = this;
        FastInvManager.register(this);

        this.conflictManager = new ConflictManager(this);
        this.roleManager = new RoleManager(this);

        scheduledExecutorService = Executors.newScheduledThreadPool(16);
        executorMonoThread = Executors.newScheduledThreadPool(1);
        scoreboardManager = new ScoreboardManager(this);

        KitAPI.kitAPIList.add(new ArcherKit());
        KitAPI.kitAPIList.add(new ScoutKit());
        KitAPI.kitAPIList.add(new SnowmanKit());
        KitAPI.kitAPIList.add(new SwordmanKit());
        KitAPI.kitAPIList.add(new WizardKit());

        Bukkit.getPluginManager().registerEvents(new JoinQuitListener(this), this);
        Bukkit.getPluginManager().registerEvents(new ItemsLitener(this), this);
        Bukkit.getPluginManager().registerEvents(new CombatListener(this), this);
        Bukkit.getPluginManager().registerEvents(new TotemListener(this), this);
        Bukkit.getPluginManager().registerEvents(new CancelListener(this), this);

        getCommand("kit").setExecutor(new KitCommand(this));
        getCommand("holotest").setExecutor(new HologramCommand(this));

        LobbyCheckRunnable lobbyCheckRunnable = new LobbyCheckRunnable(this);
        lobbyCheckRunnable.runTaskTimer(this, 0, 20);

        super.onEnable();
    }

    @Override
    public void onDisable() {
        scoreboardManager.onDisable();
        super.onDisable();
    }

    public ConflictManager getConflictManager() {
        return conflictManager;
    }

    public RoleManager getRoleManager() {
        return roleManager;
    }

    public ScheduledExecutorService getExecutorMonoThread() {
        return executorMonoThread;
    }

    public ScheduledExecutorService getScheduledExecutorService() {
        return scheduledExecutorService;
    }

    public ScoreboardManager getScoreboardManager() {
        return scoreboardManager;
    }

    public static ConflictBattle getInstance() {
        return INSTANCE;
    }
}
