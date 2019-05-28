package metier;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.robotics.RegulatedMotor;
import ressources.Etat;

public class VehiculeControler {

	Etat etatVehicule;
	int vitesse;
	private Motor moteurGauche;
	private Motor moteurDroit;

	int vitesseRange = 25;
	int vitesseBase = 50;

	public VehiculeControler() {
		etatVehicule = Etat.contact;
		this.moteurGauche = new Motor(new EV3LargeRegulatedMotor(MotorPort.D));
		this.moteurDroit = new Motor(new EV3LargeRegulatedMotor(MotorPort.A));
		RegulatedMotor T[] = { this.moteurDroit.getMotor() };
		this.moteurGauche.getMotor().synchronizeWith(T);
		vitesse = 0;
	}

	public void start() {
		// certaines conditions sont intégrées dans des public voids
		// intermédiaires afin de ne pas polluer le code.
		if (etatVehicule == Etat.contact) {
			etatVehicule = Etat.neutral;
		} else {
			System.out.println("Moteur déjà démarré");
		}
	}

	public void stop() {
		if (etatVehicule == Etat.neutral) {
			etatVehicule = Etat.contact;
		} else {
			System.out.println("vehicule déjà éteint");
		}
	}

	public void neutral() {
		if (etatVehicule == Etat.forward || etatVehicule == Etat.backward) {
			etatVehicule = Etat.neutral;
			moteurGauche.getMotor().startSynchronization();
			moteurGauche.stop();
			moteurDroit.stop();
			moteurGauche.getMotor().endSynchronization();

		} else {
			System.out.println("vehicule déjà à l'arret");
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

	public void gauche() {
		if (etatVehicule == Etat.forward || etatVehicule == Etat.backward) {
			moteurGauche.getMotor().startSynchronization();
			if (moteurGauche.getSpeed() == moteurDroit.getSpeed()) {
				vitesse = moteurGauche.getSpeed();
			}
			moteurGauche.setSpeed((int) (moteurGauche.getSpeed() * 0.66));
			moteurDroit.setSpeed((int) (moteurDroit.getSpeed() * 1.33));
			moteurGauche.getMotor().endSynchronization();
		} else {
			System.out.println("Impossible de faire tourner à gauche");
		}
	}

	public void droite() {
		if (etatVehicule == Etat.forward || etatVehicule == Etat.backward) {
			moteurGauche.getMotor().startSynchronization();
			if (moteurGauche.getSpeed() == moteurDroit.getSpeed()) {
				vitesse = moteurGauche.getSpeed();
			}
			moteurGauche.setSpeed((int) (moteurGauche.getSpeed() * 1.33));
			moteurDroit.setSpeed((int) (moteurDroit.getSpeed() * 0.66));
			moteurGauche.getMotor().endSynchronization();
		} else {
			System.out.println("Impossible de faire tourner à droite");
		}
	}

	/*
	 * public void ring() { buzz(); logger.info("left() : klaxon"); }
	 */

	public void accelerer() {
		if (moteurGauche.getSpeed() < 900 && moteurDroit.getSpeed() < 900) {
			vitesse += vitesseRange;
			moteurGauche.getMotor().startSynchronization();
			moteurGauche.setSpeed(moteurGauche.getSpeed() + vitesseRange);
			moteurDroit.setSpeed(moteurDroit.getSpeed() + vitesseRange);
			moteurGauche.getMotor().endSynchronization();
		}
	}

	public void ralentir() {
		if (moteurGauche.getSpeed() > vitesseRange && moteurDroit.getSpeed() > vitesseRange) {
			vitesse -= vitesseRange;
			moteurGauche.getMotor().startSynchronization();
			moteurGauche.setSpeed(moteurGauche.getSpeed() - vitesseRange);
			moteurDroit.setSpeed(moteurDroit.getSpeed() - vitesseRange);
			moteurGauche.getMotor().endSynchronization();
		} else {
			vitesse = 0;
			neutral();
		}
	}
	/*
	 * public void urgency() { moteur.urgency();
	 * logger.info("urgency() : le véhicule se bloque"); }
	 * 
	 * public void breakdown() { moteur.breakdown();
	 * logger.info("breakdown() : simulation de panne"); }
	 * 
	 * public void restore() { moteur.restoreMotor();
	 * logger.info("restore() : le véhicule restaure son état"); } /*public void
	 * contact() { logger.info("contact() : le véhicule a eu un contact"); return
	 * capteurContact.contact(); }
	 * 
	 * public void obstacleDetection() {
	 * logger.info("obstacleDetection() : le véhicule a observé un obstacle");
	 * return capteurUltrason.detect(); }
	 * 
	 * public void brightness() {
	 * logger.info("brightness() : le véhicule a repéré une couleur"); return
	 * capteurCouleur.color(); }
	 */

	/*
	 * public void EtatRobot() { int vitesse; int angle; Etat etatMoteur; vitesse =
	 * moteur.getVitesse(); angle = moteur.getAngle(); etatMoteur =
	 * moteur.getEtatMoteur();
	 * logger.info("getEtatRobot() : récupération de l’état du robot"); //retourner
	 * l’état du robot au format json }
	 */

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
