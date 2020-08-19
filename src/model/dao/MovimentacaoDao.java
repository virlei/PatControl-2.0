package model.dao;

import java.util.List;

import model.entities.Movimentacao;

public interface MovimentacaoDao {

	void insert (Movimentacao obj);
	void update (Movimentacao obj);
	void deleteById(Long id);
	Movimentacao findById(Long id);
	List<Movimentacao>findAll();
	
}
