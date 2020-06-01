package metier;

import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.SampleProvider;


/**
 * Classe CapteurObstacle
 * Cette classe permet de définir le capteur d'obstacle du Robot EV3s
 * @version 1.0
 */
public class CapteurObstacle {
	
	/**
	 * capteurSensor : Définit le capteur de présence.
	 */
	public EV3UltrasonicSensor capteurSensor;

	/**
	 * DISTANCE_OBST : Distance de la détection de l'obstacle.
	 */
	public final double DISTANCE_OBST=0.20;
	
	
	/**
	 * Constructeur de la classe CapteurObstacle
	 * @param port : le port de la brique EV3
	 */
	public CapteurObstacle(Port port) {
		this.capteurSensor = new EV3UltrasonicSensor(port);
	}
	
	/**
	 * Méthode qui détecte un obstacle à une distance DISTANCE_OBST
	 * @return (true) si un obstacle est détecté à la distace DISTANCE_OBST, (false) sinon.
	 */
	public boolean obstacleDetect() {
		boolean obstacle = false;
		SampleProvider dist = this.capteurSensor.getDistanceMode();
	
		float[] sample = new float[dist.sampleSize()]; 
		int offsetSample = 0;
		
		this.capteurSensor.fetchSample(sample, offsetSample);
		float distanceObjet = (float)sample[0];
		
		if(distanceObjet<DISTANCE_OBST) {
			obstacle = true;
		}
		return obstacle;
	}
}



	


