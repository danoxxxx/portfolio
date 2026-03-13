package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import modele.Detenu;

class DetenuTest {

    @Test
    void testConstructeurEtGetters() {
        // 1. Préparation (Arrange)
        // On crée un détenu fictif avec toutes les infos
        int numEcrou = 555;
        String nom = "Dalton";
        String prenom = "Joe";
        String crime = "Attaque de banque";
        int temps = 24; // mois
        String cell = "C10";
        String bat = "BatA";

        Detenu d = new Detenu(numEcrou, nom, prenom, crime, temps, cell, bat);

        // 2. Vérification (Assert)
        // On vérifie que chaque "getter" renvoie bien ce qu'on a donné au constructeur
        assertEquals(555, d.getNumEcrou(), "Le numéro d'écrou est incorrect");
        assertEquals("Dalton", d.getNom(), "Le nom est incorrect");
        assertEquals("Joe", d.getPrenom(), "Le prénom est incorrect");
        
        // Tu peux ajouter les assertions pour les autres champs si tu as généré les getters correspondants
        // assertEquals("Attaque de banque", d.getCrime()); 
    }

    @Test
    void testToString() {
        // 1. Préparation
        Detenu d = new Detenu(101, "Dupont", "Jean", "Vol", 12, "C01", "BatB");

        // 2. Exécution
        String resultat = d.toString();

        // 3. Vérification
        // On s'attend au format défini dans ta classe Detenu : "Detenu 101 : Dupont Jean (Vol)"
        String attendu = "Detenu 101 : Dupont Jean (Vol)";
        assertEquals(attendu, resultat, "La méthode toString() ne retourne pas le format attendu");
    }

    @Test
    void testToCSV() {
        // 1. Préparation
        Detenu d = new Detenu(999, "Smith", "John", "Fraude", 6, "C20", "BatC");

        // 2. Exécution
        String csv = d.toCSV();

        // 3. Vérification
        // Format attendu : numEcrou;nom;prenom;crime;temps;cellule;batiment
        String attendu = "999;Smith;John;Fraude;6;C20;BatC";
        
        assertNotNull(csv);
        assertEquals(attendu, csv, "L'export CSV du détenu est incorrect");
    }
}