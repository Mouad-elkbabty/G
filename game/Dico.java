/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package game;

/**
 *
 * @author grangeq
 */
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;

import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;

public class Dico extends DefaultHandler {

    private StringBuffer buffer;
    private boolean dansDico;
    private boolean dansNivx;
    private boolean dansNiv;
    private boolean dansMot;
    private int nivMot;
    private ArrayList<String> listeNiveau1;
    private ArrayList<String> listeNiveau2;
    private ArrayList<String> listeNiveau3;
    private ArrayList<String> listeNiveau4;
    private ArrayList<String> listeNiveau5;
    private String cheminFichierDico;

    public Dico() {
        super();
        // lecture dico avec sax
        lireDictionnaire();
    }

    public Dico(String cheminFichierDico) {
        this.cheminFichierDico = cheminFichierDico;
        this.listeNiveau1 = new ArrayList<String>();
        this.listeNiveau2 = new ArrayList<String>();
        this.listeNiveau3 = new ArrayList<String>();
        this.listeNiveau4 = new ArrayList<String>();
        this.listeNiveau5 = new ArrayList<String>();

        //lireDictionnaireDOM(cheminFichierDico); //avec dom
        lireDictionnaire();//avec sax
    }

    //Récuperer un mot depuis une des 5 listes en fonction de niveau
    public String getMotDepuisListeNiveaux(int niveau) {
        String mouad;
        switch (verifieNiveau(niveau)) {
            case 5:
                mouad = getMotDepuisListe(listeNiveau5);
                break;
            case 4:
                mouad = getMotDepuisListe(listeNiveau4);
                break;
            case 3:
                mouad = getMotDepuisListe(listeNiveau3);
                break;
            case 2:
                mouad = getMotDepuisListe(listeNiveau2);
                break;
            case 1:
                mouad = getMotDepuisListe(listeNiveau1);
                break;
            default:
                mouad = "Impossible de récupérer le mot";
        }
        return mouad;
    }

    //on récupère un mot depuis une liste (le niveau du mot n'a pas d'importance)
    public String getMotDepuisListe(ArrayList<String> list) {
        String mouad;
        if (list.isEmpty()) {
            mouad = "Empty";
        } else {
            int ind = (int) ((Math.random() * list.size()) + 1);
            mouad = list.get(ind);
        }

        return mouad;
    }

    //Vérifier si le niveau est bien entre 1 et 5 , sinon on génére un niveau entre cet intervalle
    public int verifieNiveau(int niveau) {
        switch (niveau) {
            case 5:
                return 5;
            case 4:
                return 4;
            case 3:
                return 3;

            case 2:
                return 2;
            case 1:
                return 1;
            default:
                niveau = (int) ((Math.random()) * 5 + 1);
                return niveau;
        }
    }

    //Ajouter un mot au dictionnaire , si le niveau est inexistant on l'ajoute au niv 1
    public void ajouteMotADico(int niveau, String mot) {

        switch (verifieNiveau(niveau)) {
            case 5:
                listeNiveau5.add(mot);
                break;
            case 4:
                listeNiveau4.add(mot);
                break;
            case 3:
                listeNiveau3.add(mot);
                break;

            case 2:
                listeNiveau2.add(mot);
                break;
            case 1:
                listeNiveau1.add(mot);
                break;
            default:
                String mouad = "Impossible d'ajouter ce  mot car le niveau est inexisant, ajoute au niveau 1";
                listeNiveau1.add(mot);
                System.out.println(mouad);
        }
    }

