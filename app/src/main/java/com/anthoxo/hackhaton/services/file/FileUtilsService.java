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

    private static final String TMP_FILE_LOCATION_BUILDER = "src/main/resources/tmp/%s-%s";
    private static final String PRODUCTION_FILE_LOCATION_BUILDER = "src/main/resources/prod/%s-%s";

    public File generateTmpFile(MultipartFile multipartFile) {
        String tmpFilename = String.format(TMP_FILE_LOCATION_BUILDER,
                UUID.randomUUID(),
                multipartFile.getOriginalFilename());
        return generateFile(multipartFile, tmpFilename);
    }

    public File generateProductionFile(MultipartFile multipartFile) {
        String filename = String.format(PRODUCTION_FILE_LOCATION_BUILDER,
                UUID.randomUUID(),
                multipartFile.getOriginalFilename());
        return generateFile(multipartFile, filename);
    }

    private File generateFile(MultipartFile multipartFile, String path) {
        File file = new File(path);
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
