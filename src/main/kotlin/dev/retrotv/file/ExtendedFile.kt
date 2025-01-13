package dev.retrotv.file

import dev.retrotv.crypto.enums.EHash
import dev.retrotv.crypto.enums.EHash.*
import dev.retrotv.crypto.hash.BinaryHash
import dev.retrotv.crypto.hash.Hash
import dev.retrotv.crypto.util.CodecUtils
import org.apache.tika.Tika
import org.apache.tika.metadata.Metadata
import java.io.File
import java.io.IOException
import java.net.URI
import java.nio.file.Files
import java.text.DecimalFormat
import kotlin.math.pow

/**
 * [File] 클래스의 기능을 확장한 클래스 입니다.
 *
 * @author yjj8353
 * @since 1.0.0
 */
class ExtendedFile : File {

    /**
     * 입력받은 filepath를 기반으로 [File] 객체를 생성합니다.
     *
     * @author yjj8353
     * @since 1.0.0
     * @param filepath 파일 경로
     */
    constructor(filepath: String) : super(filepath)

    /**
     * 입력받은 parent 디렉터리에 child 이름의 디렉터리나 파일을 나타내는 [File] 객체 생성합니다.
     *
     * @author yjj8353
     * @since 1.0.0
     * @param parent 디렉터리 경로 문자열
     * @param child 파일 경로 문자열
     */
    constructor(parent: String, child: String) : super(parent, child)

    /**
     * 입력받은 parent 디렉터리에 child 이름의 디렉터리나 파일을 나타내는 [File] 객체 생성합니다.
     *
     * @author yjj8353
     * @since 1.0.0
     * @param parent 디렉터리 정보가 담긴 [File] 객체
     * @param child 파일 경로 문자열
     */
    constructor(parent: File, child: String) : super(parent, child)

    /**
     * 입력받은 uri를 기반으로 [File] 객체를 생성합니다.
     *
     * @author yjj8353
     * @since 1.0.0
     * @param uri 파일의 경로가 담긴 [URI] 객체
     * @throws IllegalArgumentException uri 매개변수의 전제 조건이 유지되지 않는 경우 던져짐
     */
    constructor(uri: URI) : super(uri)

    /**
     * 파일 확장자를 반환합니다.
     * 확장자가 없거나 해당 경로가 디렉터리인 경우, 빈 문자열을 반환합니다.
     *
     * @author yjj8353
     * @since 1.0.0
     * @return 파일 확장자
     */
    fun getExtension(): String {
        if (this.isDirectory) {
            return ""
        }

        val extension = this.name.substringAfter('.')
        if (this.name == extension) {
            return ""
        }

        return extension
    }

    /**
     * 파일 및 디렉터리명을 반환합니다.
     * isRemoveExtension 매개변수를 true로 설정하면, 확장자를 제거한 파일명을 반환합니다. (기본 값: false)
     * 해당 경로가 디렉터리인 경우, isRemoveExtension 매개변수의 값과 관계없이 디렉터리명을 반환합니다.
     *
     * @author yjj8353
     * @since 1.0.0
     * @param removeExtension 확장자 제거 여부
     * @return 파일명
     * @throws SecurityException 파일 및 디렉터리 접근 권한이 없으면 던져짐
     */
    fun getName(removeExtension: Boolean = false): String {
        return if (this.isDirectory || !removeExtension) {
            this.name
        } else {
            this.name.replace(".${this.getExtension()}", "")
        }
    }

    /**
     * 파일의 MIME type을 반환합니다.
     *
     * @author yjj8353
     * @since 1.0.0
     * @return 파일의 MIME type
     * @throws IOException 파일을 읽어들이는 과정 혹은 파싱 도중에 오류가 발생하면 던져짐
     */
    @Throws(IOException::class)
    fun getMimeType(): String {
        val tika = Tika()
        val metadata = Metadata()
        tika.parse(this, metadata)

        // tika.parse() 메서드 실행 후, metadata에 Content-Type이 저장됨
        return metadata["Content-Type"]
    }

    /**
     * 파일의 유형이 이미지인지 여부를 반환합니다.
     *
     * @author yjj8353
     * @since 1.0.0
     * @return 이미지 파일인지 여부
     * @throws IOException 파일을 읽어들이는 과정 혹은 파싱 도중에 오류가 발생하면 던져짐
     */
    @Throws(IOException::class)
    fun isImage(): Boolean = getMimeType().startsWith("image/")

