package gui;

import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Equipamento;

public class TipoEquipamentoListController implements Initializable {

	@FXML
	private TableView<Equipamento> tableViewEquipamento;
	
	@FXML
	private TableColumn<Equipamento, Integer> tableColumnId;
	
	@FXML
	private TableColumn<Equipamento, String> tableColumnName;
	
	@FXML
	private Button btNew;
	
	@FXML
	public void onBtNewAction() {
		System.out.println("onBtNewAction");
	}
	
	
	
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}

	private void initializeNodes() {

		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("PK_Equipamento"));
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("TXT_Descricao"));
		
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewEquipamento.prefHeightProperty().bind(stage.heightProperty());
		
	}

}
