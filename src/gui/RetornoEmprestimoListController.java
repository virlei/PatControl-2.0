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
import model.entities.RetornoEmprestimo;
import model.services.RetornoEmprestimoService;

public class RetornoEmprestimoListController implements Initializable, DataChangeListener{

    @FXML
    private TableView<RetornoEmprestimo> TableViewRetornoEmprestimo;

    @FXML
    private Button BtnNew;

    @FXML
    private TableColumn<RetornoEmprestimo, RetornoEmprestimo> TableColumnRemove;

    @FXML
    private TableColumn<RetornoEmprestimo, RetornoEmprestimo> tableColumnEdit;

    @FXML
    private TableColumn<RetornoEmprestimo, Date> ColumnDataRetorno;

    @FXML
    private TableColumn<RetornoEmprestimo, Integer> ColumnRetorno;

    @FXML
    private TableColumn<RetornoEmprestimo, String> ColumnRecebedor;
    
    private RetornoEmprestimoService service;
    
    private ObservableList<RetornoEmprestimo> obsList;

    @FXML
    void onButtonNewAction(ActionEvent event) {    	
    	Stage parentStage = Utils.currentStage(event);
    	RetornoEmprestimo obj = new RetornoEmprestimo();
		createDialogForm(obj, "/gui/RetornoEmprestimoForm.fxml", parentStage);
    }
    
    public void setRetornoEmprestimoService (RetornoEmprestimoService service) {
		this.service = service;
	}    
   
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}

	private void initializeNodes() {
		ColumnRetorno.setCellValueFactory(new PropertyValueFactory<>("retorno"));
		ColumnDataRetorno.setCellValueFactory(new PropertyValueFactory<>("dtRetornoEmpr"));
		Utils.formatTableColumnDate(ColumnDataRetorno,"dd/MM/yyyy");
		ColumnRecebedor.setCellValueFactory(new PropertyValueFactory<>("recebedor"));
		
		Stage stage = (Stage) Main.getMainScene().getWindow();
		TableViewRetornoEmprestimo.prefHeightProperty().bind(stage.heightProperty());
	}
	
	public void updateTableView() {
		if (service == null) {
			throw new IllegalStateException("Serviço nulo");
		}
		List<RetornoEmprestimo> list = service.findAll();
		obsList = FXCollections.observableArrayList(list);
		TableViewRetornoEmprestimo.setItems(obsList);
		initEditButtons();
		initRemoveButtons();
	}

	private void createDialogForm(RetornoEmprestimo obj, String absoluteName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();

			RetornoEmprestimoFormController controller = loader.getController();

			controller.setRetornoEmprestimo(obj);
			controller.setRetornoEmprestimoService(new RetornoEmprestimoService());
			controller.updateFormData();
			
			controller.subscribeDataChangeListener(this);

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Entre com os dados do empréstimo");
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
		tableColumnEdit.setCellFactory(param -> new TableCell<RetornoEmprestimo, RetornoEmprestimo>() {
			private final Button button = new Button("Editar");

			@Override
			protected void updateItem(RetornoEmprestimo obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(
						event -> createDialogForm(obj, "/gui/RetornoEmprestimoForm.fxml", Utils.currentStage(event)));
			}
		});
	}

	private void initRemoveButtons() {
		TableColumnRemove.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		TableColumnRemove.setCellFactory(param -> new TableCell<RetornoEmprestimo, RetornoEmprestimo>() {
			private final Button button = new Button("Remover");

			@Override
			protected void updateItem(RetornoEmprestimo obj, boolean empty) {
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

	private void removeEntity(RetornoEmprestimo obj) {
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
