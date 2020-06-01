package metier;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.robotics.RegulatedMotor;
import ressources.Etat;
import lejos.hardware.port.SensorPort;

/**
* Classe VehiculeControler.
* Cette classe définit les méthodes du véhicule EV3.
* @version 1.0
*/
public class VehiculeControler {

	Etat etatVehicule, saveEtatVehicule;
	int vitesse, saveVitesseMoteurGauche, saveVitesseMoteurDroit;
	private Motor moteurGauche;
	private Motor moteurDroit;
	private CapteurObstacle capteurPresence;
	private CapteurContacte capteurContacte;
	int vitesseRange = 25;

	/**
	 * Mise en place du VehiculeControler avec l'instanciation des moteurs
	 */
	public VehiculeControler() {
		this.etatVehicule = Etat.off;
		//Initialisation du capteur.
		this.moteurGauche = new Motor(new EV3LargeRegulatedMotor(MotorPort.D));
		this.moteurDroit = new Motor(new EV3LargeRegulatedMotor(MotorPort.A));
		this.capteurPresence = new CapteurObstacle(SensorPort.S2);
		this.capteurContacte = new CapteurContacte(SensorPort.S1);		
		RegulatedMotor T[] = { this.moteurDroit.getMotorLejos() };
		this.moteurGauche.getMotorLejos().synchronizeWith(T);
		vitesse = 0;
		saveVitesseMoteurGauche = 0;
		saveVitesseMoteurDroit = 0;
	}

	/**
	 * Methode permettant de passer le véhicule d'état off à l'état neutral
	 */
	public void start() {
		if (etatVehicule == Etat.off) {
			etatVehicule = Etat.neutral;
			System.out.println("start() : Passage de l'etat moteur a neutral");
		} else {
			System.out.println("start() : le vehicule est deja demarre");
		}
	}

	/**	
	 * Methode permettant l'arret des moteur et passage a l'etat off.
	 */
	public void stop() {
		// Si l'état different de off. On arrête le moteur?
		if (etatVehicule != Etat.off) {
			etatVehicule = Etat.off;
			moteurGauche.getMotorLejos().startSynchronization();
			moteurGauche.stopped();
			moteurDroit.stopped();
			moteurGauche.getMotorLejos().endSynchronization();
			saveVitesseMoteurGauche = moteurGauche.getVitesse();
			saveVitesseMoteurDroit = moteurDroit.getVitesse();
			vitesse = 0;
			System.out.println("stop() : arret du vehicule");
		} else {
			System.out.println("stop() : le vehicule est deja stoppe");
		}
	}

