package metier;

import lejos.robotics.RegulatedMotor;

/**
 * Classe Motor.
 * Cette classe d�finit le motor EV3.
 * @version 1.0
 */
public class Motor {

	private RegulatedMotor motorLejos;
	private int vitesse;

	/**
	 * Constructeur de la classe Moteur.
	 * @param motorLejos : Interface for encoded motors .
	 */
	public Motor(RegulatedMotor motorLejos) {
		this.motorLejos = motorLejos;
		this.vitesse = 0;
	}

	/**
	 * M�thode qui donne la vitesse du moteur.
	 * @return retourne un entier qui correspond � la vitesse.
	 */
	public int getVitesse() {
		return vitesse;
	}
	
	/**
	 * M�thode qui permet de modifier la vitesse du moteur.
	 * @param vitesse : un entier qui correspond � la nouvelle vitesse � modifier.
	 */
	public void setVitesse(int vitesse) {
		this.vitesse = vitesse;
		motorLejos.setSpeed(vitesse);
	}

	/**
	 * Modifie la vitesse pour faire tourner le moteur dans le sens inverse.
	 * @param vitesse : un entier qui correspond � la nouvelle vitesse � modifier.
	 */
	public void pull(int vitesse) {
		setVitesse(vitesse);
		motorLejos.backward();
	}

	/**
	 * Modifie la vitesse pour faire tourner le moteur dans le sens.
	 * @param vitesse : un entier qui correspond � la nouvelle vitesse � modifier.
	 */
	public void push(int vitesse) {
		setVitesse(vitesse);
		motorLejos.forward();
	}

	/**
	 * @return motorLejos : Interface for encoded motors.
	 */
	public RegulatedMotor getMotorLejos() {
		return motorLejos;
	}

	/**
	 * Modifie l'interface du motor
	 * @param motorLejos : Interface for encoded motors .
	 */
	public void setMotorLejos(RegulatedMotor motorLejos) {
		this.motorLejos = motorLejos;
	}

	/**
	 * Stop le moteur.
	 */
	public void stopped() {
		motorLejos.stop();
		setVitesse(0);
	}
}
