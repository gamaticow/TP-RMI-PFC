package versions;
import versions.v3.PlayerImp;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

//2. Implémentation de l'interface
public class PlayerDispenserImp extends UnicastRemoteObject implements PlayerDispenser {
	private static final long serialVersionUID = 1L;
	
	private String url = null; // url construite
	private int id = 0;
	
	public PlayerDispenserImp() throws RemoteException, MalformedURLException, InterruptedException {
		super();
		url = "rmi://localhost/player";

	}

	/**
	 * retourne l'url sous laquelle un objet Player est enregistré
	 * @return
	 * retourne l'url sous laquelle un objet Player est enregistré
	 */
	public synchronized String getUrl() throws RemoteException {
        // --- A COMPLETER ---
		return url + id;
	}

	/**
	 * définit la nouvelle url pour délivrer un objet Player
	 * @param url
	 * url d'un objet Player
	 */
	public void setUrl() throws RemoteException, MalformedURLException {
		Naming.rebind(url+id, new PlayerImp(this));
	}

	/**
	 * incrémente l'url et débloque le 1er thread en attente
	 */
	public synchronized void nextUrl() throws RemoteException, MalformedURLException {
        id++;
        notify();
	}

	
	/**
	 * attend la délivrance d'une nouvelle url
	 */
	public synchronized void waitForNewUrl() throws RemoteException, InterruptedException {
        wait();
	}


}
