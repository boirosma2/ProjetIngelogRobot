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
		//Mise en place du VehiculeControler avec l'instanciation des moteurs
		etatVehicule = Etat.off;
		this.moteurGauche = new Motor(new EV3LargeRegulatedMotor(MotorPort.D));
		this.moteurDroit = new Motor(new EV3LargeRegulatedMotor(MotorPort.B));
		RegulatedMotor T[] = { this.moteurDroit.getMotorLejos() };
		this.moteurGauche.getMotorLejos().synchronizeWith(T);
		saveVitesseMoteurGauche = 0;
		saveVitesseMoteurDroit = 0;
	}

	public void start() {
		//Si l'�tat est � off alors on le passe � neutral
		//Simulation du d�marrage des moteurs
		if (etatVehicule == Etat.off) {
			etatVehicule = Etat.neutral;
			System.out.println("start() : Passage de l'etat moteur a neutral");
		} else {
			System.out.println("start() : le vehicule est deja demarre");
		}
	}

	public void stop() {
		//Si l'�tat n'est pas off alors on le met � off et on stop tous les moteurs
		//Simulation d'�teindre les moteurs
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
		//Si le moteur est en marche arri�re il doit d�abord passer
		//Par l��tat neutral (point mort) pour pouvoir �tre en marche avant (forward)
		if (etatVehicule == Etat.neutral) {
			etatVehicule = Etat.forward;
			moteurGauche.getMotorLejos().startSynchronization();
			moteurGauche.push(vitesseRange);
			moteurDroit.push(vitesseRange);
			moteurGauche.getMotorLejos().endSynchronization();
			saveVitesseMoteurGauche = moteurGauche.getVitesse();
			saveVitesseMoteurDroit = moteurDroit.getVitesse();
			System.out.println("forward() : le moteur tourne en avant");
			//Si l'�tat est d�j� � forward alors on augmente la vitesse des moteurs
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
		//Si le moteur est en marche avant il doit d�abord passer
		//Par l��tat neutral (point mort) pour pouvoir �tre en marche arri�re (backward)
		if (etatVehicule == Etat.neutral) {
			etatVehicule = Etat.backward;
			moteurGauche.getMotorLejos().startSynchronization();
			moteurGauche.pull(vitesseRange);
			moteurDroit.pull(vitesseRange);
			moteurGauche.getMotorLejos().endSynchronization();
			saveVitesseMoteurGauche = moteurGauche.getVitesse();
			saveVitesseMoteurDroit = moteurDroit.getVitesse();
			System.out.println("backward() : le moteur tourne en arriere");
			//Si l'�tat est d�j� � backward alors on augmente les vitesses des moteurs
		} else if (etatVehicule == Etat.backward) {
			moteurGauche.getMotorLejos().startSynchronization();
			moteurGauche.setVitesse(moteurGauche.getVitesse() + vitesseRange);
			moteurDroit.setVitesse(moteurGauche.getVitesse() + vitesseRange);
			moteurGauche.getMotorLejos().endSynchronization();
			saveVitesseMoteurGauche = moteurGauche.getVitesse();
			saveVitesseMoteurDroit = moteurDroit.getVitesse();
			System.out.println("backward() : le moteur tourne en arriere");
		} else {
			System.out.println("backward() : impossible de faire tourner le moteur en arri�re");
		}
	}

	public void left() {
		//Si le moteur est en marche avant ou en marche arri�re, alors on fait tourner le robot vers la gauche
		//On augmente plus la vitesse de rotation du moteur droit et on diminue la vitesse du moteur gauche
		//Ainsi, le robot va tourner vers la gauche
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
		//Si le moteur est en marche avant ou en marche arri�re, alors on fait tourner le robot vers la droite
		//On augmente plus la vitesse de rotation du moteur gauche et on diminue la vitesse du moteur droit
		//Ainsi, le robot va tourner vers la droite
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
		//Si la vitesse maximum des moteurs n'est pas atteinte alors on augmente la vitesse des moteurs
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
		//Si la vitesse des moteurs est sup�rieurs � la vitesse de rang
		//Alors on diminue la vitesse gr�ce � la variable vitesseRange (25)
		//Sinon on passe la vitesse des moteurs � 0 et on passe l'�tat � neutral
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
			System.out.println("down() : le robot est � l'arr�t");
		}
	}

	public void urgency() {
		//Simulation d'une urgence sur le robot
		//Passage des moteurs � l'arr�t et l'�tat du robot � urgency
		//On conserve la vitesse actuelle des moteurs et l'�tat du moteur
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
		//Simulation d'une panne sur le robot
		//Passage des moteurs � l'arr�t et l'�tat du robot � panne
		//On conserve la vitesse actuelle des moteurs et l'�tat du moteur
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
		//Restauration des anciennes valeurs de la vitesse et de l'�tat du robot
		//M�thode qui est appel� apr�s un �tat de panne ou urgency
		moteurGauche.getMotorLejos().startSynchronization();
		moteurGauche.setVitesse(saveVitesseMoteurGauche);
		moteurDroit.setVitesse(saveVitesseMoteurDroit);
		moteurGauche.getMotorLejos().endSynchronization();
		etatVehicule = saveEtatVehicule;
		System.out.println("restore() : le v�hicule restaure son �tat");
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