    /**
     * 파일의 유형이 텍스트인지 여부를 반환합니다.
     *
     * @author yjj8353
     * @since 1.0.0
     * @return 텍스트 파일인지 여부
     * @throws IOException 파일을 읽어들이는 과정 혹은 파싱 도중에 오류가 발생하면 던져짐
     */
    @Throws(IOException::class)
    fun isText(): Boolean = getMimeType().startsWith("text/")

    /**
     * 파일의 유형이 오디오인지 여부를 반환합니다.
     *
     * @author yjj8353
     * @since 1.0.0
     * @return 오디오 파일인지 여부
     * @throws IOException 파일을 읽어들이는 과정 혹은 파싱 도중에 오류가 발생하면 던져짐
     */
    @Throws(IOException::class)
    fun isAudio(): Boolean = getMimeType().startsWith("audio/")

    /**
     * 파일의 유형이 비디오인지 여부를 반환합니다.
     *
     * @author yjj8353
     * @since 1.0.0
     * @return 비디오 파일인지 여부
     * @throws IOException 파일을 읽어들이는 과정 혹은 파싱 도중에 오류가 발생하면 던져짐
     */
    @Throws(IOException::class)
    fun isVideo(): Boolean = getMimeType().startsWith("video/")

    /**
     * 파일의 유형이 지정한 파일 유형인지 여부를 반환합니다.
     *
     * @author yjj8353
     * @since 1.0.0
     * @sample matchesMimeType("application/pdf")
     * @return 지정한 파일 유형인지 여부
     * @throws IOException 파일을 읽어들이는 과정 혹은 파싱 도중에 오류가 발생하면 던져짐
     */
    @Throws(IOException::class)
    fun matchesMimeType(mimeType: String): Boolean = getMimeType() == mimeType

    /**
     * 파일의 해시 코드를 생성해서, 동일한 파일인지 여부를 반환합니다.
     * 파일 해시 알고리즘을 별도로 지정하지 않는 경우, SHA-256 알고리즘을 사용합니다.
     *
     * @author yjj8353
     * @since 1.0.0
     * @param file 비교할 [File] 객체
     * @param hash 파일 해시 알고리즘
     * @return 동일한 파일인지 여부
     */
    @JvmOverloads
    fun matches(file: File, hash: BinaryHash = Hash.getInstance(SHA256)): Boolean {
        return hash.matches(this.readBytes(), CodecUtils.encode(hash.hashing(file.readBytes())))
    }

    /**
     * 파일의 해시 코드를 생성해서, 동일한 파일인지 여부를 반환합니다.
     * 파일 해시 알고리즘을 별도로 지정하지 않는 경우, SHA-256 알고리즘을 사용합니다.
     *
     * @param file 비교할 [File] 객체
     * @param hash 파일 해시 알고리즘 (문자열)
     * @return 동일한 파일인지 여부
     */
    fun matches(file: File, hash: String): Boolean = matches(file, Hash.getInstance(selectHashAlgorithm(hash)))

    /**
     * 파일을 처음부터 끝까지 읽어서, 동일한 파일인지 여부를 반환합니다.
     * 해시 코드를 이용한 비교보다 정확하지만 파일의 크기에 따라 성능에 영향을 미칠 수 있습니다.
     *
     * @author yjj8353
     * @since 1.0.0
     * @param file 비교할 [File] 객체
     * @return 동일한 파일인지 여부
     * @throws IOException 파일을 읽어들이는 과정에서 오류가 발생하면 던져짐
     * @throws SecurityException 파일 및 디렉터리 접근 권한이 없으면 던져짐
     */
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

    /**
     * 파일의 해시 코드를 생성해서 반환합니다.
     * 파일 해시 알고리즘을 별도로 지정하지 않는 경우, SHA-256 알고리즘을 사용합니다.
     *
     * @author yjj8353
     * @since 1.0.0
     * @param hash 파일 해시 알고리즘
     * @return 파일의 해시 코드
     * @throws IOException 파일을 읽어들이는 과정에서 오류가 발생하면 던져짐
     */
    @JvmOverloads
    @Throws(IOException::class)
    fun getHash(hash: BinaryHash = Hash.getInstance(SHA256)): String = CodecUtils.encode(hash.hashing(this.readBytes()))

