package worker;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

public class AnalyseDocument {

	private String[] args = {"xmlParser", "etudiants.xml"};
	
	public static void main(String[] args) {
		XMLReader parser = null;
    	try{
    		parser=XMLReaderFactory.createXMLReader(args[0]);
    	}catch(SAXException e){
    		System.err.println("Impossible d'instancier l'analyseur");
    		System.exit(1);
    	}
    	try{
    		InputSource ls = new InputSource(new FileInputStream(new File(args[1])));
    		parser.parse(ls);
    	}catch(SAXException e){
    		System.err.println("Erreur dans l'analyse du document.");
    	}catch(IOException f){
    		System.err.println("Erreur dans la lecture du document.");
    	}
    	System.out.println("Analyse terminé.");
	}

}
