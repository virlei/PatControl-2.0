package model.services;

import model.dao.DaoFactory;
import model.dao.MovimentacaoDao;
import model.entities.Movimentacao;
import java.util.List;

public class MovimentacaoService {
	
	private MovimentacaoDao dao = DaoFactory.createMovimentacaoDao();
	
	public List <Movimentacao> findAll(){
		return dao.findAll();
	}
	
	public void saveOrUpdate(Movimentacao obj ) {
		if(obj.getNumeroGuia() == null) {
			dao.insert(obj);
		}else {
			dao.update(obj);
		}
	}
	
	public void remove(Movimentacao obj) {
		dao.deleteById(obj.getNumeroGuia());
	}
	

}
