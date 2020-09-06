package model.entities;

import java.io.Serializable;
import java.util.Date;

public class Emprestimo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Integer emprestimo;
	private Date datEmprestimo;
	private String setor;
	private String responsavel;
//	private List<patrimonio> patrimonios;
	
	public Emprestimo() {		
	}

	public Emprestimo(Integer emprestimo, Date datEmprestimo, String setor, String responsavel) {
		super();
		this.emprestimo = emprestimo;
		this.datEmprestimo = datEmprestimo;
		this.setor = setor;
		this.responsavel = responsavel;
	}

	public Integer getEmprestimo() {
		return emprestimo;
	}

	public void setEmprestimo(Integer emprestimo) {
		this.emprestimo = emprestimo;
	}

//	public String getDtEmprestimo() {
//		return dtEmprestimo;
//	}
//
//	public void setDtEmprestimo(String dtEmprestimo) {
//		this.dtEmprestimo = dtEmprestimo;
//	}

	public Date getDatEmprestimo() {
		return datEmprestimo;
	}

	public void setDatEmprestimo(Date datEmprestimo) {
		this.datEmprestimo = datEmprestimo;
	}

	public String getSetor() {
		return setor;
	}

	public void setSetor(String setor) {
		this.setor = setor;
	}

	public String getResponsavel() {
		return responsavel;
	}

	public void setResponsavel(String responsavel) {
		this.responsavel = responsavel;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((emprestimo == null) ? 0 : emprestimo.hashCode());
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
		Emprestimo other = (Emprestimo) obj;
		if (emprestimo == null) {
			if (other.emprestimo != null)
				return false;
		} else if (!emprestimo.equals(other.emprestimo))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return datEmprestimo + " - " + responsavel;
	}

	
}
