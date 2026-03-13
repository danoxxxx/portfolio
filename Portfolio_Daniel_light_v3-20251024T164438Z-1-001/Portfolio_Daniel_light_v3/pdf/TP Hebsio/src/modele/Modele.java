package modele;
import java.sql.*;
import java.util.ArrayList;

/**
 * Gestionnaire d'accès aux données (Data Access Object).
 * Cette classe gère la connexion à la base de données MySQL et l'exécution 
 * des requêtes pour récupérer les cellules et les détenus.
 * * <p>
 * Configuration BDD : 172.16.203.106 / Base: contact
 * </p>
 */
public class Modele {

    /**
     * Compte le nombre total de cellules enregistrées en base.
     * Exécute la requête : "SELECT COUNT(*) FROM CELLULE"
     * * @return Le nombre entier de cellules, ou 0 si une erreur survient.
     */
    public int getNbCellules() { //
        int count = 0;
        String requete = "SELECT COUNT(*) FROM CELLULE";
        
        // Gestion des ressources avec try-with-resources pour fermeture automatique
        try (Connection con = DriverManager.getConnection("jdbc:mysql://172.16.203.106/HEBSSIO?zeroDateTimeBehavior=CONVERT_TO_NULL&serverTimezone=UTC", "sio", "slam");
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(requete)) {
             
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }
    
    /**
     * Récupère la liste complète des cellules.
     * Mappe chaque ligne de la table CELLULE vers un objet Java {@link Cellule}.
     * * @return Une ArrayList contenant toutes les cellules trouvées.
     */
    public ArrayList<Cellule> getLesCellules() { //
        ArrayList<Cellule> lesCellules = new ArrayList<>();
        String requete = "SELECT * FROM CELLULE";
        
        try (Connection con = DriverManager.getConnection("jdbc:mysql://172.16.203.106/HEBSSIO?zeroDateTimeBehavior=CONVERT_TO_NULL&serverTimezone=UTC", "sio", "slam");
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(requete)) {
             
            while (rs.next()) {
                Cellule c = new Cellule(
                    rs.getString("numCellule"),
                    rs.getInt("NbPlace"),
                    rs.getString("numBat")
                );
                lesCellules.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lesCellules;
    }

    /**
     * Récupère la liste complète des détenus.
     * * @return Une ArrayList d'objets {@link Detenu}.
     */
    public ArrayList<Detenu> getLesPrisonniers() {
        
        return new ArrayList<>(); // Simplifié pour l'exemple de doc
    }
 // Ajoute un détenu dans la BDD
    public void ajouterDetenu(Detenu d) {
        // Attention : numEcrou est souvent auto-incrémenté, mais ici on le passe manuellement selon ton constructeur
        String requete = "INSERT INTO DETENU (numEcrou, nom, prenom, crime, tmpsIncarceration, numCellule, numBat) VALUES (" 
                         + d.getNumEcrou() + ", '" 
                         + d.getNom() + "', '" 
                         + d.getPrenom() + "', '" 
                         + d.getCrime() + "', " 
                         + "0, '" // tmpsIncarceration à 0 par défaut ou à récupérer
                         + d.getNumCellule() + "', '" 
                         + d.getNumBat() + "')";
                         
        try (Connection con = DriverManager.getConnection("jdbc:mysql://172.16.203.106/HEBSSIO?zeroDateTimeBehavior=CONVERT_TO_NULL&serverTimezone=UTC", "sio", "slam");
             Statement stmt = con.createStatement()) {
            stmt.executeUpdate(requete);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
 
    public void ajouterCellule(Cellule c) {
        String requete = "INSERT INTO CELLULE (numCellule, NbPlace, numBat) VALUES ('" 
                         + c.getNumCellule() + "', " + c.getNbPlace() + ", '" + c.getNumBat() + "')";
        try (Connection con = DriverManager.getConnection("jdbc:mysql://172.16.203.106/HEBSSIO?zeroDateTimeBehavior=CONVERT_TO_NULL&serverTimezone=UTC", "sio", "slam");
             Statement stmt = con.createStatement()) {
            stmt.executeUpdate(requete);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
 // --- Suppression d'un détenu ---
    public void supprimerDetenu(int numEcrou) {
        String requete = "DELETE FROM DETENU WHERE numEcrou = " + numEcrou;
        try (Connection con = DriverManager.getConnection("jdbc:mysql://172.16.203.106/HEBSSIO?zeroDateTimeBehavior=CONVERT_TO_NULL&serverTimezone=UTC", "sio", "slam");
             Statement stmt = con.createStatement()) {
            stmt.executeUpdate(requete);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // --- Suppression d'une cellule ---
    public void supprimerCellule(String numCellule, String numBat) {
        // Attention aux guillemets simples ' ' car ce sont des chaînes de caractères (CHAR/VARCHAR)
        String requete = "DELETE FROM CELLULE WHERE numCellule = '" + numCellule + "' AND numBat = '" + numBat + "'";
        try (Connection con = DriverManager.getConnection("jdbc:mysql://172.16.203.106/HEBSSIO?zeroDateTimeBehavior=CONVERT_TO_NULL&serverTimezone=UTC", "sio", "slam");
             Statement stmt = con.createStatement()) {
            stmt.executeUpdate(requete);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
 // --- Fonctions pour PRISON ---

 // 1. Lire (Récupérer toutes les prisons)
    public ArrayList<Prisons> getLesPrisons() {
     ArrayList<Prisons> lesPrisons = new ArrayList<>();
     String requete = "SELECT * FROM PRISON";
     try (Connection con = DriverManager.getConnection("jdbc:mysql://172.16.203.106/HEBSSIO?zeroDateTimeBehavior=CONVERT_TO_NULL&serverTimezone=UTC", "sio", "slam");
          Statement stmt = con.createStatement();
          ResultSet rs = stmt.executeQuery(requete)) {

         while (rs.next()) {
             Prisons p = new Prisons(
                 rs.getInt("idPrison"),
                 rs.getString("nomPrison"),
                 rs.getString("adresse"),
                 rs.getInt("idDirecteur")
             );
             lesPrisons.add(p);
         }
     } catch (SQLException e) {
         e.printStackTrace();
     }
     return lesPrisons;
 }

 // 2. Ajouter (Créer) une prison
   public void ajouterPrison(Prisons p) {
     // Vérifiez que la table PRISON n'a pas idPrison en auto-incrément
     String requete = "INSERT INTO PRISON (idPrison, nomPrison, adresse, idDirecteur) VALUES (" 
                      + p.getIdPrison() + ", '" 
                      + p.getNomPrison() + "', '" 
                      + p.getAdresse() + "', " 
                      + p.getIdDirecteur() + ")";
     try (Connection con = DriverManager.getConnection("jdbc:mysql://172.16.203.106/HEBSSIO?zeroDateTimeBehavior=CONVERT_TO_NULL&serverTimezone=UTC", "sio", "slam");
          Statement stmt = con.createStatement()) {
         stmt.executeUpdate(requete);
     } catch (SQLException e) {
         e.printStackTrace();
     }
 }

 // 3. Supprimer une prison
 public void supprimerPrison(int idPrison) {
     // ATTENTION : Cette requête échouera si des BATIMENTs sont encore liés à cette prison !
     String requete = "DELETE FROM PRISON WHERE idPrison = " + idPrison;
     try (Connection con = DriverManager.getConnection("jdbc:mysql://172.16.203.106/HEBSSIO?zeroDateTimeBehavior=CONVERT_TO_NULL&serverTimezone=UTC", "sio", "slam");
          Statement stmt = con.createStatement()) {
         stmt.executeUpdate(requete);
     } catch (SQLException e) {
         e.printStackTrace();
     }
 }
 
//--- Fonctions pour PERSONNEL ---

	private void ajouterPersonnel(Personnel p) throws SQLException {
	  String requete = "INSERT INTO PERSONNEL (idPersonnel, nom, prenom, numTel) VALUES (" 
	                   + p.getIdPersonnel() + ", '" 
	                   + p.getNom() + "', '" 
	                   + p.getPrenom() + "', " 
	                   + p.getNumTel() + ")";
	  
	  // Utiliser un PreparedStatement si on veut une transaction propre, 
	  // mais on reste avec Statement pour la cohérence du projet.
	  try (Connection con = DriverManager.getConnection("jdbc:mysql://172.16.203.106/HEBSSIO?zeroDateTimeBehavior=CONVERT_TO_NULL&serverTimezone=UTC", "sio", "slam");
	       Statement stmt = con.createStatement()) {
	      stmt.executeUpdate(requete);
	  }
	}
	
	private void supprimerPersonnel(int idPersonnel) throws SQLException {
	  String requete = "DELETE FROM PERSONNEL WHERE idPersonnel = " + idPersonnel;
	  try (Connection con = DriverManager.getConnection("jdbc:mysql://172.16.203.106/HEBSSIO?zeroDateTimeBehavior=CONVERT_TO_NULL&serverTimezone=UTC", "sio", "slam");
	       Statement stmt = con.createStatement()) {
	      stmt.executeUpdate(requete);
	  }
	}
	
	
	//--- Fonctions pour DIRECTEUR ---
	
	public void ajouterDirecteur(Directeur d) throws SQLException {
	  // 1. Ajouter d'abord dans la table parent (PERSONNEL)
	  ajouterPersonnel(d); 
	
	  // 2. Ajouter ensuite dans la table enfant (DIRECTEUR)
	  String requeteDirecteur = "INSERT INTO DIRECTEUR (idPersonnel, dateIntegration) VALUES (" 
	                            + d.getIdPersonnel() + ", '" 
	                            + d.getDateIntegration() + "')";
	                            
	  try (Connection con = DriverManager.getConnection("jdbc:mysql://172.16.203.106/HEBSSIO?zeroDateTimeBehavior=CONVERT_TO_NULL&serverTimezone=UTC", "sio", "slam");
	       Statement stmt = con.createStatement()) {
	      stmt.executeUpdate(requeteDirecteur);
	  }
	}
	
	public void supprimerDirecteur(int idPersonnel) throws SQLException {
	  // 1. Supprimer d'abord de la table enfant (DIRECTEUR)
	  String requeteDirecteur = "DELETE FROM DIRECTEUR WHERE idPersonnel = " + idPersonnel;
	  try (Connection con = DriverManager.getConnection("jdbc:mysql://172.16.203.106/HEBSSIO?zeroDateTimeBehavior=CONVERT_TO_NULL&serverTimezone=UTC", "sio", "slam");
	       Statement stmt = con.createStatement()) {
	      stmt.executeUpdate(requeteDirecteur);
	  }
	
	  // 2. Supprimer ensuite de la table parent (PERSONNEL)
	  // NOTE: Si la clé étrangère de DIRECTEUR est bien définie en CASCADE, 
	  // la suppression de PERSONNEL suffit, mais on assure les deux pour la robustesse.
	  supprimerPersonnel(idPersonnel); 
	}
	
	public ArrayList<Directeur> getLesDirecteurs() {
	  ArrayList<Directeur> lesDirecteurs = new ArrayList<>();
	  // Jointure pour récupérer toutes les informations (Personnel + Directeur)
	  String requete = "SELECT P.idPersonnel, P.nom, P.prenom, P.numTel, D.dateIntegration "
	                 + "FROM PERSONNEL P JOIN DIRECTEUR D ON P.idPersonnel = D.idPersonnel";
	                 
	  try (Connection con = DriverManager.getConnection("jdbc:mysql://172.16.203.106/HEBSSIO?zeroDateTimeBehavior=CONVERT_TO_NULL&serverTimezone=UTC", "sio", "slam");
	       Statement stmt = con.createStatement();
	       ResultSet rs = stmt.executeQuery(requete)) {
	
	      while (rs.next()) {
	          Directeur d = new Directeur(
	              rs.getInt("idPersonnel"),
	              rs.getString("nom"),
	              rs.getString("prenom"),
	              rs.getInt("numTel"),
	              rs.getString("dateIntegration")
	          );
	          lesDirecteurs.add(d);
	      }
	  } catch (SQLException e) {
	      e.printStackTrace();
	  }
	  return lesDirecteurs;
	}
	
	//--- Fonctions pour BATIMENT ---
	
	//1. Lire (Récupérer tous les bâtiments)
	public ArrayList<Batiment> getLesBatiments() {
	 ArrayList<Batiment> lesBatiments = new ArrayList<>();
	 String requete = "SELECT numBat, idPrison FROM BATIMENT";
	 try (Connection con = DriverManager.getConnection("jdbc:mysql://172.16.203.106/HEBSSIO?zeroDateTimeBehavior=CONVERT_TO_NULL&serverTimezone=UTC", "sio", "slam");
	      Statement stmt = con.createStatement();
	      ResultSet rs = stmt.executeQuery(requete)) {
	
	     while (rs.next()) {
	         Batiment b = new Batiment(
	             rs.getString("numBat"),
	             rs.getInt("idPrison")
	         );
	         lesBatiments.add(b);
	     }
	 } catch (SQLException e) {
	     e.printStackTrace();
	 }
	 return lesBatiments;
	}
	
	//2. Ajouter un bâtiment
	public void ajouterBatiment(Batiment b) throws SQLException {
	 String requete = "INSERT INTO BATIMENT (numBat, idPrison) VALUES ('" 
	                  + b.getNumBat() + "', " 
	                  + b.getIdPrison() + ")";
	                  
	 try (Connection con = DriverManager.getConnection("jdbc:mysql://172.16.203.106/HEBSSIO?zeroDateTimeBehavior=CONVERT_TO_NULL&serverTimezone=UTC", "sio", "slam");
	      Statement stmt = con.createStatement()) {
	     stmt.executeUpdate(requete);
	 }
	}
	
	//3. Supprimer un bâtiment
	public void supprimerBatiment(String numBat) throws SQLException {
	 // ATTENTION : Cela échouera si des CELLULEs sont encore rattachées à ce bâtiment !
	 String requete = "DELETE FROM BATIMENT WHERE numBat = '" + numBat + "'";
	 try (Connection con = DriverManager.getConnection("jdbc:mysql://172.16.203.106/HEBSSIO?zeroDateTimeBehavior=CONVERT_TO_NULL&serverTimezone=UTC", "sio", "slam");
	      Statement stmt = con.createStatement()) {
	     stmt.executeUpdate(requete);
	 }
	}
	
	// --- Fonctions pour PERSONNEL (Gestion de base) ---

	// 1. Lire (Récupérer tout le personnel)
	public ArrayList<Personnel> getToutLePersonnel() {
	    ArrayList<Personnel> toutLePersonnel = new ArrayList<>();
	    // Récupère uniquement les champs de la table PERSONNEL
	    String requete = "SELECT idPersonnel, nom, prenom, numTel FROM PERSONNEL";
	    
	    try (Connection con = DriverManager.getConnection("jdbc:mysql://172.16.203.106/HEBSSIO?zeroDateTimeBehavior=CONVERT_TO_NULL&serverTimezone=UTC", "sio", "slam");
	         Statement stmt = con.createStatement();
	         ResultSet rs = stmt.executeQuery(requete)) {

	        while (rs.next()) {
	            // Instanciation de la classe Personnel
	            Personnel p = new Personnel(
	                rs.getInt("idPersonnel"),
	                rs.getString("nom"),
	                rs.getString("prenom"),
	                rs.getInt("numTel")
	            );
	            toutLePersonnel.add(p);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return toutLePersonnel;
	}

	// 2. Ajouter un Personnel
	// On utilise ici la méthode ajouterPersonnel rendue publique ou réécrite
	public void ajouterPersonnelDeBase(Personnel p) throws SQLException {
	    String requete = "INSERT INTO PERSONNEL (idPersonnel, nom, prenom, numTel) VALUES (" 
	                     + p.getIdPersonnel() + ", '" 
	                     + p.getNom() + "', '" 
	                     + p.getPrenom() + "', " 
	                     + p.getNumTel() + ")";
	    
	    try (Connection con = DriverManager.getConnection("jdbc:mysql://172.16.203.106/HEBSSIO?zeroDateTimeBehavior=CONVERT_TO_NULL&serverTimezone=UTC", "sio", "slam");
	         Statement stmt = con.createStatement()) {
	        stmt.executeUpdate(requete);
	    }
	}

	// 3. Supprimer un Personnel
	public void supprimerPersonnelDeBase(int idPersonnel) throws SQLException {
	    // ATTENTION : Cela échouera si cet ID est utilisé par DIRECTEUR ou SURVEILLANT!
	    // Ces sous-classes doivent être supprimées AVANT la suppression du Personnel de base.
	    String requete = "DELETE FROM PERSONNEL WHERE idPersonnel = " + idPersonnel;
	    try (Connection con = DriverManager.getConnection("jdbc:mysql://172.16.203.106/HEBSSIO?zeroDateTimeBehavior=CONVERT_TO_NULL&serverTimezone=UTC", "sio", "slam");
	         Statement stmt = con.createStatement()) {
	        stmt.executeUpdate(requete);
	    }
	}
	
	// --- Fonctions pour SURVEILLANT ---

	public void ajouterSurveillant(Surveillant s) throws SQLException {
	    // 1. Ajouter d'abord dans la table parent (PERSONNEL)
	    // On réutilise la méthode d'ajout de base, qui insère les champs de Personnel
	    ajouterPersonnelDeBase(s); 

	    // 2. Ajouter ensuite dans la table enfant (SURVEILLANT)
	    String requeteSurveillant = "INSERT INTO SURVEILLANT (idPersonnel, grade) VALUES (" 
	                              + s.getIdPersonnel() + ", '" 
	                              + s.getGrade() + "')";
	                              
	    try (Connection con = DriverManager.getConnection("jdbc:mysql://172.16.203.106/HEBSSIO?zeroDateTimeBehavior=CONVERT_TO_NULL&serverTimezone=UTC", "sio", "slam");
	         Statement stmt = con.createStatement()) {
	        stmt.executeUpdate(requeteSurveillant);
	    }
	}

	public void supprimerSurveillant(int idPersonnel) throws SQLException {
	    // 1. Supprimer d'abord de la table enfant (SURVEILLANT)
	    String requeteSurveillant = "DELETE FROM SURVEILLANT WHERE idPersonnel = " + idPersonnel;
	    try (Connection con = DriverManager.getConnection("jdbc:mysql://172.16.203.106/HEBSSIO?zeroDateTimeBehavior=CONVERT_TO_NULL&serverTimezone=UTC", "sio", "slam");
	         Statement stmt = con.createStatement()) {
	        stmt.executeUpdate(requeteSurveillant);
	    }

	    // 2. Supprimer ensuite de la table parent (PERSONNEL)
	    supprimerPersonnelDeBase(idPersonnel); 
	}

	public ArrayList<Surveillant> getLesSurveillants() {
	    ArrayList<Surveillant> lesSurveillants = new ArrayList<>();
	    // Jointure pour récupérer toutes les informations
	    String requete = "SELECT P.idPersonnel, P.nom, P.prenom, P.numTel, S.grade "
	                   + "FROM PERSONNEL P JOIN SURVEILLANT S ON P.idPersonnel = S.idPersonnel";
	                   
	    try (Connection con = DriverManager.getConnection("jdbc:mysql://172.16.203.106/HEBSSIO?zeroDateTimeBehavior=CONVERT_TO_NULL&serverTimezone=UTC", "sio", "slam");
	         Statement stmt = con.createStatement();
	         ResultSet rs = stmt.executeQuery(requete)) {

	        while (rs.next()) {
	            Surveillant s = new Surveillant(
	                rs.getInt("idPersonnel"),
	                rs.getString("nom"),
	                rs.getString("prenom"),
	                rs.getInt("numTel"),
	                rs.getString("grade")
	            );
	            lesSurveillants.add(s);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return lesSurveillants;
	}
	
	// --- Fonctions pour SURVEILLER (Association Surveillant <-> Cellule) ---

	// 1. Lire (Récupérer toutes les affectations de surveillance)
	public ArrayList<Surveiller> getLesAffectations() {
	    ArrayList<Surveiller> lesAffectations = new ArrayList<>();
	    String requete = "SELECT idPersonnel, numCellule, numBat FROM SURVEILLER";
	    
	    try (Connection con = DriverManager.getConnection("jdbc:mysql://172.16.203.106/HEBSSIO?zeroDateTimeBehavior=CONVERT_TO_NULL&serverTimezone=UTC", "sio", "slam");
	         Statement stmt = con.createStatement();
	         ResultSet rs = stmt.executeQuery(requete)) {

	        while (rs.next()) {
	            Surveiller s = new Surveiller(
	                rs.getInt("idPersonnel"),
	                rs.getString("numCellule"),
	                rs.getString("numBat")
	            );
	            lesAffectations.add(s);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return lesAffectations;
	}

	// 2. Affecter (Créer) une nouvelle surveillance
	public void affecterSurveillance(Surveiller s) throws SQLException {
	    // La requête doit insérer les trois clés
	    String requete = "INSERT INTO SURVEILLER (idPersonnel, numCellule, numBat) VALUES (" 
	                     + s.getIdPersonnel() + ", '" 
	                     + s.getNumCellule() + "', '" 
	                     + s.getNumBat() + "')";
	                     
	    try (Connection con = DriverManager.getConnection("jdbc:mysql://172.16.203.106/HEBSSIO?zeroDateTimeBehavior=CONVERT_TO_NULL&serverTimezone=UTC", "sio", "slam");
	         Statement stmt = con.createStatement()) {
	        stmt.executeUpdate(requete);
	    }
	}

	// 3. Désaffecter (Supprimer) une surveillance spécifique
	public void desaffecterSurveillance(int idPersonnel, String numCellule, String numBat) throws SQLException {
	    String requete = "DELETE FROM SURVEILLER WHERE idPersonnel = " + idPersonnel
	                     + " AND numCellule = '" + numCellule + "'"
	                     + " AND numBat = '" + numBat + "'";
	                     
	    try (Connection con = DriverManager.getConnection("jdbc:mysql://172.16.203.106/HEBSSIO?zeroDateTimeBehavior=CONVERT_TO_NULL&serverTimezone=UTC", "sio", "slam");
	         Statement stmt = con.createStatement()) {
	        stmt.executeUpdate(requete);
	    }
	}
}