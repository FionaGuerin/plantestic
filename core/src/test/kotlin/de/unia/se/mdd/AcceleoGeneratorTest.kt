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
import io.kotlintest.Description
import io.kotlintest.TestResult

class AcceleoGeneratorTest : StringSpec({
    "Transform a Rest Assured EObject input to Java Code for complex hello" {
        MetaModelSetup.doSetup()

        val pumlInputModelURI = URI.createFileURI(COMPLEX_HELLO_INPUT_PATH)
        val pumlInputModel = ResourceSetImpl().getResource(pumlInputModelURI, true).contents[0]
        val outputFolder = File(OUTPUT_PATH)

        AcceleoCodeGenerator.generateCode(pumlInputModel, outputFolder)
        outputFolder.listFiles().size.shouldBeGreaterThanOrEqual(1)

        printCode(outputFolder)
    }

    "Acceleo generation produces valid Java code for complex example" {
        MetaModelSetup.doSetup()

        val pumlInputModelURI = URI.createFileURI(COMPLEX_HELLO_INPUT_PATH)
        val pumlInputModel = ResourceSetImpl().getResource(pumlInputModelURI, true).contents[0]
        val outputFolder = File(OUTPUT_PATH)

        AcceleoCodeGenerator.generateCode(pumlInputModel, outputFolder)

        // Now compile the resulting code
        Reflect.compile("com.mdd.test.Test", File("$OUTPUT_PATH/scenario.java").readText()).create(COMPLEX_HELLO_CONFIG_PATH)
    }

    "Acceleo generation test receives request on mock server for the complex example" {
        val body = """{
            |"itemA" : "value1",
            |"itemB" : "value2",
            |}""".trimMargin()

        wireMockServer.stubFor(
            WireMock
                .get(WireMock.urlPathMatching("/testReceiver/test/123"))
                .willReturn(WireMock.aResponse().withStatus(200).withBody(body)))

        MetaModelSetup.doSetup()

        val pumlInputModelURI = URI.createFileURI(COMPLEX_HELLO_INPUT_PATH)
        val pumlInputModel = ResourceSetImpl().getResource(pumlInputModelURI, true).contents[0]
        val outputFolder = File(OUTPUT_PATH)

        AcceleoCodeGenerator.generateCode(pumlInputModel, outputFolder)

        // Now compile the resulting code and execute it
        val compiledTest = Reflect.compile(
            "com.mdd.test.Test",
            File("$OUTPUT_PATH/scenario.java")
            .readText()
        ).create(COMPLEX_HELLO_CONFIG_PATH)
        compiledTest.call("test")

        // Check if we received a correct request
        wireMockServer.allServeEvents.size shouldBe 1
        wireMockServer.allServeEvents[0].response.status shouldBe 200
        wireMockServer.allServeEvents.forEach { serveEvent -> println(serveEvent.request) }
    }

    "Transform a Rest Assured EObject input to Java Code for rerouting".config(enabled = false) {
        MetaModelSetup.doSetup()

        val pumlInputModelURI = URI.createFileURI(REROUTING_INPUT_PATH)
        val pumlInputModel = ResourceSetImpl().getResource(pumlInputModelURI, true).contents[0]
        val outputFolder = File(OUTPUT_PATH)

        AcceleoCodeGenerator.generateCode(pumlInputModel, outputFolder)
        outputFolder.listFiles().size.shouldBeGreaterThanOrEqual(1)

        printCode(outputFolder)
    }

    "Acceleo generation produces valid Java code for rerouting".config(enabled = false) {
        MetaModelSetup.doSetup()

        val pumlInputModelURI = URI.createFileURI(REROUTING_INPUT_PATH)
        val pumlInputModel = ResourceSetImpl().getResource(pumlInputModelURI, true).contents[0]
        val outputFolder = File(OUTPUT_PATH)

        AcceleoCodeGenerator.generateCode(pumlInputModel, outputFolder)

        // Now compile the resulting code
        Reflect.compile("com.mdd.test.Test", File("$OUTPUT_PATH/scenario.java").readText()).create(REROUTING_CONFIG_PATH)
    }

    "Acceleo generation test receives request on mock server for rerouting".config(enabled = false) {
        wireMockServer.stubFor(WireMock.get(WireMock.urlEqualTo("/hello/123")).willReturn(WireMock.aResponse().withBody("test")))

        MetaModelSetup.doSetup()

        val pumlInputModelURI = URI.createFileURI(REROUTING_INPUT_PATH)
        val pumlInputModel = ResourceSetImpl().getResource(pumlInputModelURI, true).contents[0]
        val outputFolder = File(OUTPUT_PATH)

        AcceleoCodeGenerator.generateCode(pumlInputModel, outputFolder)

        // Now compile the resulting code and execute it
        val compiledTest = Reflect.compile("com.mdd.test.Test", File("$OUTPUT_PATH/scenario.java").readText()).create(REROUTING_CONFIG_PATH)
        compiledTest.call("test")

        // Check if we received a correct request
        wireMockServer.allServeEvents.size shouldBe 1
        wireMockServer.allServeEvents[0].response.status shouldBe 200
        wireMockServer.allServeEvents.forEach { serveEvent -> println(serveEvent.request) }
    }

    "Transform a Rest Assured EObject input to Java Code for xcall".config(enabled = false) {
        MetaModelSetup.doSetup()

        val pumlInputModelURI = URI.createFileURI(XCALL_INPUT_PATH)
        val pumlInputModel = ResourceSetImpl().getResource(pumlInputModelURI, true).contents[0]
        val outputFolder = File(OUTPUT_PATH)

        AcceleoCodeGenerator.generateCode(pumlInputModel, outputFolder)
        outputFolder.listFiles().size.shouldBeGreaterThanOrEqual(1)

        printCode(outputFolder)
    }

    "Acceleo generation produces valid Java code for xcall".config(enabled = false) {
        MetaModelSetup.doSetup()

        val pumlInputModelURI = URI.createFileURI(XCALL_INPUT_PATH)
        val pumlInputModel = ResourceSetImpl().getResource(pumlInputModelURI, true).contents[0]
        val outputFolder = File(OUTPUT_PATH)

        AcceleoCodeGenerator.generateCode(pumlInputModel, outputFolder)

        // Now compile the resulting code
        Reflect.compile("com.mdd.test.Test", File("$OUTPUT_PATH/scenario.java").readText()).create(XCALL_CONFIG_PATH)
    }

    "Acceleo generation test receives request on mock server for the xcall".config(enabled = false) {
        wireMockServer.stubFor(WireMock.get(WireMock.urlEqualTo("/hello/123")).willReturn(WireMock.aResponse().withBody("test")))

        MetaModelSetup.doSetup()

        val pumlInputModelURI = URI.createFileURI(XCALL_INPUT_PATH)
        val pumlInputModel = ResourceSetImpl().getResource(pumlInputModelURI, true).contents[0]
        val outputFolder = File(OUTPUT_PATH)

        AcceleoCodeGenerator.generateCode(pumlInputModel, outputFolder)

        // Now compile the resulting code and execute it
        val compiledTest = Reflect.compile("com.mdd.test.Test", File("$OUTPUT_PATH/scenario.java").readText()).create(XCALL_CONFIG_PATH)
        compiledTest.call("test")

        // Check if we received a correct request
        wireMockServer.allServeEvents.size shouldBe 1
        wireMockServer.allServeEvents[0].response.status shouldBe 200
        wireMockServer.allServeEvents.forEach { serveEvent -> println(serveEvent.request) }
    }
}) {
    companion object {
        private val COMPLEX_HELLO_INPUT_PATH = Resources.getResource("complex_hello_restassured.xmi").path
        private val COMPLEX_HELLO_CONFIG_PATH = Resources.getResource("end2end_test_config_minimal_hello.toml").path

        // TODO: set correct path to xmi file
        private val REROUTING_INPUT_PATH = Resources.getResource("complex_hello_restassured.xmi").path
        private val REROUTING_CONFIG_PATH = Resources.getResource("end2end_test_config_rerouting.toml").path

        // TODO: set correct path to xmi-file
        private val XCALL_INPUT_PATH = Resources.getResource("complex_hello_restassured.xmi").path
        private val XCALL_CONFIG_PATH = Resources.getResource("end2end_test_config_xcall.toml").path

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
