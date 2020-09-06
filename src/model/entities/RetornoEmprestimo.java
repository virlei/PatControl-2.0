package model.entities;

import java.io.Serializable;
import java.util.Date;

public class RetornoEmprestimo implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private Integer retorno;
	//private String dtRetorno;
	private Date dtRetornoEmpr;
	private String recebedor;
	
	public RetornoEmprestimo() {
}

	public RetornoEmprestimo(Integer retorno, Date dtRetornoEmpr, String recebedor) {
		super();
		this.retorno = retorno;
		this.dtRetornoEmpr = dtRetornoEmpr;
		this.recebedor = recebedor;
	}

	public Integer getRetorno() {
		return retorno;
	}

	public void setRetorno(Integer retorno) {
		this.retorno = retorno;
	}

	public Date getDtRetornoEmpr() {
		return dtRetornoEmpr;
	}

	public void setDtRetornoEmpr(Date dtRetornoEmpr) {
		this.dtRetornoEmpr = dtRetornoEmpr;
	}

	public String getRecebedor() {
		return recebedor;
	}

	public void setRecebedor(String recebedor) {
		this.recebedor = recebedor;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((retorno == null) ? 0 : retorno.hashCode());
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
		RetornoEmprestimo other = (RetornoEmprestimo) obj;
		if (retorno == null) {
			if (other.retorno != null)
				return false;
		} else if (!retorno.equals(other.retorno))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return dtRetornoEmpr + " - "+ recebedor;
	}
	
}
