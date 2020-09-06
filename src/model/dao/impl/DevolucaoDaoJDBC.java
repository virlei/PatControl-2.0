package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;
import db.DbIntegrityException;
import gui.util.Utils;
import model.dao.DevolucaoDao;
import model.entities.Devolucao;

public class DevolucaoDaoJDBC implements DevolucaoDao {

private Connection conn;
	
	public DevolucaoDaoJDBC(Connection conn) {
		this.conn = conn;
	}
	
	@Override
	public void insert (Devolucao obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"INSERT INTO TB_Devolucao "
					+ "(DAT_Devolucao, NUM_SEI, TXT_MOTIVO) "
					+ "VALUES (?, ?, ?)");
						
			st.setString(1, Utils.parseToString(obj.getDtDevolucao(), "dd/MM/yyyy"));

			st.setString(2, obj.getNumSei());
			st.setString(3, obj.getMotivo());
			
			int rowsAffected = st.executeUpdate();
			
			if (rowsAffected == 0) {
				throw new DbException("Erro inesperado! Nenhuma linha foi inserida!");
			}
		}
		catch (SQLException e) {
			throw new DbException (e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
		
	}
	
	@Override
	public void update(Devolucao obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
				"UPDATE TB_Devolucao " +
				"SET DAT_Devolucao = ?, NUM_SEI = ?, TXT_MOTIVO = ? " +		
				"WHERE PK_Devolucao = ?");

			st.setString(1, Utils.parseToString(obj.getDtDevolucao(), "dd/MM/yyyy"));
			st.setString(2, obj.getNumSei());
			st.setString(3, obj.getMotivo());
			st.setInt(4, obj.getDevolucao());

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
				"DELETE FROM TB_DEVOLUCAO WHERE PK_Devolucao = ?");
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
	
	public Devolucao findById(Integer id) {
		
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT * FROM TB_Devolucao where PK_Devolucao = ?");
			st.setInt(1, id);
			rs = st.executeQuery();
			if (rs.next()) {
				Devolucao obj = instantiateDevolucao(rs);
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
	
	private Devolucao instantiateDevolucao(ResultSet rs) throws SQLException {
		Devolucao obj = new Devolucao();
		obj.setDevolucao(rs.getInt("PK_Devolucao"));
//		obj.setDatDevolucao(rs.getString("DAT_Devolucao"));
		obj.setDtDevolucao( Utils.tryParseToDate(rs.getString("DAT_Devolucao")));
		
		obj.setNumSei(rs.getString("NUM_SEI"));
		obj.setMotivo(rs.getString("TXT_MOTIVO"));
		return obj;
	}
	
	public List<Devolucao>findAll(){
		
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT * FROM TB_DEVOLUCAO");
					
			rs = st.executeQuery();
			
			List<Devolucao> list = new ArrayList<>();
			
			while (rs.next()) {
				Devolucao obj = instantiateDevolucao(rs);
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
