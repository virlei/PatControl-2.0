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
import model.entities.GForn;
import model.services.GFornService;

public class GFornListController implements Initializable, DataChangeListener {
	
	private GFornService service;

	  @FXML
	    private Button BtnNew;

	    @FXML
	    private TableView<GForn> TableViewGForn;

	    @FXML
	    private TableColumn<GForn, Integer> ColumnPkGuia;
	    
	    @FXML
	    private TableColumn<GForn, Integer> ColumnNrGuia;

	    @FXML
	    private TableColumn<GForn, GForn> TableColumnRemove;

	    @FXML
	    private TableColumn<GForn, GForn> tableColumnEdit;	    

	    @FXML
	    private TableColumn<GForn, String> ColumnDtForn;

	    @FXML
	    void onButtonNewAction(ActionEvent event) {
	    	Stage parentStage = Utils.currentStage(event);
			GForn obj = new GForn();
			createDialogForm(obj, "/gui/GFornForm.fxml", parentStage);
	    }
	    
	    private ObservableList<GForn> obsList;

	public void setGFornService (GFornService service) {
		this.service = service;
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}

	private void initializeNodes() {

		ColumnNrGuia.setCellValueFactory(new PropertyValueFactory<>("nrGuia"));		
		ColumnPkGuia.setCellValueFactory(new PropertyValueFactory<>("pkGForn"));
		ColumnDtForn.setCellValueFactory(new PropertyValueFactory<>("dtForn"));
		
		Stage stage = (Stage) Main.getMainScene().getWindow();
		TableViewGForn.prefHeightProperty().bind(stage.heightProperty());
		
	}
	
	public void updateTableView() {
		if (service == null) {
			throw new IllegalStateException("Serviço nulo");
		}
		List<GForn> list = service.findAll();
		obsList = FXCollections.observableArrayList(list);
		TableViewGForn.setItems(obsList);
		initEditButtons();
		initRemoveButtons();
	}

	private void createDialogForm(GForn obj, String absoluteName, Stage parentStage ) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();
			
			GFornFormController controller = loader.getController();
			
			controller.setGForn(obj);
			controller.setGFornService(new GFornService());
	
			//tinha faltado essa inscrição como observer na lista
			controller.subscribeDataChangeListener(this);
			
			controller.updateFormData();
			
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Entre com os dados de Tipo de GForn:");
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
	
	public void onDataChanged() {
		updateTableView();
	}

	private void initEditButtons() {
		tableColumnEdit.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEdit.setCellFactory(param -> new TableCell<GForn, GForn>() {
			private final Button button = new Button("Editar");

			@Override
			protected void updateItem(GForn obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(
						event -> createDialogForm(obj, "/gui/GFornForm.fxml", Utils.currentStage(event)));
			}
		});
	}

	private void initRemoveButtons() {
		TableColumnRemove.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		TableColumnRemove.setCellFactory(param -> new TableCell<GForn, GForn>() {
			private final Button button = new Button("Remover");

			@Override
			protected void updateItem(GForn obj, boolean empty) {
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

	private void removeEntity(GForn obj) {
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