	/**
	 * Methode permettant de faire avancer le robot EV3 vers l'avant.
	 * @param vitesseAjout : correspond à la vitesse qu'on incrémente au moteur
	 * @param vitesseMax : correspond à la vitesse maximum que peut atteindre le robot en marche avant.
	 */
	public void forward(int vitesseAjout,int vitesseMax) {
		// On verifie si il y a un obstacle avant de faire avance le robot
		if(!capteurPresence.obstacleDetect()) {
			// Moteur passer de l'etat neutral a forward
			if (etatVehicule == Etat.neutral) {
				etatVehicule = Etat.forward;
				moteurGauche.getMotorLejos().startSynchronization();
				moteurGauche.push(vitesseRange);
				moteurDroit.push(vitesseRange);
				moteurGauche.getMotorLejos().endSynchronization();
				vitesse = vitesseRange;
				saveVitesseMoteurGauche = moteurGauche.getVitesse();
				saveVitesseMoteurDroit = moteurDroit.getVitesse();
				System.out.println("forward() : le moteur tourne en avant");
				//On vérifie que le moteur n'atteint pas la vitesse max
				if (moteurGauche.getVitesse()+ vitesseAjout < vitesseMax && moteurDroit.getVitesse()+ vitesseAjout < vitesseMax) {
					moteurGauche.getMotorLejos().startSynchronization();
					moteurGauche.setVitesse(moteurGauche.getVitesse() + vitesseAjout);
					moteurDroit.setVitesse(moteurDroit.getVitesse() + vitesseAjout);
					moteurGauche.getMotorLejos().endSynchronization();
					vitesse += vitesseAjout;
					saveVitesseMoteurGauche = moteurGauche.getVitesse();
					saveVitesseMoteurDroit = moteurDroit.getVitesse();
					System.out.println("up() : la vitesse du robot augmente");
				} else {
					System.out.println("up() : la vitesse est au maximum");
				}
				//Si l'état est déjà à forward alors on augmente juste la vitesse des moteurs
			} else if (etatVehicule == Etat.forward) {
				moteurGauche.getMotorLejos().startSynchronization();
				moteurGauche.setVitesse(vitesse);
				moteurDroit.setVitesse(vitesse);
				moteurGauche.getMotorLejos().endSynchronization();
				saveVitesseMoteurGauche = moteurGauche.getVitesse();
				saveVitesseMoteurDroit = moteurDroit.getVitesse();
				System.out.println("forward() : le moteur tourne en avant");
				//On vérifie que le moteur n'atteint pas la vitesse max
				if (moteurGauche.getVitesse()+ vitesseAjout < vitesseMax && moteurDroit.getVitesse()+ vitesseAjout < vitesseMax) {
					moteurGauche.getMotorLejos().startSynchronization();
					moteurGauche.setVitesse(moteurGauche.getVitesse() + vitesseAjout);
					moteurDroit.setVitesse(moteurDroit.getVitesse() + vitesseAjout);
					moteurGauche.getMotorLejos().endSynchronization();
					vitesse += vitesseAjout;
					saveVitesseMoteurGauche = moteurGauche.getVitesse();
					saveVitesseMoteurDroit = moteurDroit.getVitesse();
					System.out.println("up() : la vitesse du robot augmente");
				} else {
					System.out.println("up() : la vitesse est au maximum");
				}
				//Si il était en marche arrière alors, on le fait passer à nouveau en etat forward.
			} else if(etatVehicule == Etat.backward) {
				moteurGauche.stopped();
				moteurDroit.stopped();
				etatVehicule = Etat.forward;
				moteurGauche.getMotorLejos().startSynchronization();
				moteurGauche.push(vitesseRange);
				moteurDroit.push(vitesseRange);
				moteurGauche.getMotorLejos().endSynchronization();
				vitesse = vitesseRange;
				saveVitesseMoteurGauche = moteurGauche.getVitesse();
				saveVitesseMoteurDroit = moteurDroit.getVitesse();
			}else {
				System.out.println("forward() : impossible de faire tourner le moteur en avant");
			}
			//gestion erreur capteur.
		}else {
			moteurGauche.getMotorLejos().startSynchronization();
			moteurGauche.stopped();
			moteurDroit.stopped();
			moteurGauche.getMotorLejos().endSynchronization();
			saveVitesseMoteurGauche = moteurGauche.getVitesse();
			saveVitesseMoteurDroit = moteurDroit.getVitesse();
			vitesse = 0;
			etatVehicule = Etat.neutral;
			System.out.println("----------------");
			System.out.println("    Obstacle    ");
			System.out.println("----------------");
		}
		
	}
	
	/**
	 * Methode permettant d'arreter le véhicule en stoppant les deux moteurs.
	 */
	public void arret() {
		moteurGauche.getMotorLejos().startSynchronization();
		moteurGauche.stopped();
		moteurDroit.stopped();
		moteurGauche.getMotorLejos().endSynchronization();
		saveVitesseMoteurGauche = moteurGauche.getVitesse();
		saveVitesseMoteurDroit = moteurDroit.getVitesse();
		vitesse = 0;
		etatVehicule = Etat.neutral;
	}
	
