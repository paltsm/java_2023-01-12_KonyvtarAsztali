module com.example.konyvtarasztali {
	requires javafx.controls;
	requires javafx.fxml;
	requires java.sql;


	opens org.example.konyvtarasztali to javafx.fxml;
	exports org.example.konyvtarasztali;
}