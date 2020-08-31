package model.entities;

import java.io.Serializable;

public class GForn implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private Integer pkGForn;
	private Integer nrGuia;
	private String dtForn;
//	private List<Patrimonio> patrimonios;
	
	public GForn() {
	}

	public GForn(Integer pkGForn, Integer nrGuia, String dtForn) {
		this.pkGForn = pkGForn;
		this.nrGuia = nrGuia;
		this.dtForn = dtForn;
	}


	public Integer getPkGForn() {
		return pkGForn;
	}

	public void setPkGForn(Integer pkGForn) {
		this.pkGForn = pkGForn;
	}

	public Integer getNrGuia() {
		return nrGuia;
	}

	public void setNrGuia(Integer nrGuia) {
		this.nrGuia = nrGuia;
	}

	public String getDtForn() {
		return dtForn;
	}

	public void setDtForn(String dtForn) {
		this.dtForn = dtForn;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((pkGForn == null) ? 0 : pkGForn.hashCode());
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
		GForn other = (GForn) obj;
		if (pkGForn == null) {
			if (other.pkGForn != null)
				return false;
		} else if (!pkGForn.equals(other.pkGForn))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "GForn [nrGuia=" + nrGuia + ", dtForn=" + dtForn + "]";
	}

}
