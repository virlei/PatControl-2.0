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
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import model.entities.Devolucao;
import model.exceptions.ValidationException;
import model.services.DevolucaoService;

public class DevolucaoFormController implements Initializable{

	@FXML
	private Label lblPkDevolucao;

	@FXML
	private Label lblNumSei;

    @FXML
    private Label lblMotivo;

    @FXML
    private Label lblDtDevolucao;

    @FXML
    private TextField txtChaveDevolucao;

	@FXML
	private DatePicker dpDtDevolucao;

	@FXML
	private Label lblErrorDtDevolucao;

    @FXML
    private Button btCancel;

    @FXML
    private TextField txtMotivo;

 
    @FXML
    private Button btSave;

    @FXML
    private TextField txtNumSei;
    
	private DevolucaoService service;
	
	private Devolucao entity;
	
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
	
	public void setDevolucao(Devolucao entity) {
		this.entity = entity;
	}
	
	public void setDevolucaoService(DevolucaoService service) {
		this.service = service;	
	}
	
	private Devolucao getFormData() {
		Devolucao obj = new Devolucao();
		
		ValidationException exception = new ValidationException("Erro de Validação");			
		
		obj.setDevolucao(Utils.tryParseToInt(txtChaveDevolucao.getText()));
		
//		if (dpDtDevolucao.getValue() == null ) {
		if (dpDtDevolucao.getValue() == null  || dpDtDevolucao.getValue().toString().trim().equals("") ) {
			exception.addError("DtDevolucao", "Data da Guia não pode ser nula");
		}
		else {
			Instant instant = Instant.from(dpDtDevolucao.getValue().atStartOfDay(ZoneId.systemDefault()));
			obj.setDtDevolucao(Date.from(instant));
		}
		
		obj.setNumSei(txtNumSei.getText());
		
		obj.setMotivo(txtMotivo.getText());
		
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
		Constraints.setTextFieldInteger(txtChaveDevolucao);
		Utils.formatDatePicker(dpDtDevolucao, "dd/MM/yyyy");
		
	}
	
	public void updateFormData() {
		if (entity == null) {
			throw new IllegalStateException ("Entidade está vazia");
		}
		
		txtChaveDevolucao.setText(String.valueOf(entity.getDevolucao()));	
		if (entity.getDtDevolucao() != null) {
			dpDtDevolucao.setValue(LocalDate.ofInstant(entity.getDtDevolucao().toInstant(), ZoneId.systemDefault()));
		}
		txtNumSei.setText(entity.getNumSei());
		txtMotivo.setText(entity.getMotivo());
	}
	
	private void setErrorMessages(Map<String, String> errors ) {
		Set<String> fields = errors.keySet();
		
		if (fields.contains("DtDevolucao")) {
			lblErrorDtDevolucao.setText(errors.get("DtDevolucao"));
		}
	}
}
