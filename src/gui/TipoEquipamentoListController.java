package gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import application.Main;
import db.DbIntegrityException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Equipamento;
import model.services.EquipamentoService;

public class TipoEquipamentoListController implements Initializable, DataChangeListener {

	private EquipamentoService service;
	// devemos evitar o comando abaixo, pois faz um acoplamento forte.
	// private EquipamentoService service = new EquipamentoService();
	// Para injetar a dependência evitando o acoplamento forte acima, devemos
	// utilizar o set
	// Assim podemos criar o setEquipamentoService

	@FXML
	private TableView<Equipamento> tableViewEquipamento;

	@FXML
	private TableColumn<Equipamento, Integer> tableColumnId;

	@FXML
	private TableColumn<Equipamento, String> tableColumnName;

	@FXML
	private TableColumn<Equipamento, Equipamento> tableColumnEDIT;

	@FXML
	private TableColumn<Equipamento, Equipamento> tableColumnREMOVE;

	@FXML
	private Button btNew;

	private ObservableList<Equipamento> obsList;

	@FXML
	public void onBtNewAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		Equipamento obj = new Equipamento();
		createDialogForm(obj, "/gui/TipoEquipamentoForm.fxml", parentStage);
	}

	// Evitando o acoplamento forte, com injeção de dependência
	public void setEquipamentoService(EquipamentoService service) {
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
		initEditButtons();
		initRemoveButtons();
	}

	private void createDialogForm(Equipamento obj, String absoluteName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();

			TipoEquipamentoFormController controller = loader.getController();
			// Injetando dependência do Equipamento no form
			controller.setEquipamento(obj);

			// Injetando dependência do EquipamentoService no form
			controller.setEquipamentoService(new EquipamentoService());

			// Inscrevendo-se no subject TipoEquipamentoFormController para receber os
			// eventos de mudança
			controller.subscribeDataChangeListener(this);

			controller.updateFormData();

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Entre com os dados de Tipo de Equipamento:");
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStage);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();

		} catch (IOException e) {
			e.printStackTrace();
			Alerts.showAlert("Exceção de E/S", "Erro de carregamento de Tela", e.getMessage(), AlertType.ERROR);
		}
	}

	@Override
	public void onDataChanged() {
		updateTableView();
	}

	private void initEditButtons() {
		tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEDIT.setCellFactory(param -> new TableCell<Equipamento, Equipamento>() {
			private final Button button = new Button("Editar");

			@Override
			protected void updateItem(Equipamento obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(
						event -> createDialogForm(obj, "/gui/TipoEquipamentoForm.fxml", Utils.currentStage(event)));
			}
		});
	}

	private void initRemoveButtons() {
		tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnREMOVE.setCellFactory(param -> new TableCell<Equipamento, Equipamento>() {
			private final Button button = new Button("Remover");

			@Override
			protected void updateItem(Equipamento obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> removeEntity(obj));
			}
		});
	}

	private void removeEntity(Equipamento obj) {
		Optional<ButtonType> result = Alerts.showConfirmation("Confirmação", "Tem certeza que deseja excluir?");
		
		if (result.get() == ButtonType.OK) {
			if (service == null) {
				throw new IllegalStateException("Serviço Nulo");
			}
			try {
				service.remove(obj);
				updateTableView();
			}
			catch (DbIntegrityException e) {
				Alerts.showAlert("Erro ao remover o registro", null, e.getMessage(), AlertType.ERROR);
			}
			
		}
	}
}
