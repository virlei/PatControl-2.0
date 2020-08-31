package model.dao;

import db.DB;
import model.dao.impl.EquipamentoDaoJDBC;
import model.dao.impl.GFornDaoJDBC;
import model.dao.impl.LocalDaoJDBC;
import model.dao.impl.MovimentacaoDaoJDBC;
import model.dao.impl.PatrimonioDaoJDBC;

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
	
}
