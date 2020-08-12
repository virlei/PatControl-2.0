package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.PatrimonioDao;
import model.entities.PatrimonioNovo;

public class PatrimonioService {

	public static boolean Insert = false;
	
	private PatrimonioDao dao = DaoFactory.createPatrimonioDao();
		
	public List<PatrimonioNovo> findAll() {
		return dao.findAll();
	}
	
	public void saveOrUpdate(PatrimonioNovo obj) {
		if (Insert) {
			dao.insert(obj);
		}
		else {
			dao.update(obj);
		}
	}
	
	public void remove(PatrimonioNovo obj) {
		dao.deleteById(obj.getNumero());
	}
}
