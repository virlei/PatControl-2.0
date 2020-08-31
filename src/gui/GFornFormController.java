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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import model.entities.GForn;
import model.exceptions.ValidationException;
import model.services.GFornService;

public class GFornFormController {

    @FXML
    private Label labelNrGuia;

    @FXML
    private Button btCancel;

    @FXML
    private TextField txtDtForn;

    @FXML
    private Button btSave;

    @FXML
    private Label labelDtForn;

    @FXML
    private TextField txtNrGuia;

    @FXML
    private Label labelErrorName;    

    @FXML
    private Label labelPKey;

    @FXML
    private TextField txtPKey;
    
    private GForn entity;
	
	private GFornService service;
	
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
	
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

    @FXML
    void onBtCancelAction(ActionEvent event) {
    	Utils.currentStage(event).close();
    }
    
	private void notifyDataChangeListeners() {
		for (DataChangeListener listener : dataChangeListeners) {
			listener.onDataChanged();
		}
	}
	
	public void setGForn(GForn entity) {
		this.entity = entity;
	}
	
	public void setGFornService(GFornService service) {
		this.service = service;
	}
	
	private GForn getFormData() {
		GForn obj = new GForn();
		
		ValidationException exception = new ValidationException("Erro de Validação");

		obj.setPkGForn(Utils.tryParseToInt(txtPKey.getText()));
		
		if(txtNrGuia.getText() == null || txtNrGuia.getText().trim().equals("")){
			exception.addError("name", "Campo não pode estar vazio");
		}
		
		obj.setNrGuia(Utils.tryParseToInt(txtNrGuia.getText()));
		
		if (txtDtForn.getText() == null || txtDtForn.getText().trim().equals("")) {
			exception.addError("name", "Campo não pode estar vazio");
		}
		obj.setDtForn(txtDtForn.getText());
		
		if (exception.getErrors().size()>0) {
			throw exception;
		}
		
		return obj;		
		
	}	

	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}
	
	private void initializeNodes() {
		Constraints.setTextFieldInteger(txtPKey);
		Constraints.setTextFieldInteger(txtNrGuia);
		Constraints.setTextFieldMaxLength(txtDtForn, 30);
	}
	
	public void updateFormData() {
		if (entity == null) {
			throw new IllegalStateException ("Entidade está vazia");
		}
		
		txtPKey.setText(String.valueOf(entity.getPkGForn()));
		txtNrGuia.setText(String.valueOf(entity.getNrGuia()));		
		if (entity.getNrGuia() == null ) {
			txtNrGuia.setText("");
		}
		txtDtForn.setText(entity.getDtForn());
	}
	
	private void setErrorMessages(Map<String, String> errors ) {
		Set<String> fields = errors.keySet();
		
		if (fields.contains("name")) {
			labelErrorName.setText(errors.get("name"));
		}
	}



}
