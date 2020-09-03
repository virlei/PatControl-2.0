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
import model.entities.RetornoEmprestimo;
import model.exceptions.ValidationException;
import model.services.RetornoEmprestimoService;

public class RetornoEmprestimoFormController implements Initializable {

    @FXML
    private Label labelDtRetorno;

    @FXML
    private Label lblErrorGuia;

    @FXML
    private Label labelRetorno;

    @FXML
    private TextField txtChaveRetorno;

    @FXML
    private Button btCancel;

    @FXML
    private TextField txtDtRetorno;

    @FXML
    private Button btSave;

    @FXML
    private TextField txtRecebedor;

    @FXML
    private Label labelChaveRetorno;
    
    private RetornoEmprestimoService service;
	
  	private RetornoEmprestimo entity;
  	
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
	
	public void setRetornoEmprestimo(RetornoEmprestimo entity) {
		this.entity = entity;
	}
	
	public void setRetornoEmprestimoService(RetornoEmprestimoService service) {
		this.service = service;	
	}
	
	private RetornoEmprestimo getFormData() {
		RetornoEmprestimo obj = new RetornoEmprestimo();
		
		ValidationException exception = new ValidationException("Erro de Validação");			
		
		obj.setRetorno(Utils.tryParseToInt(txtChaveRetorno.getText()));		
		
		obj.setDtRetorno(txtDtRetorno.getText());
		
		obj.setRecebedor(txtRecebedor.getText());
				
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
		Constraints.setTextFieldInteger(txtChaveRetorno);
		Constraints.setTextFieldMaxLength(txtDtRetorno, 30);
	}
	
	public void updateFormData() {
		if (entity == null) {
			throw new IllegalStateException ("Entidade está vazia");
		}
		
		txtChaveRetorno.setText(String.valueOf(entity.getRetorno()));	
		txtDtRetorno.setText(entity.getDtRetorno());
		txtRecebedor.setText(entity.getRecebedor());
	}
	
	private void setErrorMessages(Map<String, String> errors ) {
		Set<String> fields = errors.keySet();
		
		if (fields.contains("Guia")) {
			lblErrorGuia.setText(errors.get("Guia"));
		}

	}
}
