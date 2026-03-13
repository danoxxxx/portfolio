package vue;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class VueMenu extends JFrame implements ActionListener {

    private JButton btnGestionCellules;
    private JButton btnGestionDetenus;
    private JButton btnGestionPrisons;
    private JButton btnGestionDirecteurs;
    private JButton btnGestionBatiments;
    private JButton btnGestionPersonnel; 
    private JButton btnGestionSurveillants; 
    private JButton btnGestionSurveiller;

    public VueMenu() {
        this.setTitle("Menu Principal - Prison HEBSSIO");
        this.setSize(400, 300);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Ferme toute l'appli ici
        this.setLocationRelativeTo(null);

        // Layout principal
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 1, 20, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        //panel.setLayout(new GridLayout(3, 1, 20, 20));
        //panel.setLayout(new GridLayout(4, 1, 20, 20)); // MODIFIÉ
        //panel.setLayout(new GridLayout(5, 1, 20, 20));
        //panel.setLayout(new GridLayout(6, 1, 20, 20));
        //panel.setLayout(new GridLayout(7, 1, 20, 20));
        panel.setLayout(new GridLayout(8, 1, 20, 20)); // MODIFIÉ (anciennement 7)

        
        // Création des gros boutons
        btnGestionCellules = new JButton("Gérer les Cellules");
        btnGestionCellules.setFont(new Font("Arial", Font.BOLD, 18));
        
        btnGestionDetenus = new JButton("Gérer les Détenus");
        btnGestionDetenus.setFont(new Font("Arial", Font.BOLD, 18));
        
        // Création du nouveau bouton
        btnGestionPrisons = new JButton("Gérer les Prisons");
        btnGestionPrisons.setFont(new Font("Arial", Font.BOLD, 18));
        
        // Création du nouveau bouton
        btnGestionDirecteurs = new JButton("Gérer les Directeurs");
        btnGestionDirecteurs.setFont(new Font("Arial", Font.BOLD, 18));
        
     // Création du nouveau bouton
        btnGestionBatiments = new JButton("Gérer les Bâtiments");
        btnGestionBatiments.setFont(new Font("Arial", Font.BOLD, 18));
       
        // Création du nouveau bouton
        btnGestionPersonnel = new JButton("Gérer le Personnel (Base)");
        btnGestionPersonnel.setFont(new Font("Arial", Font.BOLD, 18));
        
        btnGestionSurveillants = new JButton("Gérer les Surveillants");
        btnGestionSurveillants.setFont(new Font("Arial", Font.BOLD, 18));
        
        // Création du nouveau bouton
        btnGestionSurveiller = new JButton("Affecter Surveillance (Cellules)");
        btnGestionSurveiller.setFont(new Font("Arial", Font.BOLD, 18));
        
        
       
        
        
        

        // Ajout des écouteurs
        btnGestionSurveiller.addActionListener(this);
        btnGestionSurveillants.addActionListener(this);
        btnGestionPersonnel.addActionListener(this);
        btnGestionBatiments.addActionListener(this);
        btnGestionDirecteurs.addActionListener(this);
        btnGestionPrisons.addActionListener(this);
        btnGestionCellules.addActionListener(this);
        btnGestionDetenus.addActionListener(this);
        
        
        
        panel.add(btnGestionPersonnel);
        panel.add(btnGestionBatiments); 
        panel.add(btnGestionDirecteurs);
        panel.add(btnGestionCellules);
        panel.add(btnGestionDetenus);
        panel.add(btnGestionCellules);
        panel.add(btnGestionDetenus);
        panel.add(btnGestionPrisons);
        panel.add(btnGestionSurveillants);
        panel.add(btnGestionSurveiller); 
        
        this.getContentPane().add(panel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnGestionCellules) {
            // Ouvre la fenêtre des cellules
            new VueCellules().setVisible(true);
        } 
        else if (e.getSource() == btnGestionDetenus) {
            // Ouvre la fenêtre des détenus
            new VueDetenus().setVisible(true);
        }
        else if (e.getSource() == btnGestionPrisons) {
            new VuePrison().setVisible(true); // Lance la nouvelle vue
        }
        else if (e.getSource() == btnGestionDirecteurs) { // AJOUTÉ
            new VueDirecteur().setVisible(true); // Lance la nouvelle vue
        }
        else if (e.getSource() == btnGestionBatiments) { // AJOUTÉ
            new VueBatiment().setVisible(true); // Lance la nouvelle vue
        }
        else if (e.getSource() == btnGestionPersonnel) { 
            new VuePersonnel().setVisible(true); // Lance la nouvelle vue
        }
        else if (e.getSource() == btnGestionSurveillants) { 
            new VueSurveillant().setVisible(true); 
        }
        else if (e.getSource() == btnGestionSurveiller) { // AJOUTÉ
            new VueSurveiller().setVisible(true); // Lance la nouvelle vue
        }
    }
    
    public static void main(String[] args) {
         SwingUtilities.invokeLater(() -> new VueMenu().setVisible(true));
    }
    
    
}