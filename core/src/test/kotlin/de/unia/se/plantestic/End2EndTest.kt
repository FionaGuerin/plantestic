package de.unia.se.plantestic

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo
import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.google.common.io.Resources
import de.unia.se.plantestic.Main.runTransformationPipeline
import io.kotlintest.Description
import io.kotlintest.TestResult
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import org.joor.Reflect
import java.io.File
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl
import java.nio.file.Paths

val wireMockServer = WireMockServer(8080)

class End2EndTest : StringSpec({

    "Pipeline produces valid Java code for minimal hello" {
        val outputFolder = File("$OUTPUT_PATH/minimal_hello")
        runTransformationPipeline(MINIMAL_EXAMPLE_INPUT_PATH, outputFolder)
        printCode(outputFolder)

        // Now compile the resulting code to check for syntax errors
        val generatedSourceFile = outputFolder.listFiles().first()
        Reflect.compile("com.plantestic.test.${generatedSourceFile.nameWithoutExtension}", generatedSourceFile.readText())
            .create(MINIMAL_EXAMPLE_CONFIG_PATH)
    }

    "End2End test receives request on mock server for the minimal hello" {
        wireMockServer.stubFor(
            get(urlEqualTo("/testB/hello"))
                .willReturn(WireMock.aResponse().withStatus(200)))

        val outputFolder = File("$OUTPUT_PATH/minimal_hello")
        runTransformationPipeline(MINIMAL_EXAMPLE_INPUT_PATH, outputFolder)

        // Now compile the resulting code and execute it
        val generatedSourceFile = outputFolder.listFiles().first()
        val compiledTest = Reflect.compile("com.plantestic.test.${generatedSourceFile.nameWithoutExtension}", generatedSourceFile.readText())
            .create(MINIMAL_EXAMPLE_CONFIG_PATH)
        compiledTest.call("test")

        // Check if we received a correct request
        wireMockServer.allServeEvents.size shouldBe 1
        wireMockServer.allServeEvents[0].response.status shouldBe 200
        wireMockServer.allServeEvents.forEach { serveEvent -> println(serveEvent.request) }
    }

    "Pipeline produces valid Java code for complex hello" {
        val outputFolder = File("$OUTPUT_PATH/complex_hello")
        runTransformationPipeline(COMPLEX_HELLO_INPUT_PATH, outputFolder)
        printCode(outputFolder)

        // Now compile the resulting code to check for syntax errors
        val generatedSourceFile = outputFolder.listFiles().first()
        Reflect.compile("com.plantestic.test.${generatedSourceFile.nameWithoutExtension}", generatedSourceFile.readText())
            .create(COMPLEX_HELLO_CONFIG_PATH)
    }

    "End2End test receives request on mock server for complex hello" {
        val body = """{
            |"itemA" : "value1",
            |"itemB" : "value2",
            |}""".trimMargin()

        wireMockServer.stubFor(
            WireMock
                .get(WireMock.urlPathMatching("/testB/test/123"))
                .willReturn(WireMock.aResponse().withStatus(200).withBody(body)))

        val outputFolder = File("$OUTPUT_PATH/complex_hello")
        runTransformationPipeline(COMPLEX_HELLO_INPUT_PATH, outputFolder)

        val generatedCodeText = File("$OUTPUT_PATH/Testcomplex_hello_puml.java").readText()
        val compiledTestClass = Reflect.compile("com.plantestic.test.Testcomplex_hello_puml", generatedCodeText)
        val compiledTestClassObject = compiledTestClass.create(COMPLEX_HELLO_CONFIG_PATH)
        compiledTestClassObject.call("test")

        // Check if we received a correct request
        wireMockServer.allServeEvents.size shouldBe 2
        wireMockServer.allServeEvents[0].response.status shouldBe 200
        wireMockServer.allServeEvents.forEach { serveEvent -> println(serveEvent.request) }
    }

    "Pipeline produces valid Java code for the rerouting example" {
        val outputFolder = File("$OUTPUT_PATH/rerouting")
        runTransformationPipeline(REROUTE_INPUT_PATH, outputFolder)
        printCode(outputFolder)

        // Now compile the resulting code to check for syntax errors
        val generatedSourceFile = outputFolder.listFiles().first()
        Reflect.compile("com.plantestic.test.${generatedSourceFile.nameWithoutExtension}", generatedSourceFile.readText())
            .create(REROUTE_CONFIG_PATH)
    }

    "End2End test receives request on mock server for rerouting - voiceEstablished == true".config(enabled = false) {
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

        val outputFolder = File("$OUTPUT_PATH/rerouting")
        runTransformationPipeline(REROUTE_INPUT_PATH, outputFolder)

        // Now compile the resulting code and execute it
        val compiledTest = Reflect.compile("com.plantestic.test.Testrerouting_puml", File("$OUTPUT_PATH/Testrerouting_puml.java").readText())
            .create(REROUTE_CONFIG_PATH)
        compiledTest.call("test")

        // Check if we received a correct request
        wireMockServer.allServeEvents.size shouldBe 1
        wireMockServer.allServeEvents[0].response.status shouldBe 200
        wireMockServer.allServeEvents.forEach { serveEvent -> println(serveEvent.request) }
    }

    "End2End test receives request on mock server for rerouting - voiceEstablished == false, return 400".config(enabled = false) {
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
        wireMockServer.stubFor(
            WireMock
                .get(WireMock.anyUrl())
                .willReturn(WireMock.aResponse()
                    .withStatus(400))
        )

        val outputFolder = File("$OUTPUT_PATH/rerouting")
        runTransformationPipeline(REROUTE_INPUT_PATH, outputFolder)

        // Now compile the resulting code and execute it
        val compiledTest = Reflect.compile("com.mdd.test.Testrerouting_puml", File("$OUTPUT_PATH/Testrerouting_puml.java").readText()).create(REROUTE_CONFIG_PATH)
        compiledTest.call("test")

        // Check if we received a correct request
        wireMockServer.allServeEvents.size shouldBe 1
        wireMockServer.allServeEvents[0].response.status shouldBe 200
        wireMockServer.allServeEvents.forEach { serveEvent -> println(serveEvent.request) }
    }

    "End2End test receives request on mock server for rerouting - voiceEstablished == false, return 404".config(enabled = false) {
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

        val outputFolder = File("$OUTPUT_PATH/rerouting")
        runTransformationPipeline(REROUTE_INPUT_PATH, outputFolder)

        // Now compile the resulting code and execute it
        val compiledTest = Reflect.compile("com.mdd.test.Testrerouting_puml", File("$OUTPUT_PATH/Testrerouting_puml.java").readText()).create(REROUTE_CONFIG_PATH)
        compiledTest.call("test")

        // Check if we received a correct request
        wireMockServer.allServeEvents.size shouldBe 1
        wireMockServer.allServeEvents[0].response.status shouldBe 200
        wireMockServer.allServeEvents.forEach { serveEvent -> println(serveEvent.request) }
    }

    "End2End test receives request on mock server for rerouting - voiceEstablished == false, return 500".config(enabled = false) {
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
        wireMockServer.stubFor(
            WireMock
                .get(WireMock.anyUrl())
                .willReturn(WireMock.aResponse()
                    .withStatus(500)))

        val outputFolder = File("$OUTPUT_PATH/rerouting")
        runTransformationPipeline(REROUTE_INPUT_PATH, outputFolder)

        val pumlInputModelURI = URI.createFileURI(REROUTE_INPUT_PATH)
        val pumlInputModel = ResourceSetImpl().getResource(pumlInputModelURI, true).contents[0]

        AcceleoCodeGenerator.generateCode(pumlInputModel, outputFolder)

        // Now compile the resulting code and execute it
        val compiledTest = Reflect.compile("com.mdd.test.Testrerouting_puml", File("$OUTPUT_PATH/Testrerouting_puml.java").readText()).create(REROUTE_CONFIG_PATH)
        compiledTest.call("test")

        // Check if we received a correct request
        wireMockServer.allServeEvents.size shouldBe 1
        wireMockServer.allServeEvents[0].response.status shouldBe 200
        wireMockServer.allServeEvents.forEach { serveEvent -> println(serveEvent.request) }
    }

    "Pipeline produces valid Java code for the xcall example" {
        val outputFolder = File("$OUTPUT_PATH/xcall")
        runTransformationPipeline(XCALL_INPUT_PATH, outputFolder)
        printCode(outputFolder)

        // Now compile the resulting code to check for syntax errors
        val generatedSourceFile = outputFolder.listFiles().first()
        Reflect.compile("com.plantestic.test.${generatedSourceFile.nameWithoutExtension}", generatedSourceFile.readText())
            .create(XCALL_CONFIG_PATH)
    }

    "End2End test receives request on mock server for the xcall example".config(enabled = false) {
        wireMockServer.stubFor(get(urlEqualTo("/hello/123")).willReturn(aResponse().withBody("test")))

        val outputFolder = File("$OUTPUT_PATH/xcall")
        runTransformationPipeline(XCALL_INPUT_PATH, outputFolder)

        // Now compile the resulting code and execute it
        val compiledTest = Reflect.compile("com.plantestic.test.Testxcall_puml", File("$OUTPUT_PATH/Testxcall_puml.java").readText())
            .create(XCALL_CONFIG_PATH)
        compiledTest.call("test")

        // Check if we received a correct request
        wireMockServer.allServeEvents.size shouldBe 1
        wireMockServer.allServeEvents[0].response.status shouldBe 200
        wireMockServer.allServeEvents.forEach { serveEvent -> println(serveEvent.request) }
    }
}) {
    companion object {
        private val MINIMAL_EXAMPLE_INPUT_PATH = Resources.getResource("minimal_hello.puml").path
        private val MINIMAL_EXAMPLE_CONFIG_PATH = Resources.getResource("minimal_hello_config.toml").path

        private val COMPLEX_HELLO_INPUT_PATH = Resources.getResource("complex_hello.puml").path
        private val COMPLEX_HELLO_CONFIG_PATH = Resources.getResource("complex_hello_config.toml").path

        private val REROUTE_INPUT_PATH = Resources.getResource("rerouting.puml").path
        private val REROUTE_CONFIG_PATH = Resources.getResource("rerouting_config.toml").path

        private val XCALL_INPUT_PATH = Resources.getResource("xcall.puml").path
        private val XCALL_CONFIG_PATH = Resources.getResource("xcall_config.toml").path

        // FIXME: no IS_WINDOWS please
        private val IS_WINDOWS = System.getProperty("os.name").contains("indow")
        private val osAppropriatePath = if (IS_WINDOWS) MINIMAL_EXAMPLE_INPUT_PATH.substring(1) else MINIMAL_EXAMPLE_INPUT_PATH
        private val OUTPUT_PATH = Paths.get(osAppropriatePath).toAbsolutePath().parent.toString() + "/generatedCode"
//        private val OUTPUT_PATH = Resources.getResource("code-generation").path + "/generatedCode"

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
