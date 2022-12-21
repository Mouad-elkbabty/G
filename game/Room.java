/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package game;

import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
/**
 *
 * @author Quentin
 */
public class Room {
    
    //attributs de la classe :
    private int depth;
    private int height;
    private int width;
    private String textureBottom;
    private String textureTop;
    private String textureNorth;
    private String textureEast;
    private String textureWest;
    private String textureSouth;
    
    //constructeur sans paramètres : 
    public Room(){
        try{
            // parser dom
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db;
            db = dbf.newDocumentBuilder();
            Document xmlDocument = db.parse(Jeu.PATH_TO_XML + "plateau.xml");

            depth = Integer.parseInt(xmlDocument.getElementsByTagName("depth").item(0).getTextContent());
            height = Integer.parseInt(xmlDocument.getElementsByTagName("height").item(0).getTextContent());
            width = Integer.parseInt(xmlDocument.getElementsByTagName("width").item(0).getTextContent());
            
            textureNorth = xmlDocument.getElementsByTagName("textureNorth").item(0).getTextContent();
            textureEast = xmlDocument.getElementsByTagName("textureEast").item(0).getTextContent();
            textureWest = xmlDocument.getElementsByTagName("textureWest").item(0).getTextContent();
            textureBottom = xmlDocument.getElementsByTagName("textureBottom").item(0).getTextContent();
            textureTop = null;
            textureSouth = null;
            
        }catch(SAXException | IOException | ParserConfigurationException e){
            System.out.println("erreur de parsing");
        }
    }

    //getters :

    public int getDepth() {
        return depth;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public String getTextureBottom() {
        return textureBottom;
    }

    public String getTextureTop() {
        return textureTop;
    }

    public String getTextureNorth() {
        return textureNorth;
    }

    public String getTextureEast() {
        return textureEast;
    }

    public String getTextureWest() {
        return textureWest;
    }

    public String getTextureSouth() {
        return textureSouth;
    }

    
    //setter

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setTextureBottom(String textureBottom) {
        this.textureBottom = textureBottom;
    }

    public void setTextureTop(String textureTop) {
        this.textureTop = textureTop;
    }

    public void setTextureNorth(String textureNorth) {
        this.textureNorth = textureNorth;
    }

    public void setTextureEast(String textureEast) {
        this.textureEast = textureEast;
    }

    public void setTextureWest(String textureWest) {
        this.textureWest = textureWest;
    }

    public void setTextureSouth(String textureSouth) {
        this.textureSouth = textureSouth;
    }
}
