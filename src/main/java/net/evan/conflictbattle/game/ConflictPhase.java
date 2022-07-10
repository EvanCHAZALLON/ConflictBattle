package net.evan.conflictbattle.game;

import fr.versamc.game.teams.Teams;

import java.util.Arrays;

public enum ConflictPhase {

    FIRST_ROUND(1, "Première manche", null),
    SECOND_ROUND(2, "Deuxième manche", null),
    LAST_ROUND(3, "Dernière manche", null),

    NONE(-1, "Aucune manche", null);

    private int id;
    private String name;
    private Teams winner;

    ConflictPhase(int id, String name, Teams winner) {
        this.id = id;
        this.name = name;
        this.winner = winner;
    }

    public static ConflictPhase getById(int id) {
        return Arrays.stream(ConflictPhase.values()).filter(conflictPhase -> conflictPhase.getId() == id).findAny().get();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Teams getWinner() {
        return winner;
    }

    public void setWinner(Teams winner) {
        this.winner = winner;
    }
}
