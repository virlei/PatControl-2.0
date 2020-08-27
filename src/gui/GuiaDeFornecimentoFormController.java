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
import model.entities.GuiaDeFornecimento;
import model.exceptions.ValidationException;
import model.services.GuiaDeFornecimentoService;

public class GuiaDeFornecimentoFormController implements Initializable {
	
	private GuiaDeFornecimento entity;
	
	private GuiaDeFornecimentoService service;
	
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
	
		@FXML
	    private TextField txtPKey;

		@FXML
	    private TextField txtNrGuia;

		@FXML
	    private DatePicker dpDtFornecimento;

	    @FXML
	    private Label lblNrGuia;

	    @FXML
	    private Label lblDtFornecimento;

	    @FXML
	    private Button btCancel;

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

	
	public void setGuiaDeFornecimento(GuiaDeFornecimento entity) {
		this.entity = entity;
	}
	
	public void setGuiaDeFornecimentoService(GuiaDeFornecimentoService service) {
		this.service = service;
	}
	
	private GuiaDeFornecimento getFormData() {
		GuiaDeFornecimento obj = new GuiaDeFornecimento();
		
		ValidationException exception = new ValidationException("Erro de Validação");
		
//		obj.setIdGuiaDeFornecimento(Utils.tryParseToInt(txtGuiaDeFornecimento.getText()));
//		
//		if (txtDescricao.getText() == null || txtDescricao.getText().trim().equals("")) {
//			exception.addError("name", "Campo não pode estar vazio");
//		}
//		obj.setDescricaoGuiaDeFornecimento(txtDescricao.getText());
//		
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
		Constraints.setTextFieldInteger(txtNrGuia);
	
		Utils.formatDatePicker(dpDtFornecimento, "dd/MM/yyyy");
	}
	
	public void updateFormData() {
		if (entity == null) {
			throw new IllegalStateException ("Entidade está vazia");
		}
		
//		txtGuiaDeFornecimento.setText(String.valueOf(entity.getIdGuiaDeFornecimento()));
//		txtDescricao.setText(entity.getDescricaoGuiaDeFornecimento());
//		
	}
	
	private void setErrorMessages(Map<String, String> errors ) {
		Set<String> fields = errors.keySet();
		
		if (fields.contains("name")) {
			labelErrorName.setText(errors.get("name"));
		}
	}

}
