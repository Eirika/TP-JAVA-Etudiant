package parserEtud;

import java.util.ArrayList;

public class HtmlGenerator {

    public HtmlGenerator() {
        super();
    }

    public String getTitle(String titre) {
        String string = "<h1>" + titre + "</h1>";
        return string;
    }

    public String getSearchForm() {
        String string = "<br/><form method=\"GET\" action=\"rechercher\">"
                + "<label for=\"champ\"> Rechercher: </label>"
                + "<input type=\"text\" name=\"champ\"/>"
                + "<button type=\"submit\">Rechercher</button>"
                + "</form><br/><br />";
        return string;
    }
    
    public String getReturnBtn() {
        return "<a href=\"./\"><button>Retour à la liste</button></a>";
    }
    
    public String getAddEtudiantBtn() {
        return "<br/><a href=\"/nouvelEtudiant\"><button>Nouvel Etudiant</button></a>";
    }

    public String getNouveau() {
        String string = "<h2>Ajout d'un &eacute;tudiant</h2>"
                + "<table>"
                + "<form method=\"GET\" action=\"ajouter\">"
                + "<tr>"
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
                + "</table>"
                + this.getReturnBtn();
        return string;
    }

    public String getDetail(Etudiant etud) {
        String string = "";
        if (etud != null) {
            string += "<h2>Modification de " + etud.getNom() + " " + etud.getPrenom() + "</h2>"
                    + "<table>"
                    + "<form method=\"GET\" action=\"modifier\">"
                    + "<tr>"
                    + "<td><label for=\"id\">Identifiant : </label></td>"
                    + "<td><input type=\"text\" name=\"id\" value=\"" + etud.getId() + "\" readOnly=\"true\"/></td>"
                    + "</tr><tr>"
                    + "<td><label for=\"nom\">Nom : </label></td>"
                    + "<td><input type=\"text\" name=\"nom\" value=\"" + etud.getNom() + "\"/></td>"
                    + "</tr><tr>"
                    + "<td><label for=\"prenom\">Prénom : </label></td>"
                    + "<td><input type=\"text\" name=\"prenom\" value=\"" + etud.getPrenom() + "\"/></td>"
                    + "</tr><tr>"
                    + "<td><label for=\"groupe\">Groupe : </label></td>"
                    + "<td><input type=\"text\" name=\"groupe\" value=\"" + etud.getGroupe() + "\"/></td>"
                    + "</tr><tr>"
                    + "<td>Action : </td><td><input type=\"submit\" value=\"Modifier\"/></td>"
                    + "</tr>"
                    + "</form>"
                    + "</table>"
                    + this.getReturnBtn();
            return string;
        } else {
            return "<h1>Etudiant inconnue !</h1><br/> " + this.getReturnBtn();
        }
    }

    public String getHeader() {
        String header = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.1//EN\"http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd\">";
        header += "<html xmlns=\"http://www.w3.org/1999/xhtml\">";
        header += "<head>";
        header += "<meta http-equiv=\"Content-Type\" content=\"application/xhtml+xml; charset=UTF-8\"/>";
        header += "<title>Les CASIR !</title>";
        header += "</head><body>";
        return header;
    }

    public String getAjoutEtudiant(boolean reussi) {
        if (reussi) {
            return "<h1>Ajout réussi </h1><br/>" + this.getReturnBtn();
        } else {
            return "<h1>Echec de l'ajout !!! </h1><br/>" + this.getReturnBtn();
        }
    }

    public String getModifEtudiant(boolean reussi) {
        if (reussi) {
            return "<h1>Modification réussi </h1><br/>" + this.getReturnBtn();
        } else {
            return "<h1>Echec de la modification !!! </h1><br/>" + this.getReturnBtn();
        }
    }

    public String getTableEtudiant(ArrayList<Etudiant> lesEtudiants) {
        if (lesEtudiants.isEmpty()) {
            return "<h1>Aucun étudiant ne correspond à la recherche ! </h1><br/>" + this.getReturnBtn();
        } else {
            String string = "<table><tr><th>Id</th><th>Nom</th><th>Prénom</th><th>Groupe</th><th>Action</th></tr>";
            for (Etudiant etudiant : lesEtudiants) {
                string += "<tr><td>" + etudiant.getId() + "</td><td>" + etudiant.getNom() + "</td><td>" + etudiant.getPrenom() + "</td><td>" + etudiant.getGroupe() + "</td><td><a href=\"/detailEtudiant?id=" + etudiant.getId() + "\"><button>D&eacute;tail</button></a><a href=\"/supprimer?id=" + etudiant.getId() + "\"><button>Supprimer</button></a></td></tr>";
            }
            string += "</table>";
            return string;
        }
    }

    public String getDelEtudiant(boolean reussi) {
        if (reussi) {
            return "<h1>Suppression réussi </h1><br/>" + this.getReturnBtn();
        } else {
            return "<h1>Echec de la suppression !!! </h1><br/>" + this.getReturnBtn();
        }
    }
    
    public String getValidity(boolean valid){
        if(valid){
            return "";
        } else {
            return "<h1>Le serveur ne semble pas être en mesure de lire la source de donnée !</h1>";
        }
    }

}
