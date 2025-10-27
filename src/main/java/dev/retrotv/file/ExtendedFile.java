package dev.retrotv.file;

import dev.retrotv.crypto.util.HEXCodecUtils;
import dev.retrotv.crypto.hash.Hash;

import lombok.NonNull;
import org.apache.tika.Tika;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.util.Arrays;

/**
 * [File] 클래스의 기능을 확장한 클래스 입니다.
 *
 * @author yjj8353
 * @since 1.0.0
 */
public class ExtendedFile extends File {

    /**
     * 파일 해시 알고리즘을 정의하는 열거형 클래스입니다.
     * 지원하는 해시 알고리즘은 CRC32, MD5, SHA1, SHA224, SHA256, SHA384, SHA512, SHA512224, SHA512256, SHA3224, SHA3256, SHA3384, SHA3512 입니다.
     *
     * @author yjj8353
     * @since 1.0.0
     */
    public enum EHash {
          CRC32
        , MD5
        , SHA1
        , SHA224
        , SHA256
        , SHA384
        , SHA512
        , SHA512224
        , SHA512256
        , SHA3224
        , SHA3256
        , SHA3384
        , SHA3512
    }

    /**
     * 입력받은 filepath를 기반으로 [File] 객체를 생성합니다.
     *
     * @author yjj8353
     * @since 1.0.0
     * @param filepath 파일 경로
     */
    public ExtendedFile(@NonNull String filepath) {
        super(filepath);
    }

    /**
     * 입력받은 parent 디렉터리에 child 이름의 디렉터리나 파일을 나타내는 [File] 객체 생성합니다.
     *
     * @author yjj8353
     * @since 1.0.0
     * @param parent 디렉터리 경로 문자열
     * @param child 파일 경로 문자열
     */
    public ExtendedFile(@NonNull String parent, @NonNull String child) {
        super(parent, child);
    }
    /**
     * 입력받은 parent 디렉터리에 child 이름의 디렉터리나 파일을 나타내는 [File] 객체 생성합니다.
     *
     * @author yjj8353
     * @since 1.0.0
     * @param parent 디렉터리 정보가 담긴 [File] 객체
     * @param child 파일 경로 문자열
     */
    public ExtendedFile(@NonNull File parent, @NonNull String child) {
        super(parent, child);
    }

    /**
     * 입력받은 uri를 기반으로 [File] 객체를 생성합니다.
     *
     * @author yjj8353
     * @since 1.0.0
     * @param uri 파일의 경로가 담긴 [URI] 객체
     * @throws IllegalArgumentException uri 매개변수의 전제 조건이 유지되지 않는 경우 던져짐
     */
    public ExtendedFile(@NonNull URI uri) {
        super(uri);
    }

    /**
     * 파일의 복합 확장자를 반환합니다. (EX: tar.gz)
     * 해당 메서드는 가장 처음의 점(.) 이후의 모든 문자열을 확장자로 간주합니다.
     * 확장자가 없거나 해당 경로가 디렉터리인 경우, 빈 문자열을 반환합니다.
     *
     * @author yjj8353
     * @since 1.7.0
     * @return 파일 확장자
     */
    @NonNull public String getCompoundExtension() {
        return getExtension(true);
    }

    /**
     * 파일 확장자를 반환합니다.
     * 확장자가 없거나 해당 경로가 디렉터리인 경우, 빈 문자열을 반환합니다.
     *
     * @author yjj8353
     * @since 1.0.0
     * @return 파일 확장자
     */
    @NonNull public String getExtension() {
        return getExtension(false);
    }

    /**
     * 파일 및 디렉터리명을 반환합니다.
     * removeExtension 매개변수를 true로 설정하면, 확장자를 제거한 파일명을 반환합니다.
     * 해당 경로가 디렉터리인 경우, isRemoveExtension 매개변수의 값과 관계없이 디렉터리명을 반환합니다.
     *
     * @author yjj8353
     * @since 1.0.0
     * @param removeExtension 확장자 제거 여부
     * @return 파일명
     * @throws SecurityException 파일 및 디렉터리 접근 권한이 없으면 던져짐
     */
    @NonNull public String getName(boolean removeExtension) {
        return this.getName(removeExtension, false);
    }

