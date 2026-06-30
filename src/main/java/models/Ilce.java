/**
 *
 * @author [ömer hamza elmas] - hamza.elmas@ogr.sakarya.edu.tr
 * @since 17 Nisan 2026
 * <p>
 * İlçeleri temsil ediyor. İçindeki mahallelerin nüfusunu toplayıp ilçenin toplam nüfusunu veriyor.
 * </p>
 */
package models;

import com.github.javafaker.Faker;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Ilce {
    private String ad;
    private List<Mahalle> mahalleler;

    // Normal başlangıç
    public Ilce(Faker faker, Random random, int mahalleSayisi, int mahalleBasinaNufus) {
        this.ad = faker.address().cityName() + " İlçesi";
        this.mahalleler = new ArrayList<>();

        for (int i = 0; i < mahalleSayisi; i++) {
            this.mahalleler.add(new Mahalle(faker, random, mahalleBasinaNufus));
        }
    }

    // YENİ: Bölünürken taşınan mahalleleri kabul eden kurucu metod
    public Ilce(String ad, List<Mahalle> tasinanMahalleler) {
        this.ad = ad;
        this.mahalleler = tasinanMahalleler;
    }

    public int getNufus() {
        int toplamNufus = 0;
        for (int i = 0; i < mahalleler.size(); i++) {
            toplamNufus += mahalleler.get(i).getNufus();
        }
        return toplamNufus;
    }

    public void turlariIslet(int artisKatsayisi, Faker faker, Random random) {
        for (Mahalle mahalle : mahalleler) {
            mahalle.nufusArtir(artisKatsayisi, faker, random);
            mahalle.yaslariArtir();
        }
    }

    public String getAd() { return ad; }
    public List<Mahalle> getMahalleler() { return mahalleler; }
}