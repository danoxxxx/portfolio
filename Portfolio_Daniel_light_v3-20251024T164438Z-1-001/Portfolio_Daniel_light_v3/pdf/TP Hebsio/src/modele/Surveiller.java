package modele;

public class Surveiller {

    // Clé étrangère vers SURVEILLANT
    private int idPersonnel; 
    
    // Clé étrangère vers CELLULE (clé composite : numCellule + numBat)
    private String numCellule;
    private String numBat;

    public Surveiller(int idPersonnel, String numCellule, String numBat) {
        this.idPersonnel = idPersonnel;
        this.numCellule = numCellule;
        this.numBat = numBat;
    }

    // Getters
    public int getIdPersonnel() { return idPersonnel; }
    public String getNumCellule() { return numCellule; }
    public String getNumBat() { return numBat; }

    @Override
    public String toString() {
        return "Surveillant ID " + idPersonnel + " surveille Cellule " + numCellule + "/" + numBat;
    }
}