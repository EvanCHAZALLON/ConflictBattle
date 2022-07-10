package net.evan.conflictbattle.runnables;

import net.evan.conflictbattle.ConflictBattle;
import org.bukkit.scheduler.BukkitRunnable;

public class BossbarRunnable extends BukkitRunnable {

    private final ConflictBattle conflictBattle;

    public BossbarRunnable(ConflictBattle conflictBattle) {
        this.conflictBattle = conflictBattle;
    }

    @Override
    public void run() {
        conflictBattle.getConflictManager().getConflictTotemManager().updateBossBar();
    }

}