	/**
	 * Methode permettant de faire reculer le robot EV3 vers l'avant.
	 * @param vitesseAjout : correspond à la vitesse qu'on incrémente au moteur
	 * @param vitesseMax : correspond à la vitesse maximum que peut atteindre le robot en marche avant.
	 */
	public void backward(int vitesseAjout,int vitesseMax) {
		//On verifie que le capteur d'obstacle ne soit pas enclenche avant de reculer.
		if(!capteurContacte.contactObstacle()) {
			// Modification de l'etat du vehicule en fonction de son état actuel.
			if (etatVehicule == Etat.neutral) {
				etatVehicule = Etat.backward;
				moteurGauche.getMotorLejos().startSynchronization();
				moteurGauche.pull(vitesseRange);
				moteurDroit.pull(vitesseRange);
				moteurGauche.getMotorLejos().endSynchronization();
				vitesse = vitesseRange;
				saveVitesseMoteurGauche = moteurGauche.getVitesse();
				saveVitesseMoteurDroit = moteurDroit.getVitesse();
				System.out.println("backward() : le moteur tourne en arriere");
				//On vérifie que le moteur n'atteint pas la vitesse max
				if (moteurGauche.getVitesse()+ vitesseAjout < vitesseMax && moteurDroit.getVitesse()+ vitesseAjout < vitesseMax) {
					moteurGauche.getMotorLejos().startSynchronization();
					moteurGauche.setVitesse(moteurGauche.getVitesse() + vitesseAjout);
					moteurDroit.setVitesse(moteurDroit.getVitesse() + vitesseAjout);
					moteurGauche.getMotorLejos().endSynchronization();
					vitesse += vitesseAjout;
					saveVitesseMoteurGauche = moteurGauche.getVitesse();
					saveVitesseMoteurDroit = moteurDroit.getVitesse();
					System.out.println("up() : la vitesse du robot augmente");
				} else {
					System.out.println("up() : la vitesse est au maximum");
				}
				//Si l'état est déjà à backward alors on augmente les vitesses des moteurs
			} else if (etatVehicule == Etat.backward) {
				moteurGauche.getMotorLejos().startSynchronization();
				moteurGauche.setVitesse(vitesse);
				moteurDroit.setVitesse(vitesse);
				moteurGauche.getMotorLejos().endSynchronization();
				saveVitesseMoteurGauche = moteurGauche.getVitesse();
				saveVitesseMoteurDroit = moteurDroit.getVitesse();
				System.out.println("backward() : le moteur tourne en arriere");
				if (moteurGauche.getVitesse()+ vitesseAjout < vitesseMax && moteurDroit.getVitesse()+ vitesseAjout < vitesseMax) {
					moteurGauche.getMotorLejos().startSynchronization();
					moteurGauche.setVitesse(moteurGauche.getVitesse() + vitesseAjout);
					moteurDroit.setVitesse(moteurDroit.getVitesse() + vitesseAjout);
					moteurGauche.getMotorLejos().endSynchronization();
					vitesse += vitesseAjout;
					saveVitesseMoteurGauche = moteurGauche.getVitesse();
					saveVitesseMoteurDroit = moteurDroit.getVitesse();
					System.out.println("up() : la vitesse du robot augmente");
				} else {
					System.out.println("up() : la vitesse est au maximum");
				}
				// Modification de l'etat du vehicule en fonction de son état actuel.
			} else if (etatVehicule == Etat.forward) {
				moteurGauche.stopped();
				moteurDroit.stopped();
				etatVehicule = Etat.backward;
				moteurGauche.getMotorLejos().startSynchronization();
				moteurGauche.pull(vitesseRange);
				moteurDroit.pull(vitesseRange);
				moteurGauche.getMotorLejos().endSynchronization();
				vitesse = vitesseRange;
				saveVitesseMoteurGauche = moteurGauche.getVitesse();
				saveVitesseMoteurDroit = moteurDroit.getVitesse();
				//On vérifie que le moteur n'atteint pas la vitesse max
				if (moteurGauche.getVitesse()+ vitesseAjout < vitesseMax && moteurDroit.getVitesse()+ vitesseAjout < vitesseMax) {
					moteurGauche.getMotorLejos().startSynchronization();
					moteurGauche.setVitesse(moteurGauche.getVitesse() + vitesseAjout);
					moteurDroit.setVitesse(moteurDroit.getVitesse() + vitesseAjout);
					moteurGauche.getMotorLejos().endSynchronization();
					vitesse += vitesseAjout;
					saveVitesseMoteurGauche = moteurGauche.getVitesse();
					saveVitesseMoteurDroit = moteurDroit.getVitesse();
					System.out.println("up() : la vitesse du robot augmente");
				} else {
					System.out.println("up() : la vitesse est au maximum");
				}
			}else {
				System.out.println("backward() : impossible de faire tourner le moteur en arrière");
			}
		}else {
			moteurGauche.getMotorLejos().startSynchronization();
			moteurGauche.stopped();
			moteurDroit.stopped();
			moteurGauche.getMotorLejos().endSynchronization();
			saveVitesseMoteurGauche = moteurGauche.getVitesse();
			saveVitesseMoteurDroit = moteurDroit.getVitesse();
			vitesse = 0;
			etatVehicule = Etat.neutral;
			System.out.println("----------------");
			System.out.println("     Contact    ");
			System.out.println("----------------");
		}
			
	}
	
