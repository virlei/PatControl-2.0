package model.dao;

import java.util.List;
import model.entities.Localizacao;

public interface LocalizacaoDao {
	
	void insert (Localizacao obj);
	void update (Localizacao obj);
	void deleteById(Integer id);
	Localizacao findById(Integer id);
	List<Localizacao>findAll();

}
