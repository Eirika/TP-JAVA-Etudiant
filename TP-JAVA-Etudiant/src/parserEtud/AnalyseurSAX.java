package parserEtud;

import java.net.Socket;
import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class AnalyseurSAX extends DefaultHandler {

    private Socket s;
    private String affiche;
    private ArrayList<Etudiant> lesEtudiants = new ArrayList<Etudiant>();

    public AnalyseurSAX(Socket sock) {
        super();
        this.s = sock;
        this.affiche = "";
    }

    public void startElement(String nameSpaceURI, String localName, String qName, Attributes att) throws SAXException {
        switch (qName) {
            case "Page":
                this.affiche += "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>";
                this.affiche += "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.1//EN\"";
                this.affiche += " \"http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd\">";
                this.affiche += "<html xmlns=\"http://www.w3.org/1999/xhtml\">";
                this.affiche += "<head>";
                this.affiche += "<meta http-equiv=\"Content-Type\" content=\"application/xhtml+xml; charset=UTF-8\"/>";
                this.affiche += "<title>Liste d'Etudiants XML</title>";
                this.affiche += "<style type=\"text/css\">";
                this.affiche += "body { background-color: white; }";
                this.affiche += "h2 { text-align: center; }";
                this.affiche += "</style>";
                this.affiche += "</head><body>";
                break;
            case "TitrePage":
                this.affiche += "<h2>" + att.getValue("value");
                break;

            case "Table":
                this.affiche += "<table><tr><th>Id</th><th>Nom</th><th>Prénom</th><th>Groupe</th><th>Action</th></tr>";
                break;
            case "Etudiant":

                break;
            case "LesEtudiants":
                AnalyseurDOM analyseurDom = new AnalyseurDOM();
                this.lesEtudiants = analyseurDom.analyse();
                for (int i = 0; i < this.lesEtudiants.size(); i++) {
                    this.affiche += this.lesEtudiants.get(i).getHtml();
                }
                break;
            default:
                this.affiche += "D�but El�ment " + qName + " non g�r�. <br>";
        }
    }

    public String getAffiche() {
        return affiche;
    }

    public void endElement(String nameSpaceURI, String localName, String qName) throws SAXException {
        switch (qName) {
            case "Page":
                this.affiche += "<br/><a href=\"/nouvelEtudiant\"><button>Nouvel Etudiant</button></a>";
                this.affiche += "</body></html>";
                break;
            case "TitrePage":
                this.affiche += "</h2>";
                this.affiche += "<br/><form method=\"GET\" action=\"rechercher\">"
                        + "<label for=\"champ\"> Rechercher: </label>"
                        + "<input type=\"text\" name=\"champ\"/>"
                        + "<button type=\"submit\">Rechercher</button>"
                        + "</form><br/>";
                break;
            case "Table":
                this.affiche += "</table>";
                break;
            case "LesEtudiants":
                break;
            case "Etudiant":
                break;
            default:
                this.affiche += "Fin Element " + qName + " non géré. </br>";
        }
    }
}
