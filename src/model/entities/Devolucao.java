package model.entities;

import java.io.Serializable;
import java.util.Date;

public class Devolucao implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Integer devolucao;
	private Date dtDevolucao;
	private String numSei;
	private String motivo;
//	private List<Patrimonio> patrimonios;	
	
	public Devolucao() {		
	}

	public Devolucao(Integer devolucao, Date dtDevolucao, String numSei, String motivo) {
		super();
		this.devolucao = devolucao;
		//this.datDevolucao = datDevolucao;
		this.dtDevolucao = dtDevolucao;
		this.numSei = numSei;
		this.motivo = motivo;
	}

	public Integer getDevolucao() {
		return devolucao;
	}

	public void setDevolucao(Integer devolucao) {
		this.devolucao = devolucao;
	}

	
	public String getNumSei() {
		return numSei;
	}

	public Date getDtDevolucao() {
		return dtDevolucao;
	}

	public void setDtDevolucao(Date dtDevolucao) {
		this.dtDevolucao = dtDevolucao;
	}

	public void setNumSei(String numSei) {
		this.numSei = numSei;
	}

	public String getMotivo() {
		return motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((devolucao == null) ? 0 : devolucao.hashCode());
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
		Devolucao other = (Devolucao) obj;
		if (devolucao == null) {
			if (other.devolucao != null)
				return false;
		} else if (!devolucao.equals(other.devolucao))
			return false;
		return true;
	}

	@Override
	public String toString() {
//		return "Devolucao [devolucao=" + devolucao + ", datDevolucao=" + datDevolucao + ", numSei=" + numSei
//				+ ", motivo=" + motivo + "]";
		return numSei + " - " + dtDevolucao;
	}
	
	

}
