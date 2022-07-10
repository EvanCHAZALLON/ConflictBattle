package net.evan.conflictbattle.runnables;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.line.TextLine;
import net.evan.conflictbattle.ConflictBattle;
import net.evan.conflictbattle.ConflictPlayer;
import org.bukkit.scheduler.BukkitRunnable;

public class HologramRunnable extends BukkitRunnable {

    private final ConflictBattle conflictBattle;

    public HologramRunnable(ConflictBattle conflictBattle) {
        this.conflictBattle = conflictBattle;
    }

    @Override
    public void run() {
        for (ConflictPlayer conflictPlayer : conflictBattle.getConflictManager().getPlayers()) {
            Hologram hologram = conflictPlayer.getHologram();

            TextLine textLine2 = (TextLine) hologram.getLine(1);
            TextLine textLine3 = (TextLine) hologram.getLine(2);
            TextLine textLine4 = (TextLine) hologram.getLine(3);
            TextLine textLine5 = (TextLine) hologram.getLine(4);
            TextLine textLine6 = (TextLine) hologram.getLine(5);
            TextLine textLine7 = (TextLine) hologram.getLine(6);
            TextLine textLine8 = (TextLine) hologram.getLine(7);
            TextLine textLine9 = (TextLine) hologram.getLine(8);
            TextLine textLine10 = (TextLine) hologram.getLine(9);
            TextLine textLine11 = (TextLine) hologram.getLine(10);
            TextLine textLine12 = (TextLine) hologram.getLine(11);

            textLine2.setText("§9§lStatistiques");
            textLine3.setText("");
            textLine4.setText("Rôle: " + conflictPlayer.getRole().getName());
            textLine5.setText("");
            textLine6.setText("Points personnels: " + conflictPlayer.getTotalPoints());
            textLine7.setText("Dégâts au totem: " + conflictPlayer.getTotemDamage());
            textLine8.setText("");
            textLine9.setText("§eKills: " + conflictPlayer.getKills());
            textLine10.setText("§eMorts: " + conflictPlayer.getDeaths());
            textLine11.setText("§eRatio: §cSOON");
            textLine12.setText("");
        }
    }

}
