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
import model.dao.MovimentacaoDao;
import model.entities.Equipamento;
import model.entities.Local;
import model.entities.Movimentacao;
import model.entities.Patrimonio;

public class MovimentacaoDaoJDBC implements MovimentacaoDao {

	private Connection conn;
	
	public MovimentacaoDaoJDBC(Connection conn) {
		this.conn = conn;
	}
	
	public void insert(Movimentacao obj) {
		
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"INSERT INTO TB_MOVIMENTACAO "
					+ "(PK_Patrimonio, PK_DataEntrada, INT_NumeroGuia, DAT_DataDevolucao)"
					+ "Values (?,?,?,?)");
			st.setLong(1, obj.getPatrimonio().getNumero());
			st.setString(2, obj.getDataEntrada());
			st.setLong(3, obj.getNumeroGuia());
			st.setString(4, obj.getDataDevolucao());
			
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
	
	public void update(Movimentacao obj) {
		
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"UPDATE TB_MOVIMENTACAO "
					+ "SET Patrimonio=?, PK_DataEntrada=?, DAT_DataDevolucao=? "
					+ "WHERE INT_NumeroGuia = ?");
			
			//st.setLong(1, obj.getNumero());
			st.setLong(1, obj.getPatrimonio().getNumero());
			st.setString(2, obj.getDataEntrada());
			st.setString(3, obj.getDataDevolucao());
			st.setLong(4, obj.getNumeroGuia());
			
			st.executeUpdate();
			
		}
		catch (SQLException e) {
			throw new DbException (e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
		
	}
	
	public void deleteById(Long numeroGuia) {
		
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("DELETE FROM TB_MOVIMENTACAO WHERE INT_NumeroGuia = ?");
			st.setLong(1, numeroGuia);
			st.executeUpdate();
		}
		catch (SQLException e) {
			throw new DbException (e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
		
	}
	
	public Movimentacao findById(Long id) {
		
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT TB_MOVIMENTACAO.*, TB_PATRIMONIO.PK_Patrimonio as patrimonio "
					+ "FROM TB_MOVIMENTACAO INNER JOIN TB_PATRIMONIO "
					+ "ON TB_MOVIMENTACAO.PK_Patrimonio = TB_PATRIMONIO.PK_Patrimonio "
					+ "where TB_MOVIMENTACAO.PK_Patrimonio = ?");
			st.setLong(1, id);
			rs = st.executeQuery();
			if (rs.next()) {
				
				Patrimonio patrimonio = new Patrimonio();
				patrimonio.setNumero(rs.getLong("PK_Patrimonio"));
				
				Movimentacao movimentacao = new Movimentacao();
				movimentacao.setDataDevolucao(rs.getString("Dat_DataDevolucao"));
				movimentacao.setDataEntrada(rs.getString("PK_DataEntrada"));
				movimentacao.setNumeroGuia(rs.getLong("INT_NumeroGuia"));
				movimentacao.setPatrimonio(patrimonio);
				return movimentacao;				
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
	
	private Movimentacao instantiateMovimentacao(ResultSet rs, Patrimonio patrimonio) throws SQLException{
		
		Movimentacao movimentacao = new Movimentacao();
		movimentacao.setPatrimonio(patrimonio);
		movimentacao.setDataEntrada(rs.getString("PK_DataEntrada"));
		movimentacao.setNumeroGuia(rs.getLong("INT_NumeroGuia"));
		movimentacao.setDataDevolucao(rs.getString("DAT_DataDevolucao"));
		
		return movimentacao;
	}
	
	private Patrimonio instantiatePatrimonio(ResultSet rs, Equipamento equip, Local local) throws SQLException {
		
		Patrimonio pat = new Patrimonio();
		pat.setNumero(rs.getLong("NrPatrim"));
		pat.setMarca(rs.getString("TXT_Marca"));
		pat.setFabricante(rs.getString("TXT_Fabricante"));
		pat.setDescricao(rs.getString("DescrPatrim"));
		pat.setCondicaoUso(rs.getByte("INT_condicaoUso"));
		pat.setTipEquip(equip);
		pat.setPatrLocal(local);
		return pat;
	}
	
	private Equipamento instantiateEquipamento(ResultSet rs) throws SQLException {

		Equipamento equip = new Equipamento();
		equip.setId(rs.getInt("idEquip"));
		equip.setDescricao(rs.getString("DescrEquip"));
		return equip;
	}

	private Local instantiateLocal(ResultSet rs) throws SQLException {
		
		Local local = new Local();
		local.setIdLocal(rs.getInt("idLocal"));
		local.setDescricaoLocal(rs.getString("DescrLocal"));
		return local;
		
	}
	
	public List<Movimentacao> findAll(){		
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
				"SELECT "
				+ "	   TB_MOVIMENTACAO.PK_PATRIMONIO as NrPatrim, "
				+ "	   TB_PATRIMONIO.FK_EQUIPAMENTO as idEquip, " 
				+ "   TB_EQUIPAMENTO.TXT_DESCRICAO as DescrEquip, "
				+ "	   TB_PATRIMONIO.TXT_FABRICANTE, "
				+ "	   TB_PATRIMONIO.TXT_MARCA, "
				+ "	   TB_PATRIMONIO.TXT_DESCRICAO as DescrPatrim, "
				+ "	   TB_PATRIMONIO.INT_CONDICAOUSO, "
				+ "	   TB_PATRIMONIO.FK_LOCAL as idLocal, "
				+ "	   TB_LOCAL.TXT_DESCRICAO as DescrLocal, "
				+ "	   TB_MOVIMENTACAO.PK_DataEntrada, "
				+ "	   TB_MOVIMENTACAO.INT_NumeroGuia, "
				+ "	   TB_MOVIMENTACAO.DAT_DataDevolucao "
				+ "	FROM "
				+ "	   TB_MOVIMENTACAO, " 
				+ "	   TB_PATRIMONIO, "
				+ "	   TB_EQUIPAMENTO, "
				+ "	   TB_LOCAL "
				+ "	WHERE "
				+ "	   (TB_MOVIMENTACAO.PK_PATRIMONIO = TB_PATRIMONIO.PK_Patrimonio) AND "
				+ "	   (TB_PATRIMONIO.FK_EQUIPAMENTO = TB_EQUIPAMENTO.PK_EQUIPAMENTO) AND "
				+ "	   (TB_PATRIMONIO.FK_LOCAL = TB_LOCAL.PK_LOCAL) ");
			rs = st.executeQuery();
			
			List<Movimentacao> list = new ArrayList<>();
			Map<Long, Patrimonio> map = new HashMap<>();
			Map<Integer, Equipamento> mapEquip = new HashMap<>();
			Map<Integer, Local> mapLocal = new HashMap<>();		
				
			while (rs.next()) {
				Patrimonio pat = map.get(rs.getLong("NrPatrim"));
				Equipamento equip = mapEquip.get(rs.getInt("idEquip"));				
				Local local = mapLocal.get(rs.getInt("idLocal"));				
				
				if (equip == null) {
					equip = instantiateEquipamento(rs);
					mapEquip.put(rs.getInt("idEquip"), equip);
				}
				
				if (local == null) {
					local = instantiateLocal(rs);
					mapLocal.put(rs.getInt("idLocal"),local);
				}
				
				if(pat == null) {
					pat = instantiatePatrimonio(rs, equip, local);
					map.put(rs.getLong("NrPatrim"), pat);
				}
				
				Movimentacao movimentacao = instantiateMovimentacao(rs, pat);
				list.add(movimentacao);
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
