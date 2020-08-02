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
	private TextField txtId;
	
	@FXML
	private TextField txtName;
	
	@FXML
	private Label labelErrorName;
	
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
			throw new IllegalStateException("Servi�o nulo");
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
		
		ValidationException exception = new ValidationException("Erro de Valida��o");
		
		obj.setNumero(Utils.tryParseToLong(txtId.getText()));
		
		if (txtName.getText() == null || txtName.getText().trim().equals("")) {
			exception.addError("name", "Campo n�o pode estar vazio");
		}
		obj.setDescricao(txtName.getText());
		
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
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtName, 30);
	}
	
	public void updateFormData() {
		if (entity == null) {
			throw new IllegalStateException ("Entidade est� vazia");
		}
		
		//txtId.setText(String.valueOf(entity.getId()));
		
		txtId.setText(String.valueOf(entity.getNumero()));
				
		txtName.setText(entity.getDescricao());
	}
	
	private void setErrorMessages(Map<String, String> errors ) {
		Set<String> fields = errors.keySet();
		
		if (fields.contains("name")) {
			labelErrorName.setText(errors.get("name"));
		}
	}

}
