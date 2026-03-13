package vue;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import modele.Personnel;
import modele.Modele;
import java.sql.SQLException;

public class VuePersonnel extends JFrame implements ActionListener {

    private JTable tableau;
    private DefaultTableModel modeleTableau;
    private Modele modeleDonnees;

    // Champs de saisie
    private JTextField jtfIdPersonnel, jtfNom, jtfPrenom, jtfNumTel;
    private JButton btnAjouter, btnSupprimer, btnFermer;

    public VuePersonnel() {
        modeleDonnees = new Modele();
        
        setTitle("Gestion du Personnel (Base)");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel contentPane = new JPanel(new BorderLayout());
        this.setContentPane(contentPane);

        // 1. Tableau (Centre)
        String[] colonnes = {"ID", "Nom", "Prénom", "Téléphone"};
        modeleTableau = new DefaultTableModel(colonnes, 0);
        tableau = new JTable(modeleTableau);
        chargerDonnees();
        contentPane.add(new JScrollPane(tableau), BorderLayout.CENTER);

        // 2. Formulaire et Boutons (Sud)
        JPanel panelSud = new JPanel(new BorderLayout());
        
        // Grille pour les 4 champs + labels
        JPanel panelForm = new JPanel(new GridLayout(2, 4, 10, 10)); 
        panelForm.setBorder(BorderFactory.createTitledBorder("Ajouter un Personnel de base"));

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

        panelSud.add(panelForm, BorderLayout.CENTER);

        // Boutons
        JPanel panelBoutons = new JPanel();
        btnAjouter = new JButton("Ajouter Personnel");
        btnSupprimer = new JButton("Supprimer Personnel");
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
        ArrayList<Personnel> toutLePersonnel = modeleDonnees.getToutLePersonnel();
        for (Personnel p : toutLePersonnel) {
            modeleTableau.addRow(new Object[]{
                p.getIdPersonnel(), p.getNom(), p.getPrenom(), p.getNumTel()
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
                
                Personnel p = new Personnel(id, nom, prenom, tel);
                modeleDonnees.ajouterPersonnelDeBase(p);
                
                chargerDonnees();
                viderChamps();
                JOptionPane.showMessageDialog(this, "Personnel de base ajouté !");

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Les champs ID et Téléphone doivent être des nombres.", "Erreur Saisie", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Erreur SQL lors de l'ajout : " + ex.getMessage() + "\n(Vérifiez l'unicité de l'ID)", "Erreur", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur inattendue : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        } 
        else if (e.getSource() == btnSupprimer) {
            int ligne = tableau.getSelectedRow();
            if (ligne == -1) {
                JOptionPane.showMessageDialog(this, "Veuillez sélectionner un membre du personnel à supprimer.");
            } else {
                int idPersonnel = (int) modeleTableau.getValueAt(ligne, 0); 
                
                int reponse = JOptionPane.showConfirmDialog(this, 
                        "Supprimer le Personnel ID " + idPersonnel + "?\n(Attention : Échouera si c'est un Directeur ou Surveillant actif !)", 
                        "Confirmation de Suppression", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

                if (reponse == JOptionPane.YES_OPTION) {
                    try {
                        modeleDonnees.supprimerPersonnelDeBase(idPersonnel);
                        chargerDonnees();
                        JOptionPane.showMessageDialog(this, "Personnel supprimé.");
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(this, "Échec de la suppression : Le membre du personnel est probablement encore rattaché en tant que Directeur ou Surveillant. Supprimez d'abord son rôle spécifique.", "Erreur SQL", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }
    }
}