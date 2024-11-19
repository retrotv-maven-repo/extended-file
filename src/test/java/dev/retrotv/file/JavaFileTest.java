package dev.retrotv.file;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class JavaFileTest {
    private final URL textFile = this.getClass().getClassLoader().getResource("text_file");
    private final URL textFileCopy = this.getClass().getClassLoader().getResource("text_file_copy");
    private final URL extensionFile = this.getClass().getClassLoader().getResource("extension.txt");
    private final URL extensionFile2 = this.getClass().getClassLoader().getResource("extension.tar.gz");

    @Test
    @DisplayName("getFileHash() 메소드 테스트")
    void test_getFileHash() throws IOException, URISyntaxException {
        ExtendedFile file = new ExtendedFile(Objects.requireNonNull(textFile).toURI());
        assertNotNull(file.getHash());
    }

    @Test
    @DisplayName("matches() 메소드 테스트")
    void test_matches() throws URISyntaxException {
        ExtendedFile file = new ExtendedFile(Objects.requireNonNull(textFile).toURI());
        ExtendedFile file2 = new ExtendedFile(Objects.requireNonNull(textFileCopy).toURI());
        assertTrue(file.matches(file2));
    }

    @Test
    @DisplayName("getName() 메소드 테스트")
    void test_getName() throws URISyntaxException {
        ExtendedFile file = new ExtendedFile(Objects.requireNonNull(textFile).toURI());
        assertEquals("text_file", file.getName());

        ExtendedFile file2 = new ExtendedFile(Objects.requireNonNull(extensionFile).toURI());
        assertEquals("extension", file2.getName(true));
        assertEquals("extension.txt", file2.getName(false));

        ExtendedFile file3 = new ExtendedFile(Objects.requireNonNull(extensionFile2).toURI());
        assertEquals("extension", file3.getName(true));
        assertEquals("extension.tar.gz", file3.getName(false));
    }
}
