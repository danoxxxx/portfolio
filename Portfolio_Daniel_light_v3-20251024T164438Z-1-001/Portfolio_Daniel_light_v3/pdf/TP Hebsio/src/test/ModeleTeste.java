 package test;

        import modele.*;
        import org.junit.jupiter.api.*;
        import static org.junit.jupiter.api.Assertions.*;

        import java.sql.SQLException;
        import java.util.ArrayList;

class ModeleTest {
	private Modele modele;
	 // Déclaration des ID de test
    private final int ID_PRISON_TEST = 9999;
    private final String NUM_BAT_TEST = "Z9";
    private final int ID_PERS_TEST = 9998;
    private final String NUM_CELL_TEST = "C99";
    private final String GRADE_TEST = "Major";

    @Test
    void testGetNbCellules() {
        // Attention : Ce test nécessite que ta BDD contienne des données !
        Modele modele = new Modele();
        int nombre = modele.getNbCellules();
        
        // Exemple : si tu as inséré 5 cellules dans ta base manuellement
        //assertTrue(nombre >= 0, "Le nombre de cellules ne peut pas être négatif");
        // Ou si tu connais le nombre exact :
        // assertEquals(5, nombre); 
        System.out.println("Nombre de cellules trouvé : " + nombre);
         
                modele = new Modele();
                // Optionnel : s'assurer que les données de test sont propres avant chaque test
                try {
                     // Nettoyage complet (ordre inverse des dépendances)
                     modele.desaffecterSurveillance(ID_PERS_TEST, NUM_CELL_TEST, NUM_BAT_TEST);
                     modele.supprimerSurveillant(ID_PERS_TEST);
                     modele.supprimerDirecteur(ID_PERS_TEST + 1); // Si on teste le directeur
                     modele.supprimerPersonnelDeBase(ID_PERS_TEST);
                     modele.supprimerCellule(NUM_CELL_TEST, NUM_BAT_TEST);
                     modele.supprimerBatiment(NUM_BAT_TEST);
                     modele.supprimerPrison(ID_PRISON_TEST);
                } catch (Exception e) {
                     // Ignorer, car les lignes n'existent peut-être pas
                }
            }

            @AfterEach
            void tearDown() {
                // Nettoyage après chaque test pour assurer l'indépendance
                //setUp(); 
            }

            // ==========================================================
            // TESTS SUR PRISON
            // ==========================================================
            
            @Test
            void testAjouterEtSupprimerPrison() throws SQLException {
                // Préparation (le directeur doit exister dans la DB ou être géré en amont)
                Prisons p = new Prisons(ID_PRISON_TEST, "Prison Test", "Adresse Test", 1); // 1 est un ID Directeur par défaut
                
                // Tester l'ajout
                modele.ajouterPrison(p);
                ArrayList<Prisons> prisons = modele.getLesPrisons();
                
                assertTrue(prisons.stream().anyMatch(pr -> pr.getIdPrison() == ID_PRISON_TEST), 
                           "La prison devrait être présente après l'ajout.");
                
                // Tester la suppression
                modele.supprimerPrison(ID_PRISON_TEST);
                prisons = modele.getLesPrisons();
                
                assertFalse(prisons.stream().anyMatch(pr -> pr.getIdPrison() == ID_PRISON_TEST), 
                            "La prison devrait avoir été supprimée.");
            }

            // ==========================================================
            // TESTS SUR BATIMENT
            // ==========================================================

            @Test
            void testAjouterEtSupprimerBatiment() throws SQLException {
                // Dépendance : Créer une Prison
                modele.ajouterPrison(new Prisons(ID_PRISON_TEST, "P. Bat. Test", "Adr.", 1));
                
                Batiment b = new Batiment(NUM_BAT_TEST, ID_PRISON_TEST);
                
                // Tester l'ajout
                modele.ajouterBatiment(b);
                ArrayList<Batiment> batiments = modele.getLesBatiments();
                
                assertTrue(batiments.stream().anyMatch(bat -> bat.getNumBat().equals(NUM_BAT_TEST)), 
                           "Le bâtiment devrait être présent après l'ajout.");

                // Tester la suppression
                modele.supprimerBatiment(NUM_BAT_TEST);
                batiments = modele.getLesBatiments();
                
                assertFalse(batiments.stream().anyMatch(bat -> bat.getNumBat().equals(NUM_BAT_TEST)), 
                            "Le bâtiment devrait avoir été supprimé.");
            }
            
            @Test
            void testAjoutBatimentSansPrisonFerme() {
                // Tente d'ajouter un bâtiment avec une prison ID inexistante.
                // Ce test vérifie si la DB retourne correctement une SQLException (violation FK)
                Batiment b = new Batiment("ERR", 99999);
                assertThrows(SQLException.class, () -> modele.ajouterBatiment(b), 
                             "L'ajout d'un bâtiment à une prison inexistante doit lever une SQLException (FK).");
            }

            // ==========================================================
            // TESTS SUR PERSONNEL (Base)
            // ==========================================================

