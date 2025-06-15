package dev.retrotv.file

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Order
import java.io.IOException
import java.net.URISyntaxException
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

import org.junit.jupiter.api.Assertions.*

class KotlinFileTest {
    private val textFile = this.javaClass.getClassLoader().getResource("text_file")
    private val textFileCopy = this.javaClass.getClassLoader().getResource("text_file_copy")
    private val textFileDifferent = this.javaClass.getClassLoader().getResource("text_file_different")
    private val textFileMega = this.javaClass.getClassLoader().getResource("text_file_mega")
    private val extensionFile = this.javaClass.getClassLoader().getResource("extension.txt")
    private val extensionFile2 = this.javaClass.getClassLoader().getResource("extension.tar.gz")

    @Test
    @DisplayName("getMimeType() 메서드 테스트")
    fun test_getMimeType() {
        val file = ExtendedFile(Objects.requireNonNull(textFile).toURI())
        assertTrue(file.getMimeType().startsWith("text/plain"))
    }

    @Test
    @DisplayName("matchesMimeType() 메서드 테스트")
    fun test_matchesMimeType() {
        val file = ExtendedFile(Objects.requireNonNull(textFile).toURI().toString().replace("file:", ""))
        assertTrue(file.matchesMimeType("text/plain"))
    }

    @Test
    @DisplayName("isText() 메서드 테스트")
    fun test_isText() {
        val file = ExtendedFile(Objects.requireNonNull(textFile).toURI().toString().replace("file:", ""), "")
        assertTrue(file.isText())
    }

    @Test
    @DisplayName("isAudio 메서드 테스트")
    fun test_isAudio() {
        val file = ExtendedFile(Objects.requireNonNull(textFile).toURI())
        val file2 = ExtendedFile(file, "")
        assertFalse(file2.isAudio())
    }

    @Test
    @DisplayName("isVideo() 메서드 테스트")
    fun test_isVideo() {
        val file = ExtendedFile(Objects.requireNonNull(textFile).toURI())
        assertFalse(file.isVideo())
    }

    @Test
    @DisplayName("isImage() 메서드 테스트")
    fun test_isImage() {
        val file = ExtendedFile(Objects.requireNonNull(textFile).toURI())
        assertFalse(file.isImage())
    }

    @Test
    @DisplayName("getHashCode() 메서드 테스트")
    fun test_getHashCode() {
        val file = ExtendedFile(Objects.requireNonNull(textFile).toURI())
        assertNotNull(file.getHash())
    }

    @Test
    @DisplayName("getHashCode(SHA512()) 메서드 테스트")
    fun test_getHashCode_sha512() {
        val file = ExtendedFile(Objects.requireNonNull(textFile).toURI())
        assertNotNull(file.getHash(ExtendedFile.EHash.SHA512))
        assertNotNull(file.getHash("SHA-512"))
    }

    @Nested
    @DisplayName("matches() 메서드 테스트")
    inner class MatchesTest {

        @Test
        @DisplayName("파일이 같은 경우")
        fun test_matches_same() {
            val file = ExtendedFile(Objects.requireNonNull(textFile).toURI())
            val file2 = ExtendedFile(Objects.requireNonNull(textFileCopy).toURI())
            assertTrue(file.matches(file2))
            assertTrue(file.matches(file2, ExtendedFile.EHash.SHA256))
            assertTrue(file.matches(file2, "CRC32"))
            assertTrue(file.matches(file2, "MD5"))
            assertTrue(file.matches(file2, "SHA-1"))
            assertTrue(file.matches(file2, "SHA-224"))
            assertTrue(file.matches(file2, "SHA-256"))
            assertTrue(file.matches(file2, "SHA-384"))
            assertTrue(file.matches(file2, "SHA-512"))
            assertTrue(file.matches(file2, "SHA-512/224"))
            assertTrue(file.matches(file2, "SHA-512/256"))
            assertTrue(file.matches(file2, "SHA3-224"))
            assertTrue(file.matches(file2, "SHA3-256"))
            assertTrue(file.matches(file2, "SHA3-384"))
            assertTrue(file.matches(file2, "SHA3-512"))
        }

        @Test
        @DisplayName("파일이 다른 경우")
        fun test_matches_different() {
            val file = ExtendedFile(Objects.requireNonNull(textFile).toURI())
            val file2 = ExtendedFile(Objects.requireNonNull(textFileDifferent).toURI())
            assertFalse(file.matches(file2))
            assertFalse(file.matches(file2, ExtendedFile.EHash.SHA256))
            assertFalse(file.matches(file2, "SHA-256"))
        }
    }

