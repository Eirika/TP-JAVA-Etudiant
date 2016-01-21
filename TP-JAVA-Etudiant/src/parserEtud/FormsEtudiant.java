package parserEtud;

import java.util.ArrayList;

public class FormsEtudiant {

    private String formulaire;
    private ArrayList<Etudiant> lesEtudiants = new ArrayList<Etudiant>();

    public FormsEtudiant() {
        super();
        initLesEtudiants();
    }

    public void initLesEtudiants() {
        AnalyseurDOM analyseurDom = new AnalyseurDOM();
        this.lesEtudiants = analyseurDom.analyse();
    }

    public Etudiant getEtudiant(int id) {
        Boolean trouve = false;
        int i = 0;
        Etudiant etudiant = null;
        while (!trouve) {
            if (this.lesEtudiants.get(i).getId() == id) {
                etudiant = this.lesEtudiants.get(i);
                trouve = true;
            }
            i++;
        }
        return etudiant;
    }

    public String getNouveau() {
        this.formulaire  = "<h2>Ajout d'un &eacute;tudiant</h2>"
                + "<table>"
                + "<form method=\"GET\" action=\"ajouter\">"
                + "<tr>"
                + "<td><input type=\"hidden\" name=\"id\" value=\"" + Integer.toString(this.lesEtudiants.get(this.lesEtudiants.size() - 1).getId() + 1) + "\" readOnly=\"true\"></td>"
                + "</tr><tr>"
                + "<td><label for=\"nom\">Nom : </label></td>"
                + "<td><input type=\"text\" name=\"nom\"></td>"
                + "</tr><tr>"
                + "<td><label for=\"prenom\">Pr�nom : </label></td>"
                + "<td><input type=\"text\" name=\"prenom\"></td>"
                + "</tr><tr>"
                + "<td><label for=\"groupe\">Groupe : </label></td>"
                + "<td><input type=\"text\" name=\"groupe\"></td>"
                + "</tr><tr>"
                + "<td>Action : </td><td><input type=\"submit\" value=\"Ajouter\"/></td>"
                + "</tr>"
                + "</form>"
                + "</table>";
        return this.formulaire;
    }

    public String getDetail(String id) {
        Etudiant etudiant = getEtudiant(Integer.parseInt(id));
        this.formulaire = "<h2>Modification de " + etudiant.getNom() + " " + etudiant.getPrenom() + "</h2>"
                + "<table>"
                + "<form method=\"GET\" action=\"modifier\">"
                + "<tr>"
                + "<td><label for=\"id\">Identifiant : </label></td>"
                + "<td><input type=\"text\" name=\"id\" value=\"" + etudiant.getId() + "\" readOnly=\"true\"></td>"
                + "</tr><tr>"
                + "<td><label for=\"nom\">Nom : </label></td>"
                + "<td><input type=\"text\" name=\"nom\" value=\"" + etudiant.getNom() + "\"></td>"
                + "</tr><tr>"
                + "<td><label for=\"prenom\">Pr�nom : </label></td>"
                + "<td><input type=\"text\" name=\"prenom\" value=\"" + etudiant.getPrenom() + "\"></td>"
                + "</tr><tr>"
                + "<td><label for=\"groupe\">Groupe : </label></td>"
                + "<td><input type=\"text\" name=\"groupe\" value=\"" + etudiant.getGroupe() + "\"></td>"
                + "</tr><tr>"
                + "<td>Action : </td><td><input type=\"submit\" value=\"Modifier\"/></td>"
                + "</tr>"
                + "</form>"
                + "</table>";
        return this.formulaire;
    }

}
