package model.dao;

import java.util.List;
import model.entities.GuiaDeFornecimento;
import model.entities.Patrimonio;

public interface GuiaDeFornecimentoDao {

	void insert(GuiaDeFornecimento obj);
	void update(GuiaDeFornecimento obj);
	void deleteById(Long id);
	GuiaDeFornecimento findById(Long id);
	List<GuiaDeFornecimento>findAll();
	List<Patrimonio>findAllPat();
	
}
