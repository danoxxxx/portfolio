package modele;

// Le Surveillant est un Personnel spécialisé
public class Surveillant extends Personnel {
    
    private String grade;

    public Surveillant(int idPersonnel, String nom, String prenom, int numTel, String grade) {
        // Appel au constructeur de la classe parente (Personnel)
        super(idPersonnel, nom, prenom, numTel);
        this.grade = grade;
    }

    // Getter spécifique
    public String getGrade() { return grade; }
    
    @Override
    public String toString() {
        return super.toString() + " - Grade: " + grade;
    }
}