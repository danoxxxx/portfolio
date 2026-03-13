package vue;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import modele.Directeur;
import modele.Modele;

public class VueDirecteur extends JFrame implements ActionListener {

    private JTable tableau;
    private DefaultTableModel modeleTableau;
    private Modele modeleDonnees;

    // Champs de saisie
    private JTextField jtfIdPersonnel, jtfNom, jtfPrenom, jtfNumTel, jtfDateIntegration;
    private JButton btnAjouter, btnSupprimer, btnFermer;

    public VueDirecteur() {
        modeleDonnees = new Modele();
        
        setTitle("Gestion des Directeurs");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel contentPane = new JPanel(new BorderLayout());
        this.setContentPane(contentPane);

        // 1. Tableau (Centre)
        String[] colonnes = {"ID", "Nom", "Prénom", "Téléphone", "Date Intégration"};
        modeleTableau = new DefaultTableModel(colonnes, 0);
        tableau = new JTable(modeleTableau);
        chargerDonnees();
        contentPane.add(new JScrollPane(tableau), BorderLayout.CENTER);

        // 2. Formulaire et Boutons (Sud)
        JPanel panelSud = new JPanel(new BorderLayout());
        
        // Grille pour les 5 champs + labels
        JPanel panelForm = new JPanel(new GridLayout(2, 5, 10, 10)); 
        panelForm.setBorder(BorderFactory.createTitledBorder("Ajouter un Directeur"));

        panelForm.add(new JLabel("ID :"));
        jtfIdPersonnel = new JTextField();
        panelForm.add(jtfIdPersonnel);

        panelForm.add(new JLabel("Nom :"));
        jtfNom = new JTextField();
        panelForm.add(jtfNom);

        panelForm.add(new JLabel("Prénom :"));
        jtfPrenom = new JTextField();
        panelForm.add(jtfPrenom);
        
        panelForm.add(new JLabel("Téléphone :"));
        jtfNumTel = new JTextField();
        panelForm.add(jtfNumTel);

        panelForm.add(new JLabel("Date Intégration (YYYY-MM-DD) :"));
        jtfDateIntegration = new JTextField();
        panelForm.add(jtfDateIntegration);

        panelSud.add(panelForm, BorderLayout.CENTER);

        // Boutons
        JPanel panelBoutons = new JPanel();
        btnAjouter = new JButton("Ajouter Directeur");
        btnSupprimer = new JButton("Supprimer Directeur");
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
        ArrayList<Directeur> lesDirecteurs = modeleDonnees.getLesDirecteurs();
        for (Directeur d : lesDirecteurs) {
            modeleTableau.addRow(new Object[]{
                d.getIdPersonnel(), d.getNom(), d.getPrenom(), d.getNumTel(), d.getDateIntegration()
            });
        }
    }
    
    private void viderChamps() {
        jtfIdPersonnel.setText("");
        jtfNom.setText("");
        jtfPrenom.setText("");
        jtfNumTel.setText("");
        jtfDateIntegration.setText("");
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnFermer) {
            this.dispose();
        } 
        else if (e.getSource() == btnAjouter) {
            try {
                int id = Integer.parseInt(jtfIdPersonnel.getText());
                String nom = jtfNom.getText();
                String prenom = jtfPrenom.getText();
                int tel = Integer.parseInt(jtfNumTel.getText());
                String date = jtfDateIntegration.getText();
                
                Directeur d = new Directeur(id, nom, prenom, tel, date);
                modeleDonnees.ajouterDirecteur(d);
                
                chargerDonnees();
                viderChamps();
                JOptionPane.showMessageDialog(this, "Directeur ajouté !");

            } catch (Exception ex) {
                // Si l'insertion échoue (par exemple, ID déjà utilisé ou format de date incorrect)
                JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout : " + ex.getMessage() + "\n(Vérifiez le format de la date YYYY-MM-DD, les numéros et l'unicité de l'ID)", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        } 
        else if (e.getSource() == btnSupprimer) {
            int ligne = tableau.getSelectedRow();
            if (ligne == -1) {
                JOptionPane.showMessageDialog(this, "Veuillez sélectionner un directeur à supprimer.");
            } else {
                int idPersonnel = (int) modeleTableau.getValueAt(ligne, 0); 
                
                int reponse = JOptionPane.showConfirmDialog(this, 
                        "Supprimer le Directeur ID " + idPersonnel + "?\n(Attention aux dépendances dans la table PRISON !)", 
                        "Confirmation de Suppression", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

                if (reponse == JOptionPane.YES_OPTION) {
                    try {
                        modeleDonnees.supprimerDirecteur(idPersonnel);
                        chargerDonnees();
                        JOptionPane.showMessageDialog(this, "Directeur supprimé.");
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, "Échec de la suppression : Erreur SQL ou dépendances non résolues (par exemple, un PRISON est rattaché à cet ID).", "Erreur", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }
    }
}