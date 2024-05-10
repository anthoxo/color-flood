package com.anthoxo.hackhaton.models;

import java.util.Optional;
import java.util.stream.Stream;

public enum LanguageExtension {
    JAVA("java", "java"),
    PYTHON("py", "python3");

    private String extensionName;
    private String commandRunner;

    LanguageExtension(String extension, String commandRunner) {
        this.extensionName = extension;
        this.commandRunner = commandRunner;
    }

    public static Optional<LanguageExtension> getExtension(String extensionName) {
        return Stream.of(values())
                .filter(languageExtension -> languageExtension.extensionName.equals(extensionName))
                .findFirst();
    }

    public String getCommandRunner() {
        return commandRunner;
    }
}
