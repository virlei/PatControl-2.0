package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import db.DB;
import db.DbException;
import model.dao.LocalizacaoDao;
import model.entities.*;

public class LocalizacaoDaoJDBC implements LocalizacaoDao {
	
	Equipamento equipamento = new Equipamento();	
	
	private Connection conn;
	
	public LocalizacaoDaoJDBC(Connection conn) {
		this.conn = conn;
		
	}
	
	public void insert(Localizacao obj) {
		
	}
	
	public void update(Localizacao obj) {
		
	}
	
	public void deleteById(Integer id) {
		
	}
	
	public Localizacao findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = conn.prepareStatement(
					"SELECT * FROM TB_LOCALIZACAO"
					+ "INNER JOIN TB_PATRIMONIO ON TB_PATRIMONIO.PK_Patrimonio = TB_LOCALIZACAO.PK_Patrimonio"
					+ "INNER JOIN TB_LOCAL ON TB_LOCAL.PK_Local = TB_LOCALIZACAO.PK_Local");
			st.setInt(1, id);
			rs = st.executeQuery();
			if(rs.next()) {				
				Patrimonio patrimonio = new Patrimonio();
				patrimonio.setNumero(rs.getLong("PK_Patrimonio"));	
				
				Local local = new Local();
				local.setLocal(rs.getInt("PK_Local"));
				
				Localizacao localizacao = new Localizacao();
				localizacao.setPatrimonio(patrimonio);
				localizacao.setLocal(local);
				localizacao.setDataMovimentacao(rs.getString("PK_DataMovimentacao"));
				return localizacao;
				
			}
			return null;		
		}catch (SQLException e) {
			throw new DbException (e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	
	}
	
	public List<Localizacao> findAll(){
		return null;
	}

}
