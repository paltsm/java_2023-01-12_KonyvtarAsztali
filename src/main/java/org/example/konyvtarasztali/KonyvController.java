package org.example.konyvtarasztali;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class KonyvController {
	@FXML
	private Button torlesButton;
	@FXML
	private TableView<Konyv> tablazat;
	@FXML
	private TableColumn titleCol;
	@FXML
	private TableColumn authorCol;
	@FXML
	private TableColumn publish_yearCol;
	@FXML
	private TableColumn page_countCol;
	private KonyvDB db;

	public void initialize() {
		titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
		authorCol.setCellValueFactory(new PropertyValueFactory<>("author"));
		publish_yearCol.setCellValueFactory(new PropertyValueFactory<>("publish_year"));
		page_countCol.setCellValueFactory(new PropertyValueFactory<>("page_count"));
		Platform.runLater(() -> {
			try {
				db = new KonyvDB();
				konyvekListaz();
			} catch (SQLException e) {
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle("Adatbázis Hiba");
				alert.setContentText(e.toString());
				alert.showAndWait();

				Platform.exit();
			}
		});
	}

	private void konyvekListaz() throws SQLException {
		List<Konyv> konyvek = db.konyvlistaz();
		tablazat.getItems().clear();
		tablazat.getItems().addAll(konyvek);
	}

	@FXML
	public void ontorlesClick() {
		Konyv kivalasztottKonyv = getKivalasztottKonyv();
		if (kivalasztottKonyv == null) return;
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Biztos szeretné törölni a kiválasztott könyvet? " + kivalasztottKonyv.getTitle());
		Optional<ButtonType> valasztas = alert.showAndWait();
		if (valasztas.isEmpty() || !valasztas.get().equals(ButtonType.OK) && !valasztas.get().equals(ButtonType.YES)) {
			return;
		}
		try {
			if (db.konyvTorlese(kivalasztottKonyv)) {
//				new Alert(Alert.AlertType.WARNING, "sikeres törlés").showAndWait();
				new Alert(Alert.AlertType.INFORMATION, "sikeres törlés").showAndWait();
			} else {
				new Alert(Alert.AlertType.WARNING, "sikertelen törlés").showAndWait();
			}
			konyvekListaz();
		} catch (SQLException e) {
			Alert alert2 = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Adatbázis Hiba");
			alert.setContentText(e.toString());
			alert.showAndWait();
			Platform.exit();
		}
	}

	private Konyv getKivalasztottKonyv() {
		int selectedIndex = tablazat.getSelectionModel().getSelectedIndex();
		if (selectedIndex == -1) {
			new Alert(Alert.AlertType.INFORMATION, "Törléshez előbb válasszon ki könyvet").showAndWait();
			return null;
		}
		return (Konyv) tablazat.getSelectionModel().getSelectedItem();
	}
}