package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.EmprestimoDao;
import model.entities.Emprestimo;

public class EmprestimoService {
	
	private EmprestimoDao dao = DaoFactory.createEmprestimoDao();
	
	public List<Emprestimo> findAll() {	
	
		return dao.findAll();
		
	}
	
	public void saveOrUpdate(Emprestimo obj) {		
		if (obj.getEmprestimo() == null) {
			dao.insert(obj);
		}
		else {
			dao.update(obj);
		}
	}
	
	public void remove(Emprestimo obj) {
		dao.deleteById(obj.getEmprestimo());
	}


}
