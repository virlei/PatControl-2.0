package gui;

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
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Patrimonio;
import model.services.PatrimonioService;

public class PatrimonioListController implements Initializable, DataChangeListener {

	private PatrimonioService service;
	// devemos evitar o comando abaixo, pois faz um acoplamento forte.
	// private PatrimonioService service = new PatrimonioService();
	// Para injetar a dependência evitando o acoplamento forte acima, devemos
	// utilizar o set
	// Assim podemos criar o setPatrimonioService

	@FXML
	private TableView<Patrimonio> tableViewPatrimonio;

	@FXML
	private TableColumn<Patrimonio, Long> tableColumnId;

	@FXML
	private TableColumn<Patrimonio, String> tableColumnName;

	@FXML
	private TableColumn<Patrimonio, Patrimonio> tableColumnEDIT;

	@FXML
	private TableColumn<Patrimonio, Patrimonio> tableColumnREMOVE;

	@FXML
	private Button btNew;

	private ObservableList<Patrimonio> obsList;

	@FXML
	public void onBtNewAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		Patrimonio obj = new Patrimonio();
		createDialogForm(obj, "/gui/PatrimonioForm.fxml", parentStage);
	}

	// Evitando o acoplamento forte, com injeção de dependência
	public void setPatrimonioService(PatrimonioService service) {
		this.service = service;
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}

	private void initializeNodes() {

		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("numero"));
		tableColumnName.setCellValueFactory(new PropertyValueFactory<>("descricao"));

		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewPatrimonio.prefHeightProperty().bind(stage.heightProperty());

	}

	public void updateTableView() {
		if (service == null) {
			throw new IllegalStateException("Serviço nulo");
		}
		List<Patrimonio> list = service.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewPatrimonio.setItems(obsList);
		initEditButtons();
		initRemoveButtons();
	}

	private void createDialogForm(Patrimonio obj, String absoluteName, Stage parentStage) {
//		try {
//			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
//			Pane pane = loader.load();
//
//			TipoPatrimonioFormController controller = loader.getController();
//			// Injetando dependência do Patrimonio no form
//			controller.setPatrimonio(obj);
//
//			// Injetando dependência do PatrimonioService no form
//			controller.setPatrimonioService(new PatrimonioService());
//
//			// Inscrevendo-se no subject TipoPatrimonioFormController para receber os
//			// eventos de mudança
//			controller.subscribeDataChangeListener(this);
//
//			controller.updateFormData();
//
//			Stage dialogStage = new Stage();
//			dialogStage.setTitle("Entre com os dados de Tipo de Patrimonio:");
//			dialogStage.setScene(new Scene(pane));
//			dialogStage.setResizable(false);
//			dialogStage.initOwner(parentStage);
//			dialogStage.initModality(Modality.WINDOW_MODAL);
//			dialogStage.showAndWait();
//
//		} catch (IOException e) {
//			Alerts.showAlert("Exceção de E/S", "Erro de carregamento de Tela", e.getMessage(), AlertType.ERROR);
//		}
	}

	@Override
	public void onDataChanged() {
		updateTableView();
	}

	private void initEditButtons() {
		tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEDIT.setCellFactory(param -> new TableCell<Patrimonio, Patrimonio>() {
			private final Button button = new Button("Editar");

			@Override
			protected void updateItem(Patrimonio obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(
						event -> createDialogForm(obj, "/gui/TipoPatrimonioForm.fxml", Utils.currentStage(event)));
			}
		});
	}

	private void initRemoveButtons() {
		tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnREMOVE.setCellFactory(param -> new TableCell<Patrimonio, Patrimonio>() {
			private final Button button = new Button("Remover");

			@Override
			protected void updateItem(Patrimonio obj, boolean empty) {
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

	private void removeEntity(Patrimonio obj) {
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
