package com.anthoxo.hackhaton.services.file;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

@Service
public class FileUtilsService {

    public File generateTmpFile(MultipartFile multipartFile) {
        UUID uuid = UUID.randomUUID();
        String tmpFilename = String.format("src/main/resources/tmp/%s-%s", uuid,
                multipartFile.getOriginalFilename());
        File file = new File(tmpFilename);
        try (OutputStream os = new FileOutputStream(file)) {
            os.write(multipartFile.getBytes());
        } catch (IOException e) {
            throw new IllegalArgumentException("File could not be written", e);
        }
        return file;
    }

    public void deleteFile(File file) {
        file.delete();
    }
}
