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
import model.entities.Local;
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
					+ "(pk_patrimonio, fk_equipamento, txt_fabricante, txt_marca, txt_descricao, int_condicaoUso, fk_Local) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?)");
			st.setLong(1, obj.getNumero());
			st.setInt(2, obj.getTipEquip().getId());
			st.setString(3, obj.getFabricante());
			st.setString(4, obj.getMarca());
			st.setString(5, obj.getDescricao());
			st.setInt(7, obj.getLocal().getIdLocal());
			if (obj.getCondicaoUso() == null) {
				st.setInt(6, 0);
			}
			else {
				st.setInt(6, obj.getCondicaoUso());
			}
			
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
					+ "SET fk_equipamento=?, txt_fabricante=?, txt_marca=?, txt_descricao=?, int_condicaoUso=?, fk_local=? "
					+ "WHERE PK_patrimonio = ?");
			
			//st.setLong(1, obj.getNumero());
			st.setInt(1, obj.getTipEquip().getId());
			st.setString(2, obj.getFabricante());
			st.setString(3, obj.getMarca());
			st.setString(4, obj.getDescricao());
			st.setInt(5, obj.getCondicaoUso());
			st.setInt(6, obj.getLocal().getIdLocal());
			st.setLong(7, obj.getNumero());
			
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
	public void deleteById(Long id) {
			
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
	public Patrimonio findById(Long id) {
		
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT TB_PATRIMONIO.*, TB_EQUIPAMENTO.TXT_Descricao as txtEquip, TB_EQUIPAMENTO.PK_Equipamento as idEquip, "
					+ "TB_LOCAL.PK_Local as idLocal, TB_LOCAL.TXT_Descricao as descricaoLocal "
					+ "FROM TB_PATRIMONIO, TB_EQUIPAMENTO, TB_LOCAL "
					+ "WHERE (TB_PATRIMONIO.FK_Equipamento = TB_EQUIPAMENTO.PK_Equipamento) "
					+ "AND (TB_PATRIMONIO.FK_local = TB_LOCAL.PK_local) "
					+ "AND (TB_PATRIMONIO.PK_Patrimonio = ?);");
			st.setLong(1, id);
			rs = st.executeQuery();
			if (rs.next()) {
				Equipamento equip = instantiateEquipamento(rs);
				Local local = instantiateLocal(rs);
				Patrimonio pat = instantiatePatrimonio(rs, equip, local); 
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

	private Patrimonio instantiatePatrimonio(ResultSet rs, Equipamento equip, Local local) throws SQLException {
		
		Patrimonio pat = new Patrimonio();
		pat.setNumero(rs.getLong("PK_Patrimonio"));
		pat.setMarca(rs.getString("TXT_Marca"));
		pat.setFabricante(rs.getString("TXT_Fabricante"));
		pat.setDescricao(rs.getString("TXT_Descricao"));
		pat.setCondicaoUso(rs.getByte("INT_condicaoUso"));
		pat.setTipEquip(equip);
		pat.setLocal(local);
		//Alerts.showAlert("Instanciando Patrimonio", null, pat.toString(), AlertType.INFORMATION);
		
//		if (obj.getAnyDate != null) {
//			obj.setAnyDate(new java.util.Date(rs.getTimestamp("AnyDate").getTime()));
//		}
		
		return pat;
	}

	private Equipamento instantiateEquipamento(ResultSet rs) throws SQLException {

		Equipamento equip = new Equipamento();
		equip.setId(rs.getInt("idEquip"));
		equip.setDescricao(rs.getString("txtEquip"));
		return equip;
	}

	private Local instantiateLocal(ResultSet rs) throws SQLException {
		
		Local local = new Local();
		local.setIdLocal(rs.getInt("idLocal"));
		local.setDescricaoLocal(rs.getString("descricaoLocal"));
		return local;
		
	}
	
	@Override
	public List<Patrimonio> findAll() {
		
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT TB_PATRIMONIO.*,TB_EQUIPAMENTO.PK_Equipamento as idEquip, TB_EQUIPAMENTO.TXT_Descricao AS txtEquip, "
					+ "TB_LOCAL.PK_Local as idLocal, TB_LOCAL.TXT_Descricao as descricaoLocal "
					+ "FROM TB_PATRIMONIO, TB_EQUIPAMENTO, TB_LOCAL "
					+ "WHERE TB_PATRIMONIO.FK_Equipamento = TB_EQUIPAMENTO.PK_Equipamento "
					+ "AND TB_PATRIMONIO.FK_local = TB_LOCAL.PK_local ");
					
			rs = st.executeQuery();
			
			List<Patrimonio> list = new ArrayList<>();
			
			//Para evitar repetição do Tipo de Equipamento para cada Patrimônio, utilizamos a estrutura de "Map"
			//Assim, garantimos que Patrimônios do mesmo tipo de Equipamento apontem para um único objeto instanciado
			//Economizando assim memória.
			Map<Integer, Equipamento> map = new HashMap<>();
			
			//Observar a mesma regra aplicada em Tipo de Equipamento para o Local
			Map<Integer, Local> mapLocal = new HashMap<>();
						
			while (rs.next()) {
				
				Equipamento equip = map.get(rs.getInt("idEquip"));
				
				Local local = mapLocal.get(rs.getInt("idLocal"));
				
				if (equip == null) {
					equip = instantiateEquipamento(rs);
					map.put(rs.getInt("idEquip"), equip);
				}
				
				if (local == null) {
					local = instantiateLocal(rs);
					mapLocal.put(rs.getInt("idLocal"),local);
				}
				
				Patrimonio pat = instantiatePatrimonio(rs, equip, local);
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
