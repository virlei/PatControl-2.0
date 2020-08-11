package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;
import db.DbIntegrityException;
import model.dao.LocalDao;
import model.entities.Local;


public class LocalDaoJDBC implements LocalDao{
	
	private Connection conn;
	
	public LocalDaoJDBC(Connection conn) {
		this.conn = conn;
	}
	
	public void insert(Local obj) {
		
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
				"INSERT INTO TB_LOCAL " +
				"(Txt_Descricao) " +
				"VALUES " +
				"(?)", 
				Statement.RETURN_GENERATED_KEYS);

			st.setString(1, obj.getDescricaoLocal());

			int rowsAffected = st.executeUpdate();
			
			if (rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					int local = rs.getInt(1);
					obj.setIdLocal(local);
				}
			}
			else {
				throw new DbException("Unexpected error! No rows affected!");
			}
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		} 
		finally {
			DB.closeStatement(st);
		}
	}
	
	public void update(Local obj) {
		
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
				"UPDATE TB_LOCAL " +
				"SET TXT_Descricao = ? " +
				"WHERE PK_Local = ?");

			st.setString(1, obj.getDescricaoLocal());
			st.setInt(2, obj.getIdLocal());

			st.executeUpdate();
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		} 
		finally {
			DB.closeStatement(st);
		}
	}		
	
	
	public void deleteById(Integer local) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
				"DELETE FROM TB_LOCAL WHERE PK_Local = ?");

			st.setInt(1, local);

			st.executeUpdate();
		}
		catch (SQLException e) {
			throw new DbIntegrityException(e.getMessage());
		} 
		finally {
			DB.closeStatement(st);
		}
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
				loc.setIdLocal(rs.getInt("Pk_Local"));
				loc.setDescricaoLocal(rs.getString("TXT_Descricao"));
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
	
	private Local instantiateLocal(ResultSet rs) throws SQLException {

		Local local = new Local();
		local.setIdLocal(rs.getInt("PK_Local"));
		local.setDescricaoLocal(rs.getString("TXT_Descricao"));
		return local;
	}
	
	public List<Local>findAll(){
		
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT * FROM TB_LOCAL;");
					
			rs = st.executeQuery();
			
			List<Local> list = new ArrayList<>();
			
			while (rs.next()) {
				Local loc = instantiateLocal(rs);
				list.add(loc);
			}
			return list;
		}
		catch (SQLException e) {
				throw new DbException (e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

}
