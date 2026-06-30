/**
 *
 * @author [ömer hamza elmas] - hamza.elmas@ogr.sakarya.edu.tr
 * @since 17 Nisan 2026
 * <p>
 * Şehirleri temsil ediyor. İçinde ilçeler var. Nüfus 1000'i geçince bölünme işlemini bu sınıfta hallediyorum.
 * </p>
 */
package models;

import com.github.javafaker.Faker;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Sehir {
    private String ad;
    private List<Ilce> ilceler;

    public Sehir(Faker faker, Random random, int ilceSayisi, int ilceBasinaMahalle, int mahalleBasinaNufus) {
        this.ad = faker.address().city(); 
        this.ilceler = new ArrayList<>();
        
        for (int i = 0; i < ilceSayisi; i++) {
            this.ilceler.add(new Ilce(faker, random, ilceBasinaMahalle, mahalleBasinaNufus));
        }
    }

    public Sehir(String ad, List<Ilce> transferEdilenIlceler) {
        this.ad = ad;
        this.ilceler = transferEdilenIlceler;
    }

    public int getNufus() {
        int toplamNufus = 0;
        for (Ilce ilce : ilceler) {
            toplamNufus += ilce.getNufus();
        }
        return toplamNufus;
    }

    public void turuIslet(Faker faker, Random random) {
        int guncelNufus = getNufus();
        int birler = guncelNufus % 10;
        int onlar = (guncelNufus / 10) % 10;
        int artisKatsayisi = birler + onlar;

        for (Ilce ilce : ilceler) {
            ilce.turlariIslet(artisKatsayisi, faker, random);
        }
    }

    // GÜNCELLENEN BÖLÜNME MANTIĞI
    public Sehir bolunGerekirse(Faker faker) {
        if (getNufus() >= 1000) { 
            List<Ilce> yeniSehirIlceleri = new ArrayList<>();

            if (ilceler.size() > 1) {
                // Kural 1: Birden fazla ilçe varsa ilçeleri ikiye böl
                int tasinacakIlceSayisi = ilceler.size() / 2; 
                for (int i = 0; i < tasinacakIlceSayisi; i++) {
                    yeniSehirIlceleri.add(ilceler.remove(ilceler.size() - 1));
                }
            } else {
              
                Ilce tekIlce = ilceler.get(0);
                
                if (tekIlce.getMahalleler().size() > 1) {
                    // Mahalleleri böl
                    int tasinacakMh = tekIlce.getMahalleler().size() / 2;
                    List<Mahalle> yeniMh = new ArrayList<>();
                    for (int i = 0; i < tasinacakMh; i++) {
                        yeniMh.add(tekIlce.getMahalleler().remove(tekIlce.getMahalleler().size() - 1));
                    }
                    yeniSehirIlceleri.add(new Ilce(faker.address().cityName() + " İlçesi", yeniMh));
                } else {
                    // Sadece 1 İlçe ve 1 Mahalle var. Kişileri tam ortadan ikiye bölüyoruz!
                    Mahalle tekMahalle = tekIlce.getMahalleler().get(0);
                    int tasinacakKisi = tekMahalle.getKisiler().size() / 2;
                    List<Kisi> yeniKisiler = new ArrayList<>();
                    
                    for (int i = 0; i < tasinacakKisi; i++) {
                        yeniKisiler.add(tekMahalle.getKisiler().remove(tekMahalle.getKisiler().size() - 1));
                    }
                    tekMahalle.nufusGuncelle(); // Eski mahallenin nüfusunu güncelle
                    
                    // Yeni şehre yeni bir ilçe ve mahalle verip kişileri içine koyuyoruz
                    Mahalle yeniMahalle = new Mahalle(faker, yeniKisiler);
                    List<Mahalle> yeniMhList = new ArrayList<>();
                    yeniMhList.add(yeniMahalle);
                    yeniSehirIlceleri.add(new Ilce(faker.address().cityName() + " İlçesi", yeniMhList));
                }
            }

            String yeniSehirAdi = faker.address().city();
            return new Sehir(yeniSehirAdi, yeniSehirIlceleri);
        }
        return null; 
    }

    public String getAd() { return ad; }
    public List<Ilce> getIlceler() { return ilceler; }
}