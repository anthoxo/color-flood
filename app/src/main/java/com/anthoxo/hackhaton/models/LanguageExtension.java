package com.anthoxo.hackhaton.models;

import java.util.Optional;
import java.util.stream.Stream;

public enum LanguageExtension {
    JAVA("java"),
    PYTHON("py"),
    TYPESCRIPT("ts");

    private String extensionName;

    LanguageExtension(String extension) {
        this.extensionName = extension;
    }

    public static Optional<LanguageExtension> getExtension(String extensionName) {
        return Stream.of(values())
                .filter(languageExtension -> languageExtension.extensionName.equals(extensionName))
                .findFirst();
    }
}
