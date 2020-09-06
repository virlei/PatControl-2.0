package gui;

import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
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
import model.entities.GForn;
import model.exceptions.ValidationException;
import model.services.GFornService;

public class GFornFormController implements Initializable {

	private GForn entity;
	
	@FXML
	private Label labelNrGuia;

	@FXML
	private Label labelPKey;

	@FXML
	private TextField txtPKey;

	@FXML
	private TextField txtNrGuia;

	@FXML
	private DatePicker dpDtForn;

	@FXML
	private Label lblErrorNrGuia;

	@FXML
	private Label lblErrorDtForn;

	@FXML
	private Button btSave;

	@FXML
	private Button btCancel;

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
			exception.addError("NrGuia", "Número da Guia não pode ser nulo");
		}
		
		obj.setNrGuia(Utils.tryParseToInt(txtNrGuia.getText()));
		
		if (dpDtForn.getValue() == null) {
			exception.addError("DtGuia", "Data da Guia não pode ser nula");
		}
		else {
			Instant instant = Instant.from(dpDtForn.getValue().atStartOfDay(ZoneId.systemDefault()));
			obj.setDtGForn(Date.from(instant));
		}
		
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
		Constraints.setTextFieldInteger(txtPKey);
		Constraints.setTextFieldInteger(txtNrGuia);
		Utils.formatDatePicker(dpDtForn, "dd/MM/yyyy");
	}
	
	public void updateFormData() {
		if (entity == null) {
			throw new IllegalStateException ("Entidade está vazia");
		}
		
		txtPKey.setText(String.valueOf(entity.getPkGForn()));
		txtNrGuia.setText(String.valueOf(entity.getNrGuia()));

		if (entity.getDtGForn() != null) {
			dpDtForn.setValue(LocalDate.ofInstant(entity.getDtGForn().toInstant(), ZoneId.systemDefault()));
		}
	}
	
	private void setErrorMessages(Map<String, String> errors ) {
		Set<String> fields = errors.keySet();
		
		lblErrorNrGuia.setText((fields.contains("NrGuia")? errors.get("NrGuia"): ""));
		lblErrorDtForn.setText((fields.contains("DtGuia")? errors.get("DtGuia"): ""));
		
	}

}
