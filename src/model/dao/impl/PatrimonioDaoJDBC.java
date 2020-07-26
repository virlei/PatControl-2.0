package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
import model.dao.PatrimonioDao;
import model.entities.Equipamento;
import model.entities.Patrimonio;

public class PatrimonioDaoJDBC implements PatrimonioDao {

	private Connection conn;
	
	public PatrimonioDaoJDBC(Connection conn) {
		this.conn = conn;
	}
	
	@Override
	public void insert(Patrimonio obj) {
		
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"INSERT INTO TB_PATRIMONIO "
					+ "(pk_patrimonio, fk_equipamento, txt_fabricante, txt_marca, txt_descricao, int_condicaoUso) "
					+ "VALUES (?, ?, ?, ?, ?, ?)");
			st.setLong(1, obj.getNumero());
			st.setInt(2, obj.getTipEquip().getId());
			st.setString(3, obj.getFabricante());
			st.setString(4, obj.getMarca());
			st.setString(5, obj.getDescricao());
			st.setInt(6, obj.getCondicaoUso());
			
			int rowsAffected = st.executeUpdate();
			
			if (rowsAffected == 0) {
				throw new DbException("Erro inexperado! Nenhuma linha foi inserida!");
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
	public void update(Patrimonio obj) {
		
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"UPDATE TB_PATRIMONIO "
					+ "SET fk_equipamento=?, txt_fabricante=?, txt_marca=?, txt_descricao=?, int_condicaoUso=? "
					+ "WHERE PK_patrimonio = ?");
			
			//st.setLong(1, obj.getNumero());
			st.setInt(1, obj.getTipEquip().getId());
			st.setString(2, obj.getFabricante());
			st.setString(3, obj.getMarca());
			st.setString(4, obj.getDescricao());
			st.setInt(5, obj.getCondicaoUso());
			st.setLong(6, obj.getNumero());
			
			st.executeUpdate();
			
		}
		catch (SQLException e) {
			throw new DbException (e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void deleteById(long id) {
			
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("DELETE FROM TB_Patrimonio WHERE PK_patrimonio = ?");
			st.setLong(1, id);
			st.executeUpdate();
		}
		catch (SQLException e) {
			throw new DbException (e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public Patrimonio findById(long id) {
		
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT TB_PATRIMONIO.*, TB_EQUIPAMENTO.TXT_Descricao as Equip "
					+ "FROM TB_PATRIMONIO INNER JOIN TB_EQUIPAMENTO "
					+ "ON TB_PATRIMONIO.FK_EQUIPAMENTO = TB_EQUIPAMENTO.PK_Equipamento "
					+ "where TB_PATRIMONIO.PK_Patrimonio = ?");
			st.setLong(1, id);
			rs = st.executeQuery();
			if (rs.next()) {
				Equipamento equip = instantiateEquipamento(rs);
				Patrimonio pat = instantiatePatrimonio(rs, equip); 
				return pat;				
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

	private Patrimonio instantiatePatrimonio(ResultSet rs, Equipamento equip) throws SQLException {
		
		Patrimonio pat = new Patrimonio();
		pat.setNumero(rs.getLong("PK_Patrimonio"));
		pat.setMarca(rs.getString("TXT_Marca"));
		pat.setFabricante(rs.getString("TXT_Fabricante"));
		pat.setDescricao(rs.getString("TXT_Descricao"));
		pat.setCondicaoUso(rs.getByte("INT_condicaoUso"));
		pat.setTipEquip(equip);
		return pat;
	}

	private Equipamento instantiateEquipamento(ResultSet rs) throws SQLException {

		Equipamento equip = new Equipamento();
		equip.setId(rs.getInt("FK_Equipamento"));
		equip.setDescricao(rs.getString("TXT_Descricao"));
		return equip;
	}

	@Override
	public List<Patrimonio> findAll() {
		
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
/*					"SELECT TB_PATRIMONIO.*, TB_EQUIPAMENTO.PK_Equipamento, TB_EQUIPAMENTO.TXT_Descricao AS TipEquip "
					+ "FROM TB_PATRIMONIO INNER JOIN TB_EQUIPAMENTO "
					+ "ON TB_PATRIMONIO.FK_Equipamento = TB_EQUIPAMENTO.PK_Equipamento;");
*/
					"SELECT TB_PATRIMONIO.*,TB_EQUIPAMENTO.PK_Equipamento, TB_EQUIPAMENTO.TXT_Descricao AS TipEquip "
					+ "FROM TB_PATRIMONIO, TB_EQUIPAMENTO "
					+ "WHERE TB_PATRIMONIO.FK_Equipamento = TB_EQUIPAMENTO.PK_Equipamento;");
					
			rs = st.executeQuery();
			
			List<Patrimonio> list = new ArrayList<>();
			
			//Para evitar repetição do Tipo de Equipamento para cada Patrimônio, utilizamos a estrutura de "Map"
			//Assim, garantimos que Patrimônios do mesmo tipo de Equipamento apontem para um único objeto instanciado
			//Economizando assim memória.
			Map<Integer, Equipamento> map = new HashMap<>();
			
			while (rs.next()) {
				
				Equipamento equip = map.get(rs.getInt("PK_Equipamento"));
				
				if (equip == null) {
					equip = instantiateEquipamento(rs);
					map.put(rs.getInt("PK_Equipamento"), equip);
				}
				
				Patrimonio pat = instantiatePatrimonio(rs, equip);
				list.add(pat);
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
