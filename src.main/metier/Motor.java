package metier;

import lejos.robotics.RegulatedMotor;

public class Motor {

	private RegulatedMotor motor;
	private int power;
	private int previousPower;
	private int vitesseBase = 50;

	public Motor(RegulatedMotor amotor) {
		this.motor = amotor;
	}

	public int getPreviousPower() {
		return previousPower;
	}

	public void setPreviousPower(int previousPower) {
		this.previousPower = previousPower;
	}

	public int getPower() {
		return power;
	}

	public void setPower(int power) {
		this.power = power;
		motor.setSpeed(power);
	}

	public void backward() {
		setPower(vitesseBase);
		motor.backward();
	}

	public void forward() {
		setPower(vitesseBase);
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
		setPower(0);
	}
}
