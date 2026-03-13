

package modele;
import java.sql.*;
import java.util.ArrayList;

public class Detenu {
	    private int numEcrou;
	    private String nom;
	    private String prenom;
	    private String crime;
	    private int tmpsIncarceration;
	    private String numCellule;
	    private String numBat;
	    private Connection connexion ;
		private Statement st;
		private PreparedStatement st2;

	    public Detenu(int numEcrou, String nom, String prenom, String crime, int tmpsIncarceration, String numCellule, String numBat) {
	        this.numEcrou = numEcrou;
	        this.nom = nom;
	        this.prenom = prenom;
	        this.crime = crime;
	        this.tmpsIncarceration = tmpsIncarceration;
	        this.numCellule = numCellule;
	        this.numBat = numBat;
	    }

	    public String getCrime() {
			return crime;
		}

		public void setCrime(String crime) {
			this.crime = crime;
		}

		public int getTmpsIncarceration() {
			return tmpsIncarceration;
		}

		public void setTmpsIncarceration(int tmpsIncarceration) {
			this.tmpsIncarceration = tmpsIncarceration;
		}

		public String getNumCellule() {
			return numCellule;
		}

		public void setNumCellule(String numCellule) {
			this.numCellule = numCellule;
		}

		public String getNumBat() {
			return numBat;
		}

		public void setNumBat(String numBat) {
			this.numBat = numBat;
		}

		public void setNumEcrou(int numEcrou) {
			this.numEcrou = numEcrou;
		}

		public void setNom(String nom) {
			this.nom = nom;
		}

		public void setPrenom(String prenom) {
			this.prenom = prenom;
		}

		// Getters
	    public int getNumEcrou() { return numEcrou; }
	    public String getNom() { return nom; }
	    public String getPrenom() { return prenom; }
	    // ... ajoute les autres getters si besoin

		// ... (code précédent de la classe Modele)

	    // Récupère la liste de tous les détenus
	    public ArrayList<Detenu> getLesPrisonniers() {
	        ArrayList<Detenu> lesDetenus = new ArrayList<>();
	        String requete = "SELECT * FROM DETENU";

	        try (Connection con = DriverManager.getConnection("jdbc:mysql:// 172.16.203.106/contact?zeroDateTimeBehavior=CONVERT_TO_NULL&serverTimezone=UTC ", "sio", "slam");
	             Statement stmt = con.createStatement();
	             ResultSet rs = stmt.executeQuery(requete)) {

	            while (rs.next()) {
	                Detenu d = new Detenu(
	                    rs.getInt("numEcrou"),
	                    rs.getString("nom"),
	                    rs.getString("prenom"),
	                    rs.getString("crime"),
	                    rs.getInt("tmpsIncarceration"),
	                    rs.getString("numCellule"),
	                    rs.getString("numBat")
	                );
	                lesDetenus.add(d);
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return lesDetenus;
	    }


	    @Override
	    public String toString() {
	        return "Detenu " + numEcrou + " : " + nom + " " + prenom + " (" + crime + ")";
	    }

		
	public void ajouterCellule(Cellule c) {
	    String requete = "INSERT INTO CELLULE (numCellule, NbPlace, numBat) VALUES ('" 
	                     + c.getNumCellule() + "', " + c.getNbPlace() + ", '" + c.getNumBat() + "')";
	    try (Connection con = DriverManager.getConnection("jdbc:mysql:// 172.16.203.106/HEBSSIO?zeroDateTimeBehavior=CONVERT_TO_NULL&serverTimezone=UTC ", "sio", "slam");
	         Statement stmt = con.createStatement()) {
	        stmt.executeUpdate(requete);
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	// Méthode à ajouter dans la classe Detenu pour faciliter les tests et l'export
	public String toCSV() {
	    return numEcrou + ";" + nom + ";" + prenom + ";" + crime + ";" + tmpsIncarceration + ";" + numCellule + ";" + numBat;
	}
}

