package ee.taltech.iti0203.webstore.scheduled;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ImageCleanerTest {

    @Autowired
    private ImageCleaner imageCleaner;

    @Before
    public void setUp() {
        File original = Paths.get("./images/placeholder.jpg").toFile();
        File copied = Paths.get("./images/placeholder(1).jpg").toFile();
        try (
                InputStream in = new BufferedInputStream(
                        new FileInputStream(original));
                OutputStream out = new BufferedOutputStream(
                        new FileOutputStream(copied))) {

            byte[] buffer = new byte[1024];
            int lengthRead;
            while ((lengthRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, lengthRead);
                out.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCleanImages() throws IOException {
        imageCleaner.create();
        assertEquals(2, Files.walk(Paths.get("./images/")).count());
    }
}