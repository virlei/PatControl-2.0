package model.services;

import java.util.List;
import java.util.ArrayList;

import model.dao.DaoFactory;
import model.dao.EquipamentoDao;
import model.entities.Equipamento;

public class EquipamentoService {

	private EquipamentoDao dao = DaoFactory.createEquipamentoDao();
	
	public List<Equipamento> findAll() {
		
		//MOCK
		List<Equipamento> list = new ArrayList<>();
		list.add(new Equipamento(11, "Equipamento de teste - camera"));
		list.add(new Equipamento(12, "Equipamento de teste - PAD Assinatura"));
		list.add(new Equipamento(13, "Equipamento de teste - Leitor Biometria"));
		list.add(new Equipamento(14, "Equipamento de teste - CPU"));
		return list;
		//return dao.findAll();
	}
	
	public void saveOrUpdate(Equipamento obj) {
		if (obj.getId() == null) {
			dao.insert(obj);
		}
		else {
			dao.update(obj);
		}
	}
	
	public void remove(Equipamento obj) {
		dao.deleteById(obj.getId());
	}
}
