/**
 *
 * @author [ömer hamza elmas] - hamza.elmas@ogr.sakarya.edu.tr
 * @since 17 Nisan 2026
 * <p>
 * Mahalleleri temsil ediyor. Kişilerin eklendiği ve tur sonunda yaşlarının artırıldığı sınıf burası.
 * </p>
 */
package models;

import com.github.javafaker.Faker;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Mahalle {
    private String ad;
    private int nufus;
    private List<Kisi> kisiler;

    // Normal başlangıç için kurucu metod
    public Mahalle(Faker faker, Random random, int baslangicNufusu) {
        this.ad = faker.address().streetName() + " Mahallesi"; 
        this.nufus = baslangicNufusu;
        this.kisiler = new ArrayList<>();

        for (int i = 0; i < baslangicNufusu; i++) {
            this.kisiler.add(new Kisi(faker, random));
        }
    }

    // YENİ: Şehir bölünürken taşınan kişileri kabul eden kurucu metod
    public Mahalle(Faker faker, List<Kisi> tasinanKisiler) {
        this.ad = faker.address().streetName() + " Mahallesi";
        this.kisiler = tasinanKisiler;
        this.nufus = tasinanKisiler.size();
    }

    public void yaslariArtir() {
        for (Kisi kisi : kisiler) {
            kisi.yasArtir();
        }
    }

    public void nufusArtir(int artisKatsayisi, Faker faker, Random random) {
        int eklenecekKisiSayisi;
        if (artisKatsayisi == 0) {
            eklenecekKisiSayisi = 1; 
        } else {
            int yeniNufus = this.nufus * artisKatsayisi;
            eklenecekKisiSayisi = yeniNufus - this.nufus; 
        }

        for (int i = 0; i < eklenecekKisiSayisi; i++) {
            this.kisiler.add(new Kisi(faker, random));
        }
        this.nufus += eklenecekKisiSayisi; 
    }

    // YENİ: Bölünme sonrası eski mahallenin nüfusunu güncellemek için
    public void nufusGuncelle() {
        this.nufus = this.kisiler.size();
    }

    public String getAd() { return ad; }
    public int getNufus() { return nufus; }
    public List<Kisi> getKisiler() { return kisiler; }
}