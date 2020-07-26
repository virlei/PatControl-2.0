package model.dao;

import java.util.List;

import model.entities.Patrimonio;

public interface PatrimonioDao {
	
	void insert(Patrimonio obj);
	void update (Patrimonio obj);
	void deleteById (long id);
	Patrimonio findById(long id);
	List<Patrimonio> findAll();
	
}
