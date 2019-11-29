package ee.taltech.iti0203.webstore.service;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;

@Service
public class ImageService {

    public ResponseEntity<InputStreamResource> getProductImage(String url) throws IOException {
        String uri = "./images/" + url;
        ClassPathResource imgFile = new ClassPathResource(uri);
        if (!imgFile.exists()) {
            imgFile = new ClassPathResource("image/placeholder.jpg");
        }
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(new InputStreamResource(imgFile.getInputStream()));
    }

}
