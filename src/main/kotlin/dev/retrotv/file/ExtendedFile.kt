package dev.retrotv.file

import dev.retrotv.crypto.enums.EHash.SHA256
import dev.retrotv.crypto.hash.FileHash
import dev.retrotv.crypto.hash.Hash
import org.apache.tika.Tika
import java.io.File
import java.io.IOException
import java.net.URI
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.text.DecimalFormat
import kotlin.io.path.pathString
import kotlin.math.pow

/**
 * [File] 클래스의 기능을 확장한 클래스 입니다.
 *
 * @author  yjj8353
 * @since   1.0.0
 */
class ExtendedFile : File {
    private val tika = Tika()

    /**
     * 입력받은 filepath를 기반으로 [File] 객체를 생성합니다.
     *
     * @param filepath 파일 경로
     */
    constructor(filepath: String) : super(filepath)

    /**
     * 입력받은 parent 디렉토리에 child 이름의 디렉토리나 파일을 나타내는 [File] 객체 생성합니다.
     *
     * @param parent 디렉토리 경로 문자열
     * @param child 파일 경로 문자열
     */
    constructor(parent: String, child: String) : super(parent, child)

    /**
     * 입력받은 parent 디렉토리에 child 이름의 디렉토리나 파일을 나타내는 [File] 객체 생성합니다.
     *
     * @param parent 디렉토리 정보가 담긴 [File] 객체
     * @param child 파일 경로 문자열
     */
    constructor(parent: File, child: String) : super(parent, child)

    /**
     * 입력받은 uri를 기반으로 [File] 객체를 생성합니다.
     *
     * @throws IllegalArgumentException uri 매개변수의 전제 조건이 유지되지 않는 경우 던져짐
     * @param uri 파일의 경로가 담긴 [URI] 객체
     */
    @Throws(IllegalArgumentException::class)
    constructor(uri: URI) : super(uri)

    /**
     * 파일 확장자를 반환합니다.
     * 해당 경로가 디렉토리인 경우, 빈 문자열을 반환합니다.
     *
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
     * 파일 및 디렉토리명을 반환합니다.
     * isRemoveExtension 매개변수를 true로 설정하면, 확장자를 제거한 파일명을 반환합니다.
     * 해당 경로가 디렉토리인 경우, isRemoveExtension 매개변수의 값과 관계없이 디렉토리명을 반환합니다.
     *
     * @param isRemoveExtension 확장자 제거 여부
     * @return 파일명
     */
    fun getName(isRemoveExtension: Boolean): String {
        return if (this.isDirectory || !isRemoveExtension) {
            this.name
        } else {
            this.name.replace(".${this.getExtension()}", "")
        }
    }

    /**
     * 파일의 MIME type을 반환합니다.
     *
     * @throws IOException 파일을 읽을 수 없는 경우 던져짐
     * @return 파일의 MIME type
     */
    @Throws(IOException::class)
    fun getMimeType(): String = tika.detect(this)

    /**
     * 파일의 유형이 이미지인지 여부를 반환합니다.
     *
     * @throws IOException 파일을 읽을 수 없는 경우 던져짐
     * @return 이미지 파일인지 여부
     */
    @Throws(IOException::class)
    fun isImage(): Boolean = getMimeType().startsWith("image/")

    /**
     * 파일의 유형이 텍스트인지 여부를 반환합니다.
     *
     * @throws IOException 파일을 읽을 수 없는 경우 던져짐
     * @return 텍스트 파일인지 여부
     */
    @Throws(IOException::class)
    fun isText(): Boolean = getMimeType().startsWith("text/")

    /**
     * 파일의 유형이 오디오인지 여부를 반환합니다.
     *
     * @throws IOException 파일을 읽을 수 없는 경우 던져짐
     * @return 오디오 파일인지 여부
     */
    @Throws(IOException::class)
    fun isAudio(): Boolean = getMimeType().startsWith("audio/")

    /**
     * 파일의 유형이 비디오인지 여부를 반환합니다.
     *
     * @throws IOException 파일을 읽을 수 없는 경우 던져짐
     * @return 비디오 파일인지 여부
     */
    @Throws(IOException::class)
    fun isVideo(): Boolean = getMimeType().startsWith("video/")

