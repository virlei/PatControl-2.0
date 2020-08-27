package model.services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import model.dao.DaoFactory;
import model.dao.GuiaDeFornecimentoDao;
import model.dao.GuiaDeFornecimentoTesteDao;
import model.dao.LocalDao;
import model.entities.GuiaDeFornecimento;
import model.entities.GuiaDeFornecimentoTeste;
import model.entities.Local;
import model.entities.Patrimonio;

public class GuiaDeFornecimentoTesteService {
	
	public static boolean Insert = false;
	
	private GuiaDeFornecimentoTesteDao dao = (GuiaDeFornecimentoTesteDao) DaoFactory.createGuiaDeFornecimentoDao();
	
	public List<GuiaDeFornecimentoTeste> findAll() {
		
		//MOCK		
		List<GuiaDeFornecimentoTeste> list = new ArrayList<>();
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
		list.add(new GuiaDeFornecimentoTeste(1,(long) 10000, dat,null));	
		list.add(new GuiaDeFornecimentoTeste(1, (long) 881000, dat1, null));		
		list.add(new GuiaDeFornecimentoTeste(1, (long) 981000, dat2, null));		
		return list;
		
		//return dao.findAll();
	}
	
	public GuiaDeFornecimentoTeste findById (Long numeroGuia) {
		return dao.findById(numeroGuia);
	}
	
	public void saveOrUpdate(GuiaDeFornecimentoTeste obj) {		
		if (Insert) {
			dao.insert(obj);
		}
		else {
			dao.update(obj);
		}
	}
	
	public void remove(GuiaDeFornecimentoTeste obj) {
		dao.deleteById(obj.getNrGuia());
	}
}
