package metier;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.robotics.RegulatedMotor;
import ressources.Etat;

public class VehiculeControler {

	Etat etatVehicule, saveEtatVehicule;
	int saveVitesseMoteurGauche, saveVitesseMoteurDroit;
	private Motor moteurGauche;
	private Motor moteurDroit;

	int vitesseRange = 25;

	public VehiculeControler() {
		etatVehicule = Etat.off;
		this.moteurGauche = new Motor(new EV3LargeRegulatedMotor(MotorPort.B));
		this.moteurDroit = new Motor(new EV3LargeRegulatedMotor(MotorPort.A));
		RegulatedMotor T[] = { this.moteurDroit.getMotorLejos() };
		this.moteurGauche.getMotorLejos().synchronizeWith(T);
		saveVitesseMoteurGauche = 0;
		saveVitesseMoteurDroit = 0;
	}

	public void start() {
		// certaines conditions sont intégrées dans des public voids
		// intermédiaires afin de ne pas polluer le code.
		if (etatVehicule == Etat.off) {
			etatVehicule = Etat.neutral;
			System.out.println("start() : Passage de l'etat moteur a neutral");
		} else {
			System.out.println("start() : le vehicule est deja demarre");
		}
	}

	public void stop() {
		if (etatVehicule != Etat.off) {
			etatVehicule = Etat.off;
			moteurGauche.getMotorLejos().startSynchronization();
			moteurGauche.stopped();
			moteurDroit.stopped();
			moteurGauche.getMotorLejos().endSynchronization();
			saveVitesseMoteurGauche = moteurGauche.getVitesse();
			saveVitesseMoteurDroit = moteurDroit.getVitesse();
			System.out.println("stop() : arret du vehicule");
		} else {
			System.out.println("stop() : le vehicule est deja stoppe");
		}
	}

	public void forward() {
		// si le moteur est en marche arrière il doit d’abord passer
		// par l’état neutral
		// (point mort) pour pouvoir être en marche avant (forward)
		if (etatVehicule == Etat.neutral) {
			etatVehicule = Etat.forward;
			moteurGauche.getMotorLejos().startSynchronization();
			moteurGauche.push(vitesseRange);
			moteurDroit.push(vitesseRange);
			moteurGauche.getMotorLejos().endSynchronization();
			saveVitesseMoteurGauche = moteurGauche.getVitesse();
			saveVitesseMoteurDroit = moteurDroit.getVitesse();
			System.out.println("forward() : le moteur tourne en avant");
		} else if (etatVehicule == Etat.forward) {
			moteurGauche.getMotorLejos().startSynchronization();
			moteurGauche.setVitesse(moteurGauche.getVitesse() + vitesseRange);
			moteurDroit.setVitesse(moteurGauche.getVitesse() + vitesseRange);
			moteurGauche.getMotorLejos().endSynchronization();
			saveVitesseMoteurGauche = moteurGauche.getVitesse();
			saveVitesseMoteurDroit = moteurDroit.getVitesse();
			System.out.println("forward() : le moteur tourne en avant");
		} else {
			System.out.println("forward() : impossible de faire tourner le moteur en avant");
		}
	}

	public void backward() {
		if (etatVehicule == Etat.neutral) {
			etatVehicule = Etat.backward;
			moteurGauche.getMotorLejos().startSynchronization();
			moteurGauche.pull(vitesseRange);
			moteurDroit.pull(vitesseRange);
			moteurGauche.getMotorLejos().endSynchronization();
			saveVitesseMoteurGauche = moteurGauche.getVitesse();
			saveVitesseMoteurDroit = moteurDroit.getVitesse();
			System.out.println("backward() : le moteur tourne en arriere");
		} else if (etatVehicule == Etat.backward) {
			moteurGauche.getMotorLejos().startSynchronization();
			moteurGauche.setVitesse(moteurGauche.getVitesse() + vitesseRange);
			moteurDroit.setVitesse(moteurGauche.getVitesse() + vitesseRange);
			moteurGauche.getMotorLejos().endSynchronization();
			saveVitesseMoteurGauche = moteurGauche.getVitesse();
			saveVitesseMoteurDroit = moteurDroit.getVitesse();
			System.out.println("backward() : le moteur tourne en arriere");
		} else {
			System.out.println("backward() : impossible de faire tourner le moteur en arrière");
		}
	}

