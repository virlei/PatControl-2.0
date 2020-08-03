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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import model.entities.Equipamento;
import model.entities.Patrimonio;
import model.exceptions.ValidationException;
import model.services.EquipamentoService;
import model.services.PatrimonioService;

public class PatrimonioFormController implements Initializable {

	private Patrimonio entity;

	private PatrimonioService service;

	private EquipamentoService equipamentoService;

	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

	@FXML
	private TextField txtNrPatrimonio;

	@FXML
	private TextField txtDescricao;

	@FXML
	private TextField txtFabricante;

	@FXML
	private TextField txtMarca;

	@FXML
	private TextField txtCondicaoUso;

	@FXML
	private ComboBox<Equipamento> comboBoxEquipamento;

//	@FXML
//	private DatePicker dpAnyDate;

	@FXML
	private Label labelErrorDescricao;
	
	@FXML
	private Label labelErrorNrPatrimonio;

	@FXML
	private Button btSave;

	@FXML
	private Button btCancel;

	private ObservableList<Equipamento> obsList;

	public void setPatrimonio(Patrimonio entity) {
		this.entity = entity;
	}

	public void setServices(PatrimonioService service, EquipamentoService equipamentoService, boolean insert) {
		this.service = service;
		this.equipamentoService = equipamentoService;
		this.service.Insert = insert;
	}

	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}

	@FXML
	public void onBtSaveAction(ActionEvent event) {
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
		} catch (ValidationException e) {
			setErrorMessages(e.getErrors());
		} catch (DbException e) {
			Alerts.showAlert("Erro ao salvar objeto", null, e.getMessage(), AlertType.ERROR);
		}
	}

	private void notifyDataChangeListeners() {
		for (DataChangeListener listener : dataChangeListeners) {
			listener.onDataChanged();
		}
	}

	private Patrimonio getFormData() {
		Patrimonio obj = new Patrimonio();

		ValidationException exception = new ValidationException("Erro de Validação");

		if (txtNrPatrimonio.getText() == null || txtNrPatrimonio.getText().trim().equals("")) {
			exception.addError("nrPatrimonio", "N° Patrimônio nulo");
		}
		obj.setNumero(Utils.tryParseToLong(txtNrPatrimonio.getText()));

		if (txtDescricao.getText() == null || txtDescricao.getText().trim().equals("")) {
			exception.addError("descricao", "Descrição nula");
		}
		obj.setDescricao(txtDescricao.getText());

		obj.setFabricante(txtFabricante.getText());
		
		obj.setMarca(txtMarca.getText());
		
		obj.setCondicaoUso(Utils.tryParseToByte(txtCondicaoUso.getText()));
		
		if (exception.getErrors().size() > 0) {
			throw exception;
		}
		
		obj.setTipEquip(comboBoxEquipamento.getValue());
		
		return obj;

	}

	@FXML
	public void onBtCancelAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}

	private void initializeNodes() {
		Constraints.setTextFieldInteger(txtNrPatrimonio);
		Constraints.setTextFieldMaxLength(txtDescricao, 50);
		Constraints.setTextFieldInteger(txtCondicaoUso);
		
		initializeComboBoxEquipamento();

		// Utils.formatDatePicker(dpAnyDate, "dd/MM/yyyy");
	}

	public void updateFormData() {
		if (entity == null) {
			throw new IllegalStateException("Entidade está vazia");
		}

		txtNrPatrimonio.setText(String.valueOf(entity.getNumero()));
		if (txtNrPatrimonio.getText() == null || txtNrPatrimonio.getText().trim().equals("")) {
			txtNrPatrimonio.setEditable(true);
		}
		else {
			txtNrPatrimonio.setEditable(false);
		}
		
		txtDescricao.setText(entity.getDescricao());

		txtFabricante.setText(entity.getFabricante());

		txtMarca.setText(entity.getMarca());
		
		txtCondicaoUso.setText(String.valueOf(entity.getCondicaoUso()));
		
		if (entity.getTipEquip() == null) {
			comboBoxEquipamento.getSelectionModel().selectFirst();
		}
		else {
			comboBoxEquipamento.setValue(entity.getTipEquip());
		}

		// dpAnyDate.setValue( LocalDate.ofinstant(entity.getAnyDate(),
		// ZoneId.systemdfault()));
	}

	public void loadAssociatedObjects() {
		if (equipamentoService == null) {
			throw new IllegalStateException("Lista de Tipos de Equipamentos está vazia");
		}
		List<Equipamento> list = equipamentoService.findAll();
		obsList = FXCollections.observableArrayList(list);
		comboBoxEquipamento.setItems(obsList);
	}

	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();

		labelErrorNrPatrimonio.setText((fields.contains("nrPatrimonio") ? errors.get("nrPatrimonio") : "" ));
		labelErrorDescricao.setText((fields.contains("descricao") ? errors.get("descricao") : "" ));
		
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

}
