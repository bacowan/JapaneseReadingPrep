import org.apache.tools.ant.taskdefs.condition.Os
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.2.51"
    id("org.springframework.boot") version "2.1.0.RELEASE"
}
apply(plugin = "io.spring.dependency-management")

var opencvBinaryClassifier: String = ""
ext {

    var os: String? = null
    if (Os.isFamily(Os.FAMILY_UNIX)) {
        os = "linux"
    }
    if (Os.isFamily(Os.FAMILY_MAC)) {
        os = "macosx"
    }
    if (Os.isFamily(Os.FAMILY_WINDOWS)) {
        os = "windows"
    }
    var arch = System.getProperty("os.arch")

    if (arch == "amd64") {
        arch = "x86_64"
    }
    opencvBinaryClassifier = "$os-$arch"
}

repositories {
    mavenCentral()
}

dependencies {
    // parsing
    implementation(kotlin("stdlib-jdk8"))
    implementation(group="org.apache.pdfbox", name="pdfbox", version="2.0.1")
    implementation(group="org.bytedeco.javacpp-presets", name="tesseract", version="4.0.0-rc2-1.4.3")
    implementation(group="org.bytedeco.javacpp-presets", name="tesseract", version="4.0.0-rc2-1.4.3", classifier=opencvBinaryClassifier)
    implementation(group="org.bytedeco.javacpp-presets", name="leptonica", version="1.72-1.0", classifier=opencvBinaryClassifier)
    implementation(group="com.atilika.kuromoji", name="kuromoji-ipadic", version="0.9.0")
    implementation(group="com.squareup.retrofit2", name="retrofit", version="2.4.0")
    implementation(group="com.squareup.retrofit2", name="converter-gson", version="2.4.0")

    // server
    implementation(group="org.springframework", name="spring-websocket", version="5.1.2.RELEASE")
    implementation(group="org.springframework.boot", name="spring-boot", version="2.1.0.RELEASE")
    implementation(group="org.springframework.boot", name="spring-boot-starter-web", version="2.1.0.RELEASE")

    // testing
    testImplementation(group="org.mockito", name="mockito-core", version="2.23.0")
    testImplementation(group="com.squareup.okhttp3", name="mockwebserver", version="3.12.0")
    testImplementation("junit:junit:4.12")

}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}