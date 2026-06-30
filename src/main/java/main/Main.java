/**
 *
 * @author [ömer hamza elmas] - hamza.elmas@ogr.sakarya.edu.tr
 * @since 17 Nisan 2026
 * <p>
* Programı başlatan sınıf. Sadece Oyun nesnesi oluşturup oyunu çalıştırıyor.
 * </p>
 */
package main;

import engine.Oyun;

public class Main {
    public static void main(String[] args) {
        System.out.println("Nüfus Simülasyonuna Hoş Geldiniz!\n");
        
        // Oyunu oluşturuz ve sonrasında başlatırız..
        Oyun nufusOyunu = new Oyun();
        nufusOyunu.baslat();
    }
}