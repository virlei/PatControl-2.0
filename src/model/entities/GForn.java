package model.entities;

import java.io.Serializable;
import java.util.Date;

public class GForn implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private Integer pkGForn;
	private Integer nrGuia;
	private Date dtGForn;
//	private List<Patrimonio> patrimonios;
	
	public GForn() {
	}

	public GForn(Integer pkGForn, Integer nrGuia, String dtForn, Date dtGForn) {
		this.pkGForn = pkGForn;
		this.nrGuia = nrGuia;
		this.dtGForn = dtGForn;
	}


	public Date getDtGForn() {
		return dtGForn;
	}

	public void setDtGForn(Date dtGForn) {
		this.dtGForn = dtGForn;
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
		return nrGuia + "-" + dtGForn;
	}

}
