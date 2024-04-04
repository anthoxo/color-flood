package com.anthoxo.hackhaton.services.file;

import com.anthoxo.hackhaton.models.LanguageExtension;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileCheckerService {

    public void checkFileExtension(MultipartFile file) {
        String filename = file.getOriginalFilename();
        String extension = filename.substring(filename.lastIndexOf(".") + 1);
        if (LanguageExtension.getExtension(extension).isEmpty()) {
            throw new IllegalArgumentException("The file extension is not supported. Please use java, python or typescript file.");
        }
    }
}
