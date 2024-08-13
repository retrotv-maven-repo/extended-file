package dev.retrotv.file;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JavaFileTest {
    private final URL textFile = this.getClass().getClassLoader().getResource("text_file");
    private final URL textFileCopy = this.getClass().getClassLoader().getResource("text_file_copy");

    @Test
    @DisplayName("getFileHash() 메소드 테스트")
    void test_getFileHash() throws IOException, URISyntaxException {
        ExtendedFile file = new ExtendedFile(Objects.requireNonNull(textFile).toURI());
        assertNotNull(file.getFileHash());
    }

    @Test
    @DisplayName("matches() 메소드 테스트")
    void test_matches() throws IOException, URISyntaxException {
        ExtendedFile file = new ExtendedFile(Objects.requireNonNull(textFile).toURI());
        ExtendedFile file2 = new ExtendedFile(Objects.requireNonNull(textFileCopy).toURI());
        assertTrue(file.matches(file2));
    }
}
