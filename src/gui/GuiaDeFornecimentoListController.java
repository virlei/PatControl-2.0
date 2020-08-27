package gui;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import application.Main;
import db.DbIntegrityException;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
import model.entities.GuiaDeFornecimento;
import model.services.GuiaDeFornecimentoService;

public class GuiaDeFornecimentoListController {
	
	private GuiaDeFornecimentoService service;

    @FXML
    private Button BtnNew;
    
    private ObservableList<GuiaDeFornecimento> obsList;

    @FXML
    private TableView<GuiaDeFornecimento> tableViewGuiaFornecimento;

    @FXML
    private TableColumn<GuiaDeFornecimento, Integer> tableColumnPKey;

    @FXML
    private TableColumn<GuiaDeFornecimento, Long> tableColumnNrGuia;

    @FXML
    private TableColumn<GuiaDeFornecimento, Date> tableColumnDtFornecimento;

    @FXML
    private TableColumn<GuiaDeFornecimento, GuiaDeFornecimento> tableColumnEdit;

    @FXML
    private TableColumn<GuiaDeFornecimento, GuiaDeFornecimento> TableColumnRemove;

    @FXML
    void onButtonNewAction(ActionEvent event) {
    	System.out.println("Btt new Clicked");

    }
    
	public void setGuiaDeFornecimentoService(GuiaDeFornecimentoService service) {
		this.service = service;
	}

	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}

	private void initializeNodes() {

		tableColumnPKey.setCellValueFactory(new PropertyValueFactory<>("pKey"));
		tableColumnDtFornecimento.setCellValueFactory(new PropertyValueFactory<>("dtFornecimento"));
		tableColumnNrGuia.setCellValueFactory(new PropertyValueFactory<>("nrGuia"));
		Utils.formatTableColumnDate(tableColumnDtFornecimento, "dd/MM/yyyy");
		//ColumnPatrimonio.setCellValueFactory(new PropertyValueFactory<>("patrimonios"));

		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewGuiaFornecimento.prefHeightProperty().bind(stage.heightProperty());
	}

	

	private void createDialogForm(GuiaDeFornecimento obj, String absoluteName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();

			GuiaDeFornecimentoFormController controller = loader.getController();
			
			

			controller.subscribeDataChangeListener(null);

			controller.updateFormData();
		
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Entre com os dados da Guia de Fornecimento:");
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

	public void updateTableView() {
		if (service == null) {
			throw new IllegalStateException("Serviço nulo");
		}
		List<GuiaDeFornecimento> list = service.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewGuiaFornecimento.setItems(obsList);
		initEditButtons();
		initRemoveButtons();
	}
		
	public void onDataChanged() {
		updateTableView();
	}

	private void initEditButtons() {
		tableColumnEdit.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEdit.setCellFactory(param -> new TableCell<GuiaDeFornecimento, GuiaDeFornecimento>() {
			private final Button button = new Button("Editar");

			@Override
			protected void updateItem(GuiaDeFornecimento obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(
						event -> createDialogForm(obj, "/gui/GuiaDeFornecimentoForm.fxml", Utils.currentStage(event)));
			}
		});
	}

	private void initRemoveButtons() {
		TableColumnRemove.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		TableColumnRemove.setCellFactory(param -> new TableCell<GuiaDeFornecimento, GuiaDeFornecimento>() {
			private final Button button = new Button("Remover");

			@Override
			protected void updateItem(GuiaDeFornecimento obj, boolean empty) {
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

	private void removeEntity(GuiaDeFornecimento obj) {
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

    