	public void left() {
		if (etatVehicule == Etat.forward || etatVehicule == Etat.backward) {
			moteurGauche.getMotorLejos().startSynchronization();
			moteurGauche.setVitesse((int) (moteurGauche.getVitesse() * 0.67));
			moteurDroit.setVitesse((int) (moteurDroit.getVitesse() * 1.33));
			moteurGauche.getMotorLejos().endSynchronization();
			saveVitesseMoteurGauche = moteurGauche.getVitesse();
			saveVitesseMoteurDroit = moteurDroit.getVitesse();
			System.out.println("left() : modification trajectoire (gauche)");
		} else {
			System.out.println("left() : impossible de modifier la trajectoire");
		}
	}

	public void right() {
		if (etatVehicule == Etat.forward || etatVehicule == Etat.backward) {
			moteurGauche.getMotorLejos().startSynchronization();
			moteurGauche.setVitesse((int) (moteurGauche.getVitesse() * 1.33));
			moteurDroit.setVitesse((int) (moteurDroit.getVitesse() * 0.67));
			moteurGauche.getMotorLejos().endSynchronization();
			saveVitesseMoteurGauche = moteurGauche.getVitesse();
			saveVitesseMoteurDroit = moteurDroit.getVitesse();
			System.out.println("right() : modification trajectoire (droite)");
		} else {
			System.out.println("right() : impossible de modifier la trajectoire");
		}
	}

	public void up() {
		if (moteurGauche.getVitesse() < 900 && moteurDroit.getVitesse() < 900) {
			moteurGauche.getMotorLejos().startSynchronization();
			moteurGauche.setVitesse(moteurGauche.getVitesse() + vitesseRange);
			moteurDroit.setVitesse(moteurDroit.getVitesse() + vitesseRange);
			moteurGauche.getMotorLejos().endSynchronization();
			saveVitesseMoteurGauche = moteurGauche.getVitesse();
			saveVitesseMoteurDroit = moteurDroit.getVitesse();
			System.out.println("up() : la vitesse du robot augmente");
		} else {
			System.out.println("up() : la vitesse est au maximum");
		}
	}

	public void down() {
		if (moteurGauche.getVitesse() > vitesseRange && moteurDroit.getVitesse() > vitesseRange) {
			moteurGauche.getMotorLejos().startSynchronization();
			moteurGauche.setVitesse(moteurGauche.getVitesse() - vitesseRange);
			moteurDroit.setVitesse(moteurDroit.getVitesse() - vitesseRange);
			moteurGauche.getMotorLejos().endSynchronization();
			saveVitesseMoteurGauche = moteurGauche.getVitesse();
			saveVitesseMoteurDroit = moteurDroit.getVitesse();
			System.out.println("down() : la vitesse du robot diminue");
		} else {
			moteurGauche.getMotorLejos().startSynchronization();
			moteurGauche.setVitesse(0);
			moteurDroit.setVitesse(0);
			moteurGauche.getMotorLejos().endSynchronization();
			saveVitesseMoteurGauche = moteurGauche.getVitesse();
			saveVitesseMoteurDroit = moteurDroit.getVitesse();
			etatVehicule = Etat.neutral;
			System.out.println("down() : le robot est à l'arrêt");
		}
	}

	public void urgency() {
		moteurGauche.getMotorLejos().startSynchronization();
		saveVitesseMoteurGauche = moteurGauche.getVitesse();
		saveVitesseMoteurDroit = moteurDroit.getVitesse();
		moteurGauche.stopped();
		moteurDroit.stopped();
		moteurGauche.getMotorLejos().endSynchronization();
		saveEtatVehicule = etatVehicule;
		etatVehicule = Etat.urgency;
		System.out.println("urgency() : le vehicule se bloque");
	}

	public void breakdown() {
		moteurGauche.getMotorLejos().startSynchronization();
		saveVitesseMoteurGauche = moteurGauche.getVitesse();
		saveVitesseMoteurDroit = moteurDroit.getVitesse();
		moteurGauche.stopped();
		moteurDroit.stopped();
		moteurGauche.getMotorLejos().endSynchronization();
		saveEtatVehicule = etatVehicule;
		etatVehicule = Etat.panne;
		System.out.println("breakdown() : Simulation de panne");
	}

	public void restore() {
		moteurGauche.getMotorLejos().startSynchronization();
		moteurGauche.setVitesse(saveVitesseMoteurGauche);
		moteurDroit.setVitesse(saveVitesseMoteurDroit);
		moteurGauche.getMotorLejos().endSynchronization();
		etatVehicule = saveEtatVehicule;
		System.out.println("restore() : le véhicule restaure son état");
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
}
