package gui;

import java.net.URL;
import java.util.ResourceBundle;

import db.DbException;
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
import model.entities.Equipamento;
import model.services.EquipamentoService;

public class TipoEquipamentoFormController implements Initializable {

	private Equipamento entity;
	
	private EquipamentoService service;
	
	
	@FXML
	private TextField txtId;
	
	@FXML
	private TextField txtName;
	
	@FXML
	private Label labelErrorName;
	
	@FXML
	private Button btSave;
	
	@FXML
	private Button btCancel;
	
	public void setEquipamento(Equipamento entity) {
		this.entity = entity;
	}
	
	public void setEquipamentoService(EquipamentoService service) {
		this.service = service;
	}
	
	@FXML
	public void onBtSaveAction (ActionEvent event) {
		if (entity == null) {
			throw new IllegalStateException("Entidade nula");
		}
		if (service == null) {
			throw new IllegalStateException("Serviço nulo");
		}
		try {
			entity = getFormData();
			service.saveOrUpdate(entity);
			Utils.currentStage(event).close();
		}
		catch (DbException e) {
			Alerts.showAlert("Erro ao salvar objeto", null, e.getMessage(), AlertType.ERROR);
		}
	}
	
	private Equipamento getFormData() {
		Equipamento obj = new Equipamento();
		
		obj.setId(Utils.tryParseToInt(txtId.getText()));
		obj.setDescricao(txtName.getText());
		
		return obj;
		
	}

	@FXML
	public void onBtCancelAction (ActionEvent event) {
		Utils.currentStage(event).close();
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}
	
	private void initializeNodes() {
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtName, 30);
	}
	
	public void updateFormData() {
		if (entity == null) {
			throw new IllegalStateException ("Entidade está vazia");
		}
		
		txtId.setText(String.valueOf(entity.getId()));
		txtName.setText(entity.getDescricao());
	}

}
