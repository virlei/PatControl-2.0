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
import model.entities.Local;
import model.exceptions.ValidationException;
import model.services.LocalService;

public class LocalFormController implements Initializable {
	
	private Local entity;
	
	private LocalService service;
	
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
	
		@FXML
	    private TextField txtLocal;

	    @FXML
	    private Label labelDescricao;

	    @FXML
	    private Label labelLocal;

	    @FXML
	    private Button btCancel;

	    @FXML
	    private TextField txtDescricao;

	    @FXML
	    private Button btSave;

	    @FXML
	    private Label labelErrorName;
	    
	    public void subscribeDataChangeListener(DataChangeListener listener) {
			dataChangeListeners.add(listener);
		}

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
	    
		private void notifyDataChangeListeners() {
			for (DataChangeListener listener : dataChangeListeners) {
				listener.onDataChanged();
			}
		}

	    @FXML
	    void onBtCancelAction(ActionEvent event) {
	    	Utils.currentStage(event).close();
	    }

	
	public void setLocal(Local entity) {
		this.entity = entity;
	}
	
	public void setLocalService(LocalService service) {
		this.service = service;
	}
	
	private Local getFormData() {
		Local obj = new Local();
		
ValidationException exception = new ValidationException("Erro de Validação");
		
		obj.setIdLocal(Utils.tryParseToInt(txtLocal.getText()));
		
		if (txtDescricao.getText() == null || txtDescricao.getText().trim().equals("")) {
			exception.addError("name", "Campo não pode estar vazio");
		}
		obj.setDescricaoLocal(txtDescricao.getText());
		
		if (exception.getErrors().size()>0) {
			throw exception;
		}
		
		return obj;
		
		
	}

	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}
	
	private void initializeNodes() {
		Constraints.setTextFieldInteger(txtLocal);
		Constraints.setTextFieldMaxLength(txtDescricao, 30);
	}
	
	public void updateFormData() {
		if (entity == null) {
			throw new IllegalStateException ("Entidade está vazia");
		}
		
		txtLocal.setText(String.valueOf(entity.getIdLocal()));
		txtDescricao.setText(entity.getDescricaoLocal());
	}
	
	private void setErrorMessages(Map<String, String> errors ) {
		Set<String> fields = errors.keySet();
		
		if (fields.contains("name")) {
			labelErrorName.setText(errors.get("name"));
		}
	}



}
