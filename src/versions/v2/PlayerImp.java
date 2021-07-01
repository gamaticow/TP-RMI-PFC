package versions.v2;

import enumeration.Action;
import enumeration.Result;
import versions.Player;
import versions.PlayerDispenserImp;
import versions.Score;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;

//2. Implémentation de l'interface
public class PlayerImp extends UnicastRemoteObject implements Player {
	private static final long serialVersionUID = 1L;
	
	private static final int MAXTURNS = 3;       // nombre maximum de tours
	
	private int      turn = 0;                   // compteur de tours
	private PlayerDispenserImp dispenser = null; // distributeur d'url
	
	private String[] name   = new String[2];     // noms des 2 joueurs
	private Action[] action = new Action[2];     // actions des 2 joueurs
	private Result[] result = new Result[2];     // résultats des 2 joueurs
	private int[]    v      = new int[2];        // nombre de victoires des 2 joueurs
	private int      nbPlayers = 0;              // nombre de joueurs identifiés
	
	/**
	 * @param dispenser
	 * cet objet est nécessaire pour incrémenter l'url fournie par le distributeur
	 */
	public PlayerImp(PlayerDispenserImp dispenser) throws RemoteException {
		super();

		this.v[0] = 0;
		this.v[1] = 0;
    }

	/**
	 * @param name
	 * le nom avec lequel le joueur s'identifie
	 * @return
	 * id avec lequel le joueur peut réaliser des actions
	 */
	public synchronized int hello(String name) throws RemoteException, InterruptedException {
		if(nbPlayers >= 2)
			return -1;

		int id = nbPlayers;

		System.out.println("Joueur " + name + " identifié");
		this.name[nbPlayers++] = name;

		if(nbPlayers == 1)
			wait();
		else {
			notify();
			System.out.println("Jeu démarré : " + this.name[0] + " vs " + this.name[1]);
		}

        return id;
	}
	
	/**
	 * @param id
	 * id du joueur
	 * @param action
	 * action du joueur
	 * @return
	 * résultat de l'action
	 */
	public synchronized Score play(int id, Action action) throws InterruptedException {
		Score   score      = null;		   // résultat du tour
		int opp = (id + 1) % 2;
		Result  gameResult = null;		   // résultat du jeu
		Action actionsCopy[] = null;
		
		if (action == null) return null;
		
        this.action[id] = action;
		System.out.println(this.name[id] + " -> " + action);

		if(this.action[opp] == null) {
			wait();

			if(turn >= MAXTURNS) {
				if(v[id] > v[(id+1)%2])
					gameResult = Result.GAGNE;
				else if(v[id] < v[(id+1)%2])
					gameResult = Result.PERDU;
				else
					gameResult = Result.NUL;
			}

			score = new Score(Arrays.copyOf(this.action, this.action.length), this.result[id], gameResult);

			this.action[0] = null;
			this.action[1] = null;
		}else {
			turn++;
			getResult();

			System.out.println(this.name[0] + " (" + this.action[0] + ") vs " + this.name[1] + " (" + this.action[1] + ")");
			System.out.println(this.name[0] + " (" + this.result[0] + ") vs " + this.name[1] + " (" + this.result[1] + ")");

			if(this.result[0] == Result.GAGNE)
				this.v[0] = this.v[0] + 1;
			else if(this.result[1] == Result.GAGNE)
				this.v[1] = this.v[1] + 1;

			if(turn >= MAXTURNS) {
				if(v[id] > v[(id+1)%2])
					gameResult = Result.GAGNE;
				else if(v[id] < v[(id+1)%2])
					gameResult = Result.PERDU;
				else
					gameResult = Result.NUL;

				if(gameResult == Result.GAGNE)
					System.out.println(Result.GAGNE + " -> " + this.name[id]);
				else if(gameResult == Result.PERDU)
					System.out.println(Result.GAGNE + " -> " + this.name[(id+1)%2]);
				else
					System.out.println("Match NUL");
			}

			score = new Score(Arrays.copyOf(this.action, this.action.length), this.result[id], gameResult);

			notify();
		}
		return score;
	}
	
	/**
	 * @param id
	 * id du joueur
	 * @return
	 * le nom du joueur opposant
	 */
	public String getOpponentName(int id) throws RemoteException, InterruptedException {
		return this.name[(id + 1) % 2];
	}

	private void getResult() {
		if(action[0] == null || action[1] == null)
			return;

		if(action[0] == Action.CISEAUX && action[1] == Action.FEUILLE) {
			this.result[0] = Result.GAGNE;
			this.result[1] = Result.PERDU;
		} else if(action[0] == Action.FEUILLE && action[1] == Action.PIERRE){
			this.result[0] = Result.GAGNE;
			this.result[1] = Result.PERDU;
		} else if(action[0] == Action.PIERRE && action[1] == Action.CISEAUX){
			this.result[0] = Result.GAGNE;
			this.result[1] = Result.PERDU;
		} else if(action[1] == Action.CISEAUX && action[0] == Action.FEUILLE){
			this.result[1] = Result.GAGNE;
			this.result[0] = Result.PERDU;
		} else if(action[1] == Action.FEUILLE && action[0] == Action.PIERRE){
			this.result[1] = Result.GAGNE;
			this.result[0] = Result.PERDU;
		} else if(action[1] == Action.PIERRE && action[0] == Action.CISEAUX){
			this.result[1] = Result.GAGNE;
			this.result[0] = Result.PERDU;
		} else {
			this.result[0] = Result.NUL;
			this.result[1] = Result.NUL;
		}
	}
	
}
