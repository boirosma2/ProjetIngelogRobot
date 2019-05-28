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
	public void testStart() {

		VehiculeControler vehicule = new VehiculeControler();

		assertEquals(Etat.contact, vehicule.getEtatVehicule());

		vehicule.start();
		assertEquals(Etat.neutral, vehicule.getEtatVehicule());
	}

	@Test
	public void testStop() {
		assertEquals(Etat.neutral, vehicule.getEtatVehicule());

		vehicule.stop();
		assertEquals(Etat.contact, vehicule.getEtatVehicule());

	}

	@Test
	public void testNeutralForward() {

		vehicule.start();
		vehicule.forward();
		vehicule.neutral();
		assertEquals(Etat.neutral, vehicule.getEtatVehicule());
	}

	@Test
	public void testNeutralBackward() {

		vehicule.start();
		vehicule.backward();
		vehicule.neutral();
		assertEquals(Etat.neutral, vehicule.getEtatVehicule());
	}

	@Test
	public void testForward() {
		assertEquals(Etat.contact, vehicule.getEtatVehicule());

		vehicule.forward();
		assertEquals(Etat.contact, vehicule.getEtatVehicule());

		vehicule.start();
		vehicule.forward();
		assertEquals(Etat.forward, vehicule.getEtatVehicule());
		assertEquals(50, vehicule.getMoteurGauche().getSpeed());
		assertEquals(50, vehicule.getMoteurDroit().getSpeed());

	}

	@Test
	public void testBackward() {

		assertEquals(Etat.contact, vehicule.getEtatVehicule());

		vehicule.backward();
		assertEquals(Etat.contact, vehicule.getEtatVehicule());

		vehicule.start();
		vehicule.backward();
		assertEquals(Etat.backward, vehicule.getEtatVehicule());
		assertEquals(50, vehicule.getMoteurGauche().getSpeed());
		assertEquals(50, vehicule.getMoteurDroit().getSpeed());
	}

	@Test
	public void testGauche() {

		int vitesse;

		vehicule.start();
		vehicule.forward();
		vehicule.gauche();

		vitesse = (int) ((int) vehicule.getVitesseBase() * 0.66);
		assertEquals(vitesse, vehicule.getMoteurGauche().getSpeed());

		vitesse = (int) ((int) vehicule.getVitesseBase() * 1.33);
		assertEquals(50, vehicule.getMoteurDroit().getSpeed());
	}

	@Test
	public void testDroite() {
		int vitesse;

		vehicule.start();
		vehicule.forward();
		vehicule.gauche();

		vitesse = (int) ((int) vehicule.getVitesseBase() * 1.33);
		assertEquals(vitesse, vehicule.getMoteurGauche().getSpeed());

		vitesse = (int) ((int) vehicule.getVitesseBase() * 0.66);
		assertEquals(50, vehicule.getMoteurDroit().getSpeed());
	}

	@Test
	public void testAccelerer() {
		vehicule.start();
		vehicule.forward();
		vehicule.accelerer();

		assertEquals(vehicule.getVitesseBase() + vehicule.getVitesseRange(), vehicule.getMoteurDroit().getSpeed());
		assertEquals(vehicule.getVitesseBase() + vehicule.getVitesseRange(), vehicule.getMoteurGauche().getSpeed());
	}

	@Test
	public void testRalentir() {
		vehicule.start();
		vehicule.forward();
		vehicule.ralentir();

		assertEquals(vehicule.getVitesseBase() - vehicule.getVitesseRange(), vehicule.getMoteurDroit().getSpeed());
		assertEquals(vehicule.getVitesseBase() - vehicule.getVitesseRange(), vehicule.getMoteurGauche().getSpeed());

		vehicule.ralentir();
		assertEquals(0, vehicule.getMoteurDroit().getSpeed());
		assertEquals(0, vehicule.getMoteurGauche().getSpeed());
		assertEquals(Etat.neutral, vehicule.getEtatVehicule());
	}
}
