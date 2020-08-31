package model.dao;

import java.util.List;

import model.entities.GForn;

public interface GFornDao {
	
	void insert(GForn obj);
	void update (GForn obj);
	void deleteById (Integer id);
	GForn findById(Integer id);
	List<GForn> findAll();
	
}
