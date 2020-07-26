package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import db.DB;
import db.DbException;
import model.dao.LocalDao;
import model.entities.Local;


public class LocalDaoJDBC implements LocalDao{
	
	private Connection conn;
	
	public LocalDaoJDBC(Connection conn) {
		this.conn = conn;
	}
	
	public void insert(Local obj) {
		
	}
	
	public void update(Local obj) {
		
	}
	
	public void deleteById(Integer local) {
		
	}
	
	public Local findById(Integer local) {

		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT * FROM TB_Local where PK_Local = ?");
			st.setInt(1, local);
			rs = st.executeQuery();
			if (rs.next()) {				
				Local loc = new Local();
				loc.setLocal(rs.getInt("Pk_Local"));
				loc.setDescricao(rs.getString("TXT_Descricao"));
				return loc;				
			} 
			return null;					
		}
		catch (SQLException e) {
			throw new DbException (e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	
	}
	
	public List<Local>findAll(){
		return null;
	}

}
