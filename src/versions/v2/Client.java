package versions.v2;

import enumeration.Action;
import versions.Player;
import versions.Score;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;

public class Client {
	public static void main(String[] args) throws RemoteException, InterruptedException, MalformedURLException, NotBoundException {
        Scanner  kb     = new Scanner(System.in);
        String   line;
        Player   player = null;    // objet
        String   name;            // nom du joueur
        String   opponentName;  // nom de l'opposant
        int      id;            // id du joueur
        Score    r;                // résultat
        
        //System.setSecurityManager(new RMISecurityManager());
        
        String url = "rmi://localhost/player";
        // 6. Obtention de la référence de l'objet distant
        player = (Player) Naming.lookup(url);

        //Choix du nom
        System.out.print("Nom ?");
        name = kb.nextLine();
        id = player.hello(name);

        //On prévient que le serveur est plein si sont identifiant est -1
        if(id == -1) {
            System.out.println("Le serveur est plein");
            return;
        }

        //Récupération du nom de l'opposant
        opponentName = player.getOpponentName(id);

        //Affichage du match
        System.out.println(name + " vs " + opponentName);

        do {
            Action action = null;
            do {
                System.out.println("Action ?");
                String a = kb.nextLine();
                switch (a) {
                    case "PIERRE":
                        action = Action.PIERRE;
                        break;
                    case "FEUILLE":
                        action = Action.FEUILLE;
                        break;
                    case "CISEAUX":
                        action = Action.CISEAUX;
                        break;
                }
            }while (action == null);
            r = player.play(id, action);
            System.out.println(r.getAction()[id] + " / " + r.getAction()[(id+1)%2] + " - " + r.getResult());

        } while (r.getGameResult() == null);

        System.out.println(r.getGameResult());
    }
}
