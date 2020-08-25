package model.entities;

import java.io.Serializable;

public class Patrimonio implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private Long numero;
	private String fabricante;
	private String marca;
	private String descricao;
	private Byte condicaoUso;
	
	private Equipamento TipEquip;
	
	private Local PatrLocal;
	
	public Patrimonio() {
	}

	public Patrimonio(long numero, String fabricante, String marca, String descricao, Byte i,
			Equipamento tipEquip, Local patrLocal) {
		this.numero = numero;
		this.fabricante = fabricante;
		this.marca = marca;
		this.descricao = descricao;
		this.condicaoUso = i;
		TipEquip = tipEquip;
		PatrLocal = patrLocal;
	}

	public Long getNumero() {
		return numero;
	}

	public void setNumero(Long numero) {
		this.numero = numero;
	}

	public String getFabricante() {
		return fabricante;
	}

	public void setFabricante(String fabricante) {
		this.fabricante = fabricante;
	}

	public String getMarca() {
		return marca;
	}

	public void setMarca(String marca) {
		this.marca = marca;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Byte getCondicaoUso() {
		return condicaoUso;
	}

	public void setCondicaoUso(Byte condicaoUso) {
		this.condicaoUso = condicaoUso;
	}

	public Equipamento getTipEquip() {
		return TipEquip;
	}

	public void setTipEquip(Equipamento tipEquip) {
		TipEquip = tipEquip;
	}

	public Local getPatrLocal() {
		return PatrLocal;
	}
	
	public void setPatrLocal(Local patrLocal) {
		PatrLocal = patrLocal;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (numero ^ (numero >>> 32));
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
		Patrimonio other = (Patrimonio) obj;
		if (numero != other.numero)
			return false;
		return true;
	}

	@Override
	public String toString() {
//		return "Patrimonio [numero=" + numero + ", fabricante=" + fabricante + ", marca=" + marca + ", descricao="
//				+ descricao + ", condicaoUso=" + condicaoUso + ", TipEquip=" + TipEquip + ", PatrLocal=" + PatrLocal+ "]";
		return numero + "-" + descricao;
	}

}
