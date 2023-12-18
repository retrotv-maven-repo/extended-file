package dev.retrotv

import dev.retrotv.crypto.owe.hash.FileHash
import dev.retrotv.crypto.owe.hash.sha.SHA256
import org.apache.tika.Tika
import java.io.File
import java.io.IOException
import java.net.URI
import java.nio.file.Files

class ExtendedFile : File {
    private val tika = Tika()

    constructor(filepath: String) : super(filepath)
    constructor(parent: String, child: String) : super(parent, child)
    constructor(parent: File, child: String) : super(parent, child)
    constructor(uri: URI) : super(uri)

    @Throws(IOException::class)
    fun getMimeType(): String {
        return tika.detect(this)
    }

    @Throws(IOException::class)
    fun isImage(): Boolean {
        return getMimeType().startsWith("image/")
    }

    @Throws(IOException::class)
    fun isText(): Boolean {
        return getMimeType().startsWith("text/")
    }

    @Throws(IOException::class)
    fun isAudio(): Boolean {
        return getMimeType().startsWith("audio/")
    }

    @Throws(IOException::class)
    fun isVideo(): Boolean {
        return getMimeType().startsWith("video/")
    }

    @Throws(IOException::class)
    fun matchesMimeType(mimeType: String): Boolean {
        return getMimeType() == mimeType
    }

    @JvmOverloads
    fun matches(file: File, fileHash: FileHash = SHA256()): Boolean {
        return fileHash.matches(this, file)
    }

    @Throws(IOException::class)
    fun matchesDeep(file: File): Boolean {
        val reader1 = Files.newBufferedReader(this.toPath())
        val reader2 = Files.newBufferedReader(file.toPath())

        var ch: Int
        while ((reader1.read().also { ch = it }) != -1) {
            if (ch != reader2.read()) {
                return false
            }
        }

        return true
    }

    @JvmOverloads
    fun getHashCode(fileHash: FileHash = SHA256()): String {
        return fileHash.hash(this)
    }
}