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
import model.entities.Emprestimo;
import model.exceptions.ValidationException;
import model.services.EmprestimoService;

public class EmprestimoFormController implements Initializable{

	@FXML
	private Label lblChaveEmprestimo;

	@FXML
	private Label lblDtEmprestimo;

	@FXML
	private Label lblResponsavel;
	
	@FXML
	private Label lblSetor;

	@FXML
	private DatePicker dpDatEmprestimo;
	
	@FXML
	private TextField txtResponsavel;

	@FXML
	private TextField txtSetor;

	@FXML
	private TextField txtChaveEmprestimo;

	@FXML
	private Label lblErrorResponsavel;

	@FXML
	private Label lblErrorDatEmprestimo;

	@FXML
	private Button btCancel;

	@FXML
	private Button btSave;

	private EmprestimoService service;

	private Emprestimo entity;

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
	
	public void setEmprestimo(Emprestimo entity) {
		this.entity = entity;
	}
	
	public void setEmprestimoService(EmprestimoService service) {
		this.service = service;	
	}
	
	private Emprestimo getFormData() {
		Emprestimo obj = new Emprestimo();
		
		ValidationException exception = new ValidationException("Erro de Validação");			
		
		obj.setEmprestimo(Utils.tryParseToInt(txtChaveEmprestimo.getText()));		
		
		if (dpDatEmprestimo.getValue() == null) {
			exception.addError("DtEmprestimo", "Data da Guia não pode ser nula");
		}
		else {
			Instant instant = Instant.from(dpDatEmprestimo.getValue().atStartOfDay(ZoneId.systemDefault()));
			obj.setDatEmprestimo(Date.from(instant));
		}
		
		if(txtResponsavel.getText() == null || txtResponsavel.getText().trim().equals("")){
			exception.addError("Responsavel", "Nome do Responsável não pode ser nulo");
		}
		obj.setResponsavel(txtResponsavel.getText());
		
		obj.setSetor(txtSetor.getText());
		
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
		Constraints.setTextFieldInteger(txtChaveEmprestimo);
		Utils.formatDatePicker(dpDatEmprestimo, "dd/MM/yyyy");
	}
	
	public void updateFormData() {
		if (entity == null) {
			throw new IllegalStateException ("Entidade está vazia");
		}
		
		txtChaveEmprestimo.setText(String.valueOf(entity.getEmprestimo()));	
//		txtDtEmprestimo.setText(entity.getDtEmprestimo());
		if (entity.getDatEmprestimo() != null) {
			dpDatEmprestimo.setValue(LocalDate.ofInstant(entity.getDatEmprestimo().toInstant(), ZoneId.systemDefault()));
		}
		txtSetor.setText(entity.getSetor());
		txtResponsavel.setText(entity.getResponsavel());
	}
	
	private void setErrorMessages(Map<String, String> errors ) {
		Set<String> fields = errors.keySet();
		
		lblErrorDatEmprestimo.setText(fields.contains("DtEmprestimo")? errors.get("DtEmprestimo"): "");
		lblErrorResponsavel.setText(fields.contains("Responsavel")? errors.get("Responsavel"): "");
		
	}


}