    /**
     * 파일의 유형이 지정한 파일 유형인지 여부를 반환합니다.
     *
     * @sample matchesMimeType("application/pdf")
     * @throws IOException 파일을 읽을 수 없는 경우 던져짐
     * @return 지정한 파일 유형인지 여부
     */
    @Throws(IOException::class)
    fun matchesMimeType(mimeType: String): Boolean = getMimeType() == mimeType

    /**
     * 파일의 해시 코드를 생성해서, 동일한 파일인지 여부를 반환합니다.
     * 파일 해시 알고리즘을 별도로 지정하지 않는 경우, SHA-256 알고리즘을 사용합니다.
     *
     * @throws IOException 파일을 읽을 수 없는 경우 던져짐
     * @param file 비교할 [File] 객체
     * @param fileHash 파일 해시 알고리즘
     * @return 동일한 파일인지 여부
     */
    @JvmOverloads
    @Throws(IOException::class)
    fun matches(file: File, fileHash: FileHash
        = Hash.getInstance(SHA256)): Boolean = fileHash.matches(this, fileHash.hash(file))

    /**
     * 파일을 처음부터 끝까지 읽어서, 동일한 파일인지 여부를 반환합니다.
     * 해시 코드를 이용한 비교보다 정확하지만 파일의 크기에 따라 성능에 영향을 미칠 수 있습니다.
     *
     * @throws IOException 파일을 읽을 수 없는 경우 던져짐
     * @param file 비교할 [File] 객체
     * @return 동일한 파일인지 여부
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
     * @throws IOException 파일을 읽을 수 없는 경우 던져짐
     * @param fileHash 파일 해시 알고리즘
     * @return 파일의 해시 코드
     */
    @JvmOverloads
    @Throws(IOException::class)
    fun getHash(fileHash: FileHash = Hash.getInstance(SHA256)): String = fileHash.hash(this)

    /**
     * 파일의 크기를 반환합니다.
     * isHumanReadable 매개변수를 true일 경우, 사람이 읽기 쉬운 형태로 반환합니다. (ex. 1.23 MB)
     *
     * @param isHumanReadable 사람이 읽기 쉬운 형태로 반환할지 여부 (기본 값: true)
     * @return 파일의 크기
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
     * 파일 및 디렉토리를 삭제하고 성공 여부를 반환합니다.
     *
     * @param recursive 디렉토리일 경우, 재귀적으로 삭제할지에 대한 여부 (파일일 경우 무시)
     * @return 삭제 성공 여부
     */
    @JvmOverloads
    @Throws(IOException::class, SecurityException::class)
    fun rm(recursive: Boolean = false): Boolean {
        return if (this.isFile) {
            this.delete()

        // isDirectory
        } else {
            if (!recursive) {
                this.delete()
            } else {
                rmDirectory()
            }
        }
    }

    private fun rmDirectory(): Boolean {
        val deleteFileList = ArrayList<FileVO>()

        try {
            getAllFiles(deleteFileList)

            deleteFileList.forEach {
                fileVO -> {
                    File(fileVO.path.pathString).delete()
                }
            }

            return true
        } catch (ex: IOException) {
            return false
        } catch (ex: SecurityException) {
            return false
        }
    }

    // 지정한 Path가 디렉토리일 경우, 해당 디렉토리 내부의 모든 디렉토리와 파일에 대한 Path와 파일여부 정보를 반환하는 메서드
    @Throws(IOException::class)
    private fun getAllFiles(files: MutableList<FileVO>) {
        val stream = Files.newDirectoryStream(Paths.get(this.path))

        stream.use {
            stream.forEach { entry ->
                if (Files.isDirectory(entry)) {
                    files.add(FileVO(entry, false))
                    getAllFiles(files)
                } else {
                    files.add(FileVO(entry, true))
                }
            }
        }
    }

    // 경로와 해당 경로가 파일인지 여부를 담는 VO
    private class FileVO(var path: Path, var isFile: Boolean)
}