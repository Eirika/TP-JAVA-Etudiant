package parserEtud;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

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
import static parserEtud.ServeurHTTPSax.listeEtudiant;

public class AnalyseurDOM {

    private DocumentBuilderFactory docBuildFacto;
    private DocumentBuilder docBuilder;
    private ArrayList<Etudiant> lesEtudiants = new ArrayList<>();
    Document doc;

    /**
     *
     */
    public AnalyseurDOM() {
        super();
        try {
            this.docBuildFacto = DocumentBuilderFactory.newInstance();
            this.docBuilder = docBuildFacto.newDocumentBuilder();
            FileInputStream fileInputStream = new FileInputStream(listeEtudiant);
            InputSource inputStream = new InputSource(fileInputStream);
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
    
    public boolean checkValidity(){
        try {
            NodeList page = this.doc.getElementsByTagName("Page");
            if(page.getLength() > 0 && page.item(0).getAttributes().getNamedItem("creator").getNodeValue().equals("tristan-quentin")) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public String getTitle() {
        NodeList titre = this.doc.getElementsByTagName("TitrePage");
        if (titre.getLength() > 0) {
            return titre.item(0).getAttributes().getNamedItem("value").getNodeValue();
        } else {
            return "No Title";
        }
    }

    public ArrayList<Etudiant> analyseEtudiants() {
        NodeList etudiants = this.doc.getElementsByTagName("Etudiant");
        for (int i = 0; i < etudiants.getLength(); i++) {
            NamedNodeMap mapEtud = etudiants.item(i).getAttributes();
            lesEtudiants.add(new Etudiant(mapEtud.getNamedItem("id").getNodeValue(), mapEtud.getNamedItem("Nom").getNodeValue(), mapEtud.getNamedItem("Prenom").getNodeValue(), mapEtud.getNamedItem("Groupe").getNodeValue()));
        }
        return lesEtudiants;
    }

    public Etudiant getEtudiant(String id) {
        this.lesEtudiants = this.analyseEtudiants();
        for (int i = 0; i < this.lesEtudiants.size(); i++) {
            if (this.lesEtudiants.get(i).getId().equals(id)) {
                return this.lesEtudiants.get(i);
            }
        }
        return null;
    }

    public ArrayList<Etudiant> getSearchEtudiant(String champ) {
        champ = champ.toUpperCase();
        ArrayList<Etudiant> etudiants = new ArrayList<>();
        this.lesEtudiants = this.analyseEtudiants();
        for (int i = 0; i < this.lesEtudiants.size(); i++) {
            if (((this.lesEtudiants.get(i).getId()).toUpperCase().equals(champ)) || (this.lesEtudiants.get(i).getNom().toUpperCase().equals(champ)) || (this.lesEtudiants.get(i).getPrenom().toUpperCase().equals(champ)) || (this.lesEtudiants.get(i).getGroupe().toUpperCase().equals(champ))) {
                etudiants.add(this.lesEtudiants.get(i));
            }
        }
        return etudiants;
    }

    public Boolean majEtud(Etudiant etud) {
        this.lesEtudiants = this.analyseEtudiants();
        boolean modif = false;
        try {
            for (int i = 0; i < this.lesEtudiants.size(); i++) {
                if (this.lesEtudiants.get(i).getId().equals(etud.getId())) {
                    this.lesEtudiants.set(i, etud);
                }
            }
            modif = true;
            traitementEtud();
        } catch (Exception e) {
        }
        return modif;

    }

    public Boolean delEtud(String id) {
        this.lesEtudiants = this.analyseEtudiants();
        boolean modif = false;
        try {
            for (int i = 0; i < this.lesEtudiants.size(); i++) {
                if (this.lesEtudiants.get(i).getId().equals(id)) {
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
        Attr pageValue = this.doc.createAttribute("creator");
        pageValue.setValue("tristan-quentin");
        page.setAttributeNode(pageValue);
        Element titre = this.doc.createElement("TitrePage");
        page.appendChild(titre);
        Attr titreValue = this.doc.createAttribute("value");
        titreValue.setValue("CASIR");
        titre.setAttributeNode(titreValue);
        Element laTable = this.doc.createElement("Table");
        page.appendChild(laTable);
        //Balise LesEtudiants
        Element etudiants = this.doc.createElement("LesEtudiants");
        laTable.appendChild(etudiants);

        //Etudiant
        for (Etudiant unEtudiant : this.lesEtudiants) {
            //Balise Etudiant
            Element etudiant = this.doc.createElement("Etudiant");
            etudiants.appendChild(etudiant);
            //Attributs
            Attr id = this.doc.createAttribute("id");
            id.setValue(unEtudiant.getId());
            Attr nom = this.doc.createAttribute("Nom");
            nom.setValue(unEtudiant.getNom());
            Attr prenom = this.doc.createAttribute("Prenom");
            prenom.setValue(unEtudiant.getPrenom());
            Attr groupe = this.doc.createAttribute("Groupe");
            groupe.setValue(unEtudiant.getGroupe());
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
            StreamResult result = new StreamResult(listeEtudiant);
            transformer.transform(source, result);
        } catch (Exception e) {

        }
    }

    public boolean addEtud(Etudiant etud) {
        this.lesEtudiants = this.analyseEtudiants();
        boolean ajout = false;
        this.lesEtudiants.add(etud);
        try {
            traitementEtud();
            ajout = true;
        } catch (Exception e) {
            this.lesEtudiants.remove(etud);
        }
        return ajout;
    }

}
