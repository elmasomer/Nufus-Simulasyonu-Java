import com.github.javafaker.Faker;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;

public class VeriUretici {
    public static void main(String[] args) {
        Faker faker = new Faker(new Locale("tr")); 
        
        try (FileWriter writer = new FileWriter("kisiler.txt")) {
            for (int i = 1; i <= 10000; i++) {
                int id = i; 
                String adSoyad = faker.name().firstName() + " " + faker.name().lastName();
                int yas = faker.number().numberBetween(0, 51); 
                
                writer.write(id + "#" + adSoyad + "#" + yas + "\n");
            }
            System.out.println("10.000 kisilik veri basariyla uretildi!");
        } catch (IOException e) {
            System.out.println("Hata olustu: " + e.getMessage());
        }
    }
}