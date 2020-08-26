package model.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class GuiaDeFornecimento implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Integer pKey; //Chave primária. Transparente ao usuário.
	private Long nrGuia;
	private Date dtFornecimento;
	private List<Patrimonio> patrimonios;
	
	public GuiaDeFornecimento(Integer pKey, Long nrGuia, Date dtFornecimento, List<Patrimonio> patrimonios) {
		super();

		this.pKey = pKey;
		this.nrGuia = nrGuia;
		this.dtFornecimento = dtFornecimento;
		this.patrimonios = patrimonios;
	}

	public GuiaDeFornecimento() {
		
	}

	public Integer getPKey() {
		return pKey;
	}

	public void setPKey(Integer pKey) {
		this.pKey = pKey;
	}

	public Date getDtFornecimento() {
		return dtFornecimento;
	}

	public void setDtFornecimento(Date dtFornecimento) {
		this.dtFornecimento = dtFornecimento;
	}

	public Long getNrGuia() {
		return nrGuia;
	}

	public void setNrGuia(Long nrGuia) {
		this.nrGuia = nrGuia;
	}

	public List<Patrimonio> getPatrimonios() {
		return patrimonios;
	}

	public void setPatrimonios(List<Patrimonio> patrimonios) {
		this.patrimonios = patrimonios;
	}

	@Override
	public String toString() {
		return nrGuia + " em " + dtFornecimento;
	}

	
	
	

}
