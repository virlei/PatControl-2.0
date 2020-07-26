package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import db.DB;
import db.DbException;
import model.dao.MovimentacaoDao;
import model.entities.Movimentacao;
import model.entities.Patrimonio;

public class MovimentacaoDaoJDBC implements MovimentacaoDao {

	private Connection conn;
	
	public MovimentacaoDaoJDBC(Connection conn) {
		this.conn = conn;
	}
	
	public void insert(Movimentacao obj) {
		
	}
	
	public void update(Movimentacao obj) {
		
	}
	
	public void deleteById(Integer id) {
		
	}
	
	public Movimentacao findById(Integer id) {
		
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT TB_MOVIMENTACAO.*, TB_PATRIMONIO.PK_Patrimonio as patrimonio "
					+ "FROM TB_MOVIMENTACAO INNER JOIN TB_PATRIMONIO "
					+ "ON TB_MOVIMENTACAO.PK_Patrimonio = TB_PATRIMONIO.PK_Patrimonio "
					+ "where TB_MOVIMENTACAO.PK_Patrimonio = ?");
			st.setInt(1, id);
			rs = st.executeQuery();
			if (rs.next()) {
				
				Patrimonio patrimonio = new Patrimonio();
				patrimonio.setNumero(rs.getLong("PK_Patrimonio"));
				
				Movimentacao movimentacao = new Movimentacao();
				movimentacao.setDataDevolucao(rs.getString("Dat_DataDevolucao"));
				movimentacao.setDataEntrada(rs.getString("PK_DataEntrada"));
				movimentacao.setNumeroGuia(rs.getInt("INT_NumeroGuia"));
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

	public List<Movimentacao>findAll(){
		return null;
	}
}
