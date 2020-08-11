package model.entities;

import java.io.Serializable;

public class Local implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Integer idLocal;
	private String descricaoLocal;
	
	public Local() {
	}

	public Local(Integer idLocal, String descricaoLocal) {
		this.idLocal = idLocal;
		this.descricaoLocal = descricaoLocal;
	}

	public Integer getIdLocal() {
		return idLocal;
	}

	public void setIdLocal(Integer idLocal) {
		this.idLocal = idLocal;
	}

	public String getDescricaoLocal() {
		return descricaoLocal;
	}

	public void setDescricaoLocal(String descricaoLocal) {
		this.descricaoLocal = descricaoLocal;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idLocal == null) ? 0 : idLocal.hashCode());
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
		Local other = (Local) obj;
		if (idLocal == null) {
			if (other.idLocal != null)
				return false;
		} else if (!idLocal.equals(other.idLocal))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return this.descricaoLocal;
	}
}
