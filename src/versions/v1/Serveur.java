package versions.v1;

import java.net.InetAddress;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

import versions.Player;
import versions.v1.PlayerImp;

public class Serveur {

	public static void main(String[] args) throws RemoteException {
		// 4. instanciation d'un objet serveur
		Player player = new PlayerImp(null);
		
		// mise en place du service de nommage (port rmi standard 1099)
		try {
			LocateRegistry.createRegistry(1099);
		}
		catch (RemoteException e) {
				LocateRegistry.getRegistry();
		}
		
		// mise en place d'un gestionnaire de sécurité
		//System.setSecurityManager(new RMISecurityManager());
		
		try {
			System.out.println(InetAddress.getLocalHost().getHostAddress());

		    // 5. publication auprès du service de nommage
            Naming.rebind("rmi://localhost/player", player);
		}
		catch (Exception e) {
			System.out.println("Erreur : " + e.getMessage());
		}	
	}
}
