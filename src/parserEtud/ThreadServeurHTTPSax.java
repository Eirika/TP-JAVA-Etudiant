package parserEtud;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

import static parserEtud.ServeurHTTPSax.listeEtudiant;

public class ThreadServeurHTTPSax extends Thread {

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
                if (listeEtudiant.exists()) {
                    if (listeEtudiant.isFile()) {
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
                        HtmlGenerator htmlGenerator = new HtmlGenerator();
                        this.s.getOutputStream().write(htmlGenerator.getHeader().getBytes());
                        boolean isValid = analyseur.checkValidity();
                        if (!isValid) {
                            this.s.getOutputStream().write(htmlGenerator.getValidity(isValid).getBytes());
                            br.close();
                            return;
                        }
                        this.s.getOutputStream().write(htmlGenerator.getTitle(analyseur.getTitle()).getBytes());
                        switch (nomFichier.toUpperCase()) {
                            case "NOUVELETUDIANT":
                                this.s.getOutputStream().write(htmlGenerator.getNouveau().getBytes());
                                break;
                            case "AJOUTER":
                                Etudiant newEtudiant = new Etudiant(url[0], url[1], url[2]);
                                this.s.getOutputStream().write(htmlGenerator.getAjoutEtudiant(analyseur.addEtud(newEtudiant)).getBytes());
                                break;
                            case "DETAILETUDIANT":
                                this.s.getOutputStream().write(htmlGenerator.getDetail(analyseur.getEtudiant(url[0])).getBytes());
                                break;
                            case "MODIFIER":
                                Etudiant modifEtudiant = new Etudiant(url[0], url[1], url[2], url[3]);
                                this.s.getOutputStream().write(htmlGenerator.getModifEtudiant(analyseur.majEtud(modifEtudiant)).getBytes());
                                break;
                            case "RECHERCHER":
                                this.s.getOutputStream().write(htmlGenerator.getTableEtudiant(analyseur.getSearchEtudiant(url[0])).getBytes());
                                this.s.getOutputStream().write(htmlGenerator.getReturnBtn().getBytes());
                                break;
                            case "SUPPRIMER":
                                this.s.getOutputStream().write(htmlGenerator.getDelEtudiant(analyseur.delEtud(url[0])).getBytes());
                                break;
                            default:
                                this.s.getOutputStream().write(htmlGenerator.getSearchForm().getBytes());
                                this.s.getOutputStream().write(htmlGenerator.getTableEtudiant(analyseur.analyseEtudiants()).getBytes());
                                this.s.getOutputStream().write(htmlGenerator.getAddEtudiantBtn().getBytes());
                                break;
                        }
                        String end = "</body></html>";
                        this.s.getOutputStream().write(end.getBytes());
                    }
                }
            } else {
                String message = "Aucune source de donnée XML trouvé sur le serveur. La ";
                this.s.getOutputStream().write(message.getBytes());
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
