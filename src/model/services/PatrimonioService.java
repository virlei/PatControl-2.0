package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.PatrimonioDao;
import model.entities.Patrimonio;

public class PatrimonioService {

	public static boolean Insert = false;
	
	private PatrimonioDao dao = DaoFactory.createPatrimonioDao();
		
	public List<Patrimonio> findAll() {
		return dao.findAll();
	}
	
	public Patrimonio findById (Long NrPatrim) {
		return dao.findById(NrPatrim);
	}
	
	public void saveOrUpdate(Patrimonio obj) {
		if (Insert) {
			dao.insert(obj);
		}
		else {
			dao.update(obj);
		}
	}
	
	public void remove(Patrimonio obj) {
		dao.deleteById(obj.getNumero());
	}
}
