package metier;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;


import ressources.Etat;

/**
* Classe DispacherTest.
* Cette classe d�finit l'ensemble des tests unitaires du syst�me.
* @version 1.0
*/
public class DispatcherTest {

	private VehiculeControler vehicule;
	private CapteurObstacle capteurPresence;
	private CapteurContacte capteurContact;
	private int vitesseA = 10, vitesseM = 300;

	/**
	 * methode ex�cut�e avant chaque test unitaire
	 */
	@Before
	public void init() {
		vehicule = new VehiculeControler();
	}
	
	/**
	 * test le d�marrage du robot, on verifie que le robot passe bien dans l'�tat neutral
	 */
	@Test
	public void testStart() {
		
		assertEquals(Etat.off, vehicule.getEtatVehicule());

		vehicule.start();
		assertEquals(Etat.neutral, vehicule.getEtatVehicule());
	}

	/**
	 * test le passage du robot � l'etat stop
	 */
	@Test
	public void testStop() {
		
		assertEquals(Etat.off, vehicule.getEtatVehicule());

		vehicule.start();
		vehicule.stop();
		assertEquals(Etat.off, vehicule.getEtatVehicule());

	}

	/**
	 * Methode qui Permet de tester qu�on puisse faire passer le robot 
	 * respectivement de l��tat Forward � l��tat Neutral, cela represente le fait que le v�hicule s'arrete
	 */
	@Test
	public void testStopForward() {

		vehicule.start();
		vehicule.forward(10, vitesseM);
		vehicule.stop();
		assertEquals(Etat.off, vehicule.getEtatVehicule());
	}
	
	/**
	 * Methode qui Permet de tester qu�on puisse faire passer le robot 
	 * respectivement de l��tat backward � l��tat Neutral, cela represente le fait que le v�hicule s'arrete
	 */
	@Test
	public void testStopBackward() {

		vehicule.start();
		vehicule.backward(10, vitesseM);
		vehicule.stop();
		assertEquals(Etat.off, vehicule.getEtatVehicule());
	}

	/**
	 * Permet tester le faire qu�on puisse faire avancer le robot en l'absence d'obstacle
	 */
	@Test
	public void testForward() {
		
		assertEquals(Etat.off, vehicule.getEtatVehicule());
		
		
		vehicule.forward(vitesseA, vitesseM);
		assertEquals(Etat.off, vehicule.getEtatVehicule());

		
		vehicule.start();
		vehicule.forward(vitesseA, vitesseM);
		assertEquals(Etat.forward, vehicule.getEtatVehicule());
		assertEquals(25 + vitesseA, vehicule.getMoteurGauche().getVitesse());
		assertEquals(25 + vitesseA, vehicule.getMoteurDroit().getVitesse());

	}

	/**
	 * Permet tester le faire qu�on puisse faire reculer le robot en l'absence d'obstacle
	 */
	@Test
	public void testBackward() {

		assertEquals(Etat.off, vehicule.getEtatVehicule());
		vehicule.start();
		vehicule.backward(vitesseA, vitesseM);
		assertEquals(Etat.backward, vehicule.getEtatVehicule());
		assertEquals(vitesseA + 25, vehicule.getMoteurGauche().getVitesse());
		assertEquals(vitesseA + 25, vehicule.getMoteurDroit().getVitesse());
	}
	
	/**
	 * Permet de tester le fait que l�on puisse tourner � gauche en l�absence d�obstacle.
	 */
	@Test
	public void testLeft() {

		int vitesseG,vitesseD;
		
		vehicule.start();
		
		vehicule.forward(vitesseA, vitesseM);
		
		vehicule.left(vitesseA, vitesseM);

		vitesseG = (int) ((int) vehicule.saveVitesseMoteurGauche);
		vitesseD = (int) ((int) vehicule.saveVitesseMoteurDroit);
		assertEquals(vitesseG, vehicule.getMoteurGauche().getVitesse());

		assertEquals(vitesseD, vehicule.getMoteurDroit().getVitesse());
		assertTrue(vitesseD > vitesseG);
	}

	/**
	 * Permet de tester le fait que l'on puisse tourner � droite en l�absence d�obstacle.
	 */
	@Test
	public void testRight() {
		
		
		
		int vitesseG,vitesseD;
		
		vehicule.start();
		
		vehicule.forward(vitesseA,vitesseM);
		
		vehicule.right(vitesseA,vitesseM);

		vitesseG = (int) ((int) vehicule.saveVitesseMoteurGauche);
		vitesseD = (int) ((int) vehicule.saveVitesseMoteurDroit);
		assertEquals(vitesseG, vehicule.getMoteurGauche().getVitesse());
		assertEquals(vitesseD, vehicule.getMoteurDroit().getVitesse());
		assertTrue(vitesseG > vitesseD);
	}

