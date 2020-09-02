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
import model.entities.Local;
import model.services.LocalService;

public class LocalListController implements Initializable, DataChangeListener {
	
	private LocalService service;

	@FXML
	private Button BtnNew;

	@FXML
	private TableColumn<Local, String> ColumnDescricao;

	@FXML
	private TableView<Local> TableViewLocal;

	@FXML
	private TableColumn<Local, Integer> ColumnLocal;

	@FXML
	private TableColumn<Local, Local> TableColumnRemove;

	@FXML
	private TableColumn<Local, Local> tableColumnEdit;

	@FXML
	void onButtonNewAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		Local obj = new Local();
		createDialogForm(obj, "/gui/LocalForm.fxml", parentStage);
	}
	    
	private ObservableList<Local> obsList;

	public void setLocalService(LocalService service) {
		this.service = service;
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}

	private void initializeNodes() {

		ColumnLocal.setCellValueFactory(new PropertyValueFactory<>("idLocal"));
		ColumnDescricao.setCellValueFactory(new PropertyValueFactory<>("descricaoLocal"));
		
		Stage stage = (Stage) Main.getMainScene().getWindow();
		TableViewLocal.prefHeightProperty().bind(stage.heightProperty());
		
	}
	
	public void updateTableView() {
		if (service == null) {
			throw new IllegalStateException("Servi�o nulo");
		}
		List<Local> list = service.findAll();
		obsList = FXCollections.observableArrayList(list);
		TableViewLocal.setItems(obsList);
		initEditButtons();
		initRemoveButtons();
	}

	private void createDialogForm(Local obj, String absoluteName, Stage parentStage ) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();
			
			LocalFormController controller = loader.getController();
			
			controller.setLocal(obj);
			controller.setLocalService(new LocalService());
	
			//tinha faltado essa inscri��o como observer na lista
			controller.subscribeDataChangeListener(this);
			
			controller.updateFormData();
			
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Entre com os dados de Tipo de Local:");
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStage);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();
						
		}
		catch (IOException e) {
			Alerts.showAlert("Exce��o de E/S", "Erro de carregamento de Tela", e.getMessage(), AlertType.ERROR);
		}
	}
	
	public void onDataChanged() {
		updateTableView();
	}

	private void initEditButtons() {
		tableColumnEdit.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEdit.setCellFactory(param -> new TableCell<Local, Local>() {
			private final Button button = new Button("Editar");

			@Override
			protected void updateItem(Local obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(
						event -> createDialogForm(obj, "/gui/LocalForm.fxml", Utils.currentStage(event)));
			}
		});
	}

	private void initRemoveButtons() {
		TableColumnRemove.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		TableColumnRemove.setCellFactory(param -> new TableCell<Local, Local>() {
			private final Button button = new Button("Remover");

			@Override
			protected void updateItem(Local obj, boolean empty) {
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

	private void removeEntity(Local obj) {
		Optional<ButtonType> result = Alerts.showConfirmation("Confirma��o", "Tem certeza que deseja excluir?");
		
		if (result.get() == ButtonType.OK) {
			if (service == null) {
				throw new IllegalStateException("Servi�o Nulo");
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