    @Nested
    @DisplayName("matchesDeep() 메서드 테스트")
    inner class MatchesDeepTest {

        @Test
        @DisplayName("파일이 같은 경우")
        fun test_matchesDeep_same() {
            val file = ExtendedFile(Objects.requireNonNull(textFile).toURI())
            val file2 = ExtendedFile(Objects.requireNonNull(textFileCopy).toURI())
            assertTrue(file.matchesDeep(file2))
        }

        @Test
        @DisplayName("파일이 다른 경우")
        fun test_matchesDeep_different() {
            val file = ExtendedFile(Objects.requireNonNull(textFile).toURI())
            val file2 = ExtendedFile(Objects.requireNonNull(textFileDifferent).toURI())
            assertFalse(file.matchesDeep(file2))
        }
    }

    @Nested
    @DisplayName("getFileSize() 메서드 테스트")
    inner class GetFileSizeTest {

        @Test
        @DisplayName("파일 크기 반환 (사람이 읽기 쉬운 형태)")
        fun test_getFileSize() {
            val file = ExtendedFile(Objects.requireNonNull(textFile).toURI())
            assertNotNull(file.getSize())
            assertEquals("18 Byte", file.getSize())
        }

        @Test
        @DisplayName("파일 크기 반환 MB (사람이 읽기 쉬운 형태)")
        fun test_getFileSizeMB() {
            val file = ExtendedFile(Objects.requireNonNull(textFileMega).toURI())
            assertNotNull(file.getSize())
            assert("2.29 MB" == file.getSize() || "2.38 MB" == file.getSize())
        }

        @Test
        @DisplayName("파일 크기 반환")
        fun test_getFileSize_humanReadableFalse() {
            val file = ExtendedFile(Objects.requireNonNull(textFile).toURI())
            assertNotNull(file.getSize(false))
            assertEquals("18", file.getSize(false))
        }

        @Test
        @DisplayName("파일 크기 반환 대용량")
        fun test_getFileSize_humanReadableFalseMB() {
            val file = ExtendedFile(Objects.requireNonNull(textFileMega).toURI())
            assertNotNull(file.getSize(false))
            assert("2400000" == file.getSize(false) || "2500000" == file.getSize(false))
        }
    }

    @Test
    @DisplayName("getExtension() 메서드 테스트")
    fun test_getExtension() {
        val file = ExtendedFile(Objects.requireNonNull(extensionFile).toURI())
        assertEquals("txt", file.getExtension())

        val file2 = ExtendedFile(Objects.requireNonNull(extensionFile2).toURI())
        assertEquals("tar.gz", file2.getExtension())

        val file3 = ExtendedFile(Objects.requireNonNull(textFile).toURI())
        assertEquals("", file3.getExtension())
    }

    @Test
    @DisplayName("getName() 메서드 테스트")
    @Throws(URISyntaxException::class)
    fun test_getName() {
        val file = ExtendedFile(Objects.requireNonNull(textFile).toURI())
        assertEquals("text_file", file.name)

        val file2 = ExtendedFile(Objects.requireNonNull(extensionFile).toURI())
        assertEquals("extension", file2.getName(true))
        assertEquals("extension.txt", file2.getName(false))

        val file3 = ExtendedFile(Objects.requireNonNull(extensionFile2).toURI())
        assertEquals("extension", file3.getName(true))
        assertEquals("extension.tar.gz", file3.getName(false))
    }