    /**
     * 파일의 해시 코드를 생성해서 반환합니다.
     * 파일 해시 알고리즘을 별도로 지정하지 않는 경우, SHA-256 알고리즘을 사용합니다.
     *
     * @author yjj8353
     * @since 1.0.0
     * @param hash 파일 해시 알고리즘 (문자열)
     * @return 파일의 해시 코드
     * @throws IOException 파일을 읽어들이는 과정에서 오류가 발생하면 던져짐
     */
    @Throws(IOException::class)
    fun getHash(hash: String): String = getHash(Hash.getInstance(selectHashAlgorithm(hash)))

    /**
     * 파일의 크기를 반환합니다.
     * isHumanReadable 매개변수를 true일 경우, 사람이 읽기 쉬운 형태로 반환합니다. (ex. 1.23 MB)
     *
     * @author yjj8353
     * @since 1.0.0
     * @param isHumanReadable 사람이 읽기 쉬운 형태로 반환할지 여부 (기본 값: true)
     * @return 파일의 크기
     * @throws SecurityException – 파일 및 디렉터리 접근 권한이 없으면 던져짐
     */
    @JvmOverloads
    fun getSize(isHumanReadable: Boolean = true): String {
        val fileSize = this.length()
        return if (isHumanReadable) {
            val suffix = when {
                fileSize < 1024 -> "Byte"
                fileSize < 1024 * 1024 -> "KB"
                fileSize < 1024 * 1024 * 1024 -> "MB"
                else -> "GB"
            }

            val newFileSize = when {
                fileSize < 1024 -> fileSize
                fileSize < 1024 * 1024 -> fileSize / 1024.0
                fileSize < 1024 * 1024 * 1024 -> fileSize / 1024.0.pow(2)
                else -> fileSize / 1024.0.pow(3)
            }

            val df = DecimalFormat("###.##")
            df.format(newFileSize) + " " + suffix
        } else {
            fileSize.toString()
        }
    }

    /**
     * 파일 및 디렉터리를 삭제하고 성공 여부를 반환합니다.
     *
     * @author yjj8353
     * @since 1.0.0
     * @param recursive 디렉터리일 경우, 재귀적으로 삭제할지에 대한 여부 (파일일 경우 무시)
     * @return 삭제 성공 여부
     */
    @JvmOverloads
    fun rm(recursive: Boolean = false): Boolean {
        return if (this.isFile) {
            rmFile(this)

        // isDirectory
        } else {
            if (!recursive) {
                rmFile(this)
            } else {
                val results = ArrayList<Boolean>()
                rmDirectory(this, results)

                // 모든 값이 true인 경우에만 true 반환
                results.all { it }
            }
        }
    }

    // 파일 혹은 빈 디렉터리 삭제
    private fun rmFile(file: File): Boolean {
        return try {
            Files.delete(file.toPath())
            true
        } catch (_: IOException) {
            false
        } catch (_: SecurityException) {
            false
        }
    }

    // 디렉터리 삭제 (내부에 파일이나 디렉터리가 있을 경우 재귀적으로 삭제)
    private fun rmDirectory(file: File, results: MutableList<Boolean>) {
        
        // 해당 디렉터리의 모든 파일 및 디렉터리 정보를 가져옴
        val deleteDirectoryList = file.listFiles()
        deleteDirectoryList?.forEach { file ->
            if (file.isFile && rmFile(file)) {
                results.add(true)
            } else if (file.isFile && !rmFile(file)) {
                results.add(false)
            } else {
                rmDirectory(file, results)
            }
        }

        // 해당 디렉터리 삭제
        if (rmFile(file)) {
            results.add(true)
        } else {
            results.add(false)
        }
    }

    private fun selectHashAlgorithm(hash: String): EHash {
        return when (hash) {
            "MD5", "md5" -> MD5
            "SHA-1", "sha-1", "SHA1", "sha1" -> SHA1
            "SHA-224", "sha-224", "SHA224", "sha224" -> SHA224
            "SHA-256", "sha-256", "SHA256","sha256" -> SHA256
            "SHA-384", "sha-384", "SHA384", "sha384" -> SHA384
            "SHA-512", "sha-512", "SHA512", "sha512" -> SHA512
            "SHA3-224", "sha3-224", "SHA3224", "sha3224" -> SHA3224
            "SHA3-256", "sha3-256", "SHA3256", "sha3256" -> SHA3256
            "SHA3-384", "sha3-384", "SHA3384", "sha3384" -> SHA3384
            "SHA3-512", "sha3-512", "SHA3512", "sha3512" -> SHA3512
            else -> throw IllegalArgumentException("지원하지 않는 해시 알고리즘입니다.")
        }
    }
}