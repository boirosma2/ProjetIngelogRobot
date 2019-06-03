package metier;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.robotics.RegulatedMotor;
import ressources.Etat;

public class VehiculeControler {

	Etat etatVehicule, saveEtatVehicule;
	int vitesse, saveVitesseMoteurGauche, saveVitesseMoteurDroit;
	private Motor moteurGauche;
	private Motor moteurDroit;

	int vitesseRange = 25;
	int vitesseBase = 50;

	public VehiculeControler() {
		etatVehicule = Etat.off;
		this.moteurGauche = new Motor(new EV3LargeRegulatedMotor(MotorPort.D));
		this.moteurDroit = new Motor(new EV3LargeRegulatedMotor(MotorPort.A));
		RegulatedMotor T[] = { this.moteurDroit.getMotor() };
		this.moteurGauche.getMotor().synchronizeWith(T);
		vitesse = 0;
	}

	public void on() {
		// certaines conditions sont intégrées dans des public voids
		// intermédiaires afin de ne pas polluer le code.
		if (etatVehicule == Etat.off) {
			etatVehicule = Etat.neutral;
		}
	}

	public void off() {
		if (etatVehicule == Etat.neutral) {
			etatVehicule = Etat.off;
		}
	}

	public void stop() {
		if (etatVehicule == Etat.forward || etatVehicule == Etat.backward) {
			etatVehicule = Etat.neutral;
			moteurGauche.getMotor().startSynchronization();
			moteurGauche.stop();
			moteurDroit.stop();
			moteurGauche.getMotor().endSynchronization();

		} else {
			System.out.println("vehicule deja a l arret");
		}
	}

	public void forward() {
		// if { le moteur est en marche arrière il doit d’abord passer
		// par l’état neutral
		// (point mort) pour pouvoir être en marche avant (forward)
		if (etatVehicule == Etat.neutral) {
			etatVehicule = Etat.forward;
			moteurGauche.getMotor().startSynchronization();
			moteurGauche.forward();
			moteurDroit.forward();
			moteurGauche.getMotor().endSynchronization();
			vitesse = vitesseBase;
		} else if (etatVehicule == Etat.forward) {
			moteurGauche.getMotor().startSynchronization();
			moteurGauche.getMotor().setSpeed(vitesse);
			moteurDroit.getMotor().setSpeed(vitesse);
			moteurGauche.getMotor().endSynchronization();
		} else {
			System.out.println("Impossible de faire avancer");
		}
	}

	public void backward() {
		if (etatVehicule == Etat.neutral) {
			etatVehicule = Etat.backward;
			moteurGauche.getMotor().startSynchronization();
			moteurGauche.backward();
			moteurDroit.backward();
			moteurGauche.getMotor().endSynchronization();
			vitesse = vitesseBase;
		} else if (etatVehicule == Etat.backward) {
			moteurGauche.getMotor().startSynchronization();
			moteurGauche.getMotor().setSpeed(vitesse);
			moteurDroit.getMotor().setSpeed(vitesse);
			moteurGauche.getMotor().endSynchronization();
		} else {
			System.out.println("Impossible de faire reculer");
		}
	}

	public void left() {
		if (etatVehicule == Etat.forward || etatVehicule == Etat.backward) {
			moteurGauche.getMotor().startSynchronization();
			if (moteurGauche.getPower() == moteurDroit.getPower()) {
				vitesse = moteurGauche.getPower();
			}
			moteurGauche.setPower((int) (moteurGauche.getPower() * 0.66));
			moteurDroit.setPower((int) (moteurDroit.getPower() * 1.33));
			moteurGauche.getMotor().endSynchronization();
		} else {
			System.out.println("Impossible de faire tourner a gauche");
		}
	}

	public void right() {
		if (etatVehicule == Etat.forward || etatVehicule == Etat.backward) {
			moteurGauche.getMotor().startSynchronization();
			if (moteurGauche.getPower() == moteurDroit.getPower()) {
				vitesse = moteurGauche.getPower();
			}
			moteurGauche.setPower((int) (moteurGauche.getPower() * 1.33));
			moteurDroit.setPower((int) (moteurDroit.getPower() * 0.66));
			moteurGauche.getMotor().endSynchronization();
		} else {
			System.out.println("Impossible de faire tourner a droite");
		}
	}

	public void up() {
		if (moteurGauche.getPower() < 900 && moteurDroit.getPower() < 900) {
			vitesse += vitesseRange;
			moteurGauche.getMotor().startSynchronization();
			moteurGauche.setPower(moteurGauche.getPower() + vitesseRange);
			moteurDroit.setPower(moteurDroit.getPower() + vitesseRange);
			moteurGauche.getMotor().endSynchronization();
		}
	}

	public void down() {
		if (moteurGauche.getPower() > vitesseRange && moteurDroit.getPower() > vitesseRange) {
			vitesse -= vitesseRange;
			moteurGauche.getMotor().startSynchronization();
			moteurGauche.setPower(moteurGauche.getPower() - vitesseRange);
			moteurDroit.setPower(moteurDroit.getPower() - vitesseRange);
			moteurGauche.getMotor().endSynchronization();
		} else {
			vitesse = 0;
			stop();
		}
	}

	public void urgency() {
		moteurGauche.getMotor().startSynchronization();
		saveVitesseMoteurGauche = moteurGauche.getPower();
		saveVitesseMoteurDroit = moteurDroit.getPower();
		moteurGauche.stop();
		moteurDroit.stop();
		moteurGauche.getMotor().endSynchronization();
		saveEtatVehicule = etatVehicule;
		etatVehicule = Etat.urgency;
	}

	public void breakdown() {
		moteurGauche.getMotor().startSynchronization();
		saveVitesseMoteurGauche = moteurGauche.getPower();
		saveVitesseMoteurDroit = moteurDroit.getPower();
		moteurGauche.stop();
		moteurDroit.stop();
		moteurGauche.getMotor().endSynchronization();
		saveEtatVehicule = etatVehicule;
		etatVehicule = Etat.panne;
	}

	public void restore() {
		moteurGauche.getMotor().startSynchronization();
		moteurGauche.setPower(saveVitesseMoteurGauche);
		moteurDroit.setPower(saveVitesseMoteurDroit);
		moteurGauche.getMotor().endSynchronization();
		etatVehicule = saveEtatVehicule;
	}

	public Etat getEtatVehicule() {
		return etatVehicule;
	}

	public void setEtatVehicule(Etat etatVehicule) {
		this.etatVehicule = etatVehicule;
	}

	public int getVitesseRange() {
		return vitesseRange;
	}

	public void setVitesseRange(int vitesseRange) {
		this.vitesseRange = vitesseRange;
	}

	public Motor getMoteurGauche() {
		return moteurGauche;
	}

	public void setMoteurGauche(Motor moteurGauche) {
		this.moteurGauche = moteurGauche;
	}

	public Motor getMoteurDroit() {
		return moteurDroit;
	}

	public void setMoteurDroit(Motor moteurDroit) {
		this.moteurDroit = moteurDroit;
	}

	public int getVitesseBase() {
		return vitesseBase;
	}

	public void setVitesseBase(int vitesseBase) {
		this.vitesseBase = vitesseBase;
	}

}
