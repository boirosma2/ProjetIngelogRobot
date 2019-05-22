package metier;

import lejos.hardware.port.Port;
import lejos.robotics.RegulatedMotor;
import lejos.hardware.motor.BaseRegulatedMotor;
import ressources.Etat;

public class Motor {
		
	
	//Variables d’instances
	Etat etatMoteur;	
	Etat etatMoteurHistorique;
	int angle;	
	int vitesse;
	BaseRegulatedMotor motorLejos;
	
	//Constantes
	int angleGaucheMax = -90;
	int angleDroitMax = 90;
	int vitesseMax = 50; // simulation de 5 vitesses
	
	public Motor (BaseRegulatedMotor regulator){
	etatMoteur = Etat.neutral;
	etatMoteurHistorique = Etat.neutral;
	angle = 0;
	vitesse = 0;	
	motorLejos = regulator;			
	}
	
	public void push(){		
	motorLejos.forward();
	etatMoteurHistorique = etatMoteur;
	etatMoteur = Etat.forward;	
	}
	
	public void pull(){		
	motorLejos.backward();
	etatMoteurHistorique = etatMoteur;
	etatMoteur = Etat.backward;	
	}
	
	public void stopped(){		
	motorLejos.stop();
	vitesse = 0	;
	angle = 0;
	etatMoteurHistorique = etatMoteur;
	etatMoteur = Etat.neutral;
	}
	
	public void  urgency(){
	motorLejos.stop();
	etatMoteurHistorique = etatMoteur;
	etatMoteur = Etat.urgency;
	}
	
	public void  restoreMotor(){
		
	if( etatMoteurHistorique == Etat.forward)
	{	
		motorLejos.forward();
	}
	else if( etatMoteurHistorique == Etat.backward)
	{
		motorLejos.backward();
	}
		motorLejos.rotate(angle);
		motorLejos.setSpeed(vitesse);
	}
	
	public void  rotation(int increment){
		if( ((angle + increment) >= angleGaucheMax) && 	((angle + increment) <= angleDroitMax))
		{
			motorLejos.rotate(angle + increment);
		}
		else
		{
			motorLejos.rotate(angle);
		}
	}
	
	public void  speedUp(int increment){
		if( (vitesse + increment) <= vitesseMax)
		{
			vitesse = vitesse + increment;
		}
		else
		{
			vitesse = vitesseMax;
		}
		motorLejos.setSpeed(vitesse);
	}
	
	public void  speedDown(int increment){
		if( (vitesse - increment) >= 0)
		{
			vitesse = vitesse - increment;
		}
		else
		{
			vitesse = 0;
		}
		if( vitesse == 0)  // passage au point mort
		{
			etatMoteur = Etat.neutral;
		}		
		motorLejos.setSpeed(vitesse);
	}
	
	public void  breakdown(){
		motorLejos.stop();
		etatMoteurHistorique = etatMoteur;
		etatMoteur = Etat.panne;
	}
	
	//Getters et Setter
	
	public int getVitesse() {
		return vitesse;
	}

	public void setVitesse(int vitesse) {
		this.vitesse = vitesse;
	}
	
	public int getAngle() {
		return angle;
	}

	public void setAngle(int angle) {
		this.angle = angle;
	}
	
	public Etat getEtatMoteur() {
		return etatMoteur;
	}

	public void setEtatMoteur(Etat etatMoteur) {
		this.etatMoteur = etatMoteur;
	}

	public Etat getEtatMoteurHistorique() {
		return etatMoteurHistorique;
	}

	public void setEtatMoteurHistorique(Etat etatMoteurHistorique) {
		this.etatMoteurHistorique = etatMoteurHistorique;
	}


	

}
