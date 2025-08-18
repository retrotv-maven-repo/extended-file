package dev.retrotv.file;

import org.junit.jupiter.api.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class ExtendedFileTest {
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
    void test_matches() throws IOException, URISyntaxException {
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

    private final URL wordFile = getClass().getClassLoader().getResource("test.docx");
    private final URL notWordFile = getClass().getClassLoader().getResource("not_word.docx");
    private final URL textFileDifferent = getClass().getClassLoader().getResource("text_file_different");
    private final URL textFileMega = getClass().getClassLoader().getResource("text_file_mega");

    @Test
    @DisplayName("getMimeType() 메서드 테스트")
    void test_getMimeType() throws IOException, URISyntaxException {
        ExtendedFile file = new ExtendedFile(Objects.requireNonNull(textFile).toURI());
        assertTrue(file.getMimeType().startsWith("text/plain"));

        file = new ExtendedFile(Objects.requireNonNull(wordFile).toURI());
        assertTrue(file.getMimeType().startsWith("application/vnd.openxmlformats-officedocument.wordprocessingml.document"));

        file = new ExtendedFile(Objects.requireNonNull(notWordFile).toURI());
        assertFalse(file.getMimeType().startsWith("application/vnd.openxmlformats-officedocument.wordprocessingml.document"));
    }

    @Test
    @DisplayName("matchesMimeType() 메서드 테스트")
    void test_matchesMimeType() throws IOException, URISyntaxException {
        ExtendedFile file = new ExtendedFile(Objects.requireNonNull(textFile).toURI().toString().replace("file:", ""));
        assertTrue(file.matchesMimeType("text/plain"));
    }

    @Test
    @DisplayName("isText() 메서드 테스트")
    void test_isText() throws IOException, URISyntaxException {
        ExtendedFile file = new ExtendedFile(Objects.requireNonNull(textFile).toURI().toString().replace("file:", ""), "");
        assertTrue(file.isText());
    }

    @Test
    @DisplayName("isAudio 메서드 테스트")
    void test_isAudio() throws IOException, URISyntaxException {
        ExtendedFile file = new ExtendedFile(Objects.requireNonNull(textFile).toURI());
        ExtendedFile file2 = new ExtendedFile(file, "");
        assertFalse(file2.isAudio());
    }

    @Test
    @DisplayName("isVideo() 메서드 테스트")
    void test_isVideo() throws IOException, URISyntaxException {
        ExtendedFile file = new ExtendedFile(Objects.requireNonNull(textFile).toURI());
        assertFalse(file.isVideo());
    }

    @Test
    @DisplayName("isImage() 메서드 테스트")
    void test_isImage() throws IOException, URISyntaxException {
        ExtendedFile file = new ExtendedFile(Objects.requireNonNull(textFile).toURI());
        assertFalse(file.isImage());
    }

    @Test
    @DisplayName("getHashCode() 메서드 테스트")
    void test_getHash() throws IOException, URISyntaxException {
        ExtendedFile file = new ExtendedFile(Objects.requireNonNull(textFile).toURI());
        assertNotNull(file.getHash());

        assertNotNull(file.getHash(ExtendedFile.EHash.SHA512));
        assertNotNull(file.getHash("SHA-512"));
    }

    @Nested
    @DisplayName("matches() 메서드 테스트")
    class MatchesTest {

        @Test
        @DisplayName("파일이 같은 경우")
        void test_matches_same() throws IOException, URISyntaxException {
            ExtendedFile file = new ExtendedFile(Objects.requireNonNull(textFile).toURI());
            ExtendedFile file2 = new ExtendedFile(Objects.requireNonNull(textFileCopy).toURI());
            assertTrue(file.matches(file2));
            assertTrue(file.matches(file2, ExtendedFile.EHash.SHA256));
            assertTrue(file.matches(file2, "CRC32"));
            assertTrue(file.matches(file2, "MD5"));
            assertTrue(file.matches(file2, "SHA-1"));
            assertTrue(file.matches(file2, "SHA-224"));
            assertTrue(file.matches(file2, "SHA-256"));
            assertTrue(file.matches(file2, "SHA-384"));
            assertTrue(file.matches(file2, "SHA-512"));
            assertTrue(file.matches(file2, "SHA-512/224"));
            assertTrue(file.matches(file2, "SHA-512/256"));
            assertTrue(file.matches(file2, "SHA3-224"));
            assertTrue(file.matches(file2, "SHA3-256"));
            assertTrue(file.matches(file2, "SHA3-384"));
            assertTrue(file.matches(file2, "SHA3-512"));
        }

        @Test
        @DisplayName("파일이 다른 경우")
        void test_matches_different() throws IOException, URISyntaxException {
            ExtendedFile file = new ExtendedFile(Objects.requireNonNull(textFile).toURI());
            ExtendedFile file2 = new ExtendedFile(Objects.requireNonNull(textFileDifferent).toURI());
            assertFalse(file.matches(file2));
            assertFalse(file.matches(file2, ExtendedFile.EHash.SHA256));
            assertFalse(file.matches(file2, "SHA-256"));
        }
    }

    @Nested
    @DisplayName("matchesDeep() 메서드 테스트")
    class MatchesDeepTest {

        @Test
        @DisplayName("파일이 같은 경우")
        void test_matchesDeep_same() throws IOException, URISyntaxException {
            ExtendedFile file = new ExtendedFile(Objects.requireNonNull(textFile).toURI());
            ExtendedFile file2 = new ExtendedFile(Objects.requireNonNull(textFileCopy).toURI());
            assertTrue(file.matchesDeep(file2));
        }

        @Test
        @DisplayName("파일이 다른 경우")
        void test_matchesDeep_different() throws IOException, URISyntaxException {
            ExtendedFile file = new ExtendedFile(Objects.requireNonNull(textFile).toURI());
            ExtendedFile file2 = new ExtendedFile(Objects.requireNonNull(textFileDifferent).toURI());
            assertFalse(file.matchesDeep(file2));
        }
    }

    @Nested
    @DisplayName("getFileSize() 메서드 테스트")
    class GetFileSizeTest {

        @Test
        @DisplayName("파일 크기 반환 (사람이 읽기 쉬운 형태)")
        void test_getFileSize() throws URISyntaxException {
            ExtendedFile file = new ExtendedFile(Objects.requireNonNull(textFile).toURI());
            assertNotNull(file.getSize());
            assertEquals("18.00 Byte", file.getSize());
        }

        @Test
        @DisplayName("파일 크기 반환 MB (사람이 읽기 쉬운 형태)")
        void test_getFileSizeMB() throws URISyntaxException {
            ExtendedFile file = new ExtendedFile(Objects.requireNonNull(textFileMega).toURI());
            assertNotNull(file.getSize());
            assertTrue("2.29 MB".equals(file.getSize()) || "2.38 MB".equals(file.getSize()));
        }

        @Test
        @DisplayName("파일 크기 반환")
        void test_getFileSize_humanReadableFalse() throws URISyntaxException {
            ExtendedFile file = new ExtendedFile(Objects.requireNonNull(textFile).toURI());
            assertNotNull(file.getSize(false));
            assertEquals("18", file.getSize(false));
        }

        @Test
        @DisplayName("파일 크기 반환 대용량")
        void test_getFileSize_humanReadableFalseMB() throws URISyntaxException {
            ExtendedFile file = new ExtendedFile(Objects.requireNonNull(textFileMega).toURI());
            assertNotNull(file.getSize(false));
            assertTrue("2400000".equals(file.getSize(false)) || "2500000".equals(file.getSize(false)));
        }
    }

    @Test
    @DisplayName("getExtension() 메서드 테스트")
    void test_getExtension() throws URISyntaxException {
        ExtendedFile file = new ExtendedFile(Objects.requireNonNull(extensionFile).toURI());
        assertEquals("txt", file.getExtension());

        ExtendedFile file2 = new ExtendedFile(Objects.requireNonNull(extensionFile2).toURI());
        assertEquals("tar.gz", file2.getExtension());

        ExtendedFile file3 = new ExtendedFile(Objects.requireNonNull(textFile).toURI());
        assertEquals("", file3.getExtension());
    }

    @Nested
    @DisplayName("rm() 메서드 테스트")
    class RmTest {

        @Test
        @Order(1)
        @DisplayName("단건 파일 삭제")
        void test_rm_singleFile() throws IOException {
            createTestFile();

            ExtendedFile file = new ExtendedFile("./src/test/resources/delete_test_file");
            assertTrue(file.rm());
        }

        @Test
        @Order(2)
        @DisplayName("빈 디렉터리 삭제")
        void test_rm_emptyDirectory() {
            createTestDirectory();

            ExtendedFile file = new ExtendedFile("./src/test/resources/delete_test_directory");
            assertTrue(file.rm());
        }

        @Test
        @Order(3)
        @DisplayName("비어있지 않은 디렉터리 삭제 실패")
        void test_rm_notEmptyDirectoryFail() throws IOException {
            createTestDirectoryAndFile();

            ExtendedFile file = new ExtendedFile("./src/test/resources/delete_test_directory");
            assertFalse(file.rm());
            removeTestDirectory();
        }

        @Test
        @Order(4)
        @DisplayName("비어있지 않은 디렉터리 재귀적으로 삭제")
        void test_rm_notEmptyDirectory() throws IOException {
            createTestInnerDirectoryAndFile();

            ExtendedFile file = new ExtendedFile("./src/test/resources/delete_test_directory");
            assertTrue(file.rm(true));
        }

        private void createTestFile() throws IOException {
            Files.createFile(Paths.get("./src/test/resources/delete_test_file"));
            ExtendedFile testFile = new ExtendedFile("./src/test/resources/delete_test_file");
            assertTrue(testFile.isFile());
            assertTrue(testFile.exists());
        }

        private void createTestDirectory() {
            try {
                Files.createDirectory(Paths.get("./src/test/resources/delete_test_directory"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            ExtendedFile testDirectory = new ExtendedFile("./src/test/resources/delete_test_directory");
            assertTrue(testDirectory.isDirectory());
            assertTrue(testDirectory.exists());
        }

        private void createTestDirectoryAndFile() throws IOException {
            Files.createDirectory(Paths.get("./src/test/resources/delete_test_directory"));
            ExtendedFile testDirectory = new ExtendedFile("./src/test/resources/delete_test_directory");
            assertTrue(testDirectory.isDirectory());
            assertTrue(testDirectory.exists());

            Files.createFile(Paths.get("./src/test/resources/delete_test_directory/delete_test_file"));
            ExtendedFile testFile = new ExtendedFile("./src/test/resources/delete_test_directory/delete_test_file");
            assertTrue(testFile.isFile());
            assertTrue(testFile.exists());
        }

        private void removeTestDirectory() {
            ExtendedFile file = new ExtendedFile("./src/test/resources/delete_test_directory");
            assertTrue(file.exists());
            assertTrue(file.rm(true));
        }

        private void createTestInnerDirectoryAndFile() throws IOException {
            Files.createDirectory(Paths.get("./src/test/resources/delete_test_directory"));
            ExtendedFile testDirectory = new ExtendedFile("./src/test/resources/delete_test_directory");
            assertTrue(testDirectory.isDirectory());
            assertEquals("", testDirectory.getExtension());
            assertTrue(testDirectory.exists());

            Files.createDirectory(Paths.get("./src/test/resources/delete_test_directory/delete_test_directory"));
            ExtendedFile testInnerDirectory = new ExtendedFile("./src/test/resources/delete_test_directory/delete_test_directory");
            assertTrue(testInnerDirectory.isDirectory());
            assertTrue(testInnerDirectory.exists());

            Files.createDirectory(Paths.get("./src/test/resources/delete_test_directory/delete_test_directory/delete_test_directory"));
            ExtendedFile testInnerInnerDirectory = new ExtendedFile("./src/test/resources/delete_test_directory/delete_test_directory/delete_test_directory");
            assertTrue(testInnerInnerDirectory.isDirectory());
            assertTrue(testInnerInnerDirectory.exists());

            Files.createFile(Paths.get("./src/test/resources/delete_test_directory/delete_test_file1"));
            ExtendedFile testFile1 = new ExtendedFile("./src/test/resources/delete_test_directory/delete_test_file1");
            assertTrue(testFile1.isFile());
            assertTrue(testFile1.exists());

            Files.createFile(Paths.get("./src/test/resources/delete_test_directory/delete_test_file2"));
            ExtendedFile testFile2 = new ExtendedFile("./src/test/resources/delete_test_directory/delete_test_file2");
            assertTrue(testFile2.isFile());
            assertTrue(testFile2.exists());

            Files.createFile(Paths.get("./src/test/resources/delete_test_directory/delete_test_file3"));
            ExtendedFile testFile3 = new ExtendedFile("./src/test/resources/delete_test_directory/delete_test_file3");
            assertTrue(testFile3.isFile());
            assertTrue(testFile3.exists());

            Files.createFile(Paths.get("./src/test/resources/delete_test_directory/delete_test_directory/delete_test_file1"));
            ExtendedFile testInnerFile1 = new ExtendedFile("./src/test/resources/delete_test_directory/delete_test_directory/delete_test_file1");
            assertTrue(testInnerFile1.isFile());
            assertTrue(testInnerFile1.exists());

            Files.createFile(Paths.get("./src/test/resources/delete_test_directory/delete_test_directory/delete_test_file2"));
            ExtendedFile testInnerFile2 = new ExtendedFile("./src/test/resources/delete_test_directory/delete_test_directory/delete_test_file2");
            assertTrue(testInnerFile2.isFile());
            assertTrue(testInnerFile2.exists());

            Files.createFile(Paths.get("./src/test/resources/delete_test_directory/delete_test_directory/delete_test_directory/delete_test_file1"));
            ExtendedFile testInnerInnerFile1 = new ExtendedFile("./src/test/resources/delete_test_directory/delete_test_directory/delete_test_directory/delete_test_file1");
            assertTrue(testInnerInnerFile1.isFile());
            assertTrue(testInnerInnerFile1.exists());

            Files.createFile(Paths.get("./src/test/resources/delete_test_directory/delete_test_directory/delete_test_directory/delete_test_file2"));
            ExtendedFile testInnerInnerFile2 = new ExtendedFile("./src/test/resources/delete_test_directory/delete_test_directory/delete_test_directory/delete_test_file2");
            assertTrue(testInnerInnerFile2.isFile());
            assertTrue(testInnerInnerFile2.exists());

            Files.createFile(Paths.get("./src/test/resources/delete_test_directory/delete_test_directory/delete_test_directory/delete_test_file3"));
            ExtendedFile testInnerInnerFile3 = new ExtendedFile("./src/test/resources/delete_test_directory/delete_test_directory/delete_test_directory/delete_test_file3");
            assertTrue(testInnerInnerFile3.isFile());
            assertTrue(testInnerInnerFile3.exists());

            Files.createFile(Paths.get("./src/test/resources/delete_test_directory/delete_test_directory/delete_test_directory/delete_test_file4"));
            ExtendedFile testInnerInnerFile4 = new ExtendedFile("./src/test/resources/delete_test_directory/delete_test_directory/delete_test_directory/delete_test_file4");
            assertTrue(testInnerInnerFile4.isFile());
            assertTrue(testInnerInnerFile4.exists());

            Files.createFile(Paths.get("./src/test/resources/delete_test_directory/delete_test_directory/delete_test_directory/delete_test_file5"));
            ExtendedFile testInnerInnerFile5 = new ExtendedFile("./src/test/resources/delete_test_directory/delete_test_directory/delete_test_directory/delete_test_file5");
            assertTrue(testInnerInnerFile5.isFile());
            assertTrue(testInnerInnerFile5.exists());
        }
    }
}
