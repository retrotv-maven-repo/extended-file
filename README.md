# ExtendedFile
기존 Java의 java.io.File 객체를 확장한 파일 객체 라이브러리 입니다.  
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=retrotv-maven-repo_extended-file&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=retrotv-maven-repo_extended-file)  
[![](https://jitpack.io/v/retrotv-maven-repo/extended-file.svg)](https://jitpack.io/#retrotv-maven-repo/extended-file)  
## 지원 JDK
JDK 1.8 이상
## 기존 File 대비 추가된 기능들
### 1. MIME Type 가져오기
#### Java
```JAVA
ExtendedFile file = new ExtendedFile("file.txt");
String mimeType = file.getMimeType();
```
#### Kotlin
```Kotlin
val file = ExtendedFile("file.txt")
val mimeType = file.getMimeType()
```
### 2. 이미지 / 텍스트 / 오디오 / 비디오 파일 여부 판단
#### Java
```JAVA
ExtendedFile file = new ExtendedFile("file.txt");
boolean isTextFile = file.isText();
```
#### Kotlin
```Kotlin
val file = ExtendedFile("file.txt")
val isAudioFile = file.isAudio()
```
### 3. MIME Type 일치 여부 판단  
이미지 / 텍스트 / 오디오 / 비디오로 구분되지 않는 특수한 파일을 검증하기 위한 기능입니다.
#### Java
```JAVA
ExtendedFile file = new ExtendedFile("file.txt");
boolean isMatch = file.matchesMimeType("application/octet-stream");
```
#### Kotlin
```Kotlin
val file = ExtendedFile("file.txt")
val isMatch = file.matchesMimeType("application/octet-stream")
```
### 4. 동일 파일 여부 판단  
두 개의 파일을 비교해, 동일한 파일인지 판단합니다.  
파일을 비교할 때 해시 값을 비교하는 방법, 전체 파일을 읽어들여 비교하는 방법 두 가지를 제공합니다.
#### Java
```JAVA
ExtendedFile file1 = new ExtendedFile("file1.txt");
ExtendedFile file2 = new ExtendedFile("file2.txt");

// 별도의 해시 알고리즘을 지정하지 않는다면, SHA-256 알고리즘을 이용해 해시 값을 생성하고 비교합니다.
boolean isHashMatch = file1.matches(file2);

// matchesDeep 메소드는 두 파일의 모든 Byte를 일일이 비교합니다.
boolean isDeepMatch = file1.matchesDeep(file2);
```
#### Kotlin
```Kotlin
val file1 = ExtendedFile("file1.txt")
val file2 = ExtendedFile("file2.txt")

// 별도의 해시 알고리즘을 지정해 준다면, 해당 알고리즘으로 해시 값을 생성하고 비교합니다.
val isHashMatch = file1.matches(file2, MD5())
val isDeepMatch = file1.matchesDeep(file2)
```
### 5. 파일 해시 값 가져오기
#### Java
```JAVA
ExtendedFile file = new ExtendedFile("file.txt");
String hashCode = file.getHashCode();
```
#### Kotlin
```Kotlin
val file = ExtendedFile("file.txt")
val hashCode = file.getHashCode()
```
## TODO
1. 더 다양한 포맷의 파일이 mimeType이 제공되는지 확인 (.hwp, .hwpx 같은 것들)
