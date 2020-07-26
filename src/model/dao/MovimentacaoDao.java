package model.dao;

import java.util.List;

import model.entities.Movimentacao;

public interface MovimentacaoDao {

	void insert (Movimentacao obj);
	void update (Movimentacao obj);
	void deleteById(Integer id);
	Movimentacao findById(Integer id);
	List<Movimentacao>findAll();
	
}
