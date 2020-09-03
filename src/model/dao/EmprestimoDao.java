package model.dao;

import java.util.List;

import model.entities.Emprestimo;

public interface EmprestimoDao {

	void insert(Emprestimo obj);
	void update (Emprestimo obj);
	void deleteById (Integer id);
	Emprestimo findById(Integer id);
	List<Emprestimo> findAll();
}