    /**
     * 파일 및 디렉터리명을 반환합니다.
     * removeExtension 매개변수를 true로 설정하면, 확장자를 제거한 파일명을 반환합니다.
     * 해당 경로가 디렉터리인 경우, isRemoveExtension 매개변수의 값과 관계없이 디렉터리명을 반환합니다.
     *
     * @author yjj8353
     * @since 1.7.0
     * @param removeExtension 확장자 제거 여부
     * @param isCompoundedExtension 복합 확장자 여부
     * @return 파일명
     * @throws SecurityException 파일 및 디렉터리 접근 권한이 없으면 던져짐
     */
    @NonNull public String getName(boolean removeExtension, boolean isCompoundedExtension) {
        if (this.isDirectory() || !removeExtension) {
            return this.getName();
        } else {
            return isCompoundedExtension ? this.getName().replace("." + this.getCompoundExtension(), "")
                                         : this.getName().replace("." + this.getExtension(), "");
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
    @NonNull public String getMimeType() throws IOException {
        Tika tika = new Tika();
        return tika.detect(this);
    }

    /**
     * 파일의 유형이 이미지인지 여부를 반환합니다.
     *
     * @author yjj8353
     * @since 1.0.0
     * @return 이미지 파일인지 여부
     * @throws IOException 파일을 읽어들이는 과정 혹은 파싱 도중에 오류가 발생하면 던져짐
     */
    public boolean isImage() throws IOException {
        return getMimeType().startsWith("image/");
    }

    /**
     * 파일의 유형이 텍스트인지 여부를 반환합니다.
     *
     * @author yjj8353
     * @since 1.0.0
     * @return 텍스트 파일인지 여부
     * @throws IOException 파일을 읽어들이는 과정 혹은 파싱 도중에 오류가 발생하면 던져짐
     */
    public boolean isText() throws IOException {
        return getMimeType().startsWith("text/");
    }

    /**
     * 파일의 유형이 오디오인지 여부를 반환합니다.
     *
     * @author yjj8353
     * @since 1.0.0
     * @return 오디오 파일인지 여부
     * @throws IOException 파일을 읽어들이는 과정 혹은 파싱 도중에 오류가 발생하면 던져짐
     */
    public boolean isAudio() throws IOException {
        return getMimeType().startsWith("audio/");
    }

    /**
     * 파일의 유형이 비디오인지 여부를 반환합니다.
     *
     * @author yjj8353
     * @since 1.0.0
     * @return 비디오 파일인지 여부
     * @throws IOException 파일을 읽어들이는 과정 혹은 파싱 도중에 오류가 발생하면 던져짐
     */
    public boolean isVideo() throws IOException {
        return getMimeType().startsWith("video/");
    }

    /**
     * 파일의 유형이 지정한 파일 유형인지 여부를 반환합니다.
     *
     * @author yjj8353
     * @since 1.0.0
     * @return 지정한 파일 유형인지 여부
     * @throws IOException 파일을 읽어들이는 과정 혹은 파싱 도중에 오류가 발생하면 던져짐
     */
    public boolean matchesMimeType(@NonNull String mimeType) throws IOException {
        return getMimeType().equals(mimeType);
    }

    /**
     * 파일의 해시 코드를 생성해서, 동일한 파일인지 여부를 반환합니다.
     * SHA-256 알고리즘을 사용합니다.
     *
     * @author yjj8353
     * @since 1.0.0
     * @param file 비교할 [File] 객체
     * @return 동일한 파일인지 여부
     */
    public boolean matches(@NonNull File file) throws IOException {
        return matches(file, EHash.SHA256);
    }

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
    public boolean matches(@NonNull File file, @NonNull EHash hash) throws IOException {
        try {
            byte[] thisHash = Hash.getInstance(selectHashAlgorithm(hash)).hashing(Files.readAllBytes(this.toPath()));
            byte[] fileHash = Hash.getInstance(selectHashAlgorithm(hash)).hashing(Files.readAllBytes(file.toPath()));
            return Arrays.equals(thisHash, fileHash);
        } catch (IOException e) {
            throw new IOException("파일을 읽어들이는 과정에서 오류가 발생했습니다.", e);
        }
    }

    /**
     * 파일의 해시 코드를 생성해서, 동일한 파일인지 여부를 반환합니다.
     * 파일 해시 알고리즘을 별도로 지정하지 않는 경우, SHA-256 알고리즘을 사용합니다.
     *
     * @param file 비교할 [File] 객체
     * @param hash 파일 해시 알고리즘 (문자열)
     * @return 동일한 파일인지 여부
     */
    public boolean matches(@NonNull File file, @NonNull String hash) throws IOException {
        return matches(file, selectHashAlgorithm(hash));
    }

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
    public boolean matchesDeep(@NonNull File file) throws IOException, SecurityException {
        if (!this.exists() || !file.exists()) {
            return false;
        }

        if (this.length() != file.length()) {
            return false;
        }

        try (BufferedReader reader1 = Files.newBufferedReader(this.toPath());
             BufferedReader reader2 = Files.newBufferedReader(file.toPath())) {

            int ch;
            while ((ch = reader1.read()) != -1) {
                if (ch != reader2.read()) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * 파일의 해시 코드를 생성해서 반환합니다.
     * SHA-256 알고리즘을 사용합니다.
     *
     * @author yjj8353
     * @since 1.0.0
     * @return 파일의 해시 코드
     * @throws IOException 파일을 읽어들이는 과정에서 오류가 발생하면 던져짐
     */
    @NonNull public String getHash() throws IOException {
        return getHash(EHash.SHA256);
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
    @NonNull public String getHash(@NonNull EHash hash) throws IOException {
        Hash hashInstance = Hash.getInstance(selectHashAlgorithm(hash));
        return HEXCodecUtils.encode(hashInstance.hashing(Files.readAllBytes(this.toPath())));
    }

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
    @NonNull public String getHash(@NonNull String hash) throws IOException {
        return getHash(selectHashAlgorithm(hash));
    }

    /**
     * 파일의 크기를 반환합니다.
     * 사람이 읽기 쉬운 형태로 반환합니다. (ex. 1.23 MB)
     *
     * @author yjj8353
     * @since 1.0.0
     * @return 파일의 크기
     * @throws SecurityException – 파일 및 디렉터리 접근 권한이 없으면 던져짐
     */
    @NonNull public String getSize() throws SecurityException {
        return getSize(true);
    }

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
    @NonNull public String getSize(boolean isHumanReadable) throws SecurityException {
        long fileSize = this.length();
        if (isHumanReadable) {
            String suffix;
            double newFileSize;

            if (fileSize < 1024) {
                suffix = "Byte";
                newFileSize = fileSize;
            } else if (fileSize < 1024 * 1024) {
                suffix = "KB";
                newFileSize = fileSize / 1024.0;
            } else if (fileSize < 1024 * 1024 * 1024) {
                suffix = "MB";
                newFileSize = fileSize / (1024.0 * 1024);
            } else {
                suffix = "GB";
                newFileSize = fileSize / (1024.0 * 1024 * 1024);
            }

            return String.format("%.2f %s", newFileSize, suffix);
        } else {
            return String.valueOf(fileSize);
        }
    }

    /**
     * 파일 및 디렉터리를 삭제하고 성공 여부를 반환합니다.
     * 재귀삭제를 하지 않습니다. (디렉터리일 경우, 비어있을 때만 삭제 가능)
     *
     * @author yjj8353
     * @since 1.0.0
     * @return 삭제 성공 여부
     */
    public boolean rm() {
        return rm(false);
    }

    /**
     * 파일 및 디렉터리를 삭제하고 성공 여부를 반환합니다.
     *
     * @author yjj8353
     * @since 1.0.0
     * @param recursive 디렉터리일 경우, 재귀적으로 삭제할지에 대한 여부 (파일일 경우 무시)
     * @return 삭제 성공 여부
     */
    public boolean rm(boolean recursive) {
        if (this.isFile()) {
            return rmFile(this);
        } else {
            if (!recursive) {
                return rmFile(this);
            } else {
                return rmDirectory(this);
            }
        }
    }

    // 확장자명을 반환 (isCompound가 true일 경우 복합 확장자 반환)
    private String getExtension(boolean isCompound) {
        if (this.isDirectory()) {
            return "";
        }

        String name = this.getName();
        int firstIndex = name.indexOf('.');
        if (firstIndex == -1 || firstIndex == name.length() - 1) {
            return "";
        }

        return isCompound ? name.substring(firstIndex + 1) : name.substring(name.lastIndexOf('.') + 1);
    }

    // 파일 혹은 빈 디렉터리 삭제
    private boolean rmFile(@NonNull File file) {
        if (!file.exists()) {
            return false;
        }

        try {
            Files.delete(file.toPath());
            return true;
        } catch (IOException | SecurityException e) {
            return false;
        }
    }

    // 디렉터리 삭제 (내부에 파일이나 디렉터리가 있을 경우 재귀적으로 삭제)
    private boolean rmDirectory(@NonNull File file) {
        if (!file.exists() || !file.isDirectory()) {
            return false;
        }

        // 해당 디렉터리의 모든 파일 및 디렉터리 정보를 가져옴
        File[] deleteDirectoryList = file.listFiles();
        if (deleteDirectoryList != null) {
            for (File childFile : deleteDirectoryList) {
                if (childFile.isFile()) {
                    if (!rmFile(childFile)) {
                        return false;
                    }
                } else {
                    if (!rmDirectory(childFile)) {
                        return false;
                    }
                }
            }
        }

        // 해당 디렉터리 삭제
        return rmFile(file);
    }

    // 선택한 해시 알고리즘을 ExtendedFile.EHash로 변환
    @NonNull private EHash selectHashAlgorithm(@NonNull String hash) {
        switch (hash) {
            case "CRC32":
            case "crc32":
            case "CRC-32":
            case "crc-32":
                return EHash.CRC32;
            case "MD5":
            case "md5":
                return EHash.MD5;
            case "SHA-1":
            case "sha-1":
            case "SHA1":
            case "sha1":
                return EHash.SHA1;
            case "SHA-224":
            case "sha-224":
            case "SHA224":
            case "sha224":
                return EHash.SHA224;
            case "SHA-256":
            case "sha-256":
            case "SHA256":
            case "sha256":
                return EHash.SHA256;
            case "SHA-384":
            case "sha-384":
            case "SHA384":
            case "sha384":
                return EHash.SHA384;
            case "SHA-512":
            case "sha-512":
            case "SHA512":
            case "sha512":
                return EHash.SHA512;
            case "SHA-512/224":
            case "SHA-512224":
            case "sha-512224":
            case "sha-512/224":
            case "SHA512224":
            case "sha512224":
                return EHash.SHA512224;
            case "SHA-512/256":
            case "SHA-512256":
            case "sha-512256":
            case "sha-512/256":
            case "SHA512256":
            case "sha512256":
                return EHash.SHA512256;
            case "SHA3-224":
            case "sha3-224":
            case "SHA3224":
            case "sha3224":
                return EHash.SHA3224;
            case "SHA3-256":
            case "sha3-256":
            case "SHA3256":
            case "sha3256":
                return EHash.SHA3256;
            case "SHA3-384":
            case "sha3-384":
            case "SHA3384":
            case "sha3384":
                return EHash.SHA3384;
            case "SHA3-512":
            case "sha3-512":
            case "SHA3512":
            case "sha3512":
                return EHash.SHA3512;
            default:
                throw new IllegalArgumentException("지원하지 않는 해시 알고리즘입니다: " + hash);
        }
    }

    // 선택한 해시 알고리즘을 dev.retrotv.crypto.enums.EHash로 변환
    @NonNull private dev.retrotv.crypto.hash.enums.EHash selectHashAlgorithm(@NonNull EHash hash) {
        switch (hash) {
            case CRC32:
                return dev.retrotv.crypto.hash.enums.EHash.CRC32;
            case MD5:
                return dev.retrotv.crypto.hash.enums.EHash.MD5;
            case SHA1:
                return dev.retrotv.crypto.hash.enums.EHash.SHA1;
            case SHA224:
                return dev.retrotv.crypto.hash.enums.EHash.SHA224;
            case SHA256:
                return dev.retrotv.crypto.hash.enums.EHash.SHA256;
            case SHA384:
                return dev.retrotv.crypto.hash.enums.EHash.SHA384;
            case SHA512:
                return dev.retrotv.crypto.hash.enums.EHash.SHA512;
            case SHA512224:
                return dev.retrotv.crypto.hash.enums.EHash.SHA512224;
            case SHA512256:
                return dev.retrotv.crypto.hash.enums.EHash.SHA512256;
            case SHA3224:
                return dev.retrotv.crypto.hash.enums.EHash.SHA3224;
            case SHA3256:
                return dev.retrotv.crypto.hash.enums.EHash.SHA3256;
            case SHA3384:
                return dev.retrotv.crypto.hash.enums.EHash.SHA3384;
            case SHA3512:
                return dev.retrotv.crypto.hash.enums.EHash.SHA3512;
            default:
                throw new IllegalArgumentException("지원하지 않는 해시 알고리즘입니다: " + hash);
        }
    }
}
