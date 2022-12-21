/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package game;

import javax.xml.parsers.ParserConfigurationException;

/**
 *
 * @author Quentin
 */
public class LanceurDeJeu {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)throws ParserConfigurationException {
        // Declare un Jeu et instancie un nouveau jeu
        JeuDevineLeMotOrdre jeu = new JeuDevineLeMotOrdre();
        //Execute le jeu
        jeu.execute();
    }
}