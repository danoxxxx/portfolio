package vue;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import modele.Detenu;
import modele.Modele;
import modele.Cellule;

public class VueDetenus extends JFrame implements ActionListener {

    private JTable tableau;
    private DefaultTableModel modeleTableau;
    private Modele modeleDonnees;

    private JTextField jtfNumEcrou, jtfNom, jtfPrenom, jtfCrime, jtfDuree;
    private JComboBox<String> comboCellules;
    private JButton btnAjouter, btnFermer;
    
 // 1. Déclare le bouton en haut avec les autres
    private JButton btnSupprimer;

   
    public VueDetenus() {
        modeleDonnees = new Modele();
        
        this.setTitle("Gestion des Détenus");
        this.setSize(900, 600);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Ne ferme pas le menu

        JPanel contentPane = new JPanel(new BorderLayout());
        this.setContentPane(contentPane);

        // 1. Tableau (Centre)
        String[] colonnes = {"Écrou", "Nom", "Prénom", "Crime", "Durée", "Cellule"};
        modeleTableau = new DefaultTableModel(colonnes, 0);
        tableau = new JTable(modeleTableau);
        chargerDonnees();
        contentPane.add(new JScrollPane(tableau), BorderLayout.CENTER);

        // 2. Formulaire (Sud)
        JPanel panelBas = new JPanel(new BorderLayout());
        JPanel panelForm = new JPanel(new GridLayout(3, 4, 10, 10));
        panelForm.setBorder(BorderFactory.createTitledBorder("Ajouter un Détenu"));

        panelForm.add(new JLabel("Numéro Écrou :"));
        jtfNumEcrou = new JTextField();
        panelForm.add(jtfNumEcrou);
        
        panelForm.add(new JLabel("Nom :"));
        jtfNom = new JTextField();
        panelForm.add(jtfNom);

        panelForm.add(new JLabel("Prénom :"));
        jtfPrenom = new JTextField();
        panelForm.add(jtfPrenom);

        panelForm.add(new JLabel("Crime :"));
        jtfCrime = new JTextField();
        panelForm.add(jtfCrime);
        
        panelForm.add(new JLabel("Durée (mois) :"));
        jtfDuree = new JTextField();
        panelForm.add(jtfDuree);

        panelForm.add(new JLabel("Cellule :"));
        comboCellules = new JComboBox<>();
        remplirComboCellules(); // Charge les cellules depuis la BDD
        panelForm.add(comboCellules);

        // 3. Boutons
        JPanel panelBoutons = new JPanel();
        btnAjouter = new JButton("Incarcérer");
        btnFermer = new JButton("Fermer");
        btnSupprimer = new JButton("Libérer / Supprimer");
        
        btnAjouter.addActionListener(this);
        btnFermer.addActionListener(this);
        btnSupprimer.addActionListener(this);
        

        panelBoutons.add(btnSupprimer); 
        panelBoutons.add(btnAjouter);
        panelBoutons.add(btnFermer);
        
        panelBas.add(panelForm, BorderLayout.CENTER);
        panelBas.add(panelBoutons, BorderLayout.SOUTH);
        
        contentPane.add(panelBas, BorderLayout.SOUTH);
    }

    private void chargerDonnees() {
        modeleTableau.setRowCount(0);
        ArrayList<Detenu> lesDetenus = modeleDonnees.getLesPrisonniers();
        for (Detenu d : lesDetenus) {
            modeleTableau.addRow(new Object[]{
                d.getNumEcrou(), d.getNom(), d.getPrenom(), d.getCrime(), 
                d.getTmpsIncarceration(), d.getNumCellule() + " (" + d.getNumBat() + ")"
            });
        }
    }

    private void remplirComboCellules() {
        comboCellules.removeAllItems();
        ArrayList<Cellule> cellules = modeleDonnees.getLesCellules();
        for (Cellule c : cellules) {
            comboCellules.addItem(c.getNumCellule() + " - " + c.getNumBat());
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnFermer) {
            this.dispose();
        }
        else if (e.getSource() == btnAjouter) {
            try {
                int ecrou = Integer.parseInt(jtfNumEcrou.getText());
                String nom = jtfNom.getText();
                String prenom = jtfPrenom.getText();
                String crime = jtfCrime.getText();
                int duree = Integer.parseInt(jtfDuree.getText());
                
                String selection = (String) comboCellules.getSelectedItem();
                if (selection != null) {
                    String[] parts = selection.split(" - ");
                    String numCell = parts[0];
                    String numBat = parts[1];

                    Detenu d = new Detenu(ecrou, nom, prenom, crime, duree, numCell, numBat);
                    modeleDonnees.ajouterDetenu(d);
                    
                    chargerDonnees();
                    JOptionPane.showMessageDialog(this, "Détenu ajouté !");
                    
                    // Vider champs
                    jtfNumEcrou.setText(""); jtfNom.setText(""); jtfPrenom.setText(""); 
                    jtfCrime.setText(""); jtfDuree.setText("");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur de saisie : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
        // 3. Dans la méthode actionPerformed, ajoute le cas "btnSupprimer"
        else if (e.getSource() == btnSupprimer) {
            // On regarde quelle ligne est sélectionnée dans le tableau
            int ligneSelectionnee = tableau.getSelectedRow();

            if (ligneSelectionnee == -1) {
                JOptionPane.showMessageDialog(this, "Veuillez sélectionner un détenu dans la liste.", "Erreur", JOptionPane.WARNING_MESSAGE);
            } else {
                // On récupère le numéro d'écrou (colonne 0 du tableau)
                int numEcrou = (int) modeleTableau.getValueAt(ligneSelectionnee, 0);

                // Demande de confirmation (Bonne pratique IHM vue dans tes cours) [cite: 383]
                int reponse = JOptionPane.showConfirmDialog(this, 
                        "Voulez-vous vraiment supprimer le détenu n°" + numEcrou + " ?",
                        "Confirmation", JOptionPane.YES_NO_OPTION);

                if (reponse == JOptionPane.YES_OPTION) {
                    // Appel au modèle
                    modeleDonnees.supprimerDetenu(numEcrou);
                    
                    // Rafraichir le tableau
                    chargerDonnees();
                    JOptionPane.showMessageDialog(this, "Détenu supprimé.");
                }
            }
        }
    }
}