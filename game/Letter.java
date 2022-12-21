/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package game;

import env3d.advanced.EnvNode;

/**
 *
 * @author Quentin
 */
public class Letter extends EnvNode{
    
    private char letter;
    // une lettre est representee par un caractere et des coordonnees x et z (y etant la hauteur nous n'en avons pas besoin dans les parametres)
    // y ne changera pas car on ne veut pas de lettre sous le sol ou de lettre volante
    public Letter(char l, double x, double z){
        letter = l;
        setScale(4.0);
        setX(x);
        setY(getScale() * 1.1); // lettre au sol
        setZ(z);
        if(letter==' '){
            setTexture("models/letter/cube.png");
        }else{
            setTexture("models/letter/"+ letter +".png");
        }
        setModel("models/letter/cube.obj");
        
    }
}
