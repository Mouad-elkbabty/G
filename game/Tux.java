/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package game;

import env3d.Env;
import env3d.advanced.EnvNode;
import org.lwjgl.input.Keyboard;

/**
 *
 * @author Quentin
 */
public class Tux extends EnvNode {

    private Env env;
    private Room room;

    // personnage tux
    public Tux(Env env, Room room) {
        this.env = env;// initialisation de l'attribut env
        this.room = room;// initialisation de l'attribut room
        setScale(4.0);
        setX(room.getWidth()/2);// positionnement au milieu de la largeur de la room
        setY(getScale() * 1.1); // positionnement en hauteur basé sur la taille de Tux
        setZ(room.getDepth()/2); // positionnement au milieu de la profondeur de la room
        setTexture("models/tux/tux_sad.png");
        setModel("models/tux/tux.obj");
    }
    
    // déplacement du personnage
    public void déplace(){
        // Fleche 'haut' ou Z
        if (env.getKeyDown(Keyboard.KEY_Z) || env.getKeyDown(Keyboard.KEY_UP)) { 
            // Haut
             if (collZ(this.getZ(), 3)) {
                this.setRotateY(180);
                this.setZ(this.getZ() - 1.0);
            }
       }
       // Fleche 'gauche' ou Q
       if (env.getKeyDown(Keyboard.KEY_Q) || env.getKeyDown(Keyboard.KEY_LEFT)) { 
            // Gauche
               if (collX(this.getX(), 4)) {
                this.setRotateY(255);
                this.setX(this.getX() - 1.0);
            }
       }
       // Fleche 'droite' ou D
       if (env.getKeyDown(Keyboard.KEY_D) || env.getKeyDown(Keyboard.KEY_RIGHT)) { 
            // Gauche
                if (collX(this.getX(), 96)) {
                this.setRotateY(90);
                this.setX(this.getX() + 1.0);
            }
       }
       // Fleche 'bas' ou S
        if (env.getKeyDown(Keyboard.KEY_S) || env.getKeyDown(Keyboard.KEY_DOWN)) { 
            // Haut
            if (collZ(this.getZ(), 97)) {
                this.setRotateY(0);
                this.setZ(this.getZ() + 1.0);
            }
       }
    }
    
        private boolean collX(double x1, double x2) {
        boolean collision = true;
        if (x2 == x1) {
            collision = false;
        }
        return collision;
    }

    private boolean collZ(double z1, double z2) {
        boolean collision = true;
        if (z2 == z1) {
            collision = false;
        }
        return collision;
    }
}
