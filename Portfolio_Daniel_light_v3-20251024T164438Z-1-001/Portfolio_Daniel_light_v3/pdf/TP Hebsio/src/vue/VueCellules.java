package vue;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import modele.Cellule;
import modele.Modele;

public class VueCellules extends JFrame implements ActionListener {

    private JTable tableau;
    private DefaultTableModel modeleTableau;
    private Modele modeleDonnees;

    private JTextField jtfNumCellule;
    private JTextField jtfNbPlaces;
    private JComboBox<String> listeBatiment;
    private JButton btnAjouter, btnFermer;
    private JButton btnSupprimer;


    public VueCellules() {
        modeleDonnees = new Modele();

        this.setTitle("Gestion des Cellules");
        this.setSize(800, 500);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Ne ferme pas le menu

        // --- Structure Principale ---
        JPanel contentPane = new JPanel(new BorderLayout());
        this.setContentPane(contentPane);

        // 1. Tableau (Centre)
        String[] colonnes = {"Numéro", "Nb Places", "Bâtiment"};
        modeleTableau = new DefaultTableModel(colonnes, 0);
        tableau = new JTable(modeleTableau);
        chargerDonnees();
        contentPane.add(new JScrollPane(tableau), BorderLayout.CENTER);

        // 2. Formulaire (Sud)
        JPanel panelSud = new JPanel(new BorderLayout());
        
        JPanel panelForm = new JPanel(new GridLayout(2, 4, 10, 10));
        panelForm.setBorder(BorderFactory.createTitledBorder("Ajouter une Cellule"));

        panelForm.add(new JLabel("Numéro :"));
        jtfNumCellule = new JTextField();
        panelForm.add(jtfNumCellule);

        panelForm.add(new JLabel("Nb Places :"));
        jtfNbPlaces = new JTextField();
        panelForm.add(jtfNbPlaces);

        panelForm.add(new JLabel("Bâtiment :"));
        String[] bats = {"BatA", "BatB", "BatC"};
        listeBatiment = new JComboBox<>(bats);
        panelForm.add(listeBatiment);
        
        panelForm.add(new JLabel("")); // Vide pour alignement

        // 3. Boutons
        JPanel panelBoutons = new JPanel();
        btnAjouter = new JButton("Ajouter Cellule");
        btnFermer = new JButton("Fermer");
        btnSupprimer = new JButton("Supprimer Cellule");
        
        
        btnSupprimer.addActionListener(this);
        btnAjouter.addActionListener(this);
        btnFermer.addActionListener(this);

        panelBoutons.add(btnAjouter);
        panelBoutons.add(btnFermer);
        panelBoutons.add(btnSupprimer);

        panelSud.add(panelForm, BorderLayout.CENTER);
        panelSud.add(panelBoutons, BorderLayout.SOUTH);
        contentPane.add(panelSud, BorderLayout.SOUTH);
    }

    private void chargerDonnees() {
        modeleTableau.setRowCount(0);
        ArrayList<Cellule> lesCellules = modeleDonnees.getLesCellules();
        for (Cellule c : lesCellules) {
            modeleTableau.addRow(new Object[]{c.getNumCellule(), c.getNbPlace(), c.getNumBat()});
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnFermer) {
            this.dispose();
        } 
        else if (e.getSource() == btnAjouter) {
            try {
                String num = jtfNumCellule.getText();
                int places = Integer.parseInt(jtfNbPlaces.getText());
                String bat = (String) listeBatiment.getSelectedItem();

                Cellule c = new Cellule(num, places, bat);
                modeleDonnees.ajouterCellule(c);
                
                chargerDonnees();
                JOptionPane.showMessageDialog(this, "Cellule ajoutée !");
                
                // Reset
                jtfNumCellule.setText("");
                jtfNbPlaces.setText("");

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
        // 3. Dans actionPerformed
        else if (e.getSource() == btnSupprimer) {
            int ligne = tableau.getSelectedRow();

            if (ligne == -1) {
                JOptionPane.showMessageDialog(this, "Sélectionnez une cellule à supprimer.");
            } else {
                // Colonne 0 = Numéro Cellule, Colonne 2 = Bâtiment
                String numCell = (String) modeleTableau.getValueAt(ligne, 0);
                String numBat = (String) modeleTableau.getValueAt(ligne, 2);

                int reponse = JOptionPane.showConfirmDialog(this, 
                        "Supprimer la cellule " + numCell + " du " + numBat + " ?", 
                        "Confirmation", JOptionPane.YES_NO_OPTION);

                if (reponse == JOptionPane.YES_OPTION) {
                    // Appel au modèle avec les 2 paramètres
                    modeleDonnees.supprimerCellule(numCell, numBat);
                    
                    chargerDonnees();
                }
            }
        }
    }
}