    @Nested
    @DisplayName("rm() 메서드 테스트")
    inner class RmTest {

        @Test
        @Order(1)
        @DisplayName("단건 파일 삭제")
        @Throws(IOException::class, SecurityException::class)
        fun test_rm_singleFile() {
            createTestFile()

            val file = ExtendedFile("./src/test/resources/delete_test_file")
            assertTrue(file.rm())
        }

        @Test
        @Order(2)
        @DisplayName("빈 디렉터리 삭제")
        @Throws(IOException::class, SecurityException::class)
        fun test_rm_emptyDirectory() {
            createTestDirectory()

            val file = ExtendedFile("./src/test/resources/delete_test_directory")
            assertTrue(file.rm())
        }

        @Test
        @Order(3)
        @DisplayName("비어있지 않은 디렉터리 삭제 실패")
        @Throws(IOException::class, SecurityException::class)
        fun test_rm_notEmptyDirectoryFail() {
            createTestDirectoryAndFile()

            val file = ExtendedFile("./src/test/resources/delete_test_directory")
            assertFalse(file.rm())
            removeTestDirectory()
        }

        @Test
        @Order(4)
        @DisplayName("비어있지 않은 디렉터리 재귀적으로 삭제")
        @Throws(IOException::class, SecurityException::class)
        fun test_rm_notEmptyDirectory() {
            createTestInnerDirectoryAndFile()

            val file = ExtendedFile("./src/test/resources/delete_test_directory")
            assertTrue(file.rm(true))
        }

        @Throws(IOException::class, SecurityException::class)
        private fun createTestFile() {
            Files.createFile(Paths.get("./src/test/resources/delete_test_file"))
            val testFile = ExtendedFile("./src/test/resources/delete_test_file")
            assertTrue(testFile.isFile)
            assertTrue(testFile.exists())
        }

        @Throws(SecurityException::class)
        private fun createTestDirectory() {
            Files.createDirectory(Paths.get("./src/test/resources/delete_test_directory"))
            val testDirectory = ExtendedFile("./src/test/resources/delete_test_directory")
            assertTrue(testDirectory.isDirectory)
            assertTrue(testDirectory.exists())
        }

        @Throws(IOException::class, SecurityException::class)
        private fun createTestDirectoryAndFile() {
            Files.createDirectory(Paths.get("./src/test/resources/delete_test_directory"))
            val testDirectory = ExtendedFile("./src/test/resources/delete_test_directory")
            assertTrue(testDirectory.isDirectory)
            assertTrue(testDirectory.exists())

            Files.createFile(Paths.get("./src/test/resources/delete_test_directory/delete_test_file"))
            val testFile = ExtendedFile("./src/test/resources/delete_test_directory/delete_test_file")
            assertTrue(testFile.isFile)
            assertTrue(testFile.exists())
        }

        @Throws(IOException::class, SecurityException::class)
        private fun removeTestDirectory() {
            val file = ExtendedFile("./src/test/resources/delete_test_directory")
            assertTrue(file.exists())
            assertTrue(file.rm(true))
        }

        @Throws(IOException::class, SecurityException::class)
        private fun createTestInnerDirectoryAndFile() {
            Files.createDirectory(Paths.get("./src/test/resources/delete_test_directory"))
            val testDirectory = ExtendedFile("./src/test/resources/delete_test_directory")
            assertTrue(testDirectory.isDirectory)
            assertEquals("", testDirectory.getExtension())
            assertTrue(testDirectory.exists())

            Files.createDirectory(Paths.get("./src/test/resources/delete_test_directory/delete_test_directory"))
            val testInnerDirectory = ExtendedFile("./src/test/resources/delete_test_directory/delete_test_directory")
            assertTrue(testInnerDirectory.isDirectory)
            assertTrue(testInnerDirectory.exists())

            Files.createDirectory(Paths.get("./src/test/resources/delete_test_directory/delete_test_directory/delete_test_directory"))
            val testInnerInnerDirectory = ExtendedFile("./src/test/resources/delete_test_directory/delete_test_directory/delete_test_directory")
            assertTrue(testInnerInnerDirectory.isDirectory)
            assertTrue(testInnerInnerDirectory.exists())

            Files.createFile(Paths.get("./src/test/resources/delete_test_directory/delete_test_file1"))
            val testFile1 = ExtendedFile("./src/test/resources/delete_test_directory/delete_test_file1")
            assertTrue(testFile1.isFile)
            assertTrue(testFile1.exists())

            Files.createFile(Paths.get("./src/test/resources/delete_test_directory/delete_test_file2"))
            val testFile2 = ExtendedFile("./src/test/resources/delete_test_directory/delete_test_file2")
            assertTrue(testFile2.isFile)
            assertTrue(testFile2.exists())

            Files.createFile(Paths.get("./src/test/resources/delete_test_directory/delete_test_file3"))
            val testFile3 = ExtendedFile("./src/test/resources/delete_test_directory/delete_test_file3")
            assertTrue(testFile3.isFile)
            assertTrue(testFile3.exists())

            Files.createFile(Paths.get("./src/test/resources/delete_test_directory/delete_test_directory/delete_test_file1"))
            val testInnerFile1 = ExtendedFile("./src/test/resources/delete_test_directory/delete_test_directory/delete_test_file1")
            assertTrue(testInnerFile1.isFile)
            assertTrue(testInnerFile1.exists())

            Files.createFile(Paths.get("./src/test/resources/delete_test_directory/delete_test_directory/delete_test_file2"))
            val testInnerFile2 = ExtendedFile("./src/test/resources/delete_test_directory/delete_test_directory/delete_test_file2")
            assertTrue(testInnerFile2.isFile)
            assertTrue(testInnerFile2.exists())

            Files.createFile(Paths.get("./src/test/resources/delete_test_directory/delete_test_directory/delete_test_directory/delete_test_file1"))
            val testInnerInnerFile1 = ExtendedFile("./src/test/resources/delete_test_directory/delete_test_directory/delete_test_directory/delete_test_file1")
            assertTrue(testInnerInnerFile1.isFile)
            assertTrue(testInnerInnerFile1.exists())

            Files.createFile(Paths.get("./src/test/resources/delete_test_directory/delete_test_directory/delete_test_directory/delete_test_file2"))
            val testInnerInnerFile2 = ExtendedFile("./src/test/resources/delete_test_directory/delete_test_directory/delete_test_directory/delete_test_file2")
            assertTrue(testInnerInnerFile2.isFile)
            assertTrue(testInnerInnerFile2.exists())

            Files.createFile(Paths.get("./src/test/resources/delete_test_directory/delete_test_directory/delete_test_directory/delete_test_file3"))
            val testInnerInnerFile3 = ExtendedFile("./src/test/resources/delete_test_directory/delete_test_directory/delete_test_directory/delete_test_file3")
            assertTrue(testInnerInnerFile3.isFile)
            assertTrue(testInnerInnerFile3.exists())

            Files.createFile(Paths.get("./src/test/resources/delete_test_directory/delete_test_directory/delete_test_directory/delete_test_file4"))
            val testInnerInnerFile4 = ExtendedFile("./src/test/resources/delete_test_directory/delete_test_directory/delete_test_directory/delete_test_file4")
            assertTrue(testInnerInnerFile4.isFile)
            assertTrue(testInnerInnerFile4.exists())

            Files.createFile(Paths.get("./src/test/resources/delete_test_directory/delete_test_directory/delete_test_directory/delete_test_file5"))
            val testInnerInnerFile5 = ExtendedFile("./src/test/resources/delete_test_directory/delete_test_directory/delete_test_directory/delete_test_file5")
            assertTrue(testInnerInnerFile5.isFile)
            assertTrue(testInnerInnerFile5.exists())
        }
    }
}