	/**
	 * Permet de tester le fait que le v�hicule puisse tourner l�g�rement � droite pendant 
	 * qu�il avance en l'absence d'obstacle
	 */
	@Test
	public void testRightForward() {
		
		
		
		int vitesseG,vitesseD;
		

		vehicule.start();
		vehicule.rightForward(vitesseA, vitesseM);
		

		vitesseG = (int) ((int) vehicule.saveVitesseMoteurGauche);
		assertEquals(vitesseG, vehicule.getMoteurGauche().getVitesse());

		vitesseD = (int) ((int) vehicule.saveVitesseMoteurDroit);
		assertEquals(vitesseD, vehicule.getMoteurDroit().getVitesse());
		assertTrue(vitesseG > vitesseD);
	}
	
	/**
	 * Permet de tester le fait que le v�hicule puisse tourner l�g�rement � gauche pendant 
	 * qu�il avance en l'absence d'obstacle
	 */
	@Test
	public void testLeftForward() {
		
		
		
		int vitesseG, vitesseD;
		

		vehicule.start();
		vehicule.leftForward(vitesseA, vitesseM);
		

		vitesseG = (int) ((int) vehicule.saveVitesseMoteurGauche);
		assertEquals(vitesseG, vehicule.getMoteurGauche().getVitesse());

		vitesseD = (int) ((int) vehicule.saveVitesseMoteurDroit);
		assertEquals(vitesseD, vehicule.getMoteurDroit().getVitesse());
		assertTrue(vitesseD > vitesseG);
	}
	
	/**
	 * Permet de tester le fait que le v�hicule puisse tourner l�g�rement � droite 
	 * pendant qu�il recule en l'absence d'obstacle
	 */
	@Test
	public void testRightBackward() {
		
		
		
		int vitesseG,vitesseD;
		

		vehicule.start();
		vehicule.rightBackward(vitesseA, vitesseM);
		

		vitesseG = (int) ((int) vehicule.saveVitesseMoteurGauche);
		assertEquals(vitesseG, vehicule.getMoteurGauche().getVitesse());

		vitesseD = (int) ((int) vehicule.saveVitesseMoteurDroit);
		assertEquals(vitesseD, vehicule.getMoteurDroit().getVitesse());
		assertTrue(vitesseG > vitesseD);
	}
	
	/**
	 * Permet de tester le fait que le v�hicule puisse tourner l�g�rement � gauche
	 *  pendant qu�il recule en l'absence d'obstacle
	 */
	@Test
	public void testLeftBackward() {
		
		
		
		int vitesseG, vitesseD;
		

		vehicule.start();
		vehicule.leftBackward(vitesseA, vitesseM);
		

		vitesseG = (int) ((int) vehicule.saveVitesseMoteurGauche);
		assertEquals(vitesseG, vehicule.getMoteurGauche().getVitesse());

		vitesseD = (int) ((int) vehicule.saveVitesseMoteurDroit);
		assertEquals(vitesseD, vehicule.getMoteurDroit().getVitesse());
		assertTrue(vitesseD > vitesseG);
	}
	
	/**
	 * Permet de tester la fonctionnalit� mettant le robot en �tat d�urgence.
	 */
	@Test
	public void testUrgency() {
		
		vehicule.start();
		vehicule.forward(vitesseA, vitesseM);
		vehicule.urgency();

		assertEquals(0, vehicule.getMoteurDroit().getVitesse());
		assertEquals(0, vehicule.getMoteurGauche().getVitesse());
		assertEquals(Etat.urgency, vehicule.getEtatVehicule());
	}

	/**
	 * Sert � tester la fonctionnalit� permettant de simuler une panne sur le robot.
	 */
	@Test
	public void testBreakdown() {
		
		vehicule.start();
		vehicule.forward(vitesseA, vitesseM);
		vehicule.breakdown();

		assertEquals(0, vehicule.getMoteurDroit().getVitesse());
		assertEquals(0, vehicule.getMoteurGauche().getVitesse());
		assertEquals(Etat.panne, vehicule.getEtatVehicule());
	}
	
	/**
	 * Permet de tester le fait que le v�hicule se comporte correctement quand il d�tecte un obstacle.
	 */
	@Test
	public void testcapteurPresence() {
		capteurPresence = Mockito.mock(CapteurObstacle.class);
		when(capteurPresence.obstacleDetect()).thenReturn(true);
		
		vehicule.start();
		vehicule.forward(vitesseA, vitesseM);

		assertEquals(Etat.off, vehicule.getEtatVehicule());	
		
	}
	
	/**
	 * Permet de tester le comportement du v�hicule quand il est en contact avec un obstacle
	 */
	@Test
	public void testcapteurContact() {
		capteurContact = Mockito.mock(CapteurContacte.class);
		when(capteurContact.contactObstacle()).thenReturn(true);
		
		assertEquals(Etat.off, vehicule.getEtatVehicule());

		vehicule.start();
		vehicule.backward(vitesseA, vitesseM);
		
		assertEquals(Etat.off, vehicule.getEtatVehicule());
		
	}
	

}