    // lecture du dictionnaire avec DOM
    public final void lireDictionnaireDOM(String path) {
        try {
            String mot;
            String dic = path;
            // Analyse le document et créé l’arbre DOM:
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document xmlDocument;
            xmlDocument = (Document) db.parse(dic);

            //Créé un objet chemin XPath et des Nodes pour l'élément niveau 
            XPathFactory pf = XPathFactory.newInstance();
            XPath xpath = pf.newXPath();

            //création des nodelistes
            NodeList niv1 = (NodeList) xpath.evaluate("//niveaux/niveau[@niv='1']/mot", xmlDocument, XPathConstants.NODESET);
            NodeList niv2 = (NodeList) xpath.evaluate("//niveaux/niveau[@niv='2']/mot", xmlDocument, XPathConstants.NODESET);
            NodeList niv3 = (NodeList) xpath.evaluate("//niveaux/niveau[@niv='3']/mot", xmlDocument, XPathConstants.NODESET);
            NodeList niv4 = (NodeList) xpath.evaluate("//niveaux/niveau[@niv='4']/mot", xmlDocument, XPathConstants.NODESET);
            NodeList niv5 = (NodeList) xpath.evaluate("//niveaux/niveau[@niv='5']/mot", xmlDocument, XPathConstants.NODESET);

            //Ajouter des mots de chaque niveau dans la liste convenable de meme niveau
            //exemple mot de niveau 1 sera rangé dans listeNiveau1
            for (int i = 0; i < niv1.getLength(); i++) {
                mot = (String) niv1.item(i).getTextContent();
                listeNiveau1.add(mot);
            }
            for (int i = 0; i < niv2.getLength(); i++) {
                mot = (String) niv2.item(i).getTextContent();
                listeNiveau2.add(mot);
            }
            for (int i = 0; i < niv3.getLength(); i++) {
                mot = (String) niv3.item(i).getTextContent();
                listeNiveau3.add(mot);
            }
            for (int i = 0; i < niv4.getLength(); i++) {
                mot = (String) niv4.item(i).getTextContent();
                listeNiveau4.add(mot);
            }
            for (int i = 0; i < niv5.getLength(); i++) {
                mot = (String) niv5.item(i).getTextContent();
                listeNiveau5.add(mot);
            }

        } catch (ParserConfigurationException | SAXException | IOException | XPathExpressionException ex) {
            Logger.getLogger(Dico.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    //Parsing SAX
    private void lireDictionnaire() {
        try {
            // création d'une fabrique de parseurs SAX 
            SAXParserFactory fabrique = SAXParserFactory.newInstance();

            // création d'un parseur SAX 
            SAXParser parseur = fabrique.newSAXParser();

            // lecture d'un fichier XML avec un DefaultHandler 
            File fichier = new File(Jeu.PATH_TO_XML + "dico.xml");
            DefaultHandler gestionnaire = new DefaultHandler();
            //on parse
            parseur.parse(fichier, gestionnaire);
        } catch (ParserConfigurationException pce) {
            System.out.println("Erreur de configuration du parseur");
            System.out.println("Lors de l'appel à newSAXParser()");
        } catch (SAXException se) {
            System.out.println("Erreur de parsing");
            System.out.println("Lors de l'appel à parse()");
        } catch (IOException ioe) {
            System.out.println("Erreur d'entrée/sortie");
            System.out.println("Lors de l'appel à parse()");
        }
    }

    //gestion du début d'un element
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (qName.equals("dictionnaire")) {
            dansDico = true;//pour savoir quand on se trouve dans l'element
        } else if (qName.equals("niveaux")) {
            dansNivx = true;
            listeNiveau1 = new ArrayList<>();
            listeNiveau2 = new ArrayList<>();
            listeNiveau3 = new ArrayList<>();
            listeNiveau4 = new ArrayList<>();
            listeNiveau5 = new ArrayList<>();
        } else if (qName.equals("niveau")) {
            dansNiv = true;
            nivMot = Integer.parseInt(attributes.getValue("niveau"));
        } else if (qName.equals("mot")) {
            dansMot = true;
            buffer = new StringBuffer();
        }
    }

    //gestion de la fin d'un element 
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equals("dictionnaire")) {
            if (!dansDico) {
                System.out.println("Pas dans le dico");
            } else {
                dansDico = false;// on est plus dans l'element
            }
        } else if (qName.equals("Niveaux")) {
            if (!dansNivx) {
                System.out.println("Pas dans la liste des niveaux");
            } else {
                dansNivx = false;
            }
        } else if (qName.equals("Niveau")) {
            if (!dansNiv) {
                System.out.println("Pas dans un niveau");
            } else {
                dansNiv = false;
            }
        } else if (qName.equals("mot")) {
            if (!dansMot) {
                System.out.println("Pas dans un mot");
            } else {
                dansMot = false;
                System.out.println(String.format("On ajoute le mot : %s de niv %d au dictionnaire", buffer.toString(), nivMot));                Logger.getLogger(Dico.class.getName()).log(Level.INFO, String.format("On ajoute le mot : %s de niv %d au dictionnaire", buffer.toString(), nivMot));
                ajouteMotADico(nivMot, buffer.toString());
                buffer = null;
                nivMot = 0;
            }
        } else {
            System.out.println(String.format("Element %s inattendu", qName));
        }
    }

    // gestion  du text dans  l'element
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (buffer != null) {
            String word = new String(ch, start, length);
            buffer.append(word);
        }
    }

    // au debut du parsing 
    @Override
    public void startDocument() throws SAXException {
        Logger.getLogger(Dico.class.getName()).log(Level.INFO, "Début du parsing SAX");
    }

    // a la fin du parsing
    @Override
    public void endDocument() throws SAXException {
        Logger.getLogger(Dico.class.getName()).log(Level.INFO, "Fin du parsing SAX");
    }

}
