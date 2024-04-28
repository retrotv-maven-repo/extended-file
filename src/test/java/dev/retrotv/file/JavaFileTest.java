package dev.retrotv.file;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Objects;

class JavaFileTest {
    private final URL TEXT_FILE = this.getClass().getClassLoader().getResource("text_file");

    @Test
    void test_file() throws URISyntaxException, IOException {
        ExtendedFile ef = new ExtendedFile(Objects.requireNonNull(TEXT_FILE).toURI());
        System.out.println(ef.getMimeType());
        System.out.println(ef.matches(ef));
    }
}
