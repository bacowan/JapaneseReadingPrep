import org.apache.tools.ant.taskdefs.condition.Os
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.2.51"
}

version = "1.0-SNAPSHOT"

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
    compile(kotlin("stdlib-jdk8"))
    compile(group="org.apache.pdfbox", name="pdfbox", version="2.0.1")
    compile(group="org.bytedeco.javacpp-presets", name="tesseract", version="4.0.0-rc2-1.4.3")
    compile(group="org.bytedeco.javacpp-presets", name="tesseract", version="4.0.0-rc2-1.4.3", classifier=opencvBinaryClassifier)
    compile(group="org.bytedeco.javacpp-presets", name="leptonica", version="1.72-1.0", classifier=opencvBinaryClassifier)
    compile(group="com.atilika.kuromoji", name="kuromoji-ipadic", version="0.9.0")
    compile(group="com.squareup.retrofit2", name="retrofit", version="2.4.0")
    compile(group="com.squareup.retrofit2", name="converter-gson", version="2.4.0")
    testCompile("junit:junit:4.12")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}