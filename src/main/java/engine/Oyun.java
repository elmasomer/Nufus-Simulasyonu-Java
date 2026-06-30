/**
 *
 * @author [ömer hamza elmas] - hamza.elmas@ogr.sakarya.edu.tr
 * @since 17 Nisan 2026
 * <p>
* Oyunun ana motoru. Turları döndürme, hesaplamalar ve konsola yazdırma işlerini burada yapıyorum.
 * </p>
 */
package engine;

import models.Sehir;
import models.Ilce;
import models.Mahalle;
import models.Kisi;

import com.github.javafaker.Faker;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Scanner;

public class Oyun {
    // Oyundaki tüm şehirleri tutacağımız ana liste
    private List<Sehir> sehirler;
    // Sahte isimler ve veriler üretmek için Faker nesnesi
    private Faker faker;
    // Rastgele yaş gibi sayısal atamalar için Random nesnesi
    private Random random;

    // Kurucu Metod (Oyun nesnesi oluşturulduğunda ilk çalışacak yer)
    public Oyun() {
        this.sehirler = new ArrayList<>();
        // Faker'a Türkçe dil desteği veriyoruz 
        this.faker = new Faker(new Locale("tr")); 
        this.random = new Random();
    }

    // Oyunu başlatan ve ana akışı yöneten metod
    public void baslat() {
        Scanner scanner = new Scanner(System.in);
        
        // Kullanıcıdan tur sayısını alıyoruz
        System.out.print("Oyunun çalışacağı tur sayısını giriniz: ");
        int turSayisi = scanner.nextInt();
        scanner.nextLine(); // Scanner'da rakam girildikten sonra kalan boşluğu yutmak için gerekli

        // Kullanıcıdan başlangıç şehir değerlerini alıyoruz
        System.out.println("\nŞehir nüfus sayılarını aralarında birer boşluk bırakarak giriniz (Örn: 18 25 79): ");
        String sayilarGirdisi = scanner.nextLine();
        // Girilen metni boşluklara göre parçalayıp bir diziye atıyoruz
        String[] sayilar = sayilarGirdisi.split(" ");

        // ==========================================
        // 1. AŞAMA: GİRDİLERİ HESAPLAMA VE ŞEHİRLERİ OLUŞTURMA
        // ==========================================
        for (String s : sayilar) {
            if(s.trim().isEmpty()) continue; // Eğer fazladan boşluk girildiyse atla
            
            int x = Integer.parseInt(s.trim()); // String olan sayıyı tam sayıya (int) çeviriyoruz
            
            int d = x / 10; // Sayının onlar basamağı 
            int n = x % 10; // Sayının birler basamağı (Mahalle sayısını temsil eder)

            int finalN = n;
            
            // KURAL 1: Mahalle sayısı (finalN), ilçe sayısına (d) tam bölünmelidir.
            
            while (finalN % d != 0 || finalN == 0) {
                finalN++;
                // Eğer mahalle sayısı artarken 9'u geçerse, kurallara göre tekrar 1'den başlar.
                if (finalN >= 10) {
                    finalN = 1;
                }
            }

            // KURAL 2: Birler basamağı (mahalle sayısı) değiştiği için sayının kendisini de güncelliyoruz.
          
            x = (d * 10) + finalN; 

            int p = x; // Bu yeni sayı aynı zamanda şehrin dağıtılacak başlangıç nüfusudur.
            
           
            // Eşit dağıtım yapabilmek için tam bölünene kadar nüfusu artırıyoruz.
            while (p % finalN != 0) {
                p++;
            }

            // Artık matematiksel olarak kusursuz olan değerlerimizi modellere gönderiyoruz.
            int ilceBasinaMahalle = finalN / d;
            int mahalleBasinaNufus = p / finalN;

            // Hesaplanan değerlerle yeni Şehri oluşturup ana listemize ekliyoruz.
            sehirler.add(new Sehir(faker, random, d, ilceBasinaMahalle, mahalleBasinaNufus));
        }

        // Oyun başlamadan önceki ilk (sıfırıncı) durumu ekrana yazdırıyoruz.
        System.out.println("\nBaşlangıç Nüfus Durumu:");
        matrisYazdir();

        // ==========================================
        // 2. AŞAMA: TURLARI İŞLETME VE BÖLÜNME KONTROLÜ
        // ==========================================
        for (int tur = 1; tur <= turSayisi; tur++) {
            // Eğer bu turda bölünen şehirler olursa, onları geçici olarak bu listede tutacağız.
            
            List<Sehir> yeniBolunenSehirler = new ArrayList<>();

            for (Sehir sehir : sehirler) {
                // Şehre turları işletmesi (nüfus katsayısı hesaplama ve yaş artırma) emrini veriyoruz.
                sehir.turuIslet(faker, random);
                
                // Tur işlendikten sonra şehrin nüfusu 4 basamaklı olduysa bölünme gerçekleşir.
                Sehir yeniSehir = sehir.bolunGerekirse(faker);
                
                // Eğer bölünme olduysa ve yeni bir şehir döndüyse, onu geçici listeye ekliyoruz.
                if (yeniSehir != null) {
                    yeniBolunenSehirler.add(yeniSehir);
                }
            }
            
            // Tur bittikten sonra, bölünüp ayrılan yeni şehirleri matrisin en sonuna ekliyoruz.
            sehirler.addAll(yeniBolunenSehirler);

            // Ekranı temizliyoruz ki sayılar üst üste binmesin, temiz bir matris aksın.
            ekranTemizle();
            
            // Bu turun güncel matrisini yazdırıyoruz.
            System.out.println(tur + ". Tur Sonucu:");
            matrisYazdir();
            
            // Turların akışını gözle görebilmek için programı 1 saniye uyutuyoruz (bekletiyoruz).
            try { Thread.sleep(1000); } catch (Exception e) {} 
        }

        // ==========================================
        // 3. AŞAMA: OYUN SONU VE MATRİS SEÇİM EKRANI
        // ==========================================
        finalEkrani(scanner);
        scanner.close(); // İşimiz bitince bellek sızıntısı olmaması için scanner'ı kapatıyoruz.
    }

