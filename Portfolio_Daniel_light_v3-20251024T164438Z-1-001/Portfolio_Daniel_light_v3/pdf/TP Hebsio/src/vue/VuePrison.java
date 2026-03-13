package vue;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import modele.Prisons;
import modele.Modele;

public class VuePrison extends JFrame implements ActionListener {

    private JTable tableau;
    private DefaultTableModel modeleTableau;
    private Modele modeleDonnees;

    // Composants du formulaire
    private JTextField jtfIdPrison, jtfNomPrison, jtfAdresse, jtfIdDirecteur;
    private JButton btnAjouter, btnSupprimer, btnFermer;

    public VuePrison() {
        modeleDonnees = new Modele();
        
        setTitle("Gestion des Prisons");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Ne ferme pas le menu

        JPanel contentPane = new JPanel(new BorderLayout());
        this.setContentPane(contentPane);

        // 1. Tableau (Centre)
        String[] colonnes = {"ID", "Nom", "Adresse", "ID Directeur"};
        modeleTableau = new DefaultTableModel(colonnes, 0);
        tableau = new JTable(modeleTableau);
        chargerDonnees();
        contentPane.add(new JScrollPane(tableau), BorderLayout.CENTER);

        // 2. Formulaire et Boutons (Sud)
        JPanel panelSud = new JPanel(new BorderLayout());
        
        JPanel panelForm = new JPanel(new GridLayout(2, 4, 10, 10));
        panelForm.setBorder(BorderFactory.createTitledBorder("Ajouter/Modifier une Prison"));

        panelForm.add(new JLabel("ID Prison :"));
        jtfIdPrison = new JTextField();
        panelForm.add(jtfIdPrison);

        panelForm.add(new JLabel("Nom :"));
        jtfNomPrison = new JTextField();
        panelForm.add(jtfNomPrison);
        
        panelForm.add(new JLabel("Adresse :"));
        jtfAdresse = new JTextField();
        panelForm.add(jtfAdresse);

        panelForm.add(new JLabel("ID Directeur :"));
        jtfIdDirecteur = new JTextField();
        panelForm.add(jtfIdDirecteur);

        panelSud.add(panelForm, BorderLayout.CENTER);

        // Boutons
        JPanel panelBoutons = new JPanel();
        btnAjouter = new JButton("Ajouter Prison");
        btnSupprimer = new JButton("Supprimer Prison");
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
        ArrayList<Prisons> lesPrisons = modeleDonnees.getLesPrisons();
        for (Prisons p : lesPrisons) {
            modeleTableau.addRow(new Object[]{p.getIdPrison(), p.getNomPrison(), p.getAdresse(), p.getIdDirecteur()});
        }
    }

    private void viderChamps() {
        jtfIdPrison.setText("");
        jtfNomPrison.setText("");
        jtfAdresse.setText("");
        jtfIdDirecteur.setText("");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnFermer) {
            this.dispose();
        } 
        else if (e.getSource() == btnAjouter) {
            try {
                int id = Integer.parseInt(jtfIdPrison.getText());
                String nom = jtfNomPrison.getText();
                String adresse = jtfAdresse.getText();
                int idDir = Integer.parseInt(jtfIdDirecteur.getText());
                
                Prisons p = new Prisons(id, nom, adresse, idDir);
                modeleDonnees.ajouterPrison(p);
                
                chargerDonnees();
                viderChamps();
                JOptionPane.showMessageDialog(this, "Prison ajoutée !");

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout : Vérifiez que l'ID n'existe pas déjà et que l'ID Directeur est un nombre.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        } 
        else if (e.getSource() == btnSupprimer) {
            int ligne = tableau.getSelectedRow();
            if (ligne == -1) {
                JOptionPane.showMessageDialog(this, "Veuillez sélectionner une prison à supprimer dans la liste.");
            } else {
                int idPrison = (int) modeleTableau.getValueAt(ligne, 0); // Récupère l'ID
                
                int reponse = JOptionPane.showConfirmDialog(this, 
                        "ATTENTION : Supprimer la prison ID " + idPrison + " peut échouer si des bâtiments/cellules y sont rattachés.\nVoulez-vous continuer ?", 
                        "Confirmation de Suppression", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

                if (reponse == JOptionPane.YES_OPTION) {
                    try {
                        modeleDonnees.supprimerPrison(idPrison);
                        chargerDonnees();
                        JOptionPane.showMessageDialog(this, "Prison supprimée (si aucune dépendance SQL bloquante).");
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, "Échec de la suppression : Erreur SQL ou dépendances non résolues.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }
    }
}