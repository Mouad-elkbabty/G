/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package game;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xmlpull.v1.builder.XmlDocument;

/**
 *
 * @author Quentin
 */
public class Partie {

    private String date;
    private String mot;
    private int niveau;
    private int trouvé;
    private int temps;

    public Partie(String date, String mot, int niveau) {
        this.date = date;
        this.mot = mot;
        this.niveau = niveau;
        trouvé = 0;
        temps = 0;
    }

    // Parser (Dom) le document profil.xml et récuperer date , mot , niveau et
    // trouvé
    public Partie(Element partieElt) {

        Element eltM = (Element) partieElt.getElementsByTagName("tux:mot").item(0);
        Element eltN = (Element) partieElt.getElementsByTagName("tux:niveau").item(0);
        //création de deux variables String 
        String trouvé ="";
        String niveau ="";
        String[] s;
        trouvé = partieElt.getAttribute("trouvé");
        niveau = eltN.getAttribute("niv");
        s = trouvé.split("(?=%)");//on split le string tq s[0] = xx et s[1] = %
        this.date = Profil.xmlDateToProfileDate(partieElt.getAttribute("date"));
        this.mot = eltM.getTextContent();
        
        // affectation des attribut 
        this.trouvé = Integer.parseInt(s[0]);//peut poser probleme à cause du %
        this.niveau = Integer.parseInt(niveau);
    }

    // permet d'actualiser trouve(set)
    public void setTrouve(int nbLettresRestantes) {
        trouvé = ((mot.length() - nbLettresRestantes) / mot.length()) * 100;
    }

    // actualise le temps
    public void setTemps(int temps) {
        this.temps = temps;
    }
      public int getTemps() {
        return temps;
    }
    

    // recupere le niveau
    public int getNiveau() {
        return niveau;
    }

    // recupere trouve
    public int getTrouve() {
        return trouvé;
    }

    // recupere mot
    public String getMot() {
        return mot;
    }

    @Override
    public String toString() {
        return "Partie{" + "date=" + date + ", mot=" + mot + ", niveau=" + niveau + ", trouvé=" + trouvé
                + ", temps=" + temps + '}';
    }

    // cree une partie avec ses elements
    public Element getPartie(Document doc) {

        // creation d'un element partie
        Element eltP = doc.createElement("tux:partie");

        // creation d'un element mot qu'on mettra dans partie
        Element eltM = doc.createElement("tux:mot");

        if (getTrouve() < 100) {
            eltP.setAttribute("trouvé", Integer.toString(getTrouve()) + "%");
        }else{
        Element eltT = doc.createElement("tux:temps");
        eltT.setTextContent(String.valueOf(getTemps()));
        eltP.appendChild(eltT);
        }
        //création d un élement niveau
        Element eltN=doc.createElement("tux:niveau");

        //afectation de l'attribut niv a niveau 
        eltN.setAttribute("niv", Integer.toString(getNiveau()));

        // affectation de l'attribut date a l'element partie
        eltP.setAttribute("date", this.date);

        //affectation de l'élément mot dans l'élément niveau
        eltN.appendChild(eltM);
        
        // affectation de la valeur textuelle qui se trouve dans l'element mot
        eltM.appendChild(doc.createTextNode(this.mot));

        // affectation des element temps et mot en tant que fils de l'élement partie
        eltP.appendChild(eltN);
        eltP.appendChild(eltN);
        
        eltM.setTextContent(getMot());

        return eltP;
    }

}