	/**
	 * Methode permettant au robot de tourner vers la gauche.
	 * @param vitesseAjout : correspond à la vitesse qu'on incrémente au moteur
	 * @param vitesseMax : correspond à la vitesse maximum que peut atteindre le robot en marche avant.
	 */
	public void left(int vitesseAjout,int vitesseMax) {
		//Si le moteur est en marche avant ou en marche arrière, alors on fait tourner le robot vers la gauche
		//On augmente plus la vitesse de rotation du moteur droit par rapport a celui du moteur gauche.
		//Ainsi, le robot va tourner vers la gauche
		if(!capteurPresence.obstacleDetect()) {
			etatVehicule = Etat.forward;
			moteurGauche.getMotorLejos().startSynchronization();
			moteurGauche.push(vitesseRange);
			moteurDroit.push(vitesseRange*4);
			moteurGauche.getMotorLejos().endSynchronization();
			vitesse = vitesseRange;
			saveVitesseMoteurGauche = moteurGauche.getVitesse();
			saveVitesseMoteurDroit = moteurDroit.getVitesse();
			System.out.println("forward() : le moteur tourne en avant");
			if (moteurGauche.getVitesse()+ vitesseAjout < vitesseMax && moteurDroit.getVitesse()+ vitesseAjout < vitesseMax) {
				moteurGauche.getMotorLejos().startSynchronization();
				moteurGauche.setVitesse(moteurGauche.getVitesse() + vitesseAjout);
				moteurDroit.setVitesse((moteurDroit.getVitesse() + vitesseAjout)*4);
				moteurGauche.getMotorLejos().endSynchronization();
				vitesse += vitesseAjout;
				saveVitesseMoteurGauche = moteurGauche.getVitesse();
				saveVitesseMoteurDroit = moteurDroit.getVitesse();
				System.out.println("up() : la vitesse du robot augmente");
			} else {
				System.out.println("up() : la vitesse est au maximum");
			}
				System.out.println("left() : modification trajectoire (gauche)");
		}else {
			moteurGauche.getMotorLejos().startSynchronization();
			moteurGauche.stopped();
			moteurDroit.stopped();
			moteurGauche.getMotorLejos().endSynchronization();
			saveVitesseMoteurGauche = moteurGauche.getVitesse();
			saveVitesseMoteurDroit = moteurDroit.getVitesse();
			vitesse = 0;
			etatVehicule = Etat.neutral;
			System.out.println("----------------");
			System.out.println("    Obstacle    ");
			System.out.println("----------------");
		}
		
		
	}
	
	/**
	 * Methode permettant au robot de tourner vers la droite.
	 * @param vitesseAjout : correspond à la vitesse qu'on incrémente au moteur
	 * @param vitesseMax : correspond à la vitesse maximum que peut atteindre le robot en marche avant.
	 */
	public void right(int vitesseAjout,int vitesseMax) {
		//Si le moteur est en marche avant ou en marche arrière, alors on fait tourner le robot vers la droite
		//On augmente plus la vitesse de rotation du moteur gauche par rapport a celui du moteur droit.
		//Ainsi, le robot va tourner vers la droite
		if(!capteurPresence.obstacleDetect()) {
			etatVehicule = Etat.forward;
			moteurGauche.getMotorLejos().startSynchronization();
			moteurGauche.push(vitesseRange*4);
			moteurDroit.push(vitesseRange);
			moteurGauche.getMotorLejos().endSynchronization();
			vitesse = vitesseRange;
			saveVitesseMoteurGauche = moteurGauche.getVitesse();
			saveVitesseMoteurDroit = moteurDroit.getVitesse();
			System.out.println("right() : modification trajectoire (droite)");
			if (moteurGauche.getVitesse()+ vitesseAjout < vitesseMax && moteurDroit.getVitesse()+ vitesseAjout < vitesseMax) {
				moteurGauche.getMotorLejos().startSynchronization();
				moteurGauche.setVitesse((moteurGauche.getVitesse() + vitesseAjout)*4);
				moteurDroit.setVitesse((moteurDroit.getVitesse() + vitesseAjout));
				moteurGauche.getMotorLejos().endSynchronization();
				vitesse += vitesseAjout;
				saveVitesseMoteurGauche = moteurGauche.getVitesse();
				saveVitesseMoteurDroit = moteurDroit.getVitesse();
				System.out.println("up() : la vitesse du robot augmente");
			} else {
				System.out.println("up() : la vitesse est au maximum");
			}
		}else {
			moteurGauche.getMotorLejos().startSynchronization();
			moteurGauche.stopped();
			moteurDroit.stopped();
			moteurGauche.getMotorLejos().endSynchronization();
			saveVitesseMoteurGauche = moteurGauche.getVitesse();
			saveVitesseMoteurDroit = moteurDroit.getVitesse();
			vitesse = 0;
			etatVehicule = Etat.neutral;
			System.out.println("----------------");
			System.out.println("    Obstacle    ");
			System.out.println("----------------");
		}	
	}
	
