package de.unia.se.plantestic

import com.google.common.io.Resources
import io.kotlintest.specs.StringSpec
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl
import org.joor.Reflect
import java.io.File
import io.kotlintest.Description
import io.kotlintest.TestResult

class AcceleoGeneratorTest : StringSpec({
    "Transform a Rest Assured EObject input to Java Code for minimal hello" {
        MetaModelSetup.doSetup()

        val pumlInputModelURI = URI.createFileURI(MINIMAL_EXAMPLE_INPUT_FILE.path)
        val pumlInputModel = ResourceSetImpl().getResource(pumlInputModelURI, true).contents[0]

        AcceleoCodeGenerator.generateCode(pumlInputModel, OUTPUT_FOLDER)

        // Now compile the resulting code to check for syntax errors
        val generatedSourceFile = OUTPUT_FOLDER.listFiles().filter { f -> f.name == "Testminimal_hello.java" }.first()
        printCode(generatedSourceFile)
        Reflect.compile(
            "com.plantestic.test.${generatedSourceFile.nameWithoutExtension}",
            generatedSourceFile.readText()
        ).create(MINIMAL_EXAMPLE_CONFIG_FILE.path)
    }

    "Transform a Rest Assured EObject input to Java Code for complex hello" {
        MetaModelSetup.doSetup()

        val pumlInputModelURI = URI.createFileURI(COMPLEX_HELLO_INPUT_FILE.path)
        val pumlInputModel = ResourceSetImpl().getResource(pumlInputModelURI, true).contents[0]

        AcceleoCodeGenerator.generateCode(pumlInputModel, OUTPUT_FOLDER)

        // Now compile the resulting code to check for syntax errors
        val generatedSourceFile = OUTPUT_FOLDER.listFiles().filter { f -> f.name == "Testcomplex_hello.java" }.first()
        printCode(generatedSourceFile)
        Reflect.compile(
            "com.plantestic.test.${generatedSourceFile.nameWithoutExtension}",
            generatedSourceFile.readText()
        ).create(COMPLEX_HELLO_CONFIG_FILE.path)
    }

    "Transform a Rest Assured EObject input to Java Code for rerouting" {
        MetaModelSetup.doSetup()

        val pumlInputModelURI = URI.createFileURI(REROUTE_INPUT_FILE.path)
        val pumlInputModel = ResourceSetImpl().getResource(pumlInputModelURI, true).contents[0]

        AcceleoCodeGenerator.generateCode(pumlInputModel, OUTPUT_FOLDER)

        // Now compile the resulting code to check for syntax errors
        val generatedSourceFile = OUTPUT_FOLDER.listFiles().filter { f -> f.name == "Testrerouting.java" }.first()
        printCode(generatedSourceFile)
        Reflect.compile(
            "com.plantestic.test.${generatedSourceFile.nameWithoutExtension}",
            generatedSourceFile.readText()
        ).create(REROUTE_CONFIG_FILE.path)
    }

    "Transform a Rest Assured EObject input to Java Code for xcall" {
        MetaModelSetup.doSetup()

        val pumlInputModelURI = URI.createFileURI(XCALL_INPUT_FILE.path)
        val pumlInputModel = ResourceSetImpl().getResource(pumlInputModelURI, true).contents[0]

        AcceleoCodeGenerator.generateCode(pumlInputModel, OUTPUT_FOLDER)

        // Now compile the resulting code to check for syntax errors
        val generatedSourceFile = OUTPUT_FOLDER.listFiles().filter { f -> f.name == "Testxcall.java" }.first()
        printCode(generatedSourceFile)
        Reflect.compile(
            "com.plantestic.test.${generatedSourceFile.nameWithoutExtension}",
            generatedSourceFile.readText()
        ).create(XCALL_CONFIG_FILE.path)
    }
}) {
    companion object {
        private val MINIMAL_EXAMPLE_INPUT_FILE = File(Resources.getResource("minimal_hello_restassured.xmi").path)
        private val MINIMAL_EXAMPLE_CONFIG_FILE = File(Resources.getResource("minimal_hello_config.toml").path)

        private val COMPLEX_HELLO_INPUT_FILE = File(Resources.getResource("complex_hello_restassured.xmi").path)
        private val COMPLEX_HELLO_CONFIG_FILE = File(Resources.getResource("complex_hello_config.toml").path)

        private val REROUTE_INPUT_FILE = File(Resources.getResource("rerouting_restassured.xmi").path)
        private val REROUTE_CONFIG_FILE = File(Resources.getResource("rerouting_config.toml").path)

        private val XCALL_INPUT_FILE = File(Resources.getResource("xcall_restassured.xmi").path)
        private val XCALL_CONFIG_FILE = File(Resources.getResource("xcall_config.toml").path)

        private val OUTPUT_FOLDER = File(Resources.getResource("code-generation").path + "/AcceleoGeneratorTest/GeneratedCode")

        fun printCode(file: File) {
            file.readLines().forEach { line -> println(line) }
        }
    }

    override fun beforeTest(description: Description) {
        wireMockServer.start()
    }

    override fun afterTest(description: Description, result: TestResult) {
        wireMockServer.stop()
    }
}
