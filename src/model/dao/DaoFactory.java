package model.dao;

import db.DB;
import model.dao.impl.*;

public class DaoFactory {

	public static PatrimonioDao createPatrimonioDao() {
		return new PatrimonioDaoJDBC(DB.getConnection());
	}
	
	public static EquipamentoDao createEquipamentoDao() {
		return new EquipamentoDaoJDBC(DB.getConnection());
	}
	
	public static LocalDao createLocalDao() {
		return new LocalDaoJDBC(DB.getConnection());
	}
	
	public static MovimentacaoDao createMovimentacaoDao() {
		return new MovimentacaoDaoJDBC(DB.getConnection());
	}
	
	public static GFornDao createGFornDao() {
		return new GFornDaoJDBC(DB.getConnection());
	}
	
	public static DevolucaoDao createDevolucaoDao() {
		return new DevolucaoDaoJDBC(DB.getConnection());
	}
	
	public static EmprestimoDao createEmprestimoDao() {
		return new EmprestimoDaoJDBC(DB.getConnection());
	}
	
	public static RetornoEmprestimoDao createRetornoEmprestimoDao() {
		return new RetornoEmprestimoDaoJDBC(DB.getConnection());
	}
	
}
