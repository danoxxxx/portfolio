
package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import modele.Cellule;

class CelluleTest {

	    @Test
	    void testToCSV() {
	        // 1. Préparation (Arrange) : Création d'un objet fictif
	        Cellule uneCellule = new Cellule("C101", 4, "BatA");

	        // 2. Action (Act) : Exécution de la méthode à tester
	        String resultatObtenu = uneCellule.toCSV();

	        // 3. Vérification (Assert) : On compare avec le résultat attendu
	        String resultatAttendu = "C101;4;BatA";

	        // Vérifie que la chaîne n'est pas vide et correspond au format 
	        assertNotNull(resultatObtenu); 
	        assertEquals(resultatAttendu, resultatObtenu, "Le format CSV est incorrect");
	    }
}

