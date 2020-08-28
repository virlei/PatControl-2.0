package model.dao.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
import model.entities.Equipamento;
import model.entities.GuiaDeFornecimento;
import model.entities.Local;
import model.entities.Patrimonio;
import model.dao.GuiaDeFornecimentoDao;


public class GuiaDeFornecimentoDaoJDBC implements GuiaDeFornecimentoDao {

	private Connection conn;
	
	public GuiaDeFornecimentoDaoJDBC(Connection conn) {
		this.conn = conn;
	}
	
	public void insert (GuiaDeFornecimento obj) {
		
		PreparedStatement st = null;
		try {		
			st = conn.prepareStatement("INSERT INTO TB_FORNECIMENTO "
					+ "(INT_NumeroGuia, DAT_Fornecimento) "
					+ "Values(?,?)");
			st.setLong(1, obj.getNrGuia());
			st.setDate(2, (Date) obj.getDtFornecimento()); 
			
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
	
	public void update (GuiaDeFornecimento obj) {
		
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"UPDATE TB_FORNECIMENTO "
					+ "SET INT_NumeroGuia=?, DAT_Fornecimento=? "
					+ "WHERE PK_Guia = ?");
		
			st.setLong(1, obj.getNrGuia());
			st.setDate(2, (Date) obj.getDtFornecimento());
			
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
			st = conn.prepareStatement("DELETE FROM TB_FORNECIMENTO WHERE PK_Guia = ?");
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
	public GuiaDeFornecimento findById(Long id) {
		
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT * FROM TB_FORNECIMENTO where PK_Guia = ?");
			st.setLong(1, id);
			rs = st.executeQuery();
			if (rs.next()) {				
				GuiaDeFornecimento guiaDeFornecimento = new GuiaDeFornecimento();
				guiaDeFornecimento.setNrGuia(rs.getLong("INT_NumeroGuia"));
				guiaDeFornecimento.setDtFornecimento(rs.getDate("DAT_Fornecimento"));
				return guiaDeFornecimento;				
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

	private GuiaDeFornecimento instantiateGuiaDeFornecimento(ResultSet rs) throws SQLException {
		
		GuiaDeFornecimento guia = new GuiaDeFornecimento();
		guia.setNrGuia(rs.getLong("INT_NumeroGuia"));
		//guia.setDtFornecimento(rs.getDate("DAT_Fornecimento"));
		guia.setDtFornecimento(new java.util.Date(rs.getTimestamp("DAT_Fornecimento").getTime()));
		
		return guia;
	}
	
	public List<GuiaDeFornecimento> findAll() {
		
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT * FROM TB_Fornecimento;");
					
			rs = st.executeQuery();
			
			List<GuiaDeFornecimento> list = new ArrayList<>();
			
			while (rs.next()) {
				GuiaDeFornecimento guia =  instantiateGuiaDeFornecimento(rs);
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
	
	
	@Override
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

