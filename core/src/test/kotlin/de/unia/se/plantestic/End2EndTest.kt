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

    "End2End test receives request on mock server for the minimal hello" {
        wireMockServer.stubFor(
            get(urlEqualTo("/testB/hello"))
                .willReturn(WireMock.aResponse().withStatus(200)))

        runTransformationPipeline(MINIMAL_EXAMPLE_INPUT_FILE, OUTPUT_FOLDER)

        // Now compile the resulting code to check for syntax errors
        val generatedSourceFile = OUTPUT_FOLDER.listFiles().filter { f -> f.name == "Testminimal_hello_puml.java" }.first()
        val compiledTest = Reflect.compile(
            "com.plantestic.test.${generatedSourceFile.nameWithoutExtension}",
            generatedSourceFile.readText()
        ).create(MINIMAL_EXAMPLE_CONFIG_FILE.path)
        compiledTest.call("test")

        // Check if we received a correct request
        wireMockServer.allServeEvents.size shouldBe 1
        wireMockServer.allServeEvents[0].response.status shouldBe 200
        wireMockServer.allServeEvents.forEach { serveEvent -> println(serveEvent.request) }
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

        runTransformationPipeline(COMPLEX_HELLO_INPUT_FILE, OUTPUT_FOLDER)

        // Now compile the resulting code to check for syntax errors
        val generatedSourceFile = OUTPUT_FOLDER.listFiles().filter { f -> f.name == "Testcomplex_hello_puml.java" }.first()
        val compiledTest = Reflect.compile(
            "com.plantestic.test.${generatedSourceFile.nameWithoutExtension}",
            generatedSourceFile.readText()
        ).create(COMPLEX_HELLO_CONFIG_FILE.path)
        compiledTest.call("test")

        // Check if we received a correct request
        wireMockServer.allServeEvents.size shouldBe 2
        wireMockServer.allServeEvents[0].response.status shouldBe 200
        wireMockServer.allServeEvents.forEach { serveEvent -> println(serveEvent.request) }
    }

    "End2End test receives request on mock server for rerouting - voiceEstablished == true" {
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

        runTransformationPipeline(REROUTE_INPUT_FILE, OUTPUT_FOLDER)

        // Now compile the resulting code to check for syntax errors
        val generatedSourceFile = OUTPUT_FOLDER.listFiles().filter { f -> f.name == "Testrerouting_puml.java" }.first()
        val compiledTest = Reflect.compile(
            "com.plantestic.test.${generatedSourceFile.nameWithoutExtension}",
            generatedSourceFile.readText()
        ).create(REROUTE_CONFIG_FILE.path)
        compiledTest.call("test")

        // Check if we received a correct request
        wireMockServer.allServeEvents.size shouldBe 1
        wireMockServer.allServeEvents[0].response.status shouldBe 200
        wireMockServer.allServeEvents.forEach { serveEvent -> println(serveEvent.request) }
    }

    "End2End test receives request on mock server for rerouting - voiceEstablished == false, return 400" {
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

        runTransformationPipeline(REROUTE_INPUT_FILE, OUTPUT_FOLDER)

        // Now compile the resulting code to check for syntax errors
        val generatedSourceFile = OUTPUT_FOLDER.listFiles().filter { f -> f.name == "Testrerouting_puml.java" }.first()
        val compiledTest = Reflect.compile(
            "com.plantestic.test.${generatedSourceFile.nameWithoutExtension}",
            generatedSourceFile.readText()
        ).create(REROUTE_CONFIG_FILE.path)
        compiledTest.call("test")

        // Check if we received a correct request
        wireMockServer.allServeEvents.size shouldBe 1
        wireMockServer.allServeEvents[0].response.status shouldBe 200
        wireMockServer.allServeEvents.forEach { serveEvent -> println(serveEvent.request) }
    }

    "End2End test receives request on mock server for rerouting - voiceEstablished == false, return 404" {
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

        runTransformationPipeline(REROUTE_INPUT_FILE, OUTPUT_FOLDER)

        // Now compile the resulting code to check for syntax errors
        val generatedSourceFile = OUTPUT_FOLDER.listFiles().filter { f -> f.name == "Testrerouting_puml.java" }.first()
        val compiledTest = Reflect.compile(
            "com.plantestic.test.${generatedSourceFile.nameWithoutExtension}",
            generatedSourceFile.readText()
        ).create(REROUTE_CONFIG_FILE.path)
        compiledTest.call("test")

        // Check if we received a correct request
        wireMockServer.allServeEvents.size shouldBe 1
        wireMockServer.allServeEvents[0].response.status shouldBe 200
        wireMockServer.allServeEvents.forEach { serveEvent -> println(serveEvent.request) }
    }

    "End2End test receives request on mock server for rerouting - voiceEstablished == false, return 500" {
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

        runTransformationPipeline(REROUTE_INPUT_FILE, OUTPUT_FOLDER)

        // Now compile the resulting code to check for syntax errors
        val generatedSourceFile = OUTPUT_FOLDER.listFiles().filter { f -> f.name == "Testrerouting_puml.java" }.first()
        val compiledTest = Reflect.compile(
            "com.plantestic.test.${generatedSourceFile.nameWithoutExtension}",
            generatedSourceFile.readText()
        ).create(REROUTE_CONFIG_FILE.path)
        compiledTest.call("test")

        // Check if we received a correct request
        wireMockServer.allServeEvents.size shouldBe 1
        wireMockServer.allServeEvents[0].response.status shouldBe 200
        wireMockServer.allServeEvents.forEach { serveEvent -> println(serveEvent.request) }
    }

    "End2End test receives request on mock server for the xcall example" {
        wireMockServer.stubFor(get(urlEqualTo("/hello/123")).willReturn(aResponse().withBody("test")))

        runTransformationPipeline(XCALL_INPUT_FILE, OUTPUT_FOLDER)

        // Now compile the resulting code to check for syntax errors
        val generatedSourceFile = OUTPUT_FOLDER.listFiles().filter { f -> f.name == "Testxcall_puml.java" }.first()
        val compiledTest = Reflect.compile(
            "com.plantestic.test.${generatedSourceFile.nameWithoutExtension}",
            generatedSourceFile.readText()
        ).create(XCALL_CONFIG_FILE.path)
        compiledTest.call("test")

        // Check if we received a correct request
        wireMockServer.allServeEvents.size shouldBe 1
        wireMockServer.allServeEvents[0].response.status shouldBe 200
        wireMockServer.allServeEvents.forEach { serveEvent -> println(serveEvent.request) }
    }
}) {
    companion object {
        private val MINIMAL_EXAMPLE_INPUT_FILE = File(Resources.getResource("minimal_hello.puml").path)
        private val MINIMAL_EXAMPLE_CONFIG_FILE = File(Resources.getResource("minimal_hello_config.toml").path)

        private val COMPLEX_HELLO_INPUT_FILE = File(Resources.getResource("complex_hello.puml").path)
        private val COMPLEX_HELLO_CONFIG_FILE = File(Resources.getResource("complex_hello_config.toml").path)

        private val REROUTE_INPUT_FILE = File(Resources.getResource("rerouting.puml").path)
        private val REROUTE_CONFIG_FILE = File(Resources.getResource("rerouting_config.toml").path)

        private val XCALL_INPUT_FILE = File(Resources.getResource("xcall.puml").path)
        private val XCALL_CONFIG_FILE = File(Resources.getResource("xcall_config.toml").path)

        private val OUTPUT_FOLDER = File(Resources.getResource("code-generation").path + "/End2EndTests/GeneratedCode")

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
