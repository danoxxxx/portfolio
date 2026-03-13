package modele;

public class Batiment {

    private String numBat;
    private int idPrison;

    public Batiment(String numBat, int idPrison) {
        this.numBat = numBat;
        this.idPrison = idPrison;
    }

    // Getters
    public String getNumBat() { return numBat; }
    public int getIdPrison() { return idPrison; }

    @Override
    public String toString() {
        return numBat + " (Prison ID: " + idPrison + ")";
    }
}