            @Test
            void testAjouterEtSupprimerPersonnel() throws SQLException {
                Personnel p = new Personnel(ID_PERS_TEST, "NomPers", "PrenomPers", 123456789);
                
                // Tester l'ajout
                modele.ajouterPersonnelDeBase(p);
                ArrayList<Personnel> personnel = modele.getToutLePersonnel();
                
                assertTrue(personnel.stream().anyMatch(pers -> pers.getIdPersonnel() == ID_PERS_TEST), 
                           "Le personnel devrait être présent.");

                // Tester la suppression
                modele.supprimerPersonnelDeBase(ID_PERS_TEST);
                personnel = modele.getToutLePersonnel();
                
                assertFalse(personnel.stream().anyMatch(pers -> pers.getIdPersonnel() == ID_PERS_TEST), 
                            "Le personnel devrait avoir été supprimé.");
            }

            // ==========================================================
            // TESTS SUR DIRECTEUR
            // ==========================================================
            
            @Test
            void testAjouterEtSupprimerDirecteur() throws SQLException {
                Directeur d = new Directeur(ID_PERS_TEST, "NomDir", "PrenomDir", 987654321, "2023-01-01");
                
                // Tester l'ajout (doit faire 2 inserts : PERSONNEL puis DIRECTEUR)
                modele.ajouterDirecteur(d);
                ArrayList<Directeur> directeurs = modele.getLesDirecteurs();
                
                assertTrue(directeurs.stream().anyMatch(dir -> dir.getIdPersonnel() == ID_PERS_TEST), 
                           "Le directeur devrait être présent après l'ajout.");
                
                // Vérifier que le personnel de base existe aussi
                ArrayList<Personnel> personnel = modele.getToutLePersonnel();
                assertTrue(personnel.stream().anyMatch(pers -> pers.getIdPersonnel() == ID_PERS_TEST), 
                           "Le directeur doit aussi exister dans la table PERSONNEL.");

                // Tester la suppression (doit faire 2 deletes : DIRECTEUR puis PERSONNEL)
                modele.supprimerDirecteur(ID_PERS_TEST);
                directeurs = modele.getLesDirecteurs();
                personnel = modele.getToutLePersonnel();

                assertFalse(directeurs.stream().anyMatch(dir -> dir.getIdPersonnel() == ID_PERS_TEST), 
                            "Le directeur devrait avoir été supprimé de DIRECTEUR.");
                assertFalse(personnel.stream().anyMatch(pers -> pers.getIdPersonnel() == ID_PERS_TEST), 
                            "Le directeur devrait avoir été supprimé de PERSONNEL.");
            }

            // ==========================================================
            // TESTS SUR SURVEILLANT
            // ==========================================================

            @Test
            void testAjouterEtSupprimerSurveillant() throws SQLException {
                Surveillant s = new Surveillant(ID_PERS_TEST, "NomSurv", "PrenomSurv", 666666666, GRADE_TEST);
                
                // Tester l'ajout
                modele.ajouterSurveillant(s);
                ArrayList<Surveillant> surveillants = modele.getLesSurveillants();
                
                assertTrue(surveillants.stream().anyMatch(surv -> surv.getIdPersonnel() == ID_PERS_TEST 
                                                             && surv.getGrade().equals(GRADE_TEST)), 
                           "Le surveillant devrait être présent après l'ajout.");

                // Tester la suppression
                modele.supprimerSurveillant(ID_PERS_TEST);
                surveillants = modele.getLesSurveillants();
                
                assertFalse(surveillants.stream().anyMatch(surv -> surv.getIdPersonnel() == ID_PERS_TEST), 
                            "Le surveillant devrait avoir été supprimé.");
            }

            // ==========================================================
            // TESTS SUR SURVEILLER (Affectation)
            // ==========================================================

            @Test
            void testAffecterEtDesaffecterSurveillance() throws SQLException {
                // Dépendances : Prison, Bâtiment, Cellule, Surveillant
                modele.ajouterPrison(new Prisons(ID_PRISON_TEST, "P. Surv. Test", "Adr.", 1));
                modele.ajouterBatiment(new Batiment(NUM_BAT_TEST, ID_PRISON_TEST));
                // Cellule(numCellule, nbPlace, numBat)
                modele.ajouterCellule(new Cellule(NUM_CELL_TEST, 5, NUM_BAT_TEST)); 
                // Surveillant(idPersonnel, nom, prenom, numTel, grade)
                modele.ajouterSurveillant(new Surveillant(ID_PERS_TEST, "NomSurv", "PrenomSurv", 111111111, GRADE_TEST));

                Surveiller s = new Surveiller(ID_PERS_TEST, NUM_CELL_TEST, NUM_BAT_TEST);
                
                // Tester l'affectation
                modele.affecterSurveillance(s);
                ArrayList<Surveiller> affectations = modele.getLesAffectations();
                
                assertTrue(affectations.stream().anyMatch(aff -> aff.getIdPersonnel() == ID_PERS_TEST 
                                                             && aff.getNumCellule().equals(NUM_CELL_TEST)), 
                           "L'affectation devrait être présente.");
                
                // Tester la désaffectation
                modele.desaffecterSurveillance(ID_PERS_TEST, NUM_CELL_TEST, NUM_BAT_TEST);
                affectations = modele.getLesAffectations();
                
                assertFalse(affectations.stream().anyMatch(aff -> aff.getIdPersonnel() == ID_PERS_TEST 
                                                              && aff.getNumCellule().equals(NUM_CELL_TEST)), 
                            "L'affectation devrait avoir été supprimée.");
            }
        
    }

