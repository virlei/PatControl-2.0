package model.entities;

import java.io.Serializable;

public class Movimentacao implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Patrimonio patrimonio;
	private String dataEntrada;
	private Long numeroGuia;
	private String dataDevolucao;
	
	public Movimentacao(Patrimonio patrimonio, String dataEntrada, long numeroGuia, String dataDevolucao) {
		super();
		this.patrimonio = patrimonio;
		this.dataEntrada = dataEntrada;
		this.numeroGuia = numeroGuia;
		this.dataDevolucao = dataDevolucao;
	}
	
	public Movimentacao() {
		
	}

	public Patrimonio getPatrimonio() {
		return patrimonio;
	}

	public void setPatrimonio(Patrimonio patrimonio) {
		this.patrimonio = patrimonio;
	}

	public String getDataEntrada() {
		return dataEntrada;
	}

	public void setDataEntrada(String dataEntrada) {
		this.dataEntrada = dataEntrada;
	}

	public Long getNumeroGuia() {
		return numeroGuia;
	}

	public void setNumeroGuia(Long numeroGuia) {
		this.numeroGuia = numeroGuia;
	}

	public String getDataDevolucao() {
		return dataDevolucao;
	}

	public void setDataDevolucao(String dataDevolucao) {
		this.dataDevolucao = dataDevolucao;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dataEntrada == null) ? 0 : dataEntrada.hashCode());
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
		Movimentacao other = (Movimentacao) obj;
		if (dataEntrada == null) {
			if (other.dataEntrada != null)
				return false;
		} else if (!dataEntrada.equals(other.dataEntrada))
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
		return "Movimentacao [patrimonio=" + patrimonio + ", dataEntrada=" + dataEntrada + ", numeroGuia=" + numeroGuia
				+ ", dataDevolucao=" + dataDevolucao + "]";
	}	
	
}
