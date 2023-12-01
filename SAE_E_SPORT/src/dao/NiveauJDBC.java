package dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import modele.Niveau;
import modele.Pays;

public class NiveauJDBC implements NiveauDAO{
	
	@Override
	public List<Niveau> getAll() throws Exception {
		List<Niveau> niveaux = new ArrayList<>();
        try {
			Statement st = ConnectionJDBC.getConnection().createStatement();
	        ResultSet rs = st.executeQuery("select * from NiveauTournoi");
        
	        while (rs.next()) {
	        	niveaux.add(Niveau.getNiveau(rs.getString("niveau")));
	        }
        } catch (SQLException e) {
        	e.printStackTrace();
        }
        return niveaux;
	}

	@Override
	public Optional<Niveau> getById(Integer id) throws Exception {
		return Optional.empty();
	}

	@Override
	public boolean add(Niveau niveau) throws Exception {
		boolean res = false;
		try {
			CallableStatement cs = ConnectionJDBC.getConnection().prepareCall("insert into NiveauTournoi values (?)");
			cs.setString(1, niveau.denomination());
			cs.execute();
			res = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;
	}

	@Override
	public boolean update(Niveau value) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean delete(Niveau value) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

}