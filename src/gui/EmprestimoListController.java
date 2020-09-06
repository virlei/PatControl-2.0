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
import model.entities.Emprestimo;
import model.services.EmprestimoService;

public class EmprestimoListController implements Initializable, DataChangeListener{

    @FXML
    private TableView<Emprestimo> TableViewEmprestimo;

    @FXML
    private TableColumn<Emprestimo, Date> ColumnDataEmprestimo;

    @FXML
    private Button BtnNew;

    @FXML
    private TableColumn<Emprestimo, String> ColumnResponsavel;

    @FXML
    private TableColumn<Emprestimo, Emprestimo> TableColumnRemove;

    @FXML
    private TableColumn<Emprestimo, String> ColumnSetor;

    @FXML
    private TableColumn<Emprestimo, Emprestimo> tableColumnEdit;

    @FXML
    private TableColumn<Emprestimo, Integer> ColumnEmprestimo;
    
    private EmprestimoService service;
    
    private ObservableList<Emprestimo> obsList;

    @FXML
    void onButtonNewAction(ActionEvent event) {    	
    	Stage parentStage = Utils.currentStage(event);
    	Emprestimo obj = new Emprestimo();
		createDialogForm(obj, "/gui/EmprestimoForm.fxml", parentStage);
    }
    
    public void setEmprestimoService (EmprestimoService service) {
		this.service = service;
	}    
   
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}

	private void initializeNodes() {
		ColumnEmprestimo.setCellValueFactory(new PropertyValueFactory<>("emprestimo"));
		ColumnDataEmprestimo.setCellValueFactory(new PropertyValueFactory<>("datEmprestimo"));
		Utils.formatTableColumnDate(ColumnDataEmprestimo,"dd/MM/yyyy");
		ColumnSetor.setCellValueFactory(new PropertyValueFactory<>("setor"));
		ColumnResponsavel .setCellValueFactory(new PropertyValueFactory<>("responsavel"));
		
		Stage stage = (Stage) Main.getMainScene().getWindow();
		TableViewEmprestimo.prefHeightProperty().bind(stage.heightProperty());
	}
	
	public void updateTableView() {
		if (service == null) {
			throw new IllegalStateException("Serviço nulo");
		}
		List<Emprestimo> list = service.findAll();
		obsList = FXCollections.observableArrayList(list);
		TableViewEmprestimo.setItems(obsList);
		initEditButtons();
		initRemoveButtons();
	}

	private void createDialogForm(Emprestimo obj, String absoluteName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();

			EmprestimoFormController controller = loader.getController();

			controller.setEmprestimo(obj);
			controller.setEmprestimoService(new EmprestimoService());
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
		tableColumnEdit.setCellFactory(param -> new TableCell<Emprestimo, Emprestimo>() {
			private final Button button = new Button("Editar");

			@Override
			protected void updateItem(Emprestimo obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(
						event -> createDialogForm(obj, "/gui/EmprestimoForm.fxml", Utils.currentStage(event)));
			}
		});
	}

	private void initRemoveButtons() {
		TableColumnRemove.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		TableColumnRemove.setCellFactory(param -> new TableCell<Emprestimo, Emprestimo>() {
			private final Button button = new Button("Remover");

			@Override
			protected void updateItem(Emprestimo obj, boolean empty) {
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

	private void removeEntity(Emprestimo obj) {
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
