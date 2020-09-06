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
import model.dao.EmprestimoDao;
import model.entities.Emprestimo;

public class EmprestimoDaoJDBC implements EmprestimoDao {
	
private Connection conn;
	
	public EmprestimoDaoJDBC(Connection conn) {
		this.conn = conn;
	}
	
	@Override
	public void insert (Emprestimo obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"INSERT INTO TB_Emprestimo "
					+ "(DAT_Emprestimo, TXT_Setor, TXT_Responsavel) "
					+ "VALUES (?, ?, ?)");
						
			st.setString(1, Utils.parseToString(obj.getDatEmprestimo(), "dd/MM/yyyy"));
			st.setString(2, obj.getSetor());
			st.setString(3, obj.getResponsavel());
			
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
	
	@Override
	public void update(Emprestimo obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
				"UPDATE TB_Emprestimo " +
				"SET DAT_Emprestimo = ?, TXT_Setor = ?, TXT_Responsavel = ? " +		
				"WHERE PK_Emprestimo = ?");

			st.setString(1, Utils.parseToString(obj.getDatEmprestimo(), "dd/MM/yyyy"));
			st.setString(2, obj.getSetor());
			st.setString(3, obj.getResponsavel());
			st.setInt(4, obj.getEmprestimo());

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
				"DELETE FROM TB_Emprestimo WHERE PK_Emprestimo = ?");
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
	
	public Emprestimo findById(Integer id) {
		
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT * FROM TB_Emprestimo where PK_Emprestimo = ?");
			st.setInt(1, id);
			rs = st.executeQuery();
			if (rs.next()) {
				Emprestimo obj = instantiateEmprestimo(rs);
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
	
	private Emprestimo instantiateEmprestimo(ResultSet rs) throws SQLException {
		Emprestimo obj = new Emprestimo();
		obj.setEmprestimo(rs.getInt("PK_Emprestimo"));
		obj.setDatEmprestimo( Utils.tryParseToDate(rs.getString("DAT_Emprestimo")));
		obj.setSetor(rs.getString("TXT_Setor"));
		obj.setResponsavel(rs.getString("TXT_Responsavel"));
		return obj;
	}
	
	public List<Emprestimo>findAll(){
		
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT * FROM TB_Emprestimo");
					
			rs = st.executeQuery();
			
			List<Emprestimo> list = new ArrayList<>();
			
			while (rs.next()) {
				Emprestimo obj = instantiateEmprestimo(rs);
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
