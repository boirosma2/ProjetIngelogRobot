package metier;

import lejos.robotics.RegulatedMotor;

public class Motor {

	private RegulatedMotor motorLejos;
	private int vitesse;

	public Motor(RegulatedMotor motorLejos) {
		this.motorLejos = motorLejos;
		this.vitesse = 0;
	}

	public int getVitesse() {
		return vitesse;
	}

	public void setVitesse(int vitesse) {
		this.vitesse = vitesse;
		motorLejos.setSpeed(vitesse);
	}

	public void pull(int vitesse) {
		setVitesse(vitesse);
		motorLejos.backward();
	}

	public void push(int vitesse) {
		setVitesse(vitesse);
		motorLejos.forward();
	}

	public RegulatedMotor getMotorLejos() {
		return motorLejos;
	}

	public void setMotorLejos(RegulatedMotor motorLejos) {
		this.motorLejos = motorLejos;
	}

	public void stopped() {
		motorLejos.stop();
		setVitesse(0);
	}
}
