package model.dao;

import java.util.List;

import model.entities.PatrimonioNovo;

public interface PatrimonioDao {
	
	void insert(PatrimonioNovo obj);
	void update (PatrimonioNovo obj);
	void deleteById (Long id);
	PatrimonioNovo findById(Long id);
	List<PatrimonioNovo> findAll();
	
}
