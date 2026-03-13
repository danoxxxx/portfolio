package vue;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import modele.Surveillant;
import modele.Modele;
import java.sql.SQLException;

public class VueSurveillant extends JFrame implements ActionListener {

    private JTable tableau;
    private DefaultTableModel modeleTableau;
    private Modele modeleDonnees;

    // Champs de saisie
    private JTextField jtfIdPersonnel, jtfNom, jtfPrenom, jtfNumTel;
    private JComboBox<String> comboGrade;
    private JButton btnAjouter, btnSupprimer, btnFermer;

    public VueSurveillant() {
        modeleDonnees = new Modele();
        
        setTitle("Gestion des Surveillants");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel contentPane = new JPanel(new BorderLayout());
        this.setContentPane(contentPane);

        // 1. Tableau (Centre)
        String[] colonnes = {"ID", "Nom", "Prénom", "Téléphone", "Grade"};
        modeleTableau = new DefaultTableModel(colonnes, 0);
        tableau = new JTable(modeleTableau);
        chargerDonnees();
        contentPane.add(new JScrollPane(tableau), BorderLayout.CENTER);

        // 2. Formulaire et Boutons (Sud)
        JPanel panelSud = new JPanel(new BorderLayout());
        
        // Grille pour les 4 champs Personnel + 1 champ Grade
        JPanel panelForm = new JPanel(new GridLayout(2, 5, 10, 10)); 
        panelForm.setBorder(BorderFactory.createTitledBorder("Ajouter un Surveillant"));

        panelForm.add(new JLabel("ID Personnel :"));
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

        panelForm.add(new JLabel("Grade :"));
        String[] grades = {"Gardien", "Chef d'équipe", "Major", "Capitaine"}; 
        comboGrade = new JComboBox<>(grades);
        panelForm.add(comboGrade);

        panelSud.add(panelForm, BorderLayout.CENTER);

        // Boutons
        JPanel panelBoutons = new JPanel();
        btnAjouter = new JButton("Ajouter Surveillant");
        btnSupprimer = new JButton("Supprimer Surveillant");
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
        ArrayList<Surveillant> lesSurveillants = modeleDonnees.getLesSurveillants();
        for (Surveillant s : lesSurveillants) {
            modeleTableau.addRow(new Object[]{
                s.getIdPersonnel(), s.getNom(), s.getPrenom(), s.getNumTel(), s.getGrade()
            });
        }
    }
    
    private void viderChamps() {
        jtfIdPersonnel.setText("");
        jtfNom.setText("");
        jtfPrenom.setText("");
        jtfNumTel.setText("");
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
                String grade = (String) comboGrade.getSelectedItem();
                
                // Vérification des champs
                if (nom.trim().isEmpty() || prenom.trim().isEmpty() || grade == null) {
                     JOptionPane.showMessageDialog(this, "Tous les champs (Nom, Prénom, ID, Téléphone, Grade) sont obligatoires.", "Erreur Saisie", JOptionPane.ERROR_MESSAGE);
                     return;
                }
                
                Surveillant s = new Surveillant(id, nom, prenom, tel, grade);
                modeleDonnees.ajouterSurveillant(s);
                
                chargerDonnees();
                viderChamps();
                JOptionPane.showMessageDialog(this, "Surveillant ajouté !");

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Les champs ID et Téléphone doivent être des nombres.", "Erreur Saisie", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException ex) {
                // Erreur typique : ID déjà utilisé dans PERSONNEL ou DIRECTEUR/SURVEILLANT
                JOptionPane.showMessageDialog(this, "Erreur SQL lors de l'ajout : " + ex.getMessage() + "\n(L'ID Personnel existe peut-être déjà ou est déjà rattaché à un autre rôle)", "Erreur", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur inattendue : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        } 
        else if (e.getSource() == btnSupprimer) {
            int ligne = tableau.getSelectedRow();
            if (ligne == -1) {
                JOptionPane.showMessageDialog(this, "Veuillez sélectionner un surveillant à supprimer.");
            } else {
                int idPersonnel = (int) modeleTableau.getValueAt(ligne, 0); 
                
                int reponse = JOptionPane.showConfirmDialog(this, 
                        "Supprimer le Surveillant ID " + idPersonnel + "?", 
                        "Confirmation de Suppression", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

                if (reponse == JOptionPane.YES_OPTION) {
                    try {
                        modeleDonnees.supprimerSurveillant(idPersonnel);
                        chargerDonnees();
                        JOptionPane.showMessageDialog(this, "Surveillant supprimé.");
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, "Échec de la suppression : Erreur SQL.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }
    }
}