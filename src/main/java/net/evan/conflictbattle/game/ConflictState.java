package net.evan.conflictbattle.game;

public enum ConflictState {

    WAITING("Attente"),
    STARTING("Démarrage"),
    PLAYING("Jeu"),
    FINISHING("Fin de partie");

    private String stateName;

    ConflictState(String stateName) {
        this.stateName = stateName;
    }

    public String getStateName() {
        return stateName;
    }
}
