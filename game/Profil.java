/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package game;

import java.io.IOException;
import java.util.ArrayList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 *
 * @author Quentin
 */
public class Profil {

    public Document doc;
    private String pathProfil = Jeu.PATH_PROFIL;
    private String nom;
    private String dateNaissance;
    private String avatar;
    private ArrayList<Partie> parties;

    public Profil() {
    }

    public Profil(String nomJoueur, String dateNaissance) {
        nom = nomJoueur;
        this.dateNaissance = dateNaissance;
        parties = new ArrayList<>();
    }
    
    public Profil(String nomJoueur, String dateNaissance, String avatar){
        nom = nomJoueur;
        this.dateNaissance = dateNaissance;
        this.avatar = avatar;
        parties = new ArrayList<>();
    } 

    // Cree un DOM à partir d'un fichier XML
    public Profil(String filename) {
        doc = fromXML(filename);
        Element rac = (Element) doc.getDocumentElement();

        Element eltAvatar = (Element) rac.getElementsByTagName("tux:avatar").item(0);
        Element eltN = (Element) rac.getElementsByTagName("tux:nom").item(0);
        Element eltAnniversaire = (Element) doc.getElementsByTagName("tux:anniversaire").item(0);

        //liste des parties
        NodeList partie = doc.getElementsByTagName("tux:partie");
        parties= new ArrayList<>();
        for (int i = 0; i <= partie.getLength(); i++) {
            Element partielt = (Element) partie.item(i);
            Partie p = new Partie(partielt);
            ajouterPartie(p);
        }
        this.nom = eltN.getTextContent();
        this.dateNaissance = xmlDateToProfileDate(eltAnniversaire.getTextContent());
        this.avatar = eltAvatar.getTextContent();

    }

    //  DOM à partir d'un fichier XML
    public Document fromXML(String nomFichier) {
        try {
            return XMLUtil.DocumentFactory.fromFile(pathProfil + nomFichier + ".xml");
        } catch (Exception ex) {
            Logger.getLogger(Profil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    // Sauvegarde un DOM en XML (créé un fichier xml)
    public void toXML(String nomFichier) {
        try {
            XMLUtil.DocumentTransform.writeDoc(doc, pathProfil + nomFichier + ".xml");
        } catch (Exception ex) {
            Logger.getLogger(Profil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /// Takes a date in XML format (i.e. ????-??-??) and returns a date
    /// in profile format: dd/mm/yyyy
    public static String xmlDateToProfileDate(String xmlDate) {
        String date;
        // récupérer le jour
        date = xmlDate.substring(xmlDate.lastIndexOf("-") + 1, xmlDate.length());
        date += "/";
        // récupérer le mois
        date += xmlDate.substring(xmlDate.indexOf("-") + 1, xmlDate.lastIndexOf("-"));
        date += "/";
        // récupérer l'année
        date += xmlDate.substring(0, xmlDate.indexOf("-"));

        return date;
    }

    /// Takes a date in profile format: dd/mm/yyyy and returns a date
    /// in XML format (i.e. ????-??-??)
    public static String profileDateToXmlDate(String profileDate) {
        String date;
        // Récupérer l'année
        date = profileDate.substring(profileDate.lastIndexOf("/") + 1, profileDate.length());
        date += "-";
        // Récupérer  le mois
        date += profileDate.substring(profileDate.indexOf("/") + 1, profileDate.lastIndexOf("/"));
        date += "-";
        // Récupérer le jour
        date += profileDate.substring(0, profileDate.indexOf("/"));

        return date;
    }

    // ajoute une partie
    public void ajouterPartie(Partie p) {
           parties.add(p);
         //ouvrir le fichier xml 
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = null;
        try {
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(Profil.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            doc = documentBuilder.parse("profil_"+getNom()+".xml");
        } catch (SAXException | IOException ex) {
            Logger.getLogger(Profil.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //créer l'élément parties lui affecter la partie joué
        Element root = doc.getDocumentElement();
        Element parties = doc.createElement("tux:parties");
        Element partie = p.getPartie(doc);
        parties.appendChild(partie);
        root.appendChild(parties);
        
        toXML("profil_" +getNom()+".xml");
        
    }

        /**
     * Sauvegarde un profil dans son document xml
     *  - si c'est la première fois qu'il joue ,   il faut créer le document xml et le remplir avec ses informations 
     *  - si le joueur a dèja jouer une fois avant , donc il faut enregistrer la partie dans le document xml
     * @param fileName
     * @param filename le nom du document xml du profil
     */
    
    
    public void sauvegarder(String fileName) {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            doc = dBuilder.newDocument();

            //créer l'élément profil et ses attributs
            Element eltProfil = doc.createElement("tux:profil");
            eltProfil.setAttribute("xmlns:tux", "http://myGame/tux");
            eltProfil.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
            eltProfil.setAttribute("xsi:schemaLocation", "http://myGame/tux ./profil.xsd");

            //créer l'élément nom
            Element nomElt = doc.createElement("tux:nom");
            nomElt.setTextContent(nom);
            eltProfil.appendChild(nomElt);

            //créer l'élément avatar
            Element eltAvatar = doc.createElement("tux:avatar");
            eltAvatar.setTextContent(avatar + ".jpg");
            eltProfil.appendChild(eltAvatar);

            //créer l'élément anniversaire
            Element eltAnniv = doc.createElement("tux:anniversaire");
            eltAnniv.setTextContent(dateNaissance);
            eltProfil.appendChild(eltAnniv);

            //créer l'élément parties
            /*Element partiesElt = doc.createElement("tux:parties");
            eltProfil.appendChild(partiesElt);*/
            
            //ajouter la partie joué
            /*parties.add(p);
            Element partie = p.creerPartie(doc);
            partiesElt.appendChild(partie);*/

            doc.appendChild(eltProfil);

        } catch (ParserConfigurationException | DOMException e) {
            e.printStackTrace();
        }

        
    }

    //charge une partie
    public boolean charge(String nomJoueur) {
        try{
            return (XMLUtil.DocumentFactory.fromFile("src/xml/profil_" + nomJoueur + ".xml") != null);
        } catch (Exception e){
            Logger.getLogger(Profil.class.getName()).log(Level.SEVERE, null, e);
            System.out.println("erreur : profil non trouvé");
        }
        return false;
    }
    
    // recupere le nom
    public String getNom(){
        return nom;
    }
    
    // on renvoie la liste des parties non terminées (en fonction de trouve ) quantité = 5
    public ArrayList<Partie> partiesNonTerminees(){
        ArrayList<Partie> listeParties = new ArrayList<>();
        int i = parties.size() - 1;
        while(listeParties.size() < 5 && i >= 0){
            if(parties.get(i).getTrouve() != 100){
                listeParties.add(parties.get(i));
            }
            i--;
        }
        return listeParties;
    }

}
