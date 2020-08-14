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
import model.entities.Movimentacao;
import model.services.MovimentacaoService;

public class MovimentacaoListController implements  Initializable, DataChangeListener {
	
	private MovimentacaoService service;
	
    @FXML
    private TableColumn<Movimentacao, String> tableColumnDataDevolucao;

    @FXML
    private Button btNew;

    @FXML
    private TableView<Movimentacao> tableViewMovimentacao;

    @FXML
    private TableColumn<Movimentacao, Long> tableColumnPatrimonio;

    @FXML
    private TableColumn<Movimentacao, Movimentacao> tableColumnEDIT;

    @FXML
    private TableColumn<Movimentacao, Movimentacao> tableColumnREMOVE;

    @FXML
    private TableColumn<Movimentacao, String> tableColumnDataEntrada;

    @FXML
    private TableColumn<Movimentacao, Integer> tableColumnNumeroGuia;

    @FXML
    void onBtNewAction(ActionEvent event) {
    	
    	System.out.println("Button New Clicked");

    }
    
    private ObservableList<Movimentacao> obsList;
    
	public void setMovimentacaoService(MovimentacaoService service) {
		this.service = service;
	}
	
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}
	
	private void initializeNodes() {

		tableColumnPatrimonio.setCellValueFactory(new PropertyValueFactory<>("patrimonio"));
		tableColumnDataEntrada.setCellValueFactory(new PropertyValueFactory<>("dataEntrada"));
		tableColumnNumeroGuia.setCellValueFactory(new PropertyValueFactory<>("numeroGuia"));
		tableColumnDataDevolucao.setCellValueFactory(new PropertyValueFactory<>("dataDevolucao"));
		
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewMovimentacao.prefHeightProperty().bind(stage.heightProperty());

	}
	
	public void updateTableView() {
		if (service == null) {
			throw new IllegalStateException("Serviço nulo");
		}
		List<Movimentacao> list = service.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewMovimentacao.setItems(obsList);
		initEditButtons();
		initRemoveButtons();
	}
	
	private void createDialogForm(Movimentacao obj, String absoluteName, Stage parentStage, boolean insert) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();

			MovimentacaoFormController controller = loader.getController();
			
			controller.setMovimentacao(obj);
			controller.setMovimentacaoService(new MovimentacaoService());
		
			controller.subscribeDataChangeListener(this);  

			controller.updateFormData();

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Entre com os dados de Movimentação:");
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
		tableColumnEDIT.setCellFactory(param -> new TableCell<Movimentacao, Movimentacao>() {
			private final Button button = new Button("Editar");

			@Override
			protected void updateItem(Movimentacao obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(
						event -> createDialogForm(obj, "/gui/PatrimonioForm.fxml", Utils.currentStage(event), false));
			}
		});
	}
	
	private void initRemoveButtons() {
		tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnREMOVE.setCellFactory(param -> new TableCell<Movimentacao, Movimentacao>() {
			private final Button button = new Button("Remover");

			@Override
			protected void updateItem(Movimentacao obj, boolean empty) {
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
	
	private void removeEntity(Movimentacao obj) {
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
