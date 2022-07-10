package net.evan.conflictbattle.runnables;

import fr.versamc.game.utils.TitleManager;
import net.evan.conflictbattle.ConflictBattle;
import net.evan.conflictbattle.ConflictPlayer;
import org.bukkit.scheduler.BukkitRunnable;

public class CooldownRoundRunnable extends BukkitRunnable {

    private final ConflictBattle conflictBattle;
    private int timer = 5;

    public CooldownRoundRunnable(ConflictBattle conflictBattle) {
        this.conflictBattle = conflictBattle;
    }

    @Override
    public void run() {
        if (timer <= 0) {
            this.cancel();
        }

        for (ConflictPlayer conflictPlayer : conflictBattle.getConflictManager().getPlayers()) {
            conflictPlayer.getBukkitPlayer().setLevel(timer);
            conflictBattle.getConflictManager().teleportPlayers();
        }

        timer--;
    }

}
