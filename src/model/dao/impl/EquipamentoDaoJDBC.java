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
import model.dao.EquipamentoDao;
import model.entities.Equipamento;

public class EquipamentoDaoJDBC implements EquipamentoDao{
	
	private Connection conn;
	
	public EquipamentoDaoJDBC(Connection conn) {
		this.conn = conn;
	}
	
	@Override
	public void insert (Equipamento obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
				"INSERT INTO TB_EQUIPAMENTO " +
				"(Txt_Descricao) " +
				"VALUES " +
				"(?)", 
				Statement.RETURN_GENERATED_KEYS);

			st.setString(1, obj.getDescricao());

			int rowsAffected = st.executeUpdate();
			
			if (rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					obj.setId(id);
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
	public void update(Equipamento obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
				"UPDATE TB_EQUIPAMENTO " +
				"SET TXT_Descricao = ? " +
				"WHERE PK_Equipamento = ?");

			st.setString(1, obj.getDescricao());
			st.setInt(2, obj.getId());

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
				"DELETE FROM TB_EQUIPAMENTO WHERE PK_Equipamento = ?");

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
	
	public Equipamento findById(Integer id) {
		
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT * FROM TB_Equipamento where PK_Equipamento = ?");
			st.setInt(1, id);
			rs = st.executeQuery();
			if (rs.next()) {				
				Equipamento equipamento = new Equipamento();
				equipamento.setId(rs.getInt("PK_Equipamento"));
				equipamento.setDescricao(rs.getString("TXT_Descricao"));
				return equipamento;				
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
	
	private Equipamento instantiateEquipamento(ResultSet rs) throws SQLException {

		Equipamento equip = new Equipamento();
		equip.setId(rs.getInt("PK_Equipamento"));
		equip.setDescricao(rs.getString("TXT_Descricao"));
		return equip;
	}
	
	public List<Equipamento>findAll(){
		
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT * FROM TB_EQUIPAMENTO;");
					
			rs = st.executeQuery();
			
			List<Equipamento> list = new ArrayList<>();
			
			while (rs.next()) {
				Equipamento equip = instantiateEquipamento(rs);
				list.add(equip);
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
