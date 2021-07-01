package versions.v3;

import versions.PlayerDispenserImp;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class Serveur {

	public static void main(String[] args) throws RemoteException, MalformedURLException, InterruptedException {
		// 4. instanciation d'un objet serveur
		PlayerDispenserImp playerDispenser = new PlayerDispenserImp();
		
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
            Naming.rebind("rmi://localhost/playerDispenser", playerDispenser);
		}
		catch (Exception e) {
			System.out.println("Erreur : " + e.getMessage());
		}

		while (true) {
			playerDispenser.setUrl();
			playerDispenser.waitForNewUrl();
		}
	}
}
