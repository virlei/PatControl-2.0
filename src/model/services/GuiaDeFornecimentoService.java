package model.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import model.dao.DaoFactory;
import model.dao.GuiaDeFornecimentoDao;
import model.entities.GuiaDeFornecimento;



public class GuiaDeFornecimentoService {
	
	public static boolean Insert = false;
	
	private GuiaDeFornecimentoDao dao = DaoFactory.createGuiaDeFornecimentoDao();
	
	public List<GuiaDeFornecimento> findAll() {
		
		//MOCK		
		List<GuiaDeFornecimento> list = new ArrayList<>();
		Date dat = new Date();
		list.add(new GuiaDeFornecimento(1,null, dat, null));		
		return list;
		
		//return dao.findAll();
	}
	
	public GuiaDeFornecimento findById (Long numeroGuia) {
		return dao.findById(numeroGuia);
	}
	
	public void saveOrUpdate(GuiaDeFornecimento obj) {		
		if (Insert) {
			dao.insert(obj);
		}
		else {
			dao.update(obj);
		}
	}
	
	public void remove(GuiaDeFornecimento obj) {
		dao.deleteById(obj.getNrGuia());
	}
}