	/**
	 * Methode permettant de faire avance le robot en avant avec une légère trajectoire vers la gauche. (Diagonale gauche avant)
	 * @param vitesseAjout : correspond à la vitesse qu'on incrémente au moteur
	 * @param vitesseMax : correspond à la vitesse maximum que peut atteindre le robot en marche avant.
	 */
	public void leftForward(int vitesseAjout,int vitesseMax) {
		//On augmente plus la vitesse de rotation du moteur droit par rapport a celui du moteur gauche.
		//A la différence des méthode pour tourner, la différence d'accélaration des deux moteurs est moins grande.
		//Le véhicule a donc une trajectoire ressemblant à une diagonale.
		if(!capteurPresence.obstacleDetect()) {
			etatVehicule = Etat.forward;
			moteurGauche.getMotorLejos().startSynchronization();
			moteurGauche.push(vitesseRange*3);
			moteurDroit.push(vitesseRange*4);
			moteurGauche.getMotorLejos().endSynchronization();
			vitesse = vitesseRange;
			saveVitesseMoteurGauche = moteurGauche.getVitesse();
			saveVitesseMoteurDroit = moteurDroit.getVitesse();
			System.out.println("forward() : le moteur tourne en avant");
			if (moteurGauche.getVitesse()+ vitesseAjout < vitesseMax && moteurDroit.getVitesse()+ vitesseAjout < vitesseMax) {
				moteurGauche.getMotorLejos().startSynchronization();
				moteurGauche.setVitesse((moteurGauche.getVitesse() + vitesseAjout)*3);
				moteurDroit.setVitesse((moteurDroit.getVitesse() + vitesseAjout)*4);
				moteurGauche.getMotorLejos().endSynchronization();
				vitesse += vitesseAjout;
				saveVitesseMoteurGauche = moteurGauche.getVitesse();
				saveVitesseMoteurDroit = moteurDroit.getVitesse();
				System.out.println("up() : la vitesse du robot augmente");
			} else {
				System.out.println("up() : la vitesse est au maximum");
			}
				System.out.println("left() : modification trajectoire (gauche)");
		}else {
			moteurGauche.getMotorLejos().startSynchronization();
			moteurGauche.stopped();
			moteurDroit.stopped();
			moteurGauche.getMotorLejos().endSynchronization();
			saveVitesseMoteurGauche = moteurGauche.getVitesse();
			saveVitesseMoteurDroit = moteurDroit.getVitesse();
			vitesse = 0;
			etatVehicule = Etat.neutral;
			System.out.println("----------------");
			System.out.println("    Obstacle    ");
			System.out.println("----------------");
		}
	}
	
	/**
	 * Methode permettant de faire avance le robot en avant avec une légère trajectoire vers la droite. (Diagonale droite avant)
	 * @param vitesseAjout : correspond à la vitesse qu'on incrémente au moteur
	 * @param vitesseMax : correspond à la vitesse maximum que peut atteindre le robot en marche avant.
	 */
	public void rightForward(int vitesseAjout,int vitesseMax) {
		//On augmente plus la vitesse de rotation du moteur gauche par rapport a celui du moteur droit.
		//A la différence des méthode pour tourner, la différence d'accélaration des deux moteurs est moins grande.
		//Le véhicule a donc une trajectoire ressemblant à une diagonale.
		if(!capteurPresence.obstacleDetect()) {
			etatVehicule = Etat.forward;
			moteurGauche.getMotorLejos().startSynchronization();
			moteurGauche.push(vitesseRange*4);
			moteurDroit.push(vitesseRange*3);
			moteurGauche.getMotorLejos().endSynchronization();
			vitesse = vitesseRange;
			saveVitesseMoteurGauche = moteurGauche.getVitesse();
			saveVitesseMoteurDroit = moteurDroit.getVitesse();
			System.out.println("forward() : le moteur tourne en avant");
			if (moteurGauche.getVitesse()+ vitesseAjout < vitesseMax && moteurDroit.getVitesse()+ vitesseAjout < vitesseMax) {
				moteurGauche.getMotorLejos().startSynchronization();
				moteurGauche.setVitesse((moteurGauche.getVitesse() + vitesseAjout)*4);
				moteurDroit.setVitesse((moteurDroit.getVitesse() + vitesseAjout)*3);
				moteurGauche.getMotorLejos().endSynchronization();
				vitesse += vitesseAjout;
				saveVitesseMoteurGauche = moteurGauche.getVitesse();
				saveVitesseMoteurDroit = moteurDroit.getVitesse();
				System.out.println("up() : la vitesse du robot augmente");
			} else {
				System.out.println("up() : la vitesse est au maximum");
			}
				System.out.println("left() : modification trajectoire (gauche)");
		}else {
			moteurGauche.getMotorLejos().startSynchronization();
			moteurGauche.stopped();
			moteurDroit.stopped();
			moteurGauche.getMotorLejos().endSynchronization();
			saveVitesseMoteurGauche = moteurGauche.getVitesse();
			saveVitesseMoteurDroit = moteurDroit.getVitesse();
			vitesse = 0;
			etatVehicule = Etat.neutral;
			System.out.println("----------------");
			System.out.println("    Obstacle    ");
			System.out.println("----------------");
		}
	}

	/**
	 * Methode permettant de faire reculer le robot en arrire avec une légère trajectoire vers la gauche. (Diagonale gauche arrière)
	 * @param vitesseAjout : correspond à la vitesse qu'on incrémente au moteur
	 * @param vitesseMax : correspond à la vitesse maximum que peut atteindre le robot en marche avant.
	 */
	public void leftBackward(int vitesseAjout,int vitesseMax) {
		//On augmente plus la vitesse de rotation du moteur droit par rapport a celui du moteur gauche.
		//A la différence des méthode pour tourner, la différence d'accélaration des deux moteurs est moins grande.
		//Le véhicule a donc une trajectoire ressemblant à une diagonale.
		if(!capteurContacte.contactObstacle()) {
			if (etatVehicule == Etat.neutral) {
				etatVehicule = Etat.backward;
				moteurGauche.getMotorLejos().startSynchronization();
				moteurGauche.pull(vitesseRange*2);
				moteurDroit.pull(vitesseRange*4);
				moteurGauche.getMotorLejos().endSynchronization();
				vitesse = vitesseRange;
				saveVitesseMoteurGauche = moteurGauche.getVitesse();
				saveVitesseMoteurDroit = moteurDroit.getVitesse();
				System.out.println("backward() : le moteur tourne en arriere");
				if (moteurGauche.getVitesse()+ vitesseAjout < vitesseMax && moteurDroit.getVitesse()+ vitesseAjout < vitesseMax) {
					moteurGauche.getMotorLejos().startSynchronization();
					moteurGauche.setVitesse((moteurGauche.getVitesse() + vitesseAjout)*2);
					moteurDroit.setVitesse((moteurDroit.getVitesse() + vitesseAjout)*4);
					moteurGauche.getMotorLejos().endSynchronization();
					vitesse += vitesseAjout;
					saveVitesseMoteurGauche = moteurGauche.getVitesse();
					saveVitesseMoteurDroit = moteurDroit.getVitesse();
					System.out.println("up() : la vitesse du robot augmente");
				} else {
					System.out.println("up() : la vitesse est au maximum");
				}
				//Si l'état est déjà à backward alors on augmente les vitesses des moteurs
			} else if (etatVehicule == Etat.backward) {
				moteurGauche.getMotorLejos().startSynchronization();
				moteurGauche.setVitesse(vitesse*2);
				moteurDroit.setVitesse(vitesse*4);
				moteurGauche.getMotorLejos().endSynchronization();
				saveVitesseMoteurGauche = moteurGauche.getVitesse();
				saveVitesseMoteurDroit = moteurDroit.getVitesse();
				System.out.println("backward() : le moteur tourne en arriere");
				if (moteurGauche.getVitesse()+ vitesseAjout < vitesseMax && moteurDroit.getVitesse()+ vitesseAjout < vitesseMax) {
					moteurGauche.getMotorLejos().startSynchronization();
					moteurGauche.setVitesse((moteurGauche.getVitesse() + vitesseAjout)*2);
					moteurDroit.setVitesse((moteurDroit.getVitesse() + vitesseAjout)*4);
					moteurGauche.getMotorLejos().endSynchronization();
					vitesse += vitesseAjout;
					saveVitesseMoteurGauche = moteurGauche.getVitesse();
					saveVitesseMoteurDroit = moteurDroit.getVitesse();
					System.out.println("up() : la vitesse du robot augmente");
				} else {
					System.out.println("up() : la vitesse est au maximum");
				}
			} else if (etatVehicule == Etat.forward) {
				moteurGauche.stopped();
				moteurDroit.stopped();
				etatVehicule = Etat.backward;
				moteurGauche.getMotorLejos().startSynchronization();
				moteurGauche.pull(vitesseRange*2);
				moteurDroit.pull(vitesseRange*4);
				moteurGauche.getMotorLejos().endSynchronization();
				vitesse = vitesseRange;
				saveVitesseMoteurGauche = moteurGauche.getVitesse();
				saveVitesseMoteurDroit = moteurDroit.getVitesse();
				if (moteurGauche.getVitesse()+ vitesseAjout < vitesseMax && moteurDroit.getVitesse()+ vitesseAjout < vitesseMax) {
					moteurGauche.getMotorLejos().startSynchronization();
					moteurGauche.setVitesse((moteurGauche.getVitesse() + vitesseAjout)*2);
					moteurDroit.setVitesse((moteurDroit.getVitesse() + vitesseAjout)*4);
					moteurGauche.getMotorLejos().endSynchronization();
					vitesse += vitesseAjout;
					saveVitesseMoteurGauche = moteurGauche.getVitesse();
					saveVitesseMoteurDroit = moteurDroit.getVitesse();
					System.out.println("up() : la vitesse du robot augmente");
				} else {
					System.out.println("up() : la vitesse est au maximum");
				}
			}else {
				System.out.println("backward() : impossible de faire tourner le moteur en arrière");
			}
		}else {
			moteurGauche.getMotorLejos().startSynchronization();
			moteurGauche.stopped();
			moteurDroit.stopped();
			moteurGauche.getMotorLejos().endSynchronization();
			saveVitesseMoteurGauche = moteurGauche.getVitesse();
			saveVitesseMoteurDroit = moteurDroit.getVitesse();
			vitesse = 0;
			etatVehicule = Etat.neutral;
			System.out.println("----------------");
			System.out.println("     Contact    ");
			System.out.println("----------------");
		}
	}

