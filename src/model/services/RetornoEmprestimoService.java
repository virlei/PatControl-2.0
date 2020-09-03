package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.RetornoEmprestimoDao;
import model.entities.RetornoEmprestimo;

public class RetornoEmprestimoService {
	
private RetornoEmprestimoDao dao = DaoFactory.createRetornoEmprestimoDao();
	
	public List<RetornoEmprestimo> findAll() {	
	
		return dao.findAll();
		
	}
	
	public void saveOrUpdate(RetornoEmprestimo obj) {		
		if (obj.getRetorno() == null) {
			dao.insert(obj);
		}
		else {
			dao.update(obj);
		}
	}
	
	public void remove(RetornoEmprestimo obj) {
		dao.deleteById(obj.getRetorno());
	}

}
