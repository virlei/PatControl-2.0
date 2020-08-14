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
import model.entities.Movimentacao;
import model.exceptions.ValidationException;
import model.services.MovimentacaoService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

public class MovimentacaoFormController {
	
	private Movimentacao entity;

	private MovimentacaoService service;

	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
	
	  @FXML
	    private TextField txtDataEntrada;

	    @FXML
	    private Label labelErrorNrPatrimonio;

	    @FXML
	    private Button btCancel;

	    @FXML
	    private TextField txtNumeroGuia;

	    @FXML
	    private Button btSave;

	    @FXML
	    private Label labelErrorDescricao;

	    @FXML
	    private TextField txtDataDevolucao;

	    @FXML
	    private TextField txtPatrimonio;

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
	    
	    public void setMovimentacao(Movimentacao entity) {
			this.entity = entity;
		}
		
		public void setMovimentacaoService(MovimentacaoService service) {
			this.service = service;
		}
		
		public void subscribeDataChangeListener(DataChangeListener listener) {
			dataChangeListeners.add(listener);
		}
		
		private void notifyDataChangeListeners() {
			for (DataChangeListener listener : dataChangeListeners) {
				listener.onDataChanged();
			}
		}

		private Movimentacao getFormData() {
			Movimentacao obj = new Movimentacao();

			ValidationException exception = new ValidationException("Erro de Validação");

			if (txtNumeroGuia.getText() == null || txtNumeroGuia.getText().trim().equals("")) {
				exception.addError("numeroGuia", "Número Guia nulo");
			}
			obj.setNumeroGuia(Utils.tryParseToInt(txtNumeroGuia.getText()));

			if (txtPatrimonio.getText() == null || txtPatrimonio.getText().trim().equals("")) {
				exception.addError("patrimonio", "Patrimônio nulo");
			}
			obj.setDataDevolucao(txtDataDevolucao.getText()); 
			
			obj.setDataEntrada(txtDataEntrada.getText()); 			
			
			if (exception.getErrors().size() > 0) {
				throw exception;
			}						
			return obj;
		}

		
		public void initialize(URL url, ResourceBundle rb) {
			initializeNodes();
		}
		
		private void initializeNodes() {
			Constraints.setTextFieldInteger(txtNumeroGuia);
			Constraints.setTextFieldMaxLength(txtPatrimonio, 30);
		}
		
		public void updateFormData() {
			if (entity == null) {
				throw new IllegalStateException ("Entidade está vazia");
			}
			
			txtNumeroGuia.setText(String.valueOf(entity.getNumeroGuia()));
			txtPatrimonio.setText(String.valueOf(entity.getPatrimonio()));
		}
		
		private void setErrorMessages(Map<String, String> errors ) {
			Set<String> fields = errors.keySet();
			
			if (fields.contains("name")) {
				labelErrorDescricao.setText(errors.get("name"));
			}
		}


}
