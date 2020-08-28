package model.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class GuiaDeFornecimento implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private Integer pKey;
	private Long nrGuia;
	private Date dtFornecimento;
	private List<Patrimonio> patrimonios;
	
	public GuiaDeFornecimento() {
}

	public GuiaDeFornecimento(Integer pKey, Long nrGuia, Date dtFornecimento, List<Patrimonio> patrimonios) {
		super();
		this.pKey = pKey;
		this.nrGuia = nrGuia;
		this.dtFornecimento = dtFornecimento;
		this.patrimonios = patrimonios;
	}

	@Override
	public String toString() {
		return "GuiaDeFornecimento [pKey=" + pKey + ", nrGuia=" + nrGuia + ", dtFornecimento=" + dtFornecimento
				+ ", patrimonios=" + patrimonios + "]";
	}

	public Integer getpKey() {
		return pKey;
	}

	public void setpKey(Integer pKey) {
		this.pKey = pKey;
	}

	public Long getNrGuia() {
		return nrGuia;
	}

	public void setNrGuia(Long nrGuia) {
		this.nrGuia = nrGuia;
	}

	public Date getDtFornecimento() {
		return dtFornecimento;
	}

	public void setDtFornecimento(Date dtFornecimento) {
		this.dtFornecimento = dtFornecimento;
	}

	public List<Patrimonio> getPatrimonios() {
		return patrimonios;
	}

	public void setPatrimonios(List<Patrimonio> patrimonios) {
		this.patrimonios = patrimonios;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((pKey == null) ? 0 : pKey.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GuiaDeFornecimento other = (GuiaDeFornecimento) obj;
		if (pKey == null) {
			if (other.pKey != null)
				return false;
		} else if (!pKey.equals(other.pKey))
			return false;
		return true;
	}
	
	
	
	
}