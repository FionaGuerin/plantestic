package de.unia.se.mdd

import com.github.tomakehurst.wiremock.client.WireMock
import com.google.common.io.Resources
import io.kotlintest.matchers.numerics.shouldBeGreaterThanOrEqual
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl
import org.joor.Reflect
import java.io.File

class AcceleoGeneratorTest : StringSpec({
    "Transform a Rest Assured EObject input to Java Code".config(enabled = true) {
        MetaModelSetup.doSetup()

        val pumlInputModelURI = URI.createFileURI(MINIMAL_EXAMPLE_INPUT_PATH)
        val pumlInputModel = ResourceSetImpl().getResource(pumlInputModelURI, true).contents[0]
        val outputFolder = File(OUTPUT_PATH)

        AcceleoCodeGenerator.generateCode(pumlInputModel, outputFolder)
        outputFolder.listFiles().size.shouldBeGreaterThanOrEqual(1)

        printCode(outputFolder)
    }

    "Acceleo generation produces valid Java code".config(enabled = true) {
        MetaModelSetup.doSetup()

        val pumlInputModelURI = URI.createFileURI(MINIMAL_EXAMPLE_INPUT_PATH)
        val pumlInputModel = ResourceSetImpl().getResource(pumlInputModelURI, true).contents[0]
        val outputFolder = File(OUTPUT_PATH)

        AcceleoCodeGenerator.generateCode(pumlInputModel, outputFolder)

        // Now compile the resulting code
        Reflect.compile("com.mdd.test.Test", File(OUTPUT_PATH + "/scenario.java").readText()).create(MINIMAL_EXAMPLE_CONFIG_PATH)
    }

    "Acceleo generation test receives request on mock server for the minimal example".config(enabled = true) {
        wireMockServer.stubFor(WireMock.get(WireMock.urlEqualTo("/hello/123")).willReturn(WireMock.aResponse().withBody("test")))

        MetaModelSetup.doSetup()

        val pumlInputModelURI = URI.createFileURI(MINIMAL_EXAMPLE_INPUT_PATH)
        val pumlInputModel = ResourceSetImpl().getResource(pumlInputModelURI, true).contents[0]
        val outputFolder = File(OUTPUT_PATH)

        AcceleoCodeGenerator.generateCode(pumlInputModel, outputFolder)

        // Now compile the resulting code and execute it
        val compiledTest = Reflect.compile("com.mdd.test.Test", File(OUTPUT_PATH + "/scenario.java").readText()).create(MINIMAL_EXAMPLE_CONFIG_PATH)
        compiledTest.call("test")

        // Check if we received a correct request
        wireMockServer.allServeEvents.size shouldBe 1
        wireMockServer.allServeEvents[0].response.status shouldBe 200
        wireMockServer.allServeEvents.forEach { serveEvent -> println(serveEvent.request) }
    }
}) {
    companion object {
        private val MINIMAL_EXAMPLE_INPUT_PATH = Resources.getResource("minimal_hello_restassured.xmi").path
        private val MINIMAL_EXAMPLE_CONFIG_PATH = Resources.getResource("end2end_test_config_minimal_hello.toml").path
        private val OUTPUT_PATH = Resources.getResource("code-generation").path + "/generatedCode"

        fun printCode(folder: File) {
            folder.listFiles().forEach { file ->
                val lines = file.readLines()
                lines.forEach { line -> println(line) }
            }
        }
    }
}
