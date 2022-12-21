/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package game;



/**
 *
 * @author grangeq
 */
public class JeuDevineLeMotOrdre extends Jeu {

    private int nbLettresRestantes;
    private Chronometre chrono;

    public JeuDevineLeMotOrdre() {
        //appel de la classe jeu
        super();
        // instanciation des attributs de la classe
        nbLettresRestantes = 0;
        chrono = new Chronometre(30);
        
        //affichage du chrono
        menuText.addText("Il vous reste : " + chrono.getTime() +  "  secondes", "Chrono", 200, 50);

    }

    // permet de savoir s'il y a collision avec la lettre cherchée (place de la lettre dans le  mot)
    private boolean tuxTrouveLettre() {
        boolean trouve = false;
        int dede = getMot().size() - nbLettresRestantes;
        if (collision(getMot().get(dede))) {
            trouve = true;
        }
        return trouve;
    }
    
    // renvoie le nombre de lettres restantes
    public int getNbLettresRestantes(){
        return nbLettresRestantes;
    }
    
    // voir si on garde car l'affichage du chrono ne marche pas(s'actualise pas)
    public int getTemps(){
        return chrono.getSeconds();
    }

    // gestion du début de partie
    @Override
    protected void démarrePartie(Partie partie) {
        //on lance le chrono
        chrono.start();
        //le nb de lettres restantes est egale à la taille du mot
        nbLettresRestantes = getMot().size();
        chrono = new Chronometre(5 * 1000);
        chrono.start();

    }

    // règles à appliquer lors de la partie
    @Override
    protected void appliqueRegles(Partie partie) {
        partie.setTrouve(getNbLettresRestantes());
        partie.setTemps((int) chrono.getTime());
        // gestion des lettres (on les retire quand il y a collision)
        
        if(nbLettresRestantes > 0){
            
            if (tuxTrouveLettre()) {
                // on retire la lettre correspondante
                env.removeObject(getMot().get(getMot().size()- nbLettresRestantes));
                nbLettresRestantes -= 1;     
            }
        }
        
        // conditions de fin de jeu
        if(nbLettresRestantes == 0 || !(chrono.remainsTime())){
            finished = true;//fin du jeu
        }
    }
    
    // gestion de la fin de partie
    @Override
    protected void terminePartie(Partie partie) {
        // si il reste des lettres mais que le chrono a atteint le temps limite
        chrono.stop();
        partie.setTrouve(getNbLettresRestantes());
        partie.setTemps(getTemps());
        //resultat à afficher
        menuText.addText("vous avez trouvé " + partie.getTrouve() + "% du mot ", "Score", 200, 50);
    }
}
