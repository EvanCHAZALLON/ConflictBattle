package net.evan.conflictbattle.roles;

public enum Role {

    COMMANDING_OFFICER("Commandant", 500),
    OFFICER("Officier", 25),
    SOLDIER("Soldat", 10),
    NONE("Aucun", 0);

    private String name;
    private int pointsPerKill;

    Role(String name, int pointsPerKill) {
        this.name = name;
        this.pointsPerKill = pointsPerKill;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPointsPerKill() {
        return pointsPerKill;
    }

    public void setPointsPerKill(int pointsPerKill) {
        this.pointsPerKill = pointsPerKill;
    }
}
