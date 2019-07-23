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
    "Transform a Rest Assured EObject input to Java Code for minimal hello" {
        MetaModelSetup.doSetup()

        val pumlInputModelURI = URI.createFileURI(MINIMAL_HELLO_INPUT_PATH)
        val pumlInputModel = ResourceSetImpl().getResource(pumlInputModelURI, true).contents[0]
        val outputFolder = File(OUTPUT_PATH)

        AcceleoCodeGenerator.generateCode(pumlInputModel, outputFolder)
        outputFolder.listFiles().size.shouldBeGreaterThanOrEqual(1)

        printCode(outputFolder)
    }

    "Acceleo generation produces valid Java code for minimal hello".config(enabled = false) {
        MetaModelSetup.doSetup()

        val pumlInputModelURI = URI.createFileURI(MINIMAL_HELLO_INPUT_PATH)
        val pumlInputModel = ResourceSetImpl().getResource(pumlInputModelURI, true).contents[0]
        val outputFolder = File(OUTPUT_PATH)

        AcceleoCodeGenerator.generateCode(pumlInputModel, outputFolder)

        // Now compile the resulting code
        Reflect.compile("com.mdd.test.Test", File("$OUTPUT_PATH/scenario.java").readText()).create(MINIMAL_HELLO_CONFIG_PATH)
    }

    "Acceleo generation test receives request on mock server for minimal hello".config(enabled = false) {
        wireMockServer.stubFor(WireMock.get(WireMock.anyUrl()).willReturn(WireMock.aResponse().withStatus(200)))

        MetaModelSetup.doSetup()

        val pumlInputModelURI = URI.createFileURI(MINIMAL_HELLO_INPUT_PATH)
        val pumlInputModel = ResourceSetImpl().getResource(pumlInputModelURI, true).contents[0]
        val outputFolder = File(OUTPUT_PATH)

        AcceleoCodeGenerator.generateCode(pumlInputModel, outputFolder)

        // Now compile the resulting code and execute it
        val generatedCodeText = File("$OUTPUT_PATH/testScenario.java").readText()
        val compiledTestClass = Reflect.compile("com.mdd.test.Test", generatedCodeText)
        val compiledTestClassObject = compiledTestClass.create(MINIMAL_HELLO_CONFIG_PATH)
        compiledTestClassObject.call("test")

        // Check if we received a correct request
        wireMockServer.allServeEvents.size shouldBe 1
        wireMockServer.allServeEvents[0].response.status shouldBe 200
        wireMockServer.allServeEvents.forEach { serveEvent -> println(serveEvent.request) }
    }

    "Transform a Rest Assured EObject input to Java Code for complex hello" {
        MetaModelSetup.doSetup()

        val pumlInputModelURI = URI.createFileURI(COMPLEX_HELLO_INPUT_PATH)
        val pumlInputModel = ResourceSetImpl().getResource(pumlInputModelURI, true).contents[0]
        val outputFolder = File(OUTPUT_PATH)

        AcceleoCodeGenerator.generateCode(pumlInputModel, outputFolder)
        outputFolder.listFiles().size.shouldBeGreaterThanOrEqual(1)

        printCode(outputFolder)
    }

    "Acceleo generation produces valid Java code for complex hello" {
        MetaModelSetup.doSetup()

        val pumlInputModelURI = URI.createFileURI(COMPLEX_HELLO_INPUT_PATH)
        val pumlInputModel = ResourceSetImpl().getResource(pumlInputModelURI, true).contents[0]
        val outputFolder = File(OUTPUT_PATH)

        AcceleoCodeGenerator.generateCode(pumlInputModel, outputFolder)

        // Now compile the resulting code
        Reflect.compile("com.mdd.test.Test", File("$OUTPUT_PATH/scenario.java").readText()).create(COMPLEX_HELLO_CONFIG_PATH)
    }

    "Acceleo generation test receives request on mock server for the complex hello" {
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
        val generatedCodeText = File("$OUTPUT_PATH/scenario.java").readText()
        val compiledTestClass = Reflect.compile("com.mdd.test.Test", generatedCodeText)
        val compiledTestClassObject = compiledTestClass.create(COMPLEX_HELLO_CONFIG_PATH)
        compiledTestClassObject.call("test")

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

    "Acceleo generation test receives request on mock server for rerouting - voiceEstablished == true".config(enabled = false) {
        val body_CCC_CRS = """{
            |"uiswitch" : "UISWITCH",
            |"reroute" : "REROUTE",
            |"warmhandover" : "WARMHANDOVER",
            |}""".trimMargin()
        val body_CCC_Voicemanager_voiceenabled = """{
            |"eventid1" : "/VoiceStatus/eventId1",
            |"agent1" : "/VoiceStatus/agent1/connectionstatus",
            |"agent2" : "/VoiceStatus/agent2/connectionstatus",
            |}""".trimMargin()

        wireMockServer.stubFor(
            WireMock
                .get(WireMock.urlPathMatching("/CRS/ccc/rerouteOptions"))
                .willReturn(WireMock.aResponse()
                    .withStatus(200)
                    .withBody(body_CCC_CRS)))
        wireMockServer.stubFor(
            WireMock
                .get(WireMock.urlPathMatching("/Voicemanager/ccc/events/123/isconnected"))
                .willReturn(WireMock.aResponse()
                    .withStatus(200)
                    .withBody(body_CCC_Voicemanager_voiceenabled)))

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

    "Acceleo generation test receives request on mock server for rerouting - voiceEstablished == false, return 400".config(enabled = false) {
        val body_CCC_CRS = """{
            |"uiswitch" : "UISWITCH",
            |"reroute" : "REROUTE",
            |"warmhandover" : "WARMHANDOVER",
            |}""".trimMargin()

        wireMockServer.stubFor(
            WireMock
                .get(WireMock.urlPathMatching("/CRS/ccc/rerouteOptions"))
                .willReturn(WireMock.aResponse()
                    .withStatus(200)
                    .withBody(body_CCC_CRS)))
        wireMockServer.stubFor(
            WireMock
                .get(WireMock.urlPathMatching("/Voicemanager/ccc/events/123/isconnected"))
                .willReturn(WireMock.aResponse()
                    .withStatus(400)))

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

    "Acceleo generation test receives request on mock server for rerouting - voiceEstablished == false, return 404".config(enabled = false) {
        val body_CCC_CRS = """{
            |"uiswitch" : "UISWITCH",
            |"reroute" : "REROUTE",
            |"warmhandover" : "WARMHANDOVER",
            |}""".trimMargin()

        wireMockServer.stubFor(
            WireMock
                .get(WireMock.urlPathMatching("/CRS/ccc/rerouteOptions"))
                .willReturn(WireMock.aResponse()
                    .withStatus(200)
                    .withBody(body_CCC_CRS)))
        wireMockServer.stubFor(
            WireMock
                .get(WireMock.urlPathMatching("/Voicemanager/ccc/events/123/isconnected"))
                .willReturn(WireMock.aResponse()
                    .withStatus(404)))

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

    "Acceleo generation test receives request on mock server for rerouting - voiceEstablished == false, return 500".config(enabled = false) {
        val body_CCC_CRS = """{
            |"uiswitch" : "UISWITCH",
            |"reroute" : "REROUTE",
            |"warmhandover" : "WARMHANDOVER",
            |}""".trimMargin()

        wireMockServer.stubFor(
            WireMock
                .get(WireMock.urlPathMatching("/CRS/ccc/rerouteOptions"))
                .willReturn(WireMock.aResponse()
                    .withStatus(200)
                    .withBody(body_CCC_CRS)))
        wireMockServer.stubFor(
            WireMock
                .get(WireMock.urlPathMatching("/Voicemanager/ccc/events/123/isconnected"))
                .willReturn(WireMock.aResponse()
                    .withStatus(500)))

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
        val body = """{
            |"itemA" : "value1",
            |"itemB" : "value2",
            |}""".trimMargin()

        wireMockServer.stubFor(
            WireMock
                .get(WireMock.urlPathMatching("/testReceiver/test/123"))
                .willReturn(WireMock.aResponse().withStatus(200).withBody(body)))

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
