package modele;

public class Prisons {
    private int idPrison;
    private String nomPrison;
    private String adresse;
    private int idDirecteur;

    public Prisons(int idPrison, String nomPrison, String adresse, int idDirecteur) {
        this.idPrison = idPrison;
        this.nomPrison = nomPrison;
        this.adresse = adresse;
        this.idDirecteur = idDirecteur;
    }

    // Getters
    public int getIdPrison() { return idPrison; }
    public String getNomPrison() { return nomPrison; }
    public String getAdresse() { return adresse; }
    public int getIdDirecteur() { return idDirecteur; }

    @Override
    public String toString() {
        return nomPrison + " (" + adresse + ")";
    }
    
    
}