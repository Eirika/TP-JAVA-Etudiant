package parserEtud;

public class Etudiant {

    private int id;
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
    public Etudiant(int id, String nom, String prenom, String groupe) {
        super();
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.groupe = groupe;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public String getHtml() {
        return "<tr><td>" + this.id + "</td><td>" + this.nom + "</td><td>" + this.prenom + "</td><td>" + this.groupe + "</td><td><a href=\"/detailEtudiant?id=" + this.id + "\"><button>D&eacute;tail</button></a><a href=\"/supprimer?id=" + this.id + "\"><button>Supprimer</button></a></td></tr>";
    }

}
