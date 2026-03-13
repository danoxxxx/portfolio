package vue;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.sql.SQLException;

// Import des Modèles nécessaires pour les listes déroulantes
import modele.Surveiller;
import modele.Modele;
import modele.Surveillant;
import modele.Cellule;

public class VueSurveiller extends JFrame implements ActionListener {

    private JTable tableau;
    private DefaultTableModel modeleTableau;
    private Modele modeleDonnees;

    // Champs de saisie
    private JComboBox<String> comboSurveillant;
    private JComboBox<String> comboCellule;
    private JButton btnAffecter, btnDesaffecter, btnFermer;

    public VueSurveiller() {
        modeleDonnees = new Modele();
        
        setTitle("Affectation Surveillance Cellules");
        setSize(900, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel contentPane = new JPanel(new BorderLayout());
        this.setContentPane(contentPane);

        // 1. Tableau (Centre)
        String[] colonnes = {"ID Personnel", "Nom & Prénom", "Num Cellule", "Num Bâtiment"};
        modeleTableau = new DefaultTableModel(colonnes, 0);
        tableau = new JTable(modeleTableau);
        chargerDonnees();
        contentPane.add(new JScrollPane(tableau), BorderLayout.CENTER);

        // 2. Formulaire et Boutons (Sud)
        JPanel panelSud = new JPanel(new BorderLayout());
        
        JPanel panelForm = new JPanel(new GridLayout(2, 2, 10, 10));
        panelForm.setBorder(BorderFactory.createTitledBorder("Affecter un Surveillant à une Cellule"));

        panelForm.add(new JLabel("Surveillant :"));
        comboSurveillant = new JComboBox<>();
        remplirComboSurveillants();
        panelForm.add(comboSurveillant);

        panelForm.add(new JLabel("Cellule :"));
        comboCellule = new JComboBox<>();
        remplirComboCellules();
        panelForm.add(comboCellule);

        panelSud.add(panelForm, BorderLayout.CENTER);

        // Boutons
        JPanel panelBoutons = new JPanel();
        btnAffecter = new JButton("Affecter Surveillance");
        btnDesaffecter = new JButton("Désaffecter Surveillance");
        btnFermer = new JButton("Fermer");
        
        btnAffecter.addActionListener(this);
        btnDesaffecter.addActionListener(this);
        btnFermer.addActionListener(this);

        panelBoutons.add(btnAffecter);
        panelBoutons.add(btnDesaffecter);
        panelBoutons.add(btnFermer);
        
        panelSud.add(panelBoutons, BorderLayout.SOUTH);
        contentPane.add(panelSud, BorderLayout.SOUTH);
    }

    private void chargerDonnees() {
        modeleTableau.setRowCount(0);
        ArrayList<Surveiller> lesAffectations = modeleDonnees.getLesAffectations();
        for (Surveiller s : lesAffectations) {
            // Pour l'affichage, on récupère le nom/prénom via l'ID
            String nomPrenom = "ID " + s.getIdPersonnel(); // A améliorer si l'objet Modele permet getPersonnel(id)
            
            modeleTableau.addRow(new Object[]{
                s.getIdPersonnel(), nomPrenom, s.getNumCellule(), s.getNumBat()
            });
        }
    }
    
    private void remplirComboSurveillants() {
        comboSurveillant.removeAllItems();
        ArrayList<Surveillant> lesSurveillants = modeleDonnees.getLesSurveillants();
        for (Surveillant s : lesSurveillants) {
            comboSurveillant.addItem(s.getIdPersonnel() + " - " + s.getNom() + " " + s.getPrenom());
        }
    }
    
    private void remplirComboCellules() {
        comboCellule.removeAllItems();
        ArrayList<Cellule> lesCellules = modeleDonnees.getLesCellules(); // Assurez-vous que getLesCellules existe
        for (Cellule c : lesCellules) {
            comboCellule.addItem(c.getNumCellule() + " / " + c.getNumBat());
        }
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnFermer) {
            this.dispose();
        } 
        else if (e.getSource() == btnAffecter) {
            String selectedSurveillant = (String) comboSurveillant.getSelectedItem();
            String selectedCellule = (String) comboCellule.getSelectedItem();

            if (selectedSurveillant == null || selectedCellule == null) {
                JOptionPane.showMessageDialog(this, "Veuillez sélectionner un Surveillant et une Cellule.", "Erreur Saisie", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                // Récupération des IDs
                int idPersonnel = Integer.parseInt(selectedSurveillant.split(" - ")[0]);
                String[] partsCellule = selectedCellule.split(" / ");
                String numCellule = partsCellule[0].trim();
                String numBat = partsCellule[1].trim();

                Surveiller s = new Surveiller(idPersonnel, numCellule, numBat);
                modeleDonnees.affecterSurveillance(s);
                
                chargerDonnees();
                JOptionPane.showMessageDialog(this, "Surveillance affectée !");

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Erreur SQL : Cette affectation existe peut-être déjà ou la cellule/surveillant n'existe plus.", "Erreur", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur inattendue : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        } 
        else if (e.getSource() == btnDesaffecter) {
            int ligne = tableau.getSelectedRow();
            if (ligne == -1) {
                JOptionPane.showMessageDialog(this, "Veuillez sélectionner une affectation à désaffecter.");
            } else {
                int idPersonnel = (int) modeleTableau.getValueAt(ligne, 0); 
                String numCellule = (String) modeleTableau.getValueAt(ligne, 2); 
                String numBat = (String) modeleTableau.getValueAt(ligne, 3); 
                
                int reponse = JOptionPane.showConfirmDialog(this, 
                        "Désaffecter le Surveillant ID " + idPersonnel + " de la Cellule " + numCellule + "/" + numBat + "?", 
                        "Confirmation de Suppression", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

                if (reponse == JOptionPane.YES_OPTION) {
                    try {
                        modeleDonnees.desaffecterSurveillance(idPersonnel, numCellule, numBat);
                        chargerDonnees();
                        JOptionPane.showMessageDialog(this, "Surveillance désaffectée.");
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, "Échec de la désaffectation : Erreur SQL.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }
    }
}