package model.services;

import java.util.ArrayList;
import java.util.List;

import model.dao.DaoFactory;
import model.dao.LocalDao;
import model.entities.Local;

public class LocalService {
	
	private LocalDao dao = DaoFactory.createLocalDao();
	
	public List<Local> findAll(){
		//MOCK
		//List<Local> list = new ArrayList<>();
		//list.add(new Local(11, "Equipamento de teste - camera"));
		//list.add(new Local(12, "Equipamento de teste - PAD Assinatura"));
		//list.add(new Local(13, "Equipamento de teste - Leitor Biometria"));
		//list.add(new Local(14, "Equipamento de teste - CPU"));
		//return list;
		
		return dao.findAll();
	}
	
	public void saveOrUpdate (Local obj) {
		if (obj.getLocal() == null) {
			dao.insert(obj);
		}
		else {
			dao.update(obj);
		}
	}
	
	public void remove(Local obj) {
		dao.deleteById(obj.getLocal());
	}

}
