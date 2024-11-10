package dev.retrotv.file

import dev.retrotv.crypto.enums.EHash
import dev.retrotv.crypto.hash.Hash
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import java.io.IOException
import java.net.URISyntaxException
import java.util.*
import kotlin.test.*

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
        assertNotNull(file.getHash(Hash.getInstance(EHash.SHA512)))
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
        }

        @Test
        @DisplayName("파일이 다른 경우")
        fun test_matches_different() {
            val file = ExtendedFile(Objects.requireNonNull(textFile).toURI())
            val file2 = ExtendedFile(Objects.requireNonNull(textFileDifferent).toURI())
            assertFalse(file.matches(file2))
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
            assertEquals("2.29 MB", file.getSize())
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
            assertEquals("2400000", file.getSize(false))
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
        @DisplayName("단건 파일 삭제")
        @Throws(IOException::class, SecurityException::class)
        fun test_rm_singleFile() {
            createTestFile()
            val file = ExtendedFile("./src/test/resources/delete_test_file")
            assertTrue(file.rm())
        }

        @Throws(IOException::class, SecurityException::class)
        private fun createTestFile() {
            ExtendedFile("./src/test/resources/delete_test_file").createNewFile()
        }

        @Test
        @DisplayName("빈 디렉토리 삭제")
        @Throws(IOException::class, SecurityException::class)
        fun test_rm_emptyDirectory() {
            createTestDirectory()
            val file = ExtendedFile(this.javaClass.getClassLoader().getResource("delete_test_directory")!!.toURI())
            assertTrue(file.exists())
            assertTrue(file.rm())

            createTestDirectoryAndFile()
            assertFalse(file.rm())
            removeTestDirectory()
        }

        @Throws(SecurityException::class)
        private fun createTestDirectory() {
            ExtendedFile("./src/test/resources/delete_test_directory").mkdir()
        }

        @Throws(IOException::class, SecurityException::class)
        private fun createTestDirectoryAndFile() {
            ExtendedFile("./src/test/resources/delete_test_directory").mkdir()
            ExtendedFile("./src/test/resources/delete_test_directory/delete_test_file").createNewFile()
        }

        @Throws(IOException::class, SecurityException::class)
        private fun removeTestDirectory() {
            ExtendedFile("./src/test/resources/delete_test_directory").rm()
        }
    }
}