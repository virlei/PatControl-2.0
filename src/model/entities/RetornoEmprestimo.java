package model.entities;

import java.io.Serializable;

public class RetornoEmprestimo implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private Integer retorno;
	private String dtRetorno;
	private String recebedor;
	
	public RetornoEmprestimo() {
}

	public RetornoEmprestimo(Integer retorno, String dtRetorno, String recebedor) {
		super();
		this.retorno = retorno;
		this.dtRetorno = dtRetorno;
		this.recebedor = recebedor;
	}

	public Integer getRetorno() {
		return retorno;
	}

	public void setRetorno(Integer retorno) {
		this.retorno = retorno;
	}

	public String getDtRetorno() {
		return dtRetorno;
	}

	public void setDtRetorno(String dtRetorno) {
		this.dtRetorno = dtRetorno;
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
		return "RetornoEmprestimo [retorno=" + retorno + ", dtRetorno=" + dtRetorno + ", recebedor=" + recebedor + "]";
	}
	
}
