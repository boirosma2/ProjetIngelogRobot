package metier;

import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.SensorMode;

/**
* Classe CapteurContacte
* Cette classe permet de définir le capteur de contacte du Robot EV3s
* @version 1.0
*/

public class CapteurContacte {

	/**
	 * capteurContacte : Définit le capteur de contacte.
	 */
	public EV3TouchSensor capteurContacte;

	/**
	 * Constructeur de la classe CapteurObstacle
	 * @param port : le port de la brique EV3
	 */
	public CapteurContacte(Port port) {
		this.capteurContacte = new EV3TouchSensor(port);
	}
	
	
    /**
     * Permet de savoir si le robot est entrée en contacte avec un objet se trouvant devant lui via un capteur
     * EV3TouchSensor.
     * 
     * @return (true) si il y a contact, (false) sinon.
     */
	public boolean contactObstacle() {
		SensorMode contact = this.capteurContacte.getTouchMode();
		float[] sampleContact = new float[contact.sampleSize()];
		contact.fetchSample(sampleContact, 0);
	    float cont = (int) (sampleContact[0] * 100);
	    return cont > 0 ? true : false;
	}
	
}
