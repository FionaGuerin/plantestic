package de.unia.se.plantestic

import com.google.common.io.Resources
import io.kotlintest.shouldBe
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

        val pumlInputModelURI = URI.createFileURI(MINIMAL_HELLO_INPUT_PATH)
        val pumlInputModel = ResourceSetImpl().getResource(pumlInputModelURI, true).contents[0]
        val outputFolder = File(OUTPUT_PATH + "/minimal_hello")

        AcceleoCodeGenerator.generateCode(pumlInputModel, outputFolder)
        outputFolder.listFiles().size shouldBe 1

        printCode(outputFolder)

        // Now compile the resulting code to check for syntax errors
        val generatedSourceFile = outputFolder.listFiles().first()
        Reflect.compile("com.plantestic.test.${generatedSourceFile.nameWithoutExtension}", generatedSourceFile.readText()).create(MINIMAL_HELLO_CONFIG_PATH)
    }

    "Transform a Rest Assured EObject input to Java Code for complex hello" {
        MetaModelSetup.doSetup()

        val pumlInputModelURI = URI.createFileURI(COMPLEX_HELLO_INPUT_PATH)
        val pumlInputModel = ResourceSetImpl().getResource(pumlInputModelURI, true).contents[0]
        val outputFolder = File(OUTPUT_PATH + "/complex_hello")

        AcceleoCodeGenerator.generateCode(pumlInputModel, outputFolder)
        outputFolder.listFiles().size shouldBe 1

        printCode(outputFolder)

        // Now compile the resulting code to check for syntax errors
        val generatedSourceFile = outputFolder.listFiles().first()
        Reflect.compile("com.plantestic.test.${generatedSourceFile.nameWithoutExtension}", generatedSourceFile.readText()).create(COMPLEX_HELLO_CONFIG_PATH)
    }

    "Transform a Rest Assured EObject input to Java Code for rerouting" {
        MetaModelSetup.doSetup()

        val pumlInputModelURI = URI.createFileURI(REROUTING_INPUT_PATH)
        val pumlInputModel = ResourceSetImpl().getResource(pumlInputModelURI, true).contents[0]
        val outputFolder = File(OUTPUT_PATH + "/rerouting")

        AcceleoCodeGenerator.generateCode(pumlInputModel, outputFolder)
        outputFolder.listFiles().size shouldBe 1

        printCode(outputFolder)

        // Now compile the resulting code to check for syntax errors
        val generatedSourceFile = outputFolder.listFiles().first()
        Reflect.compile("com.plantestic.test.${generatedSourceFile.nameWithoutExtension}", generatedSourceFile.readText()).create(REROUTING_CONFIG_PATH)
    }

    "Transform a Rest Assured EObject input to Java Code for xcall" {
        MetaModelSetup.doSetup()

        val pumlInputModelURI = URI.createFileURI(XCALL_INPUT_PATH)
        val pumlInputModel = ResourceSetImpl().getResource(pumlInputModelURI, true).contents[0]
        val outputFolder = File(OUTPUT_PATH + "/xcall")

        AcceleoCodeGenerator.generateCode(pumlInputModel, outputFolder)
        outputFolder.listFiles().size shouldBe 1

        printCode(outputFolder)

        // Now compile the resulting code to check for syntax errors
        val generatedSourceFile = outputFolder.listFiles().first()
        Reflect.compile("com.plantestic.test.${generatedSourceFile.nameWithoutExtension}", generatedSourceFile.readText()).create(XCALL_CONFIG_PATH)
    }
}) {
    companion object {
        private val MINIMAL_HELLO_INPUT_PATH = Resources.getResource("minimal_hello_restassured.xmi").path
        private val MINIMAL_HELLO_CONFIG_PATH = Resources.getResource("minimal_hello_config.toml").path

        private val COMPLEX_HELLO_INPUT_PATH = Resources.getResource("complex_hello_restassured.xmi").path
        private val COMPLEX_HELLO_CONFIG_PATH = Resources.getResource("complex_hello_config.toml").path

        private val REROUTING_INPUT_PATH = Resources.getResource("rerouting_restassured.xmi").path
        private val REROUTING_CONFIG_PATH = Resources.getResource("rerouting_config.toml").path

        private val XCALL_INPUT_PATH = Resources.getResource("xcall_restassured.xmi").path
        private val XCALL_CONFIG_PATH = Resources.getResource("xcall_config.toml").path

        private val OUTPUT_PATH = Resources.getResource("code-generation").path + "/generatedCode"

        fun printCode(folder: File) {
            folder.listFiles().forEach { file ->
                val lines = file.readLines()
                lines.forEach { line -> println(line) }
            }
        }
    }

    override fun beforeTest(description: Description) {
        wireMockServer.start()
    }

    override fun afterTest(description: Description, result: TestResult) {
        wireMockServer.stop()
    }
}
