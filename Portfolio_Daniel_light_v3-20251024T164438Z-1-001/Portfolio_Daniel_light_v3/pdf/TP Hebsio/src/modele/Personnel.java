package modele;

public class Personnel {
    
    // Attributs basiques (issus de la table PERSONNEL)
    private int idPersonnel;
    private String nom;
    private String prenom;
    private int numTel;

    // Constructeur de base
    public Personnel(int idPersonnel, String nom, String prenom, int numTel) {
        this.idPersonnel = idPersonnel;
        this.nom = nom;
        this.prenom = prenom;
        this.numTel = numTel;
    }

    // Getters
    public int getIdPersonnel() { return idPersonnel; }
    public String getNom() { return nom; }
    public String getPrenom() { return prenom; }
    public int getNumTel() { return numTel; }
    
    @Override
    public String toString() {
        return nom + " " + prenom + " (ID: " + idPersonnel + ")";
    }

	public void setIdPersonnel(int idPersonnel) {
		this.idPersonnel = idPersonnel;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	public void setNumTel(int numTel) {
		this.numTel = numTel;
	}
}
