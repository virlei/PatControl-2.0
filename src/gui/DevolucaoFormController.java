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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import model.entities.Devolucao;
import model.exceptions.ValidationException;
import model.services.DevolucaoService;

public class DevolucaoFormController implements Initializable{

    @FXML
    private Label labelNumSei;

    @FXML
    private Label lblErrorGuia;

    @FXML
    private Label labelMotivo;

    @FXML
    private TextField txtChaveDevolucao;

    @FXML
    private TextField txtDtDevolucao;

    @FXML
    private Button btCancel;

    @FXML
    private TextField txtMotivo;

    @FXML
    private Label labelChaveDevolucao;

    @FXML
    private Button btSave;

    @FXML
    private Label labelDtDevolucao;

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
	
	public void setDevolucaoService(DevolucaoService service, boolean insert) {
		this.service = service;
		DevolucaoService.Insert = insert;
	}
	
	private Devolucao getFormData() {
		Devolucao obj = new Devolucao();
		
		ValidationException exception = new ValidationException("Erro de Validação");			
		
		obj.setDevolucao(Utils.tryParseToInt(txtChaveDevolucao.getText()));		
		
		obj.setDatDevolucao(txtDtDevolucao.getText());
		
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
		Constraints.setTextFieldMaxLength(txtDtDevolucao, 30);
	}
	
	public void updateFormData() {
		if (entity == null) {
			throw new IllegalStateException ("Entidade está vazia");
		}
		
		txtChaveDevolucao.setText(String.valueOf(entity.getDevolucao()));
		if (txtChaveDevolucao.getText() == null || txtChaveDevolucao.getText().trim().equals("")) {
			txtChaveDevolucao.setEditable(true);
		}
		else {
			txtChaveDevolucao.setEditable(false);
		}
		txtDtDevolucao.setText(entity.getDatDevolucao());
		txtNumSei.setText(entity.getNumSei());
		txtMotivo.setText(entity.getMotivo());
	}
	
	private void setErrorMessages(Map<String, String> errors ) {
		Set<String> fields = errors.keySet();
		
		if (fields.contains("Guia")) {
			lblErrorGuia.setText(errors.get("Guia"));
		}
	}

}
