/**
 *
 * @author [ömer hamza elmas] - hamza.elmas@ogr.sakarya.edu.tr
 * @since 17 Nisan 2026
 * <p>
 * Oyundaki insanları temsil ediyor. ID'ler benzersiz, isimler ve yaşlar rastgele atanıyor.
 * </p>
 */




package models;

import com.github.javafaker.Faker;
import java.util.Random;

public class Kisi {
    // Tüm kişiler için ortak çalışacak statik bir sayaç. 
    // Her yeni kişi oluştuğunda 1 artacak, böylece ID'ler benzersiz olacak.
    private static int globalIdCounter = 1; 
    
    private int id;
    private String isimSoyisim;
    private int yas;

    // Kurucu metod (Constructor)
    public Kisi(Faker faker, Random random) {
        this.id = globalIdCounter++; 
        this.isimSoyisim = faker.name().fullName(); // Faker ile rastgele ad soyad
        this.yas = random.nextInt(51); // 0 ile 50 (dahil) arası rastgele yaş
    }

    // Her turun sonunda çağıracağımız yaş artırma metodu
    public void yasArtir() {
        this.yas++;
    }

    // Bilgileri okumak için Getter metodları
    public int getId() { return id; }
    public String getIsimSoyisim() { return isimSoyisim; }
    public int getYas() { return yas; }

    // Oyun sonunda ekrana yazdırırken istenen format: ID - isim soyisim - Yaş
    @Override
    public String toString() {
        return id + " - " + isimSoyisim + " - " + yas;
    }
}