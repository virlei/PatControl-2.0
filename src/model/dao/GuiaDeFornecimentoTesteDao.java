package model.dao;

import java.util.List;

import model.entities.GuiaDeFornecimentoTeste;
import model.entities.Patrimonio;

public interface GuiaDeFornecimentoTesteDao {
	
	void insert(GuiaDeFornecimentoTeste obj);
	void update(GuiaDeFornecimentoTeste obj);
	void deleteById(Long id);
	GuiaDeFornecimentoTeste findById(Long id);
	List<GuiaDeFornecimentoTeste> findAll();	
	List<Patrimonio> findAllPat();
	

}
