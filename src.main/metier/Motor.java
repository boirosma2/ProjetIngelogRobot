package metier;

import lejos.robotics.RegulatedMotor;

public class Motor {

	private RegulatedMotor motor;
	private int speed ;
	private int previousSpeed ;
	private int vitesseBase = 50;

	public Motor(RegulatedMotor amotor) {
		this.motor = amotor;
	}
	
	public int getPreviousSpeed() {
		return previousSpeed;
	}

	public void setPreviousSpeed(int previousSpeed) {
		this.previousSpeed = previousSpeed;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
		motor.setSpeed(speed);
	}

	public void backward() {
		setSpeed(vitesseBase);
		motor.backward();
	}
	
	public void forward() {
		setSpeed(vitesseBase);
		motor.forward();
	}
	
	public RegulatedMotor getMotor() {
		return motor;
	}

	public void setMotor(RegulatedMotor motor) {
		this.motor = motor;
	}

	public void stop() {
		motor.stop();
		setSpeed(0);
	}
}
