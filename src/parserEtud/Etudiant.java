package parserEtud;

import java.util.UUID;

public class Etudiant {

    private String id;
    private String nom;
    private String prenom;
    private String groupe;

    public Etudiant() {
        super();
    }

    /**
     * @param id
     * @param nom
     * @param prenom
     * @param groupe
     */
    public Etudiant(String id, String nom, String prenom, String groupe) {
        super();
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.groupe = groupe;
    }
    
    public Etudiant(String nom, String prenom, String groupe) {
        super();
        this.id = UUID.randomUUID().toString();
        this.nom = nom;
        this.prenom = prenom;
        this.groupe = groupe;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getGroupe() {
        return groupe;
    }

    public void setGroupe(String groupe) {
        this.groupe = groupe;
    }

}
