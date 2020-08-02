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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Patrimonio;
import model.exceptions.ValidationException;
import model.services.PatrimonioService;

public class PatrimonioFormController implements Initializable {

	private Patrimonio entity;
	
	private PatrimonioService service;
	
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

//	@FXML
//	private DatePicker dpAnyDate;
	
	@FXML
	private Label labelErrorDescricao;
	
	@FXML
	private Button btSave;
	
	@FXML
	private Button btCancel;
	
	public void setPatrimonio(Patrimonio entity) {
		this.entity = entity;
	}
	
	public void setPatrimonioService(PatrimonioService service) {
		this.service = service;
	}
	
	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}
	
	@FXML
	public void onBtSaveAction (ActionEvent event) {
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
	
	private void notifyDataChangeListeners() {
		for (DataChangeListener listener : dataChangeListeners) {
			listener.onDataChanged();
		}
	}

	private Patrimonio getFormData() {
		Patrimonio obj = new Patrimonio();
		
		ValidationException exception = new ValidationException("Erro de Validação");
		
		obj.setNumero(Utils.tryParseToLong(txtNrPatrimonio.getText()));
		
		if (txtDescricao.getText() == null || txtDescricao.getText().trim().equals("")) {
			exception.addError("description", "Campo não pode estar vazio");
		}
		obj.setDescricao(txtDescricao.getText());
		
		if (exception.getErrors().size()>0) {
			throw exception;
		}
		
		return obj;
		
	}

	@FXML
	public void onBtCancelAction (ActionEvent event) {
		Utils.currentStage(event).close();
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}
	
	private void initializeNodes() {
		Constraints.setTextFieldInteger(txtNrPatrimonio);
		Constraints.setTextFieldMaxLength(txtDescricao, 50);
		
		//Utils.formatDatePicker(dpAnyDate, "dd/MM/yyyy");
	}
	
	public void updateFormData() {
		if (entity == null) {
			throw new IllegalStateException ("Entidade está vazia");
		}
		
		//txtNrPatrimonio.setText(String.valueOf(entity.getId()));
		
		txtNrPatrimonio.setText(String.valueOf(entity.getNumero()));
				
		txtDescricao.setText(entity.getDescricao());
		
		txtFabricante.setText(entity.getFabricante());
		
		txtMarca.setText(entity.getMarca());
		
		//dpAnyDate.setValue( LocalDate.ofinstant(entity.getAnyDate(), ZoneId.systemdfault()));
	}
	
	private void setErrorMessages(Map<String, String> errors ) {
		Set<String> fields = errors.keySet();
		
		if (fields.contains("description")) {
			labelErrorDescricao.setText(errors.get("description"));
		}
	}

}
