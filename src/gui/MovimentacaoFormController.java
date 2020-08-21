package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import model.entities.Movimentacao;
import model.entities.Equipamento;
import model.entities.Local;
import model.exceptions.ValidationException;
import model.services.EquipamentoService;
import model.services.LocalService;
import model.services.MovimentacaoService;
import model.services.PatrimonioService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Pane;
import javafx.util.Callback;

public class MovimentacaoFormController {
	
	private Movimentacao entity;

	private MovimentacaoService service;
	
	private PatrimonioService patrimonioService;

	private EquipamentoService equipamentoService;
	
	private LocalService localService;

	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
	
	@FXML
	private ComboBox<Local> comboBoxLocal;	

    @FXML
    private TextField txtCondicaoUso;

    @FXML
    private ComboBox<Equipamento> comboBoxEquipamento;

    @FXML
    private TextField txtDataEntrada;
    
    @FXML
    private Label labelErrorDescricao;

    @FXML
    private Button btCancel;

    @FXML
    private TextField txtNumeroPatrimonio;

    @FXML
    private TextField txtDescricao;

    @FXML
    private Button btSave;

    @FXML
    private TextField txtFabricante;

    @FXML
    private TextField txtNumeroGuia;

    @FXML
    private TextField txtMarca;

    @FXML
    private TextField txtDataDevolucao;
    
    private ObservableList<Equipamento> obsList;
	
	private ObservableList<Local> obsLstLocal;

	    @FXML
	    void onBtSaveAction(ActionEvent event) {
			if (entity == null) {
				throw new IllegalStateException("Entidade nula");
			}
			if (service == null) {
				throw new IllegalStateException("Serviço nulo");
			}
			try {
				entity = getFormData();
				service.saveOrUpdate(entity);
				notifyDataChangeListeners();
				Utils.currentStage(event).close();
			}
			catch (ValidationException e) {
				setErrorMessages(e.getErrors());
			}
			catch (DbException e) {
				Alerts.showAlert("Erro ao salvar objeto", null, e.getMessage(), AlertType.ERROR);
			}

	    }

	    @FXML
	    void onBtCancelAction(ActionEvent event) {
	    	Utils.currentStage(event).close();
	    }
	    
	    public void setMovimentacao(Movimentacao entity) {
			this.entity = entity;
		}		
	    
	    public void setMovimentacaoService(MovimentacaoService service, PatrimonioService patrimonioService, EquipamentoService equipamentoService, LocalService localService, boolean insert) {
			this.service = service;
			this.patrimonioService = patrimonioService;
			this.equipamentoService = equipamentoService;
			this.localService = localService;
			MovimentacaoService.Insert = insert;
		}
		
		public void subscribeDataChangeListener(DataChangeListener listener) {
			dataChangeListeners.add(listener);
		}
		
		private void notifyDataChangeListeners() {
			for (DataChangeListener listener : dataChangeListeners) {
				listener.onDataChanged();
			}
		}		

		private Movimentacao getFormData() {
			
			Movimentacao obj = new Movimentacao();
			
			ValidationException exception = new ValidationException("Erro de Validação");

			if (txtNumeroGuia.getText() == null || txtNumeroGuia.getText().trim().equals("")) {
				exception.addError("nrGuia", "Número da Guia nulo");
			}
			obj.setNumeroGuia(Utils.tryParseToLong(txtNumeroGuia.getText()));		
			
			obj.setDataDevolucao(txtDataDevolucao.getText());
			
			obj.setDataEntrada(txtDataEntrada.getText());		
			
			if (exception.getErrors().size() > 0) {
				throw exception;
			}			
			
			return obj;

		}
		
		public void initialize(URL url, ResourceBundle rb) {
			initializeNodes();
		}
		
		private void initializeNodes() {
			Constraints.setTextFieldInteger(txtNumeroGuia);
			Constraints.setTextFieldMaxLength(txtNumeroPatrimonio, 30);
			
			initializeComboBoxEquipamento();
			
			initializeComboBoxLocal();
		}
		
		public void updateFormData() {
			
			if (entity == null) {
				throw new IllegalStateException("Entidade está vazia");
			}
			
			txtNumeroGuia.setText(String.valueOf(entity.getNumeroGuia()));
			if (txtNumeroGuia.getText() == null || txtNumeroGuia.getText().trim().equals("")) {
				txtNumeroGuia.setEditable(true);
			}
			else {
				txtNumeroGuia.setEditable(false);
			}			
		
			txtDataDevolucao.setText(entity.getDataDevolucao());

			txtDataEntrada.setText(entity.getDataEntrada());
			
			txtDescricao.setText(entity.getPatrimonio().getDescricao());

			txtFabricante.setText(entity.getPatrimonio().getFabricante());

			txtMarca.setText(entity.getPatrimonio().getMarca());
			
			txtCondicaoUso.setText(String.valueOf(entity.getPatrimonio().getCondicaoUso()));
			
			if (entity.getPatrimonio().getTipEquip() == null) {
				comboBoxEquipamento.getSelectionModel().selectFirst();
			}
			else {
				comboBoxEquipamento.setValue(entity.getPatrimonio().getTipEquip());
			}

			if (entity.getPatrimonio().getPatrLocal() == null) {
				comboBoxLocal.getSelectionModel().selectFirst();
			}
			else {
				comboBoxLocal.setValue(entity.getPatrimonio().getPatrLocal());
			}
						
		}
		
		public void loadAssociatedObjects() {
			if (equipamentoService == null) {
				throw new IllegalStateException("Lista de Tipos de Equipamentos está vazia");
			}
			List<Equipamento> list = equipamentoService.findAll();
			obsList = FXCollections.observableArrayList(list);
			comboBoxEquipamento.setItems(obsList);
			
			if (localService == null ) {
				throw new IllegalStateException("Lista de Locais está vazia");
			}
			List<Local> lstLocal = localService.findAll();
			obsLstLocal = FXCollections.observableArrayList(lstLocal);
			comboBoxLocal.setItems(obsLstLocal);
		}			
		
		private void setErrorMessages(Map<String, String> errors ) {
			Set<String> fields = errors.keySet();
			
			if (fields.contains("name")) {
				labelErrorDescricao.setText(errors.get("name"));
			}
		}
		private void initializeComboBoxEquipamento() {
			Callback<ListView<Equipamento>, ListCell<Equipamento>> factory = lv -> new ListCell<Equipamento>() {
				@Override
				protected void updateItem(Equipamento item, boolean empty) {
					super.updateItem(item, empty);
					setText(empty ? "" : item.getDescricao());
				}
			};
			comboBoxEquipamento.setCellFactory(factory);
			comboBoxEquipamento.setButtonCell(factory.call(null));
		}

		private void initializeComboBoxLocal() {
			Callback<ListView<Local>, ListCell<Local>> factory = lv -> new ListCell<Local>() {
				@Override
				protected void updateItem(Local item, boolean empty) {
					super.updateItem(item, empty);
					setText(empty ? "" : item.getDescricaoLocal());
				}
			};
			comboBoxLocal.setCellFactory(factory);
			comboBoxLocal.setButtonCell(factory.call(null));
		}	
		
	}


