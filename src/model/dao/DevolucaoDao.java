package model.dao;

import java.util.List;
import model.entities.Devolucao;

public interface DevolucaoDao {
	
	void insert(Devolucao obj);
	void update (Devolucao obj);
	void deleteById (Integer id);
	Devolucao findById(Integer id);
	List<Devolucao> findAll();

}
