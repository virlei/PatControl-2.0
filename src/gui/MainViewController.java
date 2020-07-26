package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

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
		loadView("/gui/TipoEquipamentoList.fxml");
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
		loadView("/gui/About.fxml");
	}
	
	@Override
	public void initialize(URL uri, ResourceBundle rb) {
		
	}
	
	private synchronized void loadView (String absoluteName) {
		try {
			
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			VBox newVBox = loader.load();
			
			Scene mainScene = Main.getMainScene();
			
			//Obtendo o 1o elemento da minha view principal através do getRoot:
			VBox mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();
			
			//Guardando referencia para o MainMenu: get(0) é o 1º filho do VBox da Janela principal. 
			Node mainMenu = mainVBox.getChildren().get(0);
			
			//Limpando todos os filhos do MainVbox - Retirando o mainMenu
			mainVBox.getChildren().clear();
			
			//adicionando o MainMenu e os filhos do newVBox
			mainVBox.getChildren().add(mainMenu);
			mainVBox.getChildren().addAll(newVBox.getChildren());
			
		}
		catch (IOException e) {
			Alerts.showAlert("Exceção de E/S", "Erro ao carregar tela", e.getMessage(), AlertType.ERROR);
		}
		
	}

}
