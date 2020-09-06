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
import model.entities.RetornoEmprestimo;
import model.exceptions.ValidationException;
import model.services.RetornoEmprestimoService;

public class RetornoEmprestimoFormController implements Initializable {

    @FXML
    private Label lblChaveRetorno;
    
    @FXML
    private Label lblDtRetorno;

    @FXML
    private Label lblRecebedor;

    @FXML
    private Label lblErrorDtRetorno;

    @FXML
    private Label lblErrorRecebedor;

    @FXML
    private TextField txtChaveRetorno;

    @FXML
    private DatePicker dpDtRetorno;

    @FXML
    private TextField txtRecebedor;

    @FXML
    private Button btCancel;

    @FXML
    private Button btSave;

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
		
//		obj.setDtRetorno(txtDtRetorno.getText());
		if (dpDtRetorno.getValue() == null || dpDtRetorno.getValue().toString().equals("") ) {
			exception.addError("DtRetorno", "Data do retorno do empréstimo não pode ser nula");
		}
		else {
			Instant instant = Instant.from(dpDtRetorno.getValue().atStartOfDay(ZoneId.systemDefault()));
			obj.setDtRetornoEmpr(Date.from(instant));
		}
		
		if(txtRecebedor.getText() == null || txtRecebedor.getText().trim().equals("")){
			exception.addError("Recebedor", "Nome de quem recebed não pode ser nulo");
		}
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
		Utils.formatDatePicker(dpDtRetorno, "dd/MM/yyyy");
	}
	
	public void updateFormData() {
		if (entity == null) {
			throw new IllegalStateException ("Entidade está vazia");
		}
		
		txtChaveRetorno.setText(String.valueOf(entity.getRetorno()));	
		if (entity.getDtRetornoEmpr() != null) {
			dpDtRetorno.setValue(LocalDate.ofInstant(entity.getDtRetornoEmpr().toInstant(), ZoneId.systemDefault()));
		}
		txtRecebedor.setText(entity.getRecebedor());
	}
	
	private void setErrorMessages(Map<String, String> errors ) {
		Set<String> fields = errors.keySet();
		lblErrorDtRetorno.setText(fields.contains("DtRetorno")? errors.get("DtRetorno"): "");
		lblErrorRecebedor.setText(fields.contains("Recebedor")? errors.get("Recebedor"): "");

	}
}
