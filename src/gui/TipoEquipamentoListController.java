package gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Equipamento;
import model.services.EquipamentoService;

public class TipoEquipamentoListController implements Initializable {

	private EquipamentoService service;
	//devemos evitar o comando abaixo, pois faz um acoplamento forte.
	//private EquipamentoService service = new EquipamentoService();
	//Para injetar a dependência evitando o acoplamento forte acima, devemos utilizar o set
	//Assim podemos criar o setEquipamentoService
		
	@FXML
	private TableView<Equipamento> tableViewEquipamento;
	
	@FXML
	private TableColumn<Equipamento, Integer> tableColumnId;
	
	@FXML
	private TableColumn<Equipamento, String> tableColumnName;
	
	@FXML
	private Button btNew;
	
	private ObservableList<Equipamento> obsList;
	
	@FXML
	public void onBtNewAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		Equipamento obj = new Equipamento();
		createDialogForm(obj, "/gui/TipoEquipamentoForm.fxml", parentStage);
	}
		
	//Evitando o acoplamento forte, com injeção de dependência
	public void setEquipamentoService (EquipamentoService service) {
		this.service = service;
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}

	private void initializeNodes() {

		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnName.setCellValueFactory(new PropertyValueFactory<>("descricao"));
		
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewEquipamento.prefHeightProperty().bind(stage.heightProperty());
		
	}
	
	public void updateTableView() {
		if (service == null) {
			throw new IllegalStateException("Serviço nulo");
		}
		List<Equipamento> list = service.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewEquipamento.setItems(obsList);
	}

	private void createDialogForm(Equipamento obj, String absoluteName, Stage parentStage ) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();
			
			TipoEquipamentoFormController controller = loader.getController();
			controller.setEquipamento(obj);
			controller.updateFormData();
			
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Entre com os dados de Tipo de Equipamento:");
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStage);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();
						
		}
		catch (IOException e) {
			Alerts.showAlert("Exceção de E/S", "Erro de carregamento de Tela", e.getMessage(), AlertType.ERROR);
		}
	}
}
