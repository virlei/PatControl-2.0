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
	
	public static LocalizacaoDao createLocalizacaoDao() {
		return new LocalizacaoDaoJDBC(DB.getConnection());
	}
	
	public static MovimentacaoDao createMovimentacaoDao() {
		return new MovimentacaoDaoJDBC(DB.getConnection());
	}
}
