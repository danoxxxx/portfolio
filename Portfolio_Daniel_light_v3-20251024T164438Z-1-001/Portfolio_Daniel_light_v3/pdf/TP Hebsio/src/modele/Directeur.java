package modele;


public class Directeur extends Personnel {
    
    private String dateIntegration; // Utiliser String pour simplifier la saisie dans Swing

    public Directeur(int idPersonnel, String nom, String prenom, int numTel, String dateIntegration) {
        // Appel au constructeur de la classe parente (Personnel)
        super(idPersonnel, nom, prenom, numTel);
        this.dateIntegration = dateIntegration;
    }

    // Getter spécifique
    public String getDateIntegration() { return dateIntegration; }
}