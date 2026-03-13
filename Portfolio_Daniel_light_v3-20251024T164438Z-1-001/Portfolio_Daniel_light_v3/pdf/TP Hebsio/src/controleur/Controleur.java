package controleur;

import modele.*;
import vue.*; // Importe toutes les vues
import java.util.ArrayList;
import javax.swing.SwingUtilities;

public class Controleur {
    
    private Modele modele;

    /**
     * Constructeur : initialise le Modèle et lance l'application graphique (VueMenu).
     * Assurez-vous que VueMenu possède un constructeur prenant un Controleur.
     */
    public Controleur() {
        this.modele = new Modele();
        
        // Lancement de l'interface graphique sur le thread dédié (Event Dispatch Thread)
        SwingUtilities.invokeLater(() -> {
            // Assurez-vous d'avoir une classe VueMenu prenant 'this' (le contrôleur) en argument
            // Si votre VueMenu ne prend pas de contrôleur, utilisez : new VueMenu().setVisible(true);
            new VueMenu().setVisible(true); 
        });
    }

    // --- Méthode essentielle pour lancer les différentes vues IHM (Contrôle) ---
    
    /**
     * Lance la fenêtre de gestion IHM demandée par le Menu.
     * @param type Nom de la vue à lancer.
     */
    public void afficherVue(String type) {
        switch (type) {
            case "Cellules":
                new VueCellules().setVisible(true);
                break;
            case "Detenus":
                new VueDetenus().setVisible(true);
                break;
            case "Prisons":
                new VuePrison().setVisible(true);
                break;
            case "Directeurs":
                new VueDirecteur().setVisible(true);
                break;
            case "Batiments":
                new VueBatiment().setVisible(true);
                break;
            case "Personnel":
                new VuePersonnel().setVisible(true);
                break;
            case "Surveillants":
                new VueSurveillant().setVisible(true);
                break;
            case "Surveiller": // Gestion de l'affectation
                new VueSurveiller().setVisible(true);
                break;
            default:
                System.err.println("Erreur : Vue non reconnue ou non implémentée : " + type);
        }
    }
    
    // --- Méthodes de test conservées (Ancienne logique Main) ---
    
    public void lancerTestsConsole() {
        System.out.println("\n=== DÉBUT DES TESTS CONSOLE ===");

        // 1. Test du nombre de cellules
        int nbCellules = modele.getNbCellules();
        System.out.println("=== STATISTIQUES ===");
        System.out.println("Nombre total de cellules : " + nbCellules);

        // 2. Affichage de toutes les cellules
        System.out.println("\n=== LISTE DES CELLULES ===");
        ArrayList<Cellule> cellules = modele.getLesCellules();
        for (Cellule c : cellules) {
            // Assurez-vous que la méthode toCSV() existe dans la classe Cellule
            // Sinon utilisez c.toString()
            System.out.println(c.toString()); 
        }

        // 3. Affichage des détenus
        System.out.println("\n=== LISTE DES DÉTENUS ===");
        ArrayList<Detenu> detenus = modele.getLesPrisonniers();
        for (Detenu d : detenus) {
            System.out.println(d.toString());
        }
    }

    // --- Point d'entrée de l'application (Main) ---

    public static void main(String[] args) {
        Controleur controleur = new Controleur();
        
        // Optionnel : Lancer les tests console si besoin
        // controleur.lancerTestsConsole();
    }
}