	/**
	 * Methode permettant de faire reculer le robot en arrire avec une légère trajectoire vers la droite. (Diagonale droite arrière)
	 * @param vitesseAjout : correspond à la vitesse qu'on incrémente au moteur
	 * @param vitesseMax : correspond à la vitesse maximum que peut atteindre le robot en marche avant.
	 */
	public void rightBackward(int vitesseAjout,int vitesseMax) {
		//On augmente plus la vitesse de rotation du moteur gauche par rapport a celui du moteur droit.
		//A la différence des méthode pour tourner, la différence d'accélaration des deux moteurs est moins grande.
		//Le véhicule a donc une trajectoire ressemblant à une diagonale.
		if(!capteurContacte.contactObstacle()) {
			if (etatVehicule == Etat.neutral) {
				etatVehicule = Etat.backward;
				moteurGauche.getMotorLejos().startSynchronization();
				moteurGauche.pull(vitesseRange*4);
				moteurDroit.pull(vitesseRange*2);
				moteurGauche.getMotorLejos().endSynchronization();
				vitesse = vitesseRange;
				saveVitesseMoteurGauche = moteurGauche.getVitesse();
				saveVitesseMoteurDroit = moteurDroit.getVitesse();
				System.out.println("backward() : le moteur tourne en arriere");
				if (moteurGauche.getVitesse()+ vitesseAjout < vitesseMax && moteurDroit.getVitesse()+ vitesseAjout < vitesseMax) {
					moteurGauche.getMotorLejos().startSynchronization();
					moteurGauche.setVitesse((moteurGauche.getVitesse() + vitesseAjout)*4);
					moteurDroit.setVitesse((moteurDroit.getVitesse() + vitesseAjout)*2);
					moteurGauche.getMotorLejos().endSynchronization();
					vitesse += vitesseAjout;
					saveVitesseMoteurGauche = moteurGauche.getVitesse();
					saveVitesseMoteurDroit = moteurDroit.getVitesse();
					System.out.println("up() : la vitesse du robot augmente");
				} else {
					System.out.println("up() : la vitesse est au maximum");
				}
				//Si l'état est déjà à backward alors on augmente les vitesses des moteurs
			} else if (etatVehicule == Etat.backward) {
				moteurGauche.getMotorLejos().startSynchronization();
				moteurGauche.setVitesse(vitesse*4);
				moteurDroit.setVitesse(vitesse*2);
				moteurGauche.getMotorLejos().endSynchronization();
				saveVitesseMoteurGauche = moteurGauche.getVitesse();
				saveVitesseMoteurDroit = moteurDroit.getVitesse();
				System.out.println("backward() : le moteur tourne en arriere");
				if (moteurGauche.getVitesse()+ vitesseAjout < vitesseMax && moteurDroit.getVitesse()+ vitesseAjout < vitesseMax) {
					moteurGauche.getMotorLejos().startSynchronization();
					moteurGauche.setVitesse((moteurGauche.getVitesse() + vitesseAjout)*4);
					moteurDroit.setVitesse((moteurDroit.getVitesse() + vitesseAjout)*2);
					moteurGauche.getMotorLejos().endSynchronization();
					vitesse += vitesseAjout;
					saveVitesseMoteurGauche = moteurGauche.getVitesse();
					saveVitesseMoteurDroit = moteurDroit.getVitesse();
					System.out.println("up() : la vitesse du robot augmente");
				} else {
					System.out.println("up() : la vitesse est au maximum");
				}
			} else if (etatVehicule == Etat.forward) {
				moteurGauche.stopped();
				moteurDroit.stopped();
				etatVehicule = Etat.backward;
				moteurGauche.getMotorLejos().startSynchronization();
				moteurGauche.pull(vitesseRange*4);
				moteurDroit.pull(vitesseRange*2);
				moteurGauche.getMotorLejos().endSynchronization();
				vitesse = vitesseRange;
				saveVitesseMoteurGauche = moteurGauche.getVitesse();
				saveVitesseMoteurDroit = moteurDroit.getVitesse();
				if (moteurGauche.getVitesse()+ vitesseAjout < vitesseMax && moteurDroit.getVitesse()+ vitesseAjout < vitesseMax) {
					moteurGauche.getMotorLejos().startSynchronization();
					moteurGauche.setVitesse((moteurGauche.getVitesse() + vitesseAjout)*4);
					moteurDroit.setVitesse((moteurDroit.getVitesse() + vitesseAjout)*2);
					moteurGauche.getMotorLejos().endSynchronization();
					vitesse += vitesseAjout;
					saveVitesseMoteurGauche = moteurGauche.getVitesse();
					saveVitesseMoteurDroit = moteurDroit.getVitesse();
					System.out.println("up() : la vitesse du robot augmente");
				} else {
					System.out.println("up() : la vitesse est au maximum");
				}
			}else {
				System.out.println("backward() : impossible de faire tourner le moteur en arrière");
			}
		}else {
			moteurGauche.getMotorLejos().startSynchronization();
			moteurGauche.stopped();
			moteurDroit.stopped();
			moteurGauche.getMotorLejos().endSynchronization();
			saveVitesseMoteurGauche = moteurGauche.getVitesse();
			saveVitesseMoteurDroit = moteurDroit.getVitesse();
			vitesse = 0;
			etatVehicule = Etat.neutral;
			System.out.println("----------------");
			System.out.println("     Contact    ");
			System.out.println("----------------");
		}
	}


