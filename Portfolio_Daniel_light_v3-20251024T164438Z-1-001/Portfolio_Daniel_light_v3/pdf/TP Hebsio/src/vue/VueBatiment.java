package vue;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import modele.Batiment;
import modele.Modele;
import modele.Prisons;

public class VueBatiment extends JFrame implements ActionListener {

    private JTable tableau;
    private DefaultTableModel modeleTableau;
    private Modele modeleDonnees;

    // Composants du formulaire
    private JTextField jtfNumBat;
    private JComboBox<String> comboIdPrison; // Pour choisir la prison de rattachement
    private JButton btnAjouter, btnSupprimer, btnFermer;

    public VueBatiment() {
        modeleDonnees = new Modele();
        
        setTitle("Gestion des Bâtiments");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel contentPane = new JPanel(new BorderLayout());
        this.setContentPane(contentPane);

        // 1. Tableau (Centre)
        String[] colonnes = {"Numéro Bâtiment", "ID Prison"};
        modeleTableau = new DefaultTableModel(colonnes, 0);
        tableau = new JTable(modeleTableau);
        chargerDonnees();
        contentPane.add(new JScrollPane(tableau), BorderLayout.CENTER);

        // 2. Formulaire et Boutons (Sud)
        JPanel panelSud = new JPanel(new BorderLayout());
        
        JPanel panelForm = new JPanel(new GridLayout(2, 2, 10, 10));
        panelForm.setBorder(BorderFactory.createTitledBorder("Ajouter un Bâtiment"));

        panelForm.add(new JLabel("Numéro Bâtiment :"));
        jtfNumBat = new JTextField();
        panelForm.add(jtfNumBat);

        panelForm.add(new JLabel("Rattaché à la Prison ID :"));
        comboIdPrison = new JComboBox<>();
        remplirComboPrisons(); // Charge les ID de prisons existantes
        panelForm.add(comboIdPrison);

        panelSud.add(panelForm, BorderLayout.CENTER);

        // Boutons
        JPanel panelBoutons = new JPanel();
        btnAjouter = new JButton("Ajouter Bâtiment");
        btnSupprimer = new JButton("Supprimer Bâtiment");
        btnFermer = new JButton("Fermer");
        
        btnAjouter.addActionListener(this);
        btnSupprimer.addActionListener(this);
        btnFermer.addActionListener(this);

        panelBoutons.add(btnAjouter);
        panelBoutons.add(btnSupprimer);
        panelBoutons.add(btnFermer);
        
        panelSud.add(panelBoutons, BorderLayout.SOUTH);
        contentPane.add(panelSud, BorderLayout.SOUTH);
    }

    private void chargerDonnees() {
        modeleTableau.setRowCount(0);
        ArrayList<Batiment> lesBatiments = modeleDonnees.getLesBatiments();
        for (Batiment b : lesBatiments) {
            modeleTableau.addRow(new Object[]{b.getNumBat(), b.getIdPrison()});
        }
    }
    
    private void remplirComboPrisons() {
        comboIdPrison.removeAllItems();
        ArrayList<Prisons> lesPrisons = modeleDonnees.getLesPrisons();
        for (Prisons p : lesPrisons) {
            // Affiche l'ID et le Nom de la prison (e.g., "1 - Prison Centrale")
            comboIdPrison.addItem(p.getIdPrison() + " - " + p.getNomPrison());
        }
    }

    private void viderChamps() {
        jtfNumBat.setText("");
        // On ne vide pas la ComboBox
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnFermer) {
            this.dispose();
        } 
        else if (e.getSource() == btnAjouter) {
            try {
                String numBat = jtfNumBat.getText().trim();
                
                String selection = (String) comboIdPrison.getSelectedItem();
                if (selection == null || selection.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Veuillez choisir une Prison de rattachement.");
                    return;
                }
                // On récupère uniquement l'ID (le premier élément avant le " - ")
                int idPrison = Integer.parseInt(selection.split(" - ")[0]);

                if (numBat.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Le numéro de Bâtiment est obligatoire.");
                    return;
                }
                
                Batiment b = new Batiment(numBat, idPrison);
                modeleDonnees.ajouterBatiment(b);
                
                chargerDonnees();
                viderChamps();
                JOptionPane.showMessageDialog(this, "Bâtiment ajouté !");

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Erreur de format : L'ID Prison doit être un nombre.", "Erreur", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout : " + ex.getMessage() + "\n(Vérifiez l'unicité du numéro de bâtiment)", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        } 
        else if (e.getSource() == btnSupprimer) {
            int ligne = tableau.getSelectedRow();
            if (ligne == -1) {
                JOptionPane.showMessageDialog(this, "Veuillez sélectionner un bâtiment à supprimer.");
            } else {
                String numBat = (String) modeleTableau.getValueAt(ligne, 0);
                
                int reponse = JOptionPane.showConfirmDialog(this, 
                        "Supprimer le Bâtiment " + numBat + "?\n(Attention : Échouera si des CELLULEs y sont rattachées !)", 
                        "Confirmation de Suppression", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

                if (reponse == JOptionPane.YES_OPTION) {
                    try {
                        modeleDonnees.supprimerBatiment(numBat);
                        chargerDonnees();
                        JOptionPane.showMessageDialog(this, "Bâtiment supprimé (si aucune dépendance).");
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, "Échec de la suppression : Erreur SQL ou dépendances non résolues (Cellules rattachées).", "Erreur", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }
    }
}
