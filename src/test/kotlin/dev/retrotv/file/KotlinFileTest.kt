package dev.retrotv.file

import dev.retrotv.crypto.owe.hash.sha.SHA512
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import java.util.*
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class KotlinFileTest {
    private val textFile = this.javaClass.getClassLoader().getResource("text_file")
    private val textFileCopy = this.javaClass.getClassLoader().getResource("text_file_copy")
    private val textFileDifferent = this.javaClass.getClassLoader().getResource("text_file_different")

    @Test
    @DisplayName("getMimeType() 메소드 테스트")
    fun test_getMimeType() {
        val file = ExtendedFile(Objects.requireNonNull(textFile).toURI())
        assertTrue(file.getMimeType().startsWith("text/plain"))
    }

    @Test
    @DisplayName("matchesMimeType() 메소드 테스트")
    fun test_matchesMimeType() {
        val file = ExtendedFile(Objects.requireNonNull(textFile).toURI().toString().replace("file:", ""))
        assertTrue(file.matchesMimeType("text/plain"))
    }

    @Test
    @DisplayName("isText() 메소드 테스트")
    fun test_isText() {
        val file = ExtendedFile(Objects.requireNonNull(textFile).toURI().toString().replace("file:", ""), "")
        assertTrue(file.isText())
    }

    @Test
    @DisplayName("isAudio 메소드 테스트")
    fun test_isAudio() {
        val file = ExtendedFile(Objects.requireNonNull(textFile).toURI())
        val file2 = ExtendedFile(file, "")
        assertFalse(file2.isAudio())
    }

    @Test
    @DisplayName("isVideo() 메소드 테스트")
    fun test_isVideo() {
        val file = ExtendedFile(Objects.requireNonNull(textFile).toURI())
        assertFalse(file.isVideo())
    }

    @Test
    @DisplayName("isImage() 메소드 테스트")
    fun test_isImage() {
        val file = ExtendedFile(Objects.requireNonNull(textFile).toURI())
        assertFalse(file.isImage())
    }

    @Test
    @DisplayName("getHashCode() 메소드 테스트")
    fun test_getHashCode() {
        val file = ExtendedFile(Objects.requireNonNull(textFile).toURI())
        assertNotNull(file.getFileHash())
    }

    @Test
    @DisplayName("getHashCode(SHA512()) 메소드 테스트")
    fun test_getHashCode_sha512() {
        val file = ExtendedFile(Objects.requireNonNull(textFile).toURI())
        assertNotNull(file.getFileHash(SHA512()))
    }

    @Nested
    @DisplayName("matches() 메소드 테스트")
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
    @DisplayName("matchesDeep() 메소드 테스트")
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
}