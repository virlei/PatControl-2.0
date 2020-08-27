package model.dao.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
import db.DbIntegrityException;
import gui.util.Alerts;
import javafx.scene.control.Alert.AlertType;
import model.dao.GuiaDeFornecimentoTesteDao;
import model.entities.Equipamento;
import model.entities.GuiaDeFornecimentoTeste;
import model.entities.Local;
import model.entities.Patrimonio;

public class GuiaDeFornecimentoTesteDaoJDBC implements GuiaDeFornecimentoTesteDao {
	
	private Connection conn;
	
	public GuiaDeFornecimentoTesteDaoJDBC(Connection conn) {
		this.conn = conn;
	}	
	
	public void insert (GuiaDeFornecimentoTeste obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
				"INSERT INTO TB_FORNECIMENTO " +
				"(INT_NumeroGuia, DAT_Fornecimento) " +
				"VALUES " +
				"(?,?)", 
				Statement.RETURN_GENERATED_KEYS);

			st.setLong(1, obj.getNrGuia());
			st.setDate(2, (Date) obj.getDtFornecimento()); 

			int rowsAffected = st.executeUpdate();
			
			if (rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					obj.setpKey(id);
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
	
	
	public void update(GuiaDeFornecimentoTeste obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
				"UPDATE TB_FORNECIMENTO " +
				"SET INT_NumeroGuia = ?, SET DAT_Fornecimento = ? " +
				"WHERE PK_Guia = ?");

			st.setLong(1, obj.getNrGuia());
			st.setDate(2, (Date) obj.getDtFornecimento());

			st.executeUpdate();
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		} 
		finally {
			DB.closeStatement(st);
		}
	}
	
	public void deleteById(Long id) {
		PreparedStatement st = null;
		try {					
			st = conn.prepareStatement(
				"DELETE FROM TB_Fornecimento WHERE INT_NumeroGuia = ?");
			st.setLong(1, id);

			st.executeUpdate();
				
		}
		catch (SQLException e) {
			throw new DbIntegrityException(e.getMessage());
		} 
		finally {
			DB.closeStatement(st);
		}
	}
	
	public GuiaDeFornecimentoTeste findById(Long id) {
		
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT * FROM TB_Fornecimento where INT_NumeroGuia = ?");
			st.setLong(1, id);
			rs = st.executeQuery();
			if (rs.next()) {				
				GuiaDeFornecimentoTeste guia = new GuiaDeFornecimentoTeste();
				guia.setNrGuia(rs.getLong("INT_NumeroGuia"));
				guia.setDtFornecimento(rs.getDate("DAT_Fornecimento"));
				return guia;				
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
	
	private GuiaDeFornecimentoTeste instantiateGuiaDeFornecimentoTeste(ResultSet rs) throws SQLException {

		GuiaDeFornecimentoTeste guia = new GuiaDeFornecimentoTeste();
		guia.setNrGuia(rs.getLong("INT_NumeroGuia"));
		guia.setDtFornecimento(rs.getDate("DAT_Fornecimento"));
		return guia;		
	}
	
	public List<GuiaDeFornecimentoTeste>findAll(){
		
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT * FROM TB_Fornecimento;");
					
			rs = st.executeQuery();
			
			List<GuiaDeFornecimentoTeste> list = new ArrayList<>();
			
			while (rs.next()) {
				GuiaDeFornecimentoTeste guia = instantiateGuiaDeFornecimentoTeste(rs);
				list.add(guia);
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
	
	private Patrimonio instantiatePatrimonio(ResultSet rs, Equipamento equip, Local local) throws SQLException {
		
		Patrimonio pat = new Patrimonio();
		pat.setNumero(rs.getLong("PK_Patrimonio"));
		pat.setMarca(rs.getString("TXT_Marca"));
		pat.setFabricante(rs.getString("TXT_Fabricante"));
		pat.setDescricao(rs.getString("TXT_Descricao"));
		pat.setCondicaoUso(rs.getByte("INT_condicaoUso"));
		pat.setTipEquip(equip);
		pat.setPatrLocal(local);	
		
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
	
	public List<Patrimonio> findAllPat() {
		
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
			
			Map<Integer, Equipamento> map = new HashMap<>();
			
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
