package gui;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
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
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Devolucao;
import model.services.DevolucaoService;


public class DevolucaoListController implements Initializable, DataChangeListener {

	@FXML
	private Button BtnNew;

	@FXML
	private TableView<Devolucao> TableViewDevolucao;

//	@FXML
//	private TableColumn<Devolucao, String> ColumnDataDevolucao;

	@FXML
	private TableColumn<Devolucao, Date> ColumnDtDevolucao;

	@FXML
	private TableColumn<Devolucao, String> ColumnSei;

	@FXML
	private TableColumn<Devolucao, String> ColumnMotivo;

	@FXML
	private TableColumn<Devolucao, Integer> ColumnDevolucao;

	@FXML
	private TableColumn<Devolucao, Devolucao> tableColumnEdit;

	@FXML
	private TableColumn<Devolucao, Devolucao> TableColumnRemove;

	private DevolucaoService service;

	private ObservableList<Devolucao> obsList;

    @FXML
    void onButtonNewAction(ActionEvent event) {    	
    	Stage parentStage = Utils.currentStage(event);
		Devolucao obj = new Devolucao();
		createDialogForm(obj, "/gui/DevolucaoForm.fxml", parentStage);

    }
    
    public void setDevolucaoService (DevolucaoService service) {
		this.service = service;
	}    
   
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}

	private void initializeNodes() {
		ColumnDevolucao.setCellValueFactory(new PropertyValueFactory<>("devolucao"));
		ColumnDtDevolucao.setCellValueFactory(new PropertyValueFactory<>("dtDevolucao"));
		Utils.formatTableColumnDate(ColumnDtDevolucao,"dd/MM/yyyy");
		ColumnSei.setCellValueFactory(new PropertyValueFactory<>("numSei"));
		ColumnMotivo.setCellValueFactory(new PropertyValueFactory<>("motivo"));
		
		Stage stage = (Stage) Main.getMainScene().getWindow();
		TableViewDevolucao.prefHeightProperty().bind(stage.heightProperty());
	}
	
	public void updateTableView() {
		if (service == null) {
			throw new IllegalStateException("Serviço nulo");
		}
		List<Devolucao> list = service.findAll();
		obsList = FXCollections.observableArrayList(list);
		TableViewDevolucao.setItems(obsList);
		initEditButtons();
		initRemoveButtons();
	}

	private void createDialogForm(Devolucao obj, String absoluteName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();

			DevolucaoFormController controller = loader.getController();

			controller.setDevolucao(obj);
			controller.setDevolucaoService(new DevolucaoService());
			controller.updateFormData();
			
			controller.subscribeDataChangeListener(this);

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Entre com os dados da devolução");
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStage);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();

		} catch (IOException e) {
			Alerts.showAlert("Exceção de E/S", "Erro de carregamento de Tela", e.getMessage(), AlertType.ERROR);
		}
	}
	
	public void onDataChanged() {
		updateTableView();
	}

	private void initEditButtons() {
		tableColumnEdit.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEdit.setCellFactory(param -> new TableCell<Devolucao, Devolucao>() {
			private final Button button = new Button("Editar");

			@Override
			protected void updateItem(Devolucao obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(
						event -> createDialogForm(obj, "/gui/DevolucaoForm.fxml", Utils.currentStage(event)));
			}
		});
	}

	private void initRemoveButtons() {
		TableColumnRemove.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		TableColumnRemove.setCellFactory(param -> new TableCell<Devolucao, Devolucao>() {
			private final Button button = new Button("Remover");

			@Override
			protected void updateItem(Devolucao obj, boolean empty) {
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

	private void removeEntity(Devolucao obj) {
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
