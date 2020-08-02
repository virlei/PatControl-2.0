package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.PatrimonioDao;
import model.entities.Patrimonio;

public class PatrimonioService {

	private PatrimonioDao dao = DaoFactory.createPatrimonioDao();
	
	public List<Patrimonio> findAll() {
		return dao.findAll();
	}
	
	public void saveOrUpdate(Patrimonio obj) {
		if (obj.getNumero() == null) {
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
