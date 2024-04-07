package com.anthoxo.hackhaton.services;

import com.anthoxo.hackhaton.entities.User;
import com.anthoxo.hackhaton.services.file.FileCheckerService;
import com.anthoxo.hackhaton.services.file.FileUtilsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Service
public class CodeService {

    private final UserService userService;
    private final FileUtilsService fileUtilsService;
    private final FileCheckerService fileCheckerService;

    public CodeService(
            UserService userService,
            FileUtilsService fileUtilsService,
            FileCheckerService fileCheckerService) {
        this.userService = userService;
        this.fileUtilsService = fileUtilsService;
        this.fileCheckerService = fileCheckerService;
    }

    public void validateCode(MultipartFile file, String name, String code) {
        fileCheckerService.checkFileExtension(file);
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Team name cannot be empty");
        }

        if (code == null || code.length() != 4) {
            throw new IllegalArgumentException(
                    "Code should have 4 digit characters.");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void pushCode(MultipartFile multipartFile, String name,
            String code) {
        boolean teamNameAlreadyExist = userService.exists(name, code);
        if (!teamNameAlreadyExist) {
            userService.createUser(name, code);
        }

        User user = userService.getUser(name, code)
                .orElseThrow(() -> new IllegalStateException(
                        "User should be created at this step"));
        File previousFile = new File(user.getCodeFilename());

        File file = fileUtilsService.generateProductionFile(multipartFile);
        user.setCodeFilename(file.getPath());
        userService.saveUser(user);
        fileUtilsService.deleteFile(previousFile);
    }
}
