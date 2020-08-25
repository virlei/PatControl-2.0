package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.MovimentacaoDao;
import model.entities.Movimentacao;

public class MovimentacaoService {

	public static boolean Insert = false;
	
	private MovimentacaoDao dao = DaoFactory.createMovimentacaoDao();
		
	public List<Movimentacao> findAll() {
			//MOCK			
			//List<Movimentacao> list = new ArrayList<>();
			//list.add(new Movimentacao(new Patrimonio((long) 1,"Fabricante F", "Marca M", "Descricao D", (byte) 2, new Equipamento(1, "Equipamento E"), new Local(3, "Local L") ), "18/08/2020", (long) 178530, "19/08/2020"));
			//return list;
		
			return dao.findAll();
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
