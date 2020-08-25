package model.entities;

import java.io.Serializable;

public class GuiaDeFornecimento implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private long numeroGuia;
	private long patrimonio;
	private String dataFornecimento;
	
	public GuiaDeFornecimento(long numeroGuia, long patrimonio, String dataFornecimento) {
		super();
		this.numeroGuia = numeroGuia;
		this.patrimonio = patrimonio;
		this.dataFornecimento = dataFornecimento;
	}

	public GuiaDeFornecimento() {
		
	}

	public String getDataFornecimento() {
		return dataFornecimento;
	}

	public void setDataFornecimento(String dataFornecimento) {
		this.dataFornecimento = dataFornecimento;
	}

	public long getNumeroGuia() {
		return numeroGuia;
	}

	public void setNumeroGuia(long numeroGuia) {
		this.numeroGuia = numeroGuia;
	}

	public long getPatrimonio() {
		return patrimonio;
	}

	public void setPatrimonio(long patrimonio) {
		this.patrimonio = patrimonio;
	}

	@Override
	public String toString() {
		return "GuiaDeFornecimento [numeroGuia=" + numeroGuia + ", patrimonio=" + patrimonio + ", dataFornecimento="
				+ dataFornecimento + "]";
	}

	
	
	

}
