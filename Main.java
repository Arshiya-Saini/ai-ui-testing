import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.io.FileHandler;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;

public class Main {

    public static void main(String[] args) {

        String url = "https://example.com";

        WebDriver driver = new ChromeDriver();

        try {
            // Open website
            driver.get(url);
            Thread.sleep(2000);

            // BEFORE screenshot
            File beforeFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            FileHandler.copy(beforeFile, new File("screenshots/before.png"));

            // Simple interaction (click first link if exists)
            try {
                driver.findElement(By.tagName("a")).click();
                Thread.sleep(2000);
            } catch (Exception e) {
                System.out.println("No clickable element found");
            }

            // AFTER screenshot
            File afterFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            FileHandler.copy(afterFile, new File("screenshots/after.png"));

            // Compare images
            String result = compareImages("screenshots/before.png", "screenshots/after.png");

            // Generate report
            generateReport(result);

            System.out.println("Test Completed!");

        } catch (Exception e) {
            e.printStackTrace();
        }

        driver.quit();
    }

    // Image comparison logic
    public static String compareImages(String img1Path, String img2Path) {
        try {
            BufferedImage img1 = ImageIO.read(new File(img1Path));
            BufferedImage img2 = ImageIO.read(new File(img2Path));

            int width = img1.getWidth();
            int height = img1.getHeight();

            int diff = 0;

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (img1.getRGB(x, y) != img2.getRGB(x, y)) {
                        diff++;
                    }
                }
            }

            if (diff > 0) {
                return "UI Changed - Possible Bug";
            } else {
                return "No Change Detected";
            }

        } catch (Exception e) {
            return "Error comparing images";
        }
    }

    // Report generation
    public static void generateReport(String result) {
        try {
            FileWriter writer = new FileWriter("report.txt");
            writer.write("=== UI TEST REPORT ===\n");
            writer.write(result);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
