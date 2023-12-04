package controleur;

import java.awt.event.ActionEvent;

import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Connection;
import java.util.stream.Collectors;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTable;

import dao.TournoiJDBC;
import ihm.VueCreationTournoi;
import ihm.VueListeTournois;
import modele.Niveau;
import modele.Status;
import modele.Tournoi;

public class ControleurListeTournois implements ActionListener, ItemListener, MouseListener {

	private VueListeTournois vue;
	private Tournoi modele;
	private TournoiJDBC jdbc;
	
	private Niveau niveau;
	private Status status;
	
	public ControleurListeTournois(VueListeTournois vue) {
		this.modele = new Tournoi();
		this.vue = vue;
		this.jdbc = new TournoiJDBC();
		this.niveau = null;
		this.status = null;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() instanceof JButton) {
			JButton bouton = (JButton) e.getSource();
			if (bouton.getText().equals("Créer Tournoi")) {
				VueCreationTournoi vue = new VueCreationTournoi();
				vue.setVisible(true);
			} else if (bouton.getText().equals("Retour")) {
				vue.dispose();
			}
		}
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		if (e.getSource() instanceof JComboBox) {
			JComboBox<String> comboBox = (JComboBox<String>) e.getSource();
			String option = (String) comboBox.getSelectedItem();
			if (vue.estOptionComboboxNiveau(option)) {
				this.niveau = Niveau.getNiveau(option);
			}
			if (vue.estOptionComboboxStatus(option)) {
				this.status = Status.getStatus(option);
			}
			System.out.println(niveau + " " + status);
			vue.afficherTournois(modele.getTournoisNiveauStatus(niveau, status));
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() == 2) {
            JTable target = (JTable) e.getSource();
            int row = target.getSelectedRow();
            VueCreationTournoi vue = new VueCreationTournoi();
			vue.setVisible(true);
         }
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
