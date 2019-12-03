package ee.taltech.iti0203.webstore.controller;

import ee.taltech.iti0203.webstore.security.Roles;
import ee.taltech.iti0203.webstore.service.ImageService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;

@RestController
@RequestMapping("upload")
public class ImageController {

    @Resource
    private ImageService imageService;

    @Secured({Roles.ROLE_USER, Roles.ROLE_ADMIN})
    @PostMapping("/image")
    public ResponseEntity uploadToLocalFileSystem(@RequestParam("file") MultipartFile file) throws IOException {
        return imageService.uploadImage(file);
    }
}
