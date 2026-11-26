package src.Tokenizer;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class FileReader {
    private FileReader() {}

    public static String readText(final String relativePath) throws IOException {
        final Path projectRoot = Path.of(System.getProperty("user.dir"));
        final Path file = projectRoot.resolve(relativePath).normalize();
        return Files.readString(file, StandardCharsets.UTF_8);
    }
}
