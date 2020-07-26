package model.entities;

import java.io.Serializable;

public class Local implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Integer local;
	private String descricao;
	
	public Local() {
	}

	public Local(Integer local, String descricao) {
		
		this.local = local;
		this.descricao = descricao;
	}

	public Integer getLocal() {
		return local;
	}

	public void setLocal(Integer local) {
		this.local = local;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((local == null) ? 0 : local.hashCode());
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
		if (local == null) {
			if (other.local != null)
				return false;
		} else if (!local.equals(other.local))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Local [local=" + local + ", descricao=" + descricao + "]";
	}

}
