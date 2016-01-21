package parserEtud;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.Socket;
import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;

public class ThreadServeurHTTPSax extends Thread {

    static String path = System.getProperty("user.dir") + "/html/";
    static File listeEtudiant = new File(path + "/listeEtudiantsXML.xml");
    private Socket s;

    public ThreadServeurHTTPSax(Socket sock) {
        super();
        this.s = sock;
    }

    public void run() {
        try {
            InputStream is = this.s.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String rq = br.readLine();
            if ((rq != null) && (rq.startsWith("GET "))) {
                String nomFichier = rq.substring(rq.indexOf('/') + 1);
                nomFichier = nomFichier.substring(0, nomFichier.indexOf(' '));
                String[] url = nomFichier.split("\\?");
                nomFichier = url[0];
                if (url.length > 1) {
                    url = url[1].split("&");
                    for (int i = 0; i < url.length; i++) {
                        url[i] = url[i].substring(url[i].indexOf("=") + 1, url[i].length());
                    }
                }
                AnalyseurDOM analyseur = new AnalyseurDOM();
                String affiche = "";
                switch (nomFichier.toUpperCase()) {
                    case "NOUVELETUDIANT":
                        this.s.getOutputStream().write(new FormsEtudiant().getNouveau().getBytes());
                        break;
                    case "AJOUTER":
                        Etudiant newEtudiant = new Etudiant(Integer.parseInt(url[0]), url[1], url[2], url[3]);
                        if (!analyseur.addEtud(newEtudiant)) {
                            affiche = "<h2>Erreur lors de l'ajout de l'&eacute;tudiant.</h2><br/><a href=\"./listeEtudiant\"><button>Liste des Etudiants</button></a>";
                            this.s.getOutputStream().write(affiche.getBytes());
                        } else {
                            affiche = "<h2>Ajout r�ussi.</h2><br/><a href=\"./listeEtudiant\"><button>Liste des Etudiants</button></a>";
                            this.s.getOutputStream().write(affiche.getBytes());
                        }
                        break;
                    case "DETAILETUDIANT":
                        this.s.getOutputStream().write(new FormsEtudiant().getDetail(url[0]).getBytes());
                        break;
                    case "MODIFIER":
                        Etudiant modifEtudiant = new Etudiant(Integer.parseInt(url[0]), url[1], url[2], url[3]);
                        if (!analyseur.majEtud(modifEtudiant)) {
                            affiche = "<h2>Erreur lors de la modification.</h2><br/><a href=\"./listeEtudiant\"><button>Liste des Etudiants</button></a>";
                            this.s.getOutputStream().write(affiche.getBytes());
                        } else {
                            affiche = "<h2>Modification r�ussie.</h2><br/><a href=\"./listeEtudiant\"><button>Liste des Etudiants</button></a>";
                            this.s.getOutputStream().write(affiche.getBytes());
                        }
                        break;
                    case "RECHERCHER":
                        ArrayList<Etudiant> etud = analyseur.getEtudiant(url[0]);
                        if (etud.size() == 0) {
                            affiche = "<h2>Aucun &eacute;tudiant ne correspond � la recherche.</h2>"
                                    + "<a href=\"./\"><button>Liste des Etudiants</button></a>";
                        } else {
                            affiche = "<table>";
                            for (int i = 0; i < etud.size(); i++) {
                                affiche += etud.get(i).getHtml();
                            }
                            affiche += "</table><br/><a href=\"./\"><button>Liste des Etudiants</button></a>";
                        }
                        this.s.getOutputStream().write(affiche.getBytes());
                        break;
                    case "SUPPRIMER":
                        if (!analyseur.delEtud(Integer.parseInt(url[0]))) {
                            affiche = "<h2>Erreur lors de la suppression.</h2><br/><a href=\"./\"><button>Liste des Etudiants</button></a>";
                            this.s.getOutputStream().write(affiche.getBytes());
                        } else {
                            affiche = "<h2>Suppression r�ussie.</h2><br/><a href=\"./\"><button>Liste des Etudiants</button></a>";
                            this.s.getOutputStream().write(affiche.getBytes());
                        }
                        break;
                    default:
                        if (listeEtudiant.exists()) {
                            if (listeEtudiant.isFile()) {
                                InputStream lis = new FileInputStream(listeEtudiant);
                                Reader lecteur = new InputStreamReader(lis, "UTF-8");
                                InputSource buflec = new InputSource(lecteur);
                                buflec.setEncoding("UTF-8");

                                SAXParserFactory fabrique = SAXParserFactory.newInstance();
                                SAXParser parseur = fabrique.newSAXParser();

                                AnalyseurSAX gestionnaire = new AnalyseurSAX(s);
                                parseur.parse(buflec, gestionnaire);
                                this.s.getOutputStream().write(gestionnaire.getAffiche().getBytes());
                            }
                        } else {
                            String message = "Le fichier " + nomFichier + " doit �tre dans G:\\Bureau\\";
                            this.s.getOutputStream().write(message.getBytes());
                        }
                        break;
                }
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            this.s.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
