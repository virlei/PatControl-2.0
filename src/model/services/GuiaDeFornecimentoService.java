package model.services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date dat = new Date();
		Date dat1 = new Date();
		Date dat2 = new Date();
		try {
			dat = sdf.parse("10/02/2020");
			dat = sdf.parse("20/03/2020");
			dat = sdf.parse("30/04/2020");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		list.add(new GuiaDeFornecimento(1, (long) 781000, dat, null));			
		list.add(new GuiaDeFornecimento(1, (long) 881000, dat1, null));		
		list.add(new GuiaDeFornecimento(1, (long) 981000, dat2, null));	
		System.out.println(list);
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
