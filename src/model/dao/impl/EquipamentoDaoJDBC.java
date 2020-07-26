package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;
import model.dao.EquipamentoDao;
import model.entities.Equipamento;
import model.entities.Patrimonio;

public class EquipamentoDaoJDBC implements EquipamentoDao{
	
	private Connection conn;
	
	public EquipamentoDaoJDBC(Connection conn) {
		this.conn = conn;
	}
	
	public void insert (Equipamento obj) {
		
	}
	
	public void update(Equipamento obj) {
		
	}
	
	public void deleteById(Integer id) {
		
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
