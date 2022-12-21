/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package game;

import env3d.Env;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.xml.parsers.ParserConfigurationException;
import org.lwjgl.input.Keyboard;

/**
 *
 * @author Quentin
 */
public abstract class Jeu {

    protected static String PATH_TO_XML = "src/xml/";// le path vers le dossier xml
    protected static String PATH_PROFIL = PATH_TO_XML + "profil_";// le path vers un profil (il faut ajouter le nom et .xml) 

    enum MENU_VAL {
        MENU_SORTIE, MENU_CONTINUE, MENU_JOUE
    }

    //attributs de la classe :
    private final Room mainRoom;
    private final Room menuRoom;
    protected Boolean finished;
    protected final Env env;
    private Profil profil;
    private Tux tux;
    private ArrayList<Letter> lettres;//plusieurs lettres(liste)
    private Dico dico;
    protected EnvTextMap menuText;
    private String mot;

    //constructeurs de la classe :
    public Jeu() {
        // on crée un nouvel environnement
        env = new Env();

        // on instancie une Room
        mainRoom = new Room();

        // Instancie une autre Room pour les menus
        menuRoom = new Room();
        menuRoom.setTextureEast("textures/black.png");
        menuRoom.setTextureWest("textures/black.png");
        menuRoom.setTextureNorth("textures/black.png");
        menuRoom.setTextureBottom("textures/black.png");

        // Règlage de la camera
        env.setCameraXYZ(50, 60, 175);
        env.setCameraPitch(-20);

        // Désactive les contrôles par défaut
        env.setDefaultControl(false);

        // Instancie un profil par défaut
        profil = new Profil();

        //Instaciation du dictionnaire
        dico = new Dico("src/xml/dico.xml");
        dico.lireDictionnaireDOM("src/xml/dico.xml");

        // Instancie une liste de lettres(vide)
        lettres = new ArrayList<Letter>();

        // instancie le menuText
        menuText = new EnvTextMap(env);

        // Textes affichés à l'écran
        // partie
        menuText.addText("Voulez vous ?", "Question", 200, 300);
        menuText.addText("1. Commencer une nouvelle partie ?", "Jeu1", 250, 280);
        menuText.addText("2. Charger une partie existante ?", "Jeu2", 250, 260);
        menuText.addText("3. Sortir de ce jeu ?", "Jeu3", 250, 240);
        menuText.addText("4. Quitter le jeu ?", "Jeu4", 250, 220);

        //  joueur
        menuText.addText("Choisissez un nom de joueur : ", "NomJoueur", 200, 300);
        menuText.addText("1. Charger un profil de joueur existant ?", "Principal1", 250, 280);
        menuText.addText("2. Créer un nouveau joueur ?", "Principal2", 250, 260);
        menuText.addText("3. Sortir du jeu ?", "Principal3", 250, 240);

        // choisir un niveau 
        menuText.addText("Choisissez un niveau [1-5] : ", "Niveau", 200, 300);

        //mot à trouver 
        menuText.addText("Trouvez le mot : ", "AfficheMot", 200, 300);

        //choix de partie à charger
        menuText.addText("Quelle partie souhaitez vous charger ?", "ChoixPartieCharge", 200, 350);
        menuText.addText("Entrez votre anniversaire [yyyy-mm-dd]: ", "annivJoueur", 200, 300);

    }
       private String getAnniv() {
        String anniversaire = "";
        while (!anniversaire.matches("\\d{4}-\\d{2}-\\d{2}")) {
            menuText.getText("annivJoueur").clean();
            menuText.getText("annivJoueur").display();
            anniversaire = menuText.getText("annivJoueur").lire(true);
        }
        menuText.getText("annivJoueur").clean();
        return anniversaire;
    }

    /* Gère le menu principal */
    public void execute() throws ParserConfigurationException {
        MENU_VAL mainLoop;
        mainLoop = MENU_VAL.MENU_SORTIE;
        do {
            mainLoop = menuPrincipal();
        } while (mainLoop != MENU_VAL.MENU_SORTIE);
        this.env.setDisplayStr("Au revoir !", 300, 30);
        // Détruit l'environnement et provoque la sortie du programme 
        env.exit();
    }

    // fourni
    private String getNomJoueur() {
        String nomJoueur = "";
        menuText.getText("NomJoueur").display();
        nomJoueur = menuText.getText("NomJoueur").lire(true);
        menuText.getText("NomJoueur").clean();
        return nomJoueur;
    }

