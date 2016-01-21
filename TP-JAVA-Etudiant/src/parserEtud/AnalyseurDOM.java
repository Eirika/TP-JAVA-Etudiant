package parserEtud;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class AnalyseurDOM {

    private DocumentBuilderFactory docBuildFacto;
    private DocumentBuilder docBuilder;
    private FileInputStream fileInputStream;
    private InputSource inputStream;
    private ArrayList<Etudiant> lesEtudiants = new ArrayList<Etudiant>();
    Document doc;

    /**
     *
     */
    public AnalyseurDOM() {
        super();
        try {
            this.docBuildFacto = DocumentBuilderFactory.newInstance();
            this.docBuilder = docBuildFacto.newDocumentBuilder();
            this.fileInputStream = new FileInputStream(ThreadServeurHTTPSax.listeEtudiant);
            this.inputStream = new InputSource(fileInputStream);
            try {
                this.doc = docBuilder.parse(inputStream);
            } catch (SAXException e) {
                e.printStackTrace();
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Etudiant> analyse() {
        NodeList etudiants = this.doc.getElementsByTagName("Etudiant");
        for (int i = 0; i < etudiants.getLength(); i++) {
            NamedNodeMap mapEtud = etudiants.item(i).getAttributes();
            lesEtudiants.add(new Etudiant(Integer.parseInt(mapEtud.getNamedItem("id").getNodeValue()), mapEtud.getNamedItem("Nom").getNodeValue(), mapEtud.getNamedItem("Prenom").getNodeValue(), mapEtud.getNamedItem("Groupe").getNodeValue()));
        }
        return lesEtudiants;
    }

    public ArrayList<Etudiant> getEtudiant(String champ) {
        champ = champ.toUpperCase();
        ArrayList<Etudiant> etudiants = new ArrayList<Etudiant>();
        this.lesEtudiants = analyse();
        for (int i = 0; i < this.lesEtudiants.size(); i++) {
            if ((Integer.toString(this.lesEtudiants.get(i).getId()).toUpperCase().equals(champ)) || (this.lesEtudiants.get(i).getNom().toUpperCase().equals(champ)) || (this.lesEtudiants.get(i).getPrenom().toUpperCase().equals(champ)) || (this.lesEtudiants.get(i).getGroupe().toUpperCase().equals(champ))) {
                etudiants.add(this.lesEtudiants.get(i));
            }
        }
        return etudiants;
    }

    public Boolean majEtud(Etudiant etud) {
        this.lesEtudiants = analyse();
        boolean modif = false;
        try {
            for (int i = 0; i < this.lesEtudiants.size(); i++) {
                if (this.lesEtudiants.get(i).getId() == etud.getId()) {
                    this.lesEtudiants.set(i, etud);
                }
            }
            modif = true;
            traitementEtud();
        } catch (Exception e) {
        }
        return modif;

    }

    public Boolean delEtud(int id) {
        this.lesEtudiants = analyse();
        boolean modif = false;
        try {
            for (int i = 0; i < this.lesEtudiants.size(); i++) {
                if (this.lesEtudiants.get(i).getId() == id) {
                    this.lesEtudiants.remove(i);
                }
            }
            modif = true;
            traitementEtud();
        } catch (Exception e) {
        }
        return modif;

    }

    public void traitementEtud() {
        this.doc = this.docBuilder.newDocument();
        Element page = this.doc.createElement("Page");
        this.doc.appendChild(page);
        Element titre = this.doc.createElement("TitrePage");
        page.appendChild(titre);
        Attr titreValue = this.doc.createAttribute("value");
        titreValue.setValue("CASIR");
        titre.setAttributeNode(titreValue);
        Element laTable = this.doc.createElement("Table");
        page.appendChild(laTable);
        //Balise LesEtudiants
        Element lesEtudiants = this.doc.createElement("LesEtudiants");
        laTable.appendChild(lesEtudiants);

        //Etudiant
        for (int i = 0; i < this.lesEtudiants.size(); i++) {
            //Balise Etudiant
            Element etudiant = this.doc.createElement("Etudiant");
            lesEtudiants.appendChild(etudiant);
            //Attributs
            Attr id = this.doc.createAttribute("id");
            id.setValue(Integer.toString(this.lesEtudiants.get(i).getId()));
            Attr nom = this.doc.createAttribute("Nom");
            nom.setValue(this.lesEtudiants.get(i).getNom());
            Attr prenom = this.doc.createAttribute("Prenom");
            prenom.setValue(this.lesEtudiants.get(i).getPrenom());
            Attr groupe = this.doc.createAttribute("Groupe");
            groupe.setValue(this.lesEtudiants.get(i).getGroupe());
            etudiant.setAttributeNode(id);
            etudiant.setAttributeNode(nom);
            etudiant.setAttributeNode(prenom);
            etudiant.setAttributeNode(groupe);
        }
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            DOMSource source = new DOMSource(this.doc);
            StreamResult result = new StreamResult(ThreadServeurHTTPSax.listeEtudiant);
            transformer.transform(source, result);
        } catch (Exception e) {

        }
    }

    public Boolean addEtud(Etudiant etud) {
        this.lesEtudiants = analyse();
        boolean ajout = false;
        this.lesEtudiants.add(etud);
        try {
            traitementEtud();
            ajout = true;
        } catch (Exception e) {
        }
        return ajout;
    }

}
