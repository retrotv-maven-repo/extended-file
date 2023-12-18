package dev.retrotv

import java.util.*
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class KotlinFileTest {
    private val TEXT_FILE = this.javaClass.getClassLoader().getResource("text_file")
    private val TEXT_FILE_COPY = this.javaClass.getClassLoader().getResource("text_file_copy")

    @Test
    fun test_file() {
        val file = ExtendedFile(Objects.requireNonNull(TEXT_FILE).toURI())
        val file2 = ExtendedFile(Objects.requireNonNull(TEXT_FILE_COPY).toURI())

        println(file.getMimeType())
        assertTrue(file.isText())
        assertTrue(file.matchesMimeType("text/plain"))
        assertFalse(file.isAudio())
        assertFalse(file.isVideo())
        assertFalse(file.isImage())
        assertTrue(file.matchesDeep(file2))
        assertTrue(file.matches(file2))
    }
}