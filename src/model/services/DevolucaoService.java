package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.DevolucaoDao;
import model.entities.Devolucao;

public class DevolucaoService {
	
	public static boolean Insert = false;
	
	private DevolucaoDao dao = DaoFactory.createDevolucaoDao();
	
	public List<Devolucao> findAll() {	
	
		return dao.findAll();
		
	}
	
	public void saveOrUpdate(Devolucao obj) {		
		if (Insert) {
			dao.insert(obj);
		}
		else {
			dao.update(obj);
		}
	}
	
	public void remove(Devolucao obj) {
		dao.deleteById(obj.getDevolucao());
	}

}
