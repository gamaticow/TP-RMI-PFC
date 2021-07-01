package versions;
import java.rmi.Remote;
import java.rmi.RemoteException;

//1. Définition de l'interface
public interface PlayerDispenser extends Remote {
	
	/**
	 * @return
	 * retourne l'url sous laquelle un objet Player est enregistré
	 */
	public String getUrl() throws RemoteException;
}