    // Şehirleri 5'li satırlar halinde istenen formatta yazdıran yardımcı metod
    private void matrisYazdir() {
        for (int i = 0; i < sehirler.size(); i++) {
            System.out.print("[" + sehirler.get(i).getNufus() + "]");
            
            // Eğer 5. elemana geldiysek alt satıra geçiyoruz.
            if ((i + 1) % 5 == 0) {
                System.out.println(); 
            } 
            // 5. eleman değilsek ve listenin son elemanı da değilsek araya tire (-) koyuyoruz.
            else if (i != sehirler.size() - 1) {
                System.out.print("-"); 
            }
        }
        System.out.println("\n");
    }

    // Oyun bitince kullanıcıdan satır/sütun alıp şehrin tüm detaylarını yazdıran metod
    private void finalEkrani(Scanner scanner) {
        System.out.println("----------------------------------------");
        System.out.println("Oyun Bitti! Detaylarını görmek istediğiniz şehrin satır ve sütununu giriniz (Sıfırdan başlar).");
        System.out.print("Satır: ");
        int satir = scanner.nextInt();
        System.out.print("Sütun: ");
        int sutun = scanner.nextInt();

        // 2 Boyutlu matris koordinatlarını, 1 Boyutlu Listemizin indeksine çevirme formülü
        int index = (satir * 5) + sutun;

        // Girilen indeksin listemizin sınırları içinde olup olmadığını kontrol ediyoruz
        if (index >= 0 && index < sehirler.size()) {
            Sehir secilenSehir = sehirler.get(index);
            
            // Hiyerarşik yazdırma işlemi (Şehir -> İlçe -> Mahalle -> Kişiler)
            System.out.println("\nŞehir: " + secilenSehir.getAd() + " - Nüfus: " + secilenSehir.getNufus());
            for (Ilce ilce : secilenSehir.getIlceler()) {
                System.out.println("İlçe: " + ilce.getAd() + " - Nüfus: " + ilce.getNufus());
                for (Mahalle mahalle : ilce.getMahalleler()) {
                    System.out.println("Mahalle: " + mahalle.getAd() + " - Nüfus: " + mahalle.getNufus());
                    System.out.println("Kişiler:");
                    for (Kisi kisi : mahalle.getKisiler()) {
                        System.out.println(kisi.toString());
                    }
                }
            }
        } else {
            System.out.println("Girdiğiniz satır ve sütuna ait bir şehir bulunamadı!");
        }
        
        System.out.println("\nProgramı kapatmak için bir sayı girip Enter'a basınız...");
        scanner.nextInt(); 
    }

    // İşletim sistemine göre konsolu temizleyen metod
    private void ekranTemizle() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            
            // Desteklemediği durumda araya çizgi çekerek görsel ayrım sağlarız.
            System.out.println("\n----------------------------------------\n");
        }
    }
}