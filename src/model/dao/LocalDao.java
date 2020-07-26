package model.dao;

import java.util.List;
import model.entities.Local;

public interface LocalDao {
	
	void insert(Local obj);
	void update(Local obj);
	void deleteById(Integer id);
	Local findById(Integer id);
	List<Local> findAll();	

}
