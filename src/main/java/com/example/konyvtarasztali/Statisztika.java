package com.example.konyvtarasztali;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class Statisztika {
	private List<Konyv> konyvek;
	private KonyvDB db;

	public Statisztika(){
		listaFeltolt();
		System.out.printf("500 oldalnál hosszabb könyvek száma: %d\n",tobbmint500());
		System.out.printf(regibbmint1950());
	}
	private void listaFeltolt(){
		try {
			db = new KonyvDB();
			konyvek=db.konyvlistaz();
		} catch (SQLException e) {
			System.out.printf("hiba: \n %s",e);
			System.exit(0);
		}
	}
	private int tobbmint500(){
		int szam=0;
		for (int i = 0; i < konyvek.size(); i++) {
			if(konyvek.get(i).getPage_count()>500){
				szam++;
			}
		}
		return szam;
	}
	private String regibbmint1950(){
		for (int i = 0; i < konyvek.size(); i++) {
			if(konyvek.get(i).getPublish_year()<1950){
				return "Van 1950-nél régebbi könyv\n";
			}
		}
		return "Nincs 1950-nél régebbi könyv\n";
	}
}
