package scenarios;

import static org.junit.Assert.*;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import Fonctions.LireCSV;
import Images.ImagesIcons;
import dao.CreateDB;
import dao.EquipeJDBC;
import dao.JoueurJDBC;
import modele.Equipe;
import modele.Joueur;
import modele.ModeleImportation;
import modele.Niveau;
import modele.Pays;
import modele.Statut;
import modele.Tournoi;
import modele.ModeleImportation.EtatEquipe;

public class TestsImportationEquipes {
	
	private Tournoi tournoi;
	private Tournoi autreTournoi;
	private String cheminCSV1;
	private String cheminCSV2;
	private ModeleImportation modele;

	@Before
	public void setUp() throws Exception {
		CreateDB.main(null);
		tournoi = Tournoi.createTournoi("Tournoi1", Niveau.LOCAL, Date.valueOf(LocalDate.of(2024, 01, 01)), Date.valueOf(LocalDate.of(2024, 01, 10)), 
				Pays.FR, Statut.ATTENTE_EQUIPES, Optional.empty(), Optional.empty());
		autreTournoi = Tournoi.createTournoi("AutreTournoi", Niveau.LOCAL, Date.valueOf(LocalDate.of(2024, 02, 01)), Date.valueOf(LocalDate.of(2024, 02, 10)), 
				Pays.FR, Statut.ATTENTE_EQUIPES, Optional.empty(), Optional.empty());
		new Tournoi().ajouterTournoi(tournoi);
		cheminCSV1 = System.getProperty("user.dir") + "\\src\\scenarios\\testCSV_Tournoi1.csv";
		cheminCSV2 = System.getProperty("user.dir") + "\\src\\scenarios\\testCSV_Tournoi1_Equipes.csv";
		modele = new ModeleImportation();
	}

	@After
	public void tearDown() throws Exception {
		tournoi = null;
		autreTournoi = null;
	}

	@Test
	public void testAdministrateurImporteEtConfirme() throws IOException {
		Equipe equipeBDD = new Equipe();
		Tournoi tournoiBDD = new Tournoi();
		modele.importerEquipesJoueurs(cheminCSV1);
		if (this.modele.verifierEquipe() == EtatEquipe.OK) {
			this.modele.enregistrerImportation(tournoi);
			tournoiBDD.changerStatutTournoi(tournoi, Statut.ATTENTE_ARBITRES);
		}
	
		assertEquals(Statut.ATTENTE_ARBITRES, tournoiBDD.getTournoiParNom(tournoi.getNomTournoi()).getStatut());
		
		int nbEquipesTotal = equipeBDD.getToutesLesEquipes().size();
		int nbEquipesTournoi = tournoiBDD.getEquipesTournoi(tournoi).size();
		assertEquals(4, nbEquipesTotal);
		assertEquals(nbEquipesTotal, nbEquipesTournoi);
		
		for (int numEquipe = 0; numEquipe < nbEquipesTournoi; numEquipe++) {
			Equipe equipe = equipeBDD.getEquipeParNom("Equipe" + (numEquipe + 1));
			List<Joueur> joueursEquipe = equipe.getJoueurs();
			assertNotNull(equipe);
			assertNotNull(joueursEquipe);
			assertEquals(joueursEquipe.size(), 5);
			for (int numJoueur = 0; numJoueur < joueursEquipe.size(); numJoueur++) {
				assertEquals(joueursEquipe.get(numJoueur).toString(), "Joueur ID[" + ((numJoueur + 1) + (numEquipe * 5)) + "] : Joueur" + ((numJoueur + 1) + (numEquipe * 5)));
			}
		}
	}
	
	@Test
	public void testFichierCSVConcerneTournoi() throws IOException {
		assertTrue(modele.fichierCSVconcerneTournoi(cheminCSV1, tournoi));
		assertFalse(modele.fichierCSVconcerneTournoi(cheminCSV1, autreTournoi));
	}
	
	@Test 
	public void testFichierCSVNombreEquipes() throws IOException {
		assertTrue(modele.fichierCSVconcerneTournoi(cheminCSV1, tournoi));
		assertTrue(modele.fichierCSVNombreEquipes(cheminCSV1, tournoi));
		assertTrue(modele.fichierCSVconcerneTournoi(cheminCSV2, tournoi));
		assertFalse(modele.fichierCSVNombreEquipes(cheminCSV2, tournoi));
	}
	
	@Test
	public void testEquipePasPresenteApplication() throws IOException {
		Equipe equipeBDD = new Equipe();
		Tournoi tournoiBDD = new Tournoi();
		
		Equipe equipeUne = new Equipe(EquipeJDBC.getNextValueSequence(), "Equipe1", 23, Pays.TW);
	    Joueur j1 = new Joueur(JoueurJDBC.getNextValueSequence(), "Joueur1", equipeUne);
	    Joueur j2 = new Joueur(JoueurJDBC.getNextValueSequence(), "Joueur2", equipeUne);
	    Joueur j3 = new Joueur(JoueurJDBC.getNextValueSequence(), "Joueur3", equipeUne);
	    Joueur j4 = new Joueur(JoueurJDBC.getNextValueSequence(), "Joueur4", equipeUne);
	    Joueur j5 = new Joueur(JoueurJDBC.getNextValueSequence(), "Joueur5", equipeUne);
	    equipeUne.ajouterJoueur(j1, j2, j3, j4, j5);
	    equipeBDD.ajouterEquipe(equipeUne);
		
		modele.importerEquipesJoueurs(cheminCSV1);
		if (this.modele.verifierEquipe() == EtatEquipe.OK) {
			this.modele.enregistrerImportation(tournoi);
			tournoiBDD.changerStatutTournoi(tournoi, Statut.ATTENTE_ARBITRES);
		}
		
		assertEquals(Statut.ATTENTE_ARBITRES, tournoiBDD.getTournoiParNom(tournoi.getNomTournoi()).getStatut());
		
		int nbEquipesTotal = equipeBDD.getToutesLesEquipes().size();
		int nbEquipesTournoi = tournoiBDD.getEquipesTournoi(tournoi).size();
		assertEquals(nbEquipesTotal, 4);
		assertEquals(nbEquipesTotal, nbEquipesTournoi);
		
		for (int numEquipe = 0; numEquipe < nbEquipesTournoi; numEquipe++) {
			Equipe equipe = equipeBDD.getEquipeParNom("Equipe" + (numEquipe + 1));
			List<Joueur> joueursEquipe = equipe.getJoueurs();
			assertNotNull(equipe);
			assertNotNull(joueursEquipe);
			assertEquals(joueursEquipe.size(), 5);
			for (int numJoueur = 0; numJoueur < joueursEquipe.size(); numJoueur++) {
				assertEquals(joueursEquipe.get(numJoueur).toString(), "Joueur ID[" + ((numJoueur + 1) + (numEquipe * 5)) + "] : Joueur" + ((numJoueur + 1) + (numEquipe * 5)));
			}
		}
	}
}
