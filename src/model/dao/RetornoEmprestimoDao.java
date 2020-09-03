package model.dao;

import java.util.List;

import model.entities.RetornoEmprestimo;

public interface RetornoEmprestimoDao {
	
	void insert(RetornoEmprestimo obj);
	void update (RetornoEmprestimo obj);
	void deleteById (Integer id);
	RetornoEmprestimo findById(Integer id);
	List<RetornoEmprestimo> findAll();

}
