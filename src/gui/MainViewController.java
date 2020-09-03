package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

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
import model.services.DevolucaoService;
import model.services.EmprestimoService;
import model.services.EquipamentoService;
import model.services.GFornService;
import model.services.LocalService;
//import model.services.GuiaDeFornecimentoTesteService;
import model.services.PatrimonioService;
import model.services.RetornoEmprestimoService;

public class MainViewController implements Initializable {

	@FXML
	private MenuItem menuItemPatrimonio;
	@FXML
	private MenuItem menuItemTipoEquipamento;
	@FXML
	private MenuItem menuItemLocal;
    @FXML
    private MenuItem menuItemFornecimento;
	@FXML
	private MenuItem menuItemRelPatrXLocal;
	@FXML
	private MenuItem menuItemAjuda;
    @FXML
    private MenuItem menuItemDevolucao;
    @FXML
    private MenuItem menuItemEmprestimo;
    @FXML
    private MenuItem menuItemRetornoEmprestimo;

	@FXML
	public void onMenuItemPatrimonioAction() {
		loadView("/gui/PatrimonioListTeste.fxml", (PatrimonioListController controller) -> {
			controller.setPatrimonioService(new PatrimonioService());
			controller.updateTableView();
			
		});
	}
	
	@FXML
	public void onMenuItemTipoEquipamentoAction() {
		loadView("/gui/TipoEquipamentoList.fxml", (TipoEquipamentoListController controller) -> {
			controller.setEquipamentoService(new EquipamentoService());
			controller.updateTableView();
		});
	}
	
	@FXML
	public void onMenuItemLocalAction() {		
		loadView("/gui/LocalList.fxml", (LocalListController controller) -> {
			controller.setLocalService(new LocalService());
			controller.updateTableView();
		});
		
	}
	
	@FXML
	public void onMenuItemFornecimentoAction() {
		loadView("/gui/GFornList.fxml", (GFornListController controller) -> {
			controller.setGFornService(new GFornService());
			controller.updateTableView();
		});
	}
	

    @FXML
    public void onMenuItemDevolucaoAction() {
		loadView("/gui/DevolucaoList.fxml", (DevolucaoListController controller) -> {
			controller.setDevolucaoService(new DevolucaoService());
			controller.updateTableView();
		});
	}
    
    @FXML
    void onMenuItemEmprestimoAction() {    	
    	loadView("/gui/EmprestimoList.fxml", (EmprestimoListController controller) -> {
			controller.setEmprestimoService(new EmprestimoService());
			controller.updateTableView();
		});
    }
    

    @FXML
    void onMenuItemRetornoEmprestimoAction() {
    	loadView("/gui/RetornoEmprestimoList.fxml", (RetornoEmprestimoListController controller) -> {
			controller.setRetornoEmprestimoService(new RetornoEmprestimoService());
			controller.updateTableView();
		});
    }

	@FXML
	public void onMenuItemRelPatrXLocalAction() {
		System.out.println("onMenuItemRelPatrXLocalAction");
	}

	@FXML
	public void onMenuItemAjudaAction() {
		loadView("/gui/About.fxml", x->{});
	}
	
	@Override
	public void initialize(URL uri, ResourceBundle rb) {
		
	}
	
	private synchronized <T> void loadView (String absoluteName, Consumer<T> initializingAction ) {
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
			
			T controller = loader.getController();
			initializingAction.accept(controller);
						
		}
		catch (IOException e) {
			Alerts.showAlert("Exceção de E/S", "Erro ao carregar tela", e.getMessage(), AlertType.ERROR);
		}
		
	}

}
