package dispatcher;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import lejos.hardware.Bluetooth;
import lejos.hardware.BrickFinder;
import lejos.hardware.ev3.EV3;
import lejos.hardware.lcd.TextLCD;
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
	private static int commande = 10;
	private static boolean stop = true;

	public static void main(String[] args) throws InterruptedException, IOException {
		connect();
		
		VehiculeControler vehicule = new VehiculeControler();
		
		while(stop) {
			try {
				commande = (int) in.readByte();
				switch (commande) {
					case 0:
						vehicule.start();
						break;
					case 1:
						vehicule.stop();
						break;
					case 2:
						vehicule.droite();
						break;
					case 3:
						vehicule.gauche();
						break;
					case 4:
						vehicule.forward();
						break;
					case 5:
						vehicule.backward();
						break;
					case 6:
						vehicule.accelerer();
						break;
					case 7:
						vehicule.ralentir();
						break;
					case 9:
						stop = false;
						in.close();
						out.close();
						break;
					default :
						break;
				}
			} catch (IOException ioe) {
				System.out.println("Exception erreur readByte");
			}
		}
		/*
		 * EV3 ev3 = (EV3) BrickFinder.getLocal(); TextLCD lcd = ev3.getTextLCD();
		 * lcd.drawString("tata", 4, 4); stop_app = true; lcd.drawString("tete", 4, 4);
		 * vehiculeControler = new VehiculeControler(); lcd.drawString("titi", 4, 4);
		 * /*while(stop_app) { try { commande = in.toString(); dispatch(commande); }
		 * catch (IOException ioe) { System.out.println("IO Exception readString"); } }
		 */

		/*
		 * vehiculeControler.start(); lcd.clear(); lcd.drawString("Demarrer", 4, 4);
		 * 
		 * Thread.sleep(2500);
		 * 
		 * vehiculeControler.forward(); lcd.clear(); lcd.drawString("Avancer", 4, 4);
		 * 
		 * Thread.sleep(2500);
		 * 
		 * vehiculeControler.up(); lcd.clear(); lcd.drawString("Accelerer", 4, 4);
		 * 
		 * Thread.sleep(2500);
		 * 
		 * vehiculeControler.right(); lcd.clear(); lcd.drawString("Droite", 4, 4);
		 * 
		 * Thread.sleep(2500);
		 * 
		 * vehiculeControler.left(); vehiculeControler.left(); lcd.clear();
		 * lcd.drawString("Gauche", 4, 4);
		 * 
		 * Thread.sleep(2500);
		 * 
		 * vehiculeControler.up(); lcd.clear(); lcd.drawString("Accelerer", 4, 4);
		 * 
		 * Thread.sleep(2500);
		 * 
		 * vehiculeControler.down(); lcd.clear(); lcd.drawString("Ralentir", 4, 4);
		 * 
		 * Thread.sleep(2500);
		 * 
		 * vehiculeControler.stop(); lcd.clear(); lcd.drawString("Stop", 4, 4);
		 * 
		 * Thread.sleep(2500);
		 */
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

	public void dispatch() {
		//inutile au bout du compte
	}

	public void retrieve() {
		// programme d’écoute du réseau avec utilisation de socket
		// lors de la réception d’une requête, la méthode dispatch(
		// “route”) est appelée pour exécuter la méthode concerné du
		// contrôleur.
	}

	public static void connect() {
		System.out.println("En attente");
		BTConnector BTconnector = (BTConnector) Bluetooth.getNXTCommConnector();
		BTConnect = (BTConnection) BTconnector.waitForConnection(30000, NXTConnection.RAW);
		out = BTConnect.openDataOutputStream();
		in = BTConnect.openDataInputStream();
	}

}
