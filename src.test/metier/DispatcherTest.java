package metier;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import ressources.Etat;

public class DispatcherTest {

	private VehiculeControler vehicule;

	@Before
	public void init() {
		vehicule = new VehiculeControler();
	}

	@Test
	public void testOn() {

		VehiculeControler vehicule = new VehiculeControler();

		assertEquals(Etat.contact, vehicule.getEtatVehicule());

		vehicule.on();
		assertEquals(Etat.neutral, vehicule.getEtatVehicule());
	}

	@Test
	public void testStop() {
		assertEquals(Etat.neutral, vehicule.getEtatVehicule());

		vehicule.stop();
		assertEquals(Etat.contact, vehicule.getEtatVehicule());

	}

	@Test
	public void testStopForward() {

		vehicule.on();
		vehicule.forward();
		vehicule.stop();
		assertEquals(Etat.neutral, vehicule.getEtatVehicule());
	}

	@Test
	public void testStopBackward() {

		vehicule.on();
		vehicule.backward();
		vehicule.stop();
		assertEquals(Etat.neutral, vehicule.getEtatVehicule());
	}

	@Test
	public void testForward() {
		assertEquals(Etat.contact, vehicule.getEtatVehicule());

		vehicule.forward();
		assertEquals(Etat.contact, vehicule.getEtatVehicule());

		vehicule.on();
		vehicule.forward();
		assertEquals(Etat.forward, vehicule.getEtatVehicule());
		assertEquals(50, vehicule.getMoteurGauche().getPower());
		assertEquals(50, vehicule.getMoteurDroit().getPower());

	}

	@Test
	public void testBackward() {

		assertEquals(Etat.contact, vehicule.getEtatVehicule());

		vehicule.backward();
		assertEquals(Etat.contact, vehicule.getEtatVehicule());

		vehicule.on();
		vehicule.backward();
		assertEquals(Etat.backward, vehicule.getEtatVehicule());
		assertEquals(50, vehicule.getMoteurGauche().getPower());
		assertEquals(50, vehicule.getMoteurDroit().getPower());
	}

	@Test
	public void testLeft() {

		int vitesse;

		vehicule.on();
		vehicule.forward();
		vehicule.left();

		vitesse = (int) ((int) vehicule.getVitesseBase() * 0.66);
		assertEquals(vitesse, vehicule.getMoteurGauche().getPower());

		vitesse = (int) ((int) vehicule.getVitesseBase() * 1.33);
		assertEquals(50, vehicule.getMoteurDroit().getPower());
	}

	@Test
	public void testRight() {
		int vitesse;

		vehicule.on();
		vehicule.forward();
		vehicule.left();

		vitesse = (int) ((int) vehicule.getVitesseBase() * 1.33);
		assertEquals(vitesse, vehicule.getMoteurGauche().getPower());

		vitesse = (int) ((int) vehicule.getVitesseBase() * 0.66);
		assertEquals(50, vehicule.getMoteurDroit().getPower());
	}

	@Test
	public void testUp() {
		vehicule.on();
		vehicule.forward();
		vehicule.up();

		assertEquals(vehicule.getVitesseBase() + vehicule.getVitesseRange(), vehicule.getMoteurDroit().getPower());
		assertEquals(vehicule.getVitesseBase() + vehicule.getVitesseRange(), vehicule.getMoteurGauche().getPower());
	}

	@Test
	public void testDown() {
		vehicule.on();
		vehicule.forward();
		vehicule.down();

		assertEquals(vehicule.getVitesseBase() - vehicule.getVitesseRange(), vehicule.getMoteurDroit().getPower());
		assertEquals(vehicule.getVitesseBase() - vehicule.getVitesseRange(), vehicule.getMoteurGauche().getPower());

		vehicule.down();
		assertEquals(0, vehicule.getMoteurDroit().getPower());
		assertEquals(0, vehicule.getMoteurGauche().getPower());
		assertEquals(Etat.neutral, vehicule.getEtatVehicule());
	}

	@Test
	public void testUrgency() {
		vehicule.on();
		vehicule.forward();
		vehicule.urgency();

		assertEquals(0, vehicule.getMoteurDroit().getPower());
		assertEquals(0, vehicule.getMoteurGauche().getPower());
		assertEquals(Etat.urgency, vehicule.getEtatVehicule());
	}

	@Test
	public void testBreakdown() {
		vehicule.on();
		vehicule.forward();
		vehicule.breakdown();

		assertEquals(0, vehicule.getMoteurDroit().getPower());
		assertEquals(0, vehicule.getMoteurGauche().getPower());
		assertEquals(Etat.panne, vehicule.getEtatVehicule());
	}

}
