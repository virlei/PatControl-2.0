package model.dao;

import java.util.List;

import model.entities.Patrimonio;

public interface PatrimonioDao {
	
	void insert(Patrimonio obj);
	void update (Patrimonio obj);
	void deleteById (Long id);
	Patrimonio findById(Long id);
	List<Patrimonio> findAll();
	
}