	/**
	 * Simulation d'une urgence sur le robot
	 */
	public void urgency() {
		//Passage des moteurs à l'arrêt et l'état du robot à urgency
		//On conserve la vitesse actuelle des moteurs et l'état du moteur
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

	/**
	 * Simulation d'une panne sur le robot
	 */
	public void breakdown() {
		//Passage des moteurs à l'arrêt et l'état du robot à panne
		//On conserve la vitesse actuelle des moteurs et l'état du moteur
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
	
	/**
	 * Restauration des anciennes valeurs de la vitesse et de l'état du robot
	 * Méthode qui est appelé après un état de panne ou urgency
	 */
	public void restore() {
		moteurGauche.getMotorLejos().startSynchronization();
		moteurGauche.setVitesse(saveVitesseMoteurGauche);
		moteurDroit.setVitesse(saveVitesseMoteurDroit);
		moteurGauche.getMotorLejos().endSynchronization();
		etatVehicule = saveEtatVehicule;
		System.out.println("restore() : le véhicule restaure son état");
	}

	/**
	 * Retourne l'etat du véhicule
	 * @return Etat
	 */
	public Etat getEtatVehicule() {
		return etatVehicule;
	}

	/**
	 * Modifie l'etat du véhicule
	 * @param etatVehicule
	 */
	public void setEtatVehicule(Etat etatVehicule) {
		this.etatVehicule = etatVehicule;
	}

	/**
	 * Retourne la vitesse
	 * @return entier
	 */
	public int getVitesseRange() {
		return vitesseRange;
	}

	/**
	 * Modifie la vitesse
	 * @param vitesseRange
	 */
	public void setVitesseRange(int vitesseRange) {
		this.vitesseRange = vitesseRange;
	}

	/**
	 * Retourne le moteur gauche
	 * @return Motor
	 */
	public Motor getMoteurGauche() {
		return moteurGauche;
	}

	/**
	 * Modifie la valeur du moteur gauche.
	 * @param moteurGauche
	 */
	public void setMoteurGauche(Motor moteurGauche) {
		this.moteurGauche = moteurGauche;
	}

	/**
	 * Retourne le moteur droit
	 * @return Motor
	 */
	public Motor getMoteurDroit() {
		return moteurDroit;
	}

	/**
	 * Modifie la valeur du moteur droit.
	 * @param moteurDroit
	 */
	public void setMoteurDroit(Motor moteurDroit) {
		this.moteurDroit = moteurDroit;
	}
}
