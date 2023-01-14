package com.example.konyvtarasztali;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.sql.SQLException;
import java.util.*;

import static java.lang.String.format;

public class Statisztika {
	private List<Konyv> konyvek;
	private KonyvDB db;

	public Statisztika() {
		listaFeltolt();
		System.out.printf("500 oldalnál hosszabb könyvek száma: %d\n", tobbmint500());
		System.out.printf(regibbmint1950());
		System.out.printf(leghosszabb());
		System.out.printf(legtobbiro());
		Scanner sc = new Scanner(System.in);
		System.out.printf("Adjon meg egy könyv címet: ");
		String title = sc.nextLine();
		System.out.printf(kereses(title));
	}

	private void listaFeltolt() {
		try {
			db = new KonyvDB();
			konyvek = db.konyvlistaz();
		} catch (SQLException e) {
			System.out.printf("hiba: \n %s", e);
			System.exit(0);
		}
	}

	private int tobbmint500() {
		int szam = 0;
		for (Konyv konyv : konyvek) {
			if (konyv.getPage_count() > 500) {
				szam++;
			}
		}
		return szam;
	}

	private String regibbmint1950() {
		for (Konyv konyv : konyvek) {
			if (konyv.getPublish_year() < 1950) {
				return "Van 1950-nél régebbi könyv\n";
			}
		}
		return "Nincs 1950-nél régebbi könyv\n";
	}

	private String leghosszabb() {
		Konyv max = new Konyv(0, "a", "a", 1000, 0);
		for (Konyv konyv : konyvek) {
			if (konyv.getPage_count() > max.getPage_count()) {
				max = konyv;
			}

		}
		if (max.getPage_count() == 0) {
			return "nincs könyv az adatbázisban";
		}
		return format("A leghosszabb könyv:\n" +
				"\tSzerző: %s\n" +
				"\tCím: %s\n" +
				"\tKiadás éve: %d\n" +
				"\tOldalszám: %d\n", max.getAuthor(), max.getTitle(), max.getPublish_year(), max.getPage_count());
	}

	private String legtobbiro() {
		HashMap<String, Integer> szerzok = new HashMap<>();
		for (Konyv konyv : konyvek) {
			if (szerzok.containsKey(konyv.getAuthor())) {
				szerzok.put(konyv.getAuthor(), szerzok.get(konyv.getAuthor()) + 1);
			} else {
				szerzok.put(konyv.getAuthor(), 1);
			}
		}
		String szerzo = "";
		int szam = 0;
		for (Map.Entry<String, Integer> entry : szerzok.entrySet()) {
			if (entry.getValue() > szam) {
				szerzo = entry.getKey();
				szam = entry.getValue();
			}
		}
		return "A legtöbb könyvvel rendelkező szerző: " + szerzo+"\n";
	}

	private String kereses(String title) {
		for (Konyv konyv : konyvek) {
			if (konyv.getTitle().equals(title)) {
				return "A megadott könyv szerzője: " + konyv.getAuthor();
			}
		}
		return "nincs ilyen könyv";
	}
}
