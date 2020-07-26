package model.entities;

import java.io.Serializable;

public class Localizacao implements Serializable {	
	private static final long serialVersionUID = 1L;
	
	private String dataMovimentacao;	
	private Patrimonio patrimonio;
	private Local local;


public Localizacao() {
}

public Localizacao(String dataMovimentacao, Patrimonio patrimonio, Local local) {
	
	this.dataMovimentacao = dataMovimentacao;
	this.patrimonio = patrimonio;
	this.local = local;
}

public String getDataMovimentacao() {
	return dataMovimentacao;
}

public void setDataMovimentacao(String dataMovimentacao) {
	this.dataMovimentacao = dataMovimentacao;
}

public Patrimonio getPatrimonio() {
	return patrimonio;
}

public void setPatrimonio(Patrimonio patrimonio) {
	this.patrimonio = patrimonio;
}

public Local getLocal() {
	return local;
}

public void setLocal(Local local) {
	this.local = local;
}

@Override
public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((dataMovimentacao == null) ? 0 : dataMovimentacao.hashCode());
	result = prime * result + ((local == null) ? 0 : local.hashCode());
	result = prime * result + ((patrimonio == null) ? 0 : patrimonio.hashCode());
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
	Localizacao other = (Localizacao) obj;
	if (dataMovimentacao == null) {
		if (other.dataMovimentacao != null)
			return false;
	} else if (!dataMovimentacao.equals(other.dataMovimentacao))
		return false;
	if (local == null) {
		if (other.local != null)
			return false;
	} else if (!local.equals(other.local))
		return false;
	if (patrimonio == null) {
		if (other.patrimonio != null)
			return false;
	} else if (!patrimonio.equals(other.patrimonio))
		return false;
	return true;
}

@Override
public String toString() {
	return "Localizacao [dataMovimentacao=" + dataMovimentacao + ", patrimonio=" + patrimonio + ", local=" + local
			+ "]";
}

}



