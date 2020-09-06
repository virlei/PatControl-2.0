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
import model.dao.RetornoEmprestimoDao;
import model.entities.RetornoEmprestimo;

public class RetornoEmprestimoDaoJDBC implements RetornoEmprestimoDao{
	
private Connection conn;
	
	public RetornoEmprestimoDaoJDBC(Connection conn) {
		this.conn = conn;
	}	
	
	public void insert (RetornoEmprestimo obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"INSERT INTO TB_RetornoEmprestimos "
					+ "(DAT_Retorno, TXT_RecebidoPor) "
					+ "VALUES (?, ?)");
						
			st.setString(1, Utils.parseToString(obj.getDtRetornoEmpr(), "dd/MM/yyyy"));
			st.setString(2, obj.getRecebedor());			
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
	
	public void update(RetornoEmprestimo obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
				"UPDATE TB_RetornoEmprestimos " +
				"SET DAT_Retorno = ?, TXT_RecebidoPor = ? " +		
				"WHERE PK_Retorno = ?");

			st.setString(1, Utils.parseToString(obj.getDtRetornoEmpr(), "dd/MM/yyyy"));
			st.setString(2, obj.getRecebedor());
			st.setInt(3, obj.getRetorno());

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
				"DELETE FROM TB_RetornoEmprestimos WHERE PK_Retorno = ?");
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
	
	public RetornoEmprestimo findById(Integer id) {
		
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT * FROM TB_RetornoEmprestimos where PK_Retorno = ?");
			st.setInt(1, id);
			rs = st.executeQuery();
			if (rs.next()) {
				RetornoEmprestimo obj = instantiateRetornoEmprestimo(rs);
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
	
	private RetornoEmprestimo instantiateRetornoEmprestimo(ResultSet rs) throws SQLException {
		RetornoEmprestimo obj = new RetornoEmprestimo();
		obj.setRetorno(rs.getInt("PK_Retorno"));
		obj.setDtRetornoEmpr( Utils.tryParseToDate(rs.getString("DAT_Retorno")));
		obj.setRecebedor(rs.getString("TXT_RecebidoPor"));
		return obj;
	}
	
	public List<RetornoEmprestimo>findAll(){
		
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT * FROM TB_RetornoEmprestimos");
					
			rs = st.executeQuery();
			
			List<RetornoEmprestimo> list = new ArrayList<>();
			
			while (rs.next()) {
				RetornoEmprestimo obj = instantiateRetornoEmprestimo(rs);
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
