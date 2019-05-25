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
	
	//Variables d’instances
	Dispatcher myDispatcher;
	VehiculeControler vehicleControler;
	
	private static DataOutputStream out; 
	private static DataInputStream in;
	private static BTConnection BTConnect;
	static String affiche;
	static int commande;
	static boolean stop_app;
	static VehiculeControler vehiculeControler;
	
	public static void main(String[] args) throws InterruptedException, IOException {
		EV3 ev3 = (EV3) BrickFinder.getLocal();
		TextLCD lcd = ev3.getTextLCD();
		connect();
		
				
		lcd.drawString("attente", 4, 4);
    	
    	Thread.sleep(2500);
    	lcd.clear();
    	lcd.drawString("envoie", 4, 4);
    	Thread.sleep(10000);
    	commande = (int) in.readByte();  
    	
    	affiche = String.valueOf(commande);
    	
		lcd.drawString(affiche, 4, 4);
		Thread.sleep(10000);
		/*EV3 ev3 = (EV3) BrickFinder.getLocal();
		TextLCD lcd = ev3.getTextLCD();
		lcd.drawString("tata", 4, 4);
    	stop_app = true;
    	lcd.drawString("tete", 4, 4);
    	vehiculeControler = new VehiculeControler();
    	lcd.drawString("titi", 4, 4);
    	/*while(stop_app) {
    		try {
    			commande = in.toString();
    			dispatch(commande);
    		}
    		catch (IOException ioe) {
    			System.out.println("IO Exception readString");
    		}
    	}*/  
    	
    	 /*vehiculeControler.start();
    	 lcd.clear();
    	 lcd.drawString("Demarrer", 4, 4);
    	 
    	 Thread.sleep(2500);
    	 
    	 vehiculeControler.forward();
    	 lcd.clear();
    	 lcd.drawString("Avancer", 4, 4);    	 
    	
    	 Thread.sleep(2500);
    	 
    	 vehiculeControler.up();
    	 lcd.clear();
    	 lcd.drawString("Accelerer", 4, 4);
    	 
    	 Thread.sleep(2500);   	 
    	  
    	 vehiculeControler.right();
    	 lcd.clear();
    	 lcd.drawString("Droite", 4, 4);
    	 
    	 Thread.sleep(2500);
    	 
    	 vehiculeControler.left();
    	 vehiculeControler.left();
    	 lcd.clear();
    	 lcd.drawString("Gauche", 4, 4);
    	 
    	 Thread.sleep(2500);    
    	 
    	 vehiculeControler.up();
    	 lcd.clear();
    	 lcd.drawString("Accelerer", 4, 4);
    	 
    	 Thread.sleep(2500);
    	 
    	 vehiculeControler.down();
    	 lcd.clear();
    	 lcd.drawString("Ralentir", 4, 4);
    	 
    	 Thread.sleep(2500);
    	 
    	 vehiculeControler.stop();
    	 lcd.clear();
    	 lcd.drawString("Stop", 4, 4);
    	 
    	 Thread.sleep(2500);*/
	}
	
	
	
	public Dispatcher()
	{
		vehicleControler = new VehiculeControler();
	}
	
	public Dispatcher getInstance()
	{
		if(myDispatcher == null)
		{
			myDispatcher = new Dispatcher();
		}
		return myDispatcher;
	}
	
	public static void dispatch(String expression) throws IOException
	{
		switch(expression) {
		  case "on":
			  vehiculeControler.start();
		    break;
		  case "off":
			  vehiculeControler.stop();
		    break;
		  case "forward":
			  vehiculeControler.forward();
			    break;
		  case "backward":
			  vehiculeControler.backward();
			    break;
		  case "up":
			  //vehiculeControler.up();
			    break;
		  case "down":
			  //vehiculeControler.down();
			    break;
		  default:
			  stop_app = false;
			  in.close();
			  out.close();
			  break;
			  
		}	
	}
	public void retrieve(){
	//programme d’écoute du réseau avec utilisation de socket
	//lors de la réception d’une requête, la méthode dispatch(
	//“route”) est appelée pour exécuter la méthode concerné du
	//contrôleur.
	}
	
	public static void connect() {  
		System.out.println("En attente");
		BTConnector BTconnector = (BTConnector) Bluetooth.getNXTCommConnector();
		BTConnect = (BTConnection) BTconnector.waitForConnection(30000, NXTConnection.RAW);
		out = BTConnect.openDataOutputStream();
		in = (DataInputStream) BTConnect.openDataInputStream();
	}

}
