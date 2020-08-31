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
import model.dao.GFornDao;
import model.entities.GForn;

public class GFornDaoJDBC implements GFornDao{
	
	private Connection conn;
	
	public GFornDaoJDBC(Connection conn) {
		this.conn = conn;
	}
	
	@Override
	public void insert (GForn obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
				"INSERT INTO TB_FORNECIMENTO " +
				"(INT_NrGuia, DAT_Fornecimento) " +
				"VALUES " +
				"(?, ?)", 
				Statement.RETURN_GENERATED_KEYS);

			st.setInt(1, obj.getNrGuia());
			st.setString(2, obj.getDtForn());

			int rowsAffected = st.executeUpdate();
			
			if (rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					obj.setPkGForn(id);
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
	
	@Override
	public void update(GForn obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
				"UPDATE TB_FORNECIMENTO " +
				"SET INT_NrGuia = ?, DAT_Fornecimento = ? " +		
				"WHERE PK_Guia = ?");

			st.setInt(1, obj.getNrGuia());
			st.setString(2, obj.getDtForn());

			st.executeUpdate();
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		} 
		finally {
			DB.closeStatement(st);
		}
	}
	
	public void deleteById(Integer id) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
				"DELETE FROM TB_FORNECIMENTO WHERE PK_Guia = ?");
			st.setInt(1, id);

			st.executeUpdate();
		}
		catch (SQLException e) {
			throw new DbIntegrityException(e.getMessage());
		} 
		finally {
			DB.closeStatement(st);
		}
	}
	
	public GForn findById(Integer id) {
		
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT * FROM TB_GForn where PK_Guia = ?");
			st.setInt(1, id);
			rs = st.executeQuery();
			if (rs.next()) {
				GForn obj = instantiateGForn(rs);
				return obj;
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
	
	private GForn instantiateGForn(ResultSet rs) throws SQLException {
		GForn obj = new GForn();
		obj.setPkGForn(rs.getInt("PK_Guia"));
		obj.setNrGuia(rs.getInt("INT_NrGuia"));
		obj.setDtForn(rs.getString("DAT_Fornecimento"));
		return obj;
	}
	
	public List<GForn>findAll(){
		
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT * FROM TB_FORNECIMENTO");
					
			rs = st.executeQuery();
			
			List<GForn> list = new ArrayList<>();
			
			while (rs.next()) {
				GForn obj = instantiateGForn(rs);
				list.add(obj);
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
