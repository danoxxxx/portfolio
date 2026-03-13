package modele;

/**
 * Représente une cellule du centre pénitentiaire.
 * Cette classe stocke les informations relatives à l'identifiant, 
 * la capacité et la localisation d'une cellule.
 * * @author HEBSSIO
 * @version 1.0
 */
public class Cellule {
    /** Le numéro unique de la cellule (ex: "C101") */
    private String numCellule;
    
    /** Le nombre de places disponibles dans la cellule */
    private int nbPlace;
    
    /** L'identifiant du bâtiment (ex: "BatA") */
    private String numBat;

    /**
     * Construit une nouvelle instance de Cellule.
     * * @param numCellule L'identifiant unique de la cellule.
     * @param nbPlace La capacité d'accueil de la cellule.
     * @param numBat L'identifiant du bâtiment où se situe la cellule.
     */
    public Cellule(String numCellule, int nbPlace, String numBat) {
        this.numCellule = numCellule;
        this.nbPlace = nbPlace;
        this.numBat = numBat;
    }

    public String getNumCellule() { return numCellule; }
    public int getNbPlace() { return nbPlace; }
    public String getNumBat() { return numBat; }

    /**
     * Retourne une représentation de la cellule au format CSV.
     * Utile pour l'export de données ou l'affichage en console.
     * * @return Une chaîne formatée : "numCellule;nbPlace;numBat"
     */
    public String toCSV() { //
        return numCellule + ";" + nbPlace + ";" + numBat;
    }
    
    /**
     * Retourne une représentation textuelle pour le débogage.
     * @return La chaîne décrivant l'objet.
     */
    @Override
    public String toString() {
        return "Cellule{" + "id='" + numCellule + '\'' + ", places=" + nbPlace + '}';
    }

	public void setNumCellule(String numCellule) {
		this.numCellule = numCellule;
	}

	public void setNbPlace(int nbPlace) {
		this.nbPlace = nbPlace;
	}

	public void setNumBat(String numBat) {
		this.numBat = numBat;
	}
}