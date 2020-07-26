package gui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;

public class MainViewController implements Initializable {

	@FXML
	private MenuItem menuItemPatrimonio;
	@FXML
	private MenuItem menuItemTipoEquipamento;
	@FXML
	private MenuItem menuItemLocal;
	@FXML
	private MenuItem menuItemLocalizacao;
	@FXML
	private MenuItem menuItemMovimentacao;
	@FXML
	private MenuItem menuItemRelPatrXLocal;
	@FXML
	private MenuItem menuItemAjuda;

	@FXML
	public void onMenuItemPatrimonioAction() {
		System.out.println("onMenuItemPatrimonioAction");
	}
	
	@FXML
	public void onMenuItemTipoEquipamentoAction() {
		System.out.println("onMenuItemTipoEquipamentoAction");
	}
	
	@FXML
	public void onMenuItemLocalAction() {
		System.out.println("onMenuItemLocalAction");
	}
	
	@FXML
	public void onMenuItemLocalizacaoAction() {
		System.out.println("onMenuItemLocalizacaoAction");
	}

	@FXML
	public void onMenuItemMovimentacaoAction() {
		System.out.println("onMenuItemMovimentacaoAction");
	}

	@FXML
	public void onMenuItemRelPatrXLocalAction() {
		System.out.println("onMenuItemRelPatrXLocalAction");
	}

	@FXML
	public void onMenuItemAjudaAction() {
		System.out.println("onMenuItemAjudaAction");
	}

	
	@Override
	public void initialize(URL uri, ResourceBundle rb) {
		
	}

}
