package ee.taltech.iti0203.webstore.service;

import ee.taltech.iti0203.webstore.pojo.ImageDto;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class ImageService {
    private static final int MAX_FILE_NAME_LENGTH = 32;

    public ImageDto uploadImage(@RequestParam("file") MultipartFile file) throws IOException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename()).replace(' ', '_');
        fileName = fileName.length() > MAX_FILE_NAME_LENGTH ? fileName.substring(fileName.length() - MAX_FILE_NAME_LENGTH) : fileName;
        Path path = Paths.get("./images/" + fileName);
        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        return new ImageDto(fileName);
    }

}
