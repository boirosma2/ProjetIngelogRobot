package metier;

import lejos.hardware.*;
import lejos.hardware.motor.BaseRegulatedMotor;
import lejos.hardware.port.MotorPort;
import ressources.Etat;


import java.util.logging.Logger;



public class VehiculeControler {		
	
	Motor moteur;
	
	MotorPort.D;
	
	BaseRegulatedMotor regulator;
	
	public VehiculeControler vehiculeControler ;
    
	public VehiculeControler() {
		regulator = new BaseRegulatedMotor();		
		moteur = new Motor(null);
	}
	
	/*UltrasonicSensor capteurUltrason;
	GyroSensor capteurGyro;
	ContactSensor capteurContact;
	ColorSensor capteurCouleur;*/
	Logger logger;
	
	//constant
	
	int vitesseRange = 10;
	int rotationRange = 10;	
	
	
	public VehiculeControler getInstance(){
		if(vehiculeControler == null)
		{
			vehiculeControler = new VehiculeControler();
		}
		return vehiculeControler;
	}
	
	public void start(){	
	// certaines conditions sont int�gr�es dans des public voids
	// interm�diaires afin de ne pas polluer le code.
		if (!(moteur.getEtatMoteur() == Etat.contact))
		{
			moteur.setEtatMoteur(Etat.neutral);	
			logger.info("start() : Passage de l��tat moteur � neutral");
		}
		else
		{
			//logger.error("start() : le v�hicule est d�j� d�marr�");
		}
	}
	
	public void stop(){
		if(moteur.getEtatMoteur() != Etat.neutral)
		{
			moteur.stopped();
			logger.info("stop() :arr�t du v�hicule");
		}
		else
		{
			//logger.error("stop() : le v�hicule est d�j� stopp�");
		}
	}
	
	public void forward(){
	// if { le moteur est en marche arri�re il doit d�abord passer
	// par l��tat neutral
	// (point mort) pour pouvoir �tre en marche avant (forward)
		if(moteur.getEtatMoteur() == Etat.neutral || moteur.getEtatMoteur() == Etat.forward)
		{
			moteur.setEtatMoteur(Etat.forward);
			moteur.push();
			moteur.speedUp(vitesseRange);
			logger.info("forward() : le moteur tourne en avant");
		}
		else
		{
			//logger.error("forward() : impossible de faire tourner le moteur en avant");
		}
	}
	
	public void backward()
	{
		if (moteur.getEtatMoteur() == Etat.neutral || moteur.getEtatMoteur() == Etat.backward)
		{
			moteur.pull();		
			moteur.speedUp(vitesseRange);
			logger.info("backward() : le moteur tourne en arri�re");
	
		}
		else
		{	
			//logger.error("backward() : impossible de faire tourner le moteur en arriere");
		}
	}
	
	public void left(){
		if (moteur.getEtatMoteur() == Etat.contact)
		{
			moteur.rotation(- rotationRange);
			logger.info("left() : modification trajectoire (gauche)");
		}
		else
		{
			//logger.error("left() : impossible de modifier la trajectoire");
		}
	}
	
	public void right()
	{
		if(moteur.getEtatMoteur() == Etat.contact)
		{
			moteur.rotation(rotationRange);
			logger.info("right() : modification trajectoire (droite)");
		}
		else
		{	
			//logger.error("right() : impossible de modifier la	trajectoire");
		}
	}
	
	public void ring()
	{
		/*buzz();*/
		logger.info("left() : klaxon");
	}
	
	public void up()
	{
		moteur.speedUp(vitesseRange);
		logger.info("up() : la vitesse du robot augmente");
	}
	
	public void down()
	{
		moteur.speedDown(vitesseRange);
		logger.info("down() : la vitesse du robot diminue");
	}
	
	public void urgency()
	{
		moteur.urgency();
		logger.info("urgency() : le v�hicule se bloque");
	}
	
	public void breakdown()
	{
		moteur.breakdown();
		logger.info("breakdown() : simulation de panne");
	}
	
	public void restore()
	{
		moteur.restoreMotor();	
		logger.info("restore() : le v�hicule restaure son �tat");
	}
	/*public void contact()
	{
		logger.info("contact() : le v�hicule a eu un contact");
		return capteurContact.contact();
	}
	
	public void obstacleDetection()
	{
		logger.info("obstacleDetection() : le v�hicule a observ� un obstacle");
		return capteurUltrason.detect();
	}
	
	public void brightness()
	{
		logger.info("brightness() : le v�hicule a rep�r� une couleur");
		return capteurCouleur.color();
	}*/
	
	/*public void rotationAngle()
	{
		logger.info("rotationAngle() : r�cup�ration de l�angle du capteur gyroscopique");
		return capteurGyro.directionAngle();
	}*/
	
	/*public void EtatRobot()
	{
		int vitesse;
		int angle;
		Etat etatMoteur;
		vitesse = moteur.getVitesse();
		angle = moteur.getAngle();
		etatMoteur = moteur.getEtatMoteur();
		logger.info("getEtatRobot() : r�cup�ration de l��tat du robot");
		//retourner l��tat du robot au format json
	}*/
	
	/*public void request(Chaine de caract�re message)
	{
		if(message = �getEtatRobot�)
		{
			envoyer une requ�te avec l��tat du robot getEtatRobot()
		}
		else if 
		{
		...
		}
		logger.info("request() : envoi d�une requ�te " + message +");
	}
	
	// public void utile � cette classe
	public void motorIsStart()
	{
		return moteur.getEtatMoteur();
	}
	*/
}
