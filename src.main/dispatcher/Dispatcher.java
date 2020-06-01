package dispatcher;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import lejos.hardware.Bluetooth;
import lejos.remote.nxt.BTConnection;
import lejos.remote.nxt.BTConnector;
import lejos.remote.nxt.NXTConnection;
import metier.VehiculeControler;

public class Dispatcher {

	// Variables d’instances
	Dispatcher myDispatcher;
	VehiculeControler vehiculeControler;

	private static DataOutputStream out;
	private static DataInputStream in;
	private static BTConnection BTConnect;
	private static int commande = 1000;
	private static boolean stop = true;
	
	public static void main(String[] args) throws InterruptedException, IOException {
		connect();
		VehiculeControler vehicule = new VehiculeControler();
		vehicule.start();

		//Boucle qui tourne en permanence
		//Elle permet de lire les messages envoyer par le robot et d'exécuter la bonne méthode du VehiculeControler
			while (stop) {
				try {
					commande = (int) in.readByte();
					switch (commande) {
					case 0 :
						vehicule.start();
						break;
					case 21:
						vehicule.right(10, 300);
						break;
					case 22:
						vehicule.right(20, 500);
						break;	
					case 23:
						vehicule.right(30, 700);
						break;	
					case 24:
						vehicule.right(40, 900);
						break;	
					case 31:
						vehicule.left(10, 300);
						break;
					case 32:
						vehicule.left(20, 500);
						break;
					case 33:
						vehicule.left(30, 700);
						break;
					case 34:
						vehicule.left(40, 900);
						break;
					case 41:
						vehicule.forward(10, 300);
						break;
					case 42:
						vehicule.forward(20, 500);
						break;
					case 43:
						vehicule.forward(30, 700);
						break;
					case 44:
						vehicule.forward(40, 900);
						break;
					case 51:
						vehicule.backward(10, 300);
						break;
					case 52:
						vehicule.backward(20, 500);
						break;
					case 53:
						vehicule.backward(30, 700);
						break;
					case 54:
						vehicule.backward(40, 900);
						break;
					case 61 : 
						vehicule.leftForward(30, 700);
						break;
					case 62 : 
						vehicule.leftForward(40, 900);
						break;
					case 71 : 
						vehicule.rightForward(30, 700);
						break;
					case 72 : 
						vehicule.rightForward(40, 900);
						break;
					case 81 : 
						vehicule.leftBackward(30, 700);
						break;
					case 82 : 
						vehicule.leftBackward(40, 900);
						break;
					case 91 : 
						vehicule.rightBackward(30, 700);
						break;
					case 92 : 
						vehicule.rightBackward(40, 900);
						break;
					case 14:
						vehicule.arret();
						break;
					case 9:
						vehicule.stop();
						stop = false;
						in.close();
						out.close();
						break;
					default:
						break;
					}
				} catch (IOException ioe) {
					System.out.println("Exception erreur readByte");
					stop = false;
					in.close();
					out.close();
				}
			}
			
			
		}
	
	
	public Dispatcher() {
		vehiculeControler = new VehiculeControler();
	}

	public Dispatcher getInstance() {
		if (myDispatcher == null) {
			myDispatcher = new Dispatcher();
		}
		return myDispatcher;
	}

	
	//Méthode permettant la connection avec le smartphone
	public static void connect() {
		System.out.println("En attente");
		//Mise en place de la connection bluetooth
		BTConnector BTconnector = (BTConnector) Bluetooth.getNXTCommConnector();
		BTConnect = (BTConnection) BTconnector.waitForConnection(30000, NXTConnection.RAW);
		//Ouverture des l'écoute et de l'envoi de message
		out = BTConnect.openDataOutputStream();
		in = BTConnect.openDataInputStream();
	}

}
