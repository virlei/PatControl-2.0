package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.MovimentacaoDao;
import model.entities.Movimentacao;
import java.util.ArrayList;
import model.entities.Patrimonio;

public class MovimentacaoService {

	public static boolean Insert = false;
	
	private MovimentacaoDao dao = DaoFactory.createMovimentacaoDao();
		
	public List<Movimentacao> findAll() {
			//MOCK			
			List<Movimentacao> list = new ArrayList<>();
			list.add(new Movimentacao(new Patrimonio(), "18/08/2020",1,"19/08/2020"));
				
			return list;
		
		//return dao.findAll();
	}
	
	public void saveOrUpdate(Movimentacao obj) {
		if (Insert) {
			dao.insert(obj);
		}
		else {
			dao.update(obj);
		}
	}
	
	public void remove(Movimentacao obj) {
		dao.deleteById(obj.getNumeroGuia());
	}
}