    // permet de récuperer le niv choisit par le joueur, s'il n'est pas entre 1 et 5 on le force à 1
    private int getNiv() {
        int niv;
        menuText.getText("Niveau").display();
        niv = Integer.parseInt(menuText.getText("Niveau").lire(true));
        if (niv < 1 || niv > 5) {
            niv = 1;//on prend niv 1 si l'utilisateur choisit un niv qui n'existe pas
        }
        menuText.getText("Niveau").clean();
        env.advanceOneFrame();
        return niv;
    }

    // affiche le mot à trouver pendant 5 secondes
    private void afficheMot(String mot) {
        menuText.getText("AfficheMot").addTextAndDisplay("", mot);
        env.advanceOneFrame();
        try {
            Thread.sleep(2000);//attend 2 secondes
        } catch (InterruptedException ex) {
            Logger.getLogger(Jeu.class.getName()).log(Level.SEVERE, null, ex);
        }
        // on efface l'affichage 
        menuText.getText("AfficheMot").clean();
        env.advanceOneFrame();
    }

    // à compléter
    private MENU_VAL menuJeu() throws ParserConfigurationException {

        MENU_VAL playTheGame;
        playTheGame = MENU_VAL.MENU_JOUE;
        Partie partie;
        do {
            // restaure la room du menu
            env.setRoom(menuRoom);

            // affiche menu
            menuText.getText("Question").display();
            menuText.getText("Jeu1").display();
            menuText.getText("Jeu2").display();
            menuText.getText("Jeu3").display();
            menuText.getText("Jeu4").display();

            // vérifie qu'une touche 1, 2, 3 ou 4 est pressée
            int touche = 0;
            while (!(touche == Keyboard.KEY_1 || touche == Keyboard.KEY_NUMPAD1 || touche == Keyboard.KEY_2 || touche == Keyboard.KEY_NUMPAD2 || touche == Keyboard.KEY_3 || touche == Keyboard.KEY_NUMPAD3)) {
                touche = env.getKey();
                env.advanceOneFrame();
            }

            // nettoie l'environnement du texte
            menuText.getText("Question").clean();
            menuText.getText("Jeu1").clean();
            menuText.getText("Jeu2").clean();
            menuText.getText("Jeu3").clean();
            menuText.getText("Jeu4").clean();

            // restaure la room du jeu
            env.setRoom(mainRoom);

            // et décide quoi faire en fonction de la touche pressée
            switch (touche) {
                // -----------------------------------------
                // Touche 1 : Commencer une nouvelle partie
                // -----------------------------------------    
                case Keyboard.KEY_NUMPAD1:
                case Keyboard.KEY_1:
                    // choisi un niveau et charge un mot depuis le dico
                    // il faut ajouter un menu pour dire : choisir un niv 
                    int niv = getNiv();
                    mot = dico.getMotDepuisListeNiveaux(niv);
                    //une fois le niv choisit il faut afficher le mot à trouver

                    afficheMot(mot);
                    //---------------------------------------------------------

                    // crée une nouvelle partie
                    partie = new Partie(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), mot, niv);

                    // joue
                    joue(partie);

                    // enregistre la partie dans le profil --> enregistre le profil
                    // .......... profil.*****
                    System.out.println("salam : ");
                    profil.sauvegarder("profil_"+profil.getNom());
                    profil.ajouterPartie(partie);
                    
                    playTheGame = MENU_VAL.MENU_JOUE;
                    break;

                // -----------------------------------------
                // Touche 2 : Charger une partie existante
                // ----------------------------------------- 
                case Keyboard.KEY_NUMPAD2:
                case Keyboard.KEY_2: // charge une partie existante
                    Profil p = new Profil("profil_" + getNomJoueur() + ".xml");
                    ArrayList<Partie> listePartiesNonTerminees = p.partiesNonTerminees();
                    // on choisi une partie parmis celles qui ne sont pas finies
                    partie = choixPartie(listePartiesNonTerminees);
                    // Recupère le mot de la partie existante
                    // ..........
                    // joue
                    if (partie != null) {
                        // Joue
                        joue(partie);
                        // Sauvegarde de la partie dans le profil
                        profil.ajouterPartie(partie);
                    }
                    playTheGame = MENU_VAL.MENU_JOUE;
                    break;

                // -----------------------------------------
                // Touche 3 : Sortie de ce jeu
                // ----------------------------------------- 
                case Keyboard.KEY_NUMPAD3:
                case Keyboard.KEY_3:
                    playTheGame = MENU_VAL.MENU_CONTINUE;
                    break;

                // -----------------------------------------
                // Touche 4 : Quitter le jeu
                // ----------------------------------------- 
                case Keyboard.KEY_NUMPAD4:
                case Keyboard.KEY_4:
                    playTheGame = MENU_VAL.MENU_SORTIE;
            }
        } while (playTheGame == MENU_VAL.MENU_JOUE);
        return playTheGame;
    }

    //choixPartie à faire lors du chargement d'une partie
    private Partie choixPartie(ArrayList<Partie> listeParties) {
        int choix = -1;
        Partie partie = null;
        // choix partie dispo
        if (affichePartiesDispo(listeParties)) {
            while (choix != Keyboard.KEY_1 && choix != Keyboard.KEY_2 && choix != Keyboard.KEY_3
                    && choix != Keyboard.KEY_4 && choix != Keyboard.KEY_5 && choix != Keyboard.KEY_ESCAPE) {
                choix = env.getKey();
                env.advanceOneFrame();
            }
            String date = new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime());

            //traitement du choix
            if (choix != Keyboard.KEY_ESCAPE) {
                if (choix >= 0 && choix < listeParties.size()) {
                    partie = new Partie(date, listeParties.get(choix).getMot(), listeParties.get(choix).getNiveau());
                }
            }
        }
        menuText.getText("ChoixPartieCharge").clean();
        listeParties.stream().forEach(p -> {
            menuText.getText("ChoixPartie" + p.hashCode()).clean();
            menuText.getText("ChoixPartie" + p.hashCode()).destroy();
        });
        return partie;
    }

    // renvoie true si des parties sont disponibles et les affiches
    private boolean affichePartiesDispo(ArrayList<Partie> listePartiesNonTerminees) {
        int i = 1;
        boolean existe = true;
        // existance de la partie
        if (listePartiesNonTerminees.isEmpty()) {
            existe = false;
        } else {
            menuText.getText("ChoixPartieCharge").display();
            for (Partie p : listePartiesNonTerminees) {
                menuText.addText(String.format("%d : mot %s (niveau %d), trouvé à %d%%", i, p.getMot(), p.getNiveau(), p.getTrouve()), "ChoixPartie" + p.hashCode(), 180, 330 - i * 20);

                i++;
            }
        }
        return existe;
    }

    private MENU_VAL menuPrincipal() throws ParserConfigurationException {

        MENU_VAL choix = MENU_VAL.MENU_CONTINUE;
        String nomJoueur;

        // restaure la room du menu
        env.setRoom(menuRoom);

        menuText.getText("Question").display();
        menuText.getText("Principal1").display();
        menuText.getText("Principal2").display();
        menuText.getText("Principal3").display();

        // vérifie qu'une touche 1, 2 ou 3 est pressée
        int touche = 0;
        while (!(touche == Keyboard.KEY_1 || touche == Keyboard.KEY_NUMPAD1 || touche == Keyboard.KEY_2 || touche == Keyboard.KEY_NUMPAD2 || touche == Keyboard.KEY_3 || touche == Keyboard.KEY_NUMPAD3)) {
            touche = env.getKey();
            env.advanceOneFrame();
        }

        menuText.getText("Question").clean();
        menuText.getText("Principal1").clean();
        menuText.getText("Principal2").clean();
        
        
        ;
        menuText.getText("Principal3").clean();

        // et décide quoi faire en fonction de la touche pressée
        switch (touche) {
            // -------------------------------------
            // Touche 1 : Charger un profil existant
            // -------------------------------------
            case Keyboard.KEY_NUMPAD1:
            case Keyboard.KEY_1:
                // demande le nom du joueur existant
                nomJoueur = getNomJoueur();
                // charge le profil de ce joueur si possible
                if (profil.charge(nomJoueur)) {
                    choix = menuJeu();
                    profil = new Profil(nomJoueur);
                } else {
                    choix = MENU_VAL.MENU_SORTIE;//CONTINUE;
                }
                break;

            // -------------------------------------
            // Touche 2 : Créer un nouveau joueur
            // -------------------------------------
            case Keyboard.KEY_NUMPAD2:
            case Keyboard.KEY_2:
                //à modifier

                // demande le nom du nouveau joueur et l'anniversaire
                nomJoueur = getNomJoueur();
                String anniversaire = getAnniv();
                // crée un profil avec le nom d'un nouveau joueur
                //!!!!!!!!!!!!!!!!!!!!!!!!\\
                profil = new Profil(nomJoueur,anniversaire);
                choix = menuJeu();
                break;

            // -------------------------------------
            // Touche 3 : Sortir du jeu
            // -------------------------------------
            case Keyboard.KEY_NUMPAD3:
            case Keyboard.KEY_3:
                choix = MENU_VAL.MENU_SORTIE;
        }
        return choix;
    }

    public void joue(Partie partie) {

        // Instancie un Tux et l'ajoute à l'environnement
        tux = new Tux(env, mainRoom);
        env.addObject(tux);

        // création des lettres et distribution aleatoire (ne sort pas de la room mais les lettres peuvent se chevaucher)
        mot = sansAccent(mot);
        placeLettres(mot);
        // Ici, on peut initialiser des valeurs pour une nouvelle partie
        démarrePartie(partie);

        // Boucle de jeu
        finished = false;
        while (!finished) {
            // affiche le temps restant
            menuText.getText("Chrono").display();

            // Contrôles globaux du jeu (sortie, ...)
            // 1 is for escape key
            if (env.getKey() == 1) {
                finished = true;
            }

            // Contrôles des déplacements de Tux (gauche, droite, ...)
            tux.déplace();

            // Ici, on applique les regles
            appliqueRegles(partie);

            // Fait avancer le moteur de jeu (mise à jour de l'affichage, de l'écoute des événements clavier...)
            env.advanceOneFrame();
        }

        // Ici on peut calculer des valeurs lorsque la partie est terminée
        terminePartie(partie);
        // ajout de la partie
        //profil.ajouterPartie(partie);
        //score 
        menuText.getText("Score").display();

    }

    // s'occupe du placement des lettres de manière aleatoire
    public void placeLettres(String mot) {
        double random;
        double randX;
        double randZ;
        for (int i = 0; i < mot.length(); i++) {
            random = Math.random();
            randX = mainRoom.getWidth() * random;
            random = Math.random();
            randZ = mainRoom.getDepth() * random;
            while (!placementOk(randX, randZ)) {
                random = Math.random();
                randX = mainRoom.getWidth() * random;
                random = Math.random();
                randZ = mainRoom.getDepth() * random;
            }
            Letter letter = new Letter(mot.charAt(i), randX, randZ);
            lettres.add(letter);
            env.addObject(letter);
        }
    }

    // vérifie que le placement est bon (pas en dehors de la room) 
    private boolean placementOk(double x, double z) {
        boolean placement = true;
        // place prise par la lettre
        double placeOccupee = Math.sqrt(Math.pow(4.0 * 3.0, 2) * 2.0);
        // la lettre ne doit pas chevaucher tux et ne doit pas sortir de la room
        if (((x <= mainRoom.getWidth() / 2.0 + placeOccupee) && (x >= mainRoom.getWidth() / 2.0 - placeOccupee))
                || ((z <= mainRoom.getDepth() / 2.0 + placeOccupee) && (z >= mainRoom.getDepth() / 2.0 - placeOccupee))
                || (x > mainRoom.getWidth() - 4.0 * 2)
                || (x < 4.0 * 2)
                || (z > mainRoom.getDepth() - 4.0 * 2)
                || (z < 4.0 * 2)) {

            placement = false;
        }
        //chevauchement de lettres non traité
        return placement;
    }

    // distance entre tux et la lettre
    protected double distance(Letter letter) {
        double dis;
        double tuxX = tux.getX();
        double tuxY = tux.getY();
        double tuxZ = tux.getZ();
        double letterX = letter.getX();
        double letterY = letter.getY();
        double letterZ = letter.getZ();
        double dis1 = Math.pow(tuxX - letterX, 2) + Math.pow(tuxY - letterY, 2) + Math.pow(tuxZ - letterZ, 2);
        dis = Math.sqrt(dis1);
        return dis;
    }

    // renvoie vrai s'il y a collision entre tux et une lettre
    protected boolean collision(Letter letter) {
        boolean collision = false;
        double dis = distance(letter);
        if (dis <= this.tux.getScale() + letter.getScale()) {
            collision = true;
        }
        return collision;
    }

    // récupère la liste des lettres 
    public ArrayList<Letter> getMot() {
        return lettres;
    }

    //récupère le dico
    public Dico getDico() {
        return dico;
    }

    protected abstract void démarrePartie(Partie partie);

    protected abstract void appliqueRegles(Partie partie);

    protected abstract void terminePartie(Partie partie);

    // retire les accents d'un mot (sinon le jeu plante car nous n'avons pas les lettres accentuées) //!!\\ les mots avec des - font planter le jeu //!!\\
    public String sansAccent(String s) {
        String strTemp = Normalizer.normalize(s, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(strTemp).replaceAll("");

    }
}
