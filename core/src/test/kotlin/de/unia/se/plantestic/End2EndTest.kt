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

val wireMockServer = WireMockServer(8080)

class End2EndTest : StringSpec({

    "End2End test works for the minimal hello".config(enabled = true) {
        runTransformationPipeline(MINIMAL_EXAMPLE_INPUT_PATH)
    }

    "End2End test produces valid Java code for the minimal hello".config(enabled = false) {
        runTransformationPipeline(MINIMAL_EXAMPLE_INPUT_PATH)

        // Now compile the resulting code
        Reflect.compile("com.plantestic.test.Test", File("$OUTPUT_PATH/testScenario.java").readText())
            .create(MINIMAL_EXAMPLE_CONFIG_PATH)
    }

    "End2End test receives request on mock server for the minimal hello".config(enabled = false) {
        val body = """{ "httpResponseDatumXPath" : "value1", "httpResponseDatumXPath2" : "value2", "key" : "value" }"""
        wireMockServer.stubFor(WireMock.get(WireMock.urlEqualTo("/testReceiver/test/123?variableName=%24%7BvariableName%7D&variableName2=%24%7BvariableName2%7D")).willReturn(
                WireMock.aResponse().withStatus(404).withBody(body)))

        runTransformationPipeline(MINIMAL_EXAMPLE_INPUT_PATH)

        // Now compile the resulting code and execute it
        val compiledTest = Reflect.compile("com.plantestic.test.Test", File("$OUTPUT_PATH/scenario.java").readText())
            .create(MINIMAL_EXAMPLE_CONFIG_PATH)
        compiledTest.call("test")

        // Check if we received a correct request
        wireMockServer.allServeEvents.size shouldBe 1
        wireMockServer.allServeEvents[0].response.status shouldBe 404
        wireMockServer.allServeEvents.forEach { serveEvent -> println(serveEvent.request) }
    }

    "End2End test works for complex hello".config(enabled = true) {
        runTransformationPipeline(COMPLEX_HELLO_INPUT_PATH)
    }

    "End2End test produces valid Java code for complex hello".config(enabled = false) {
        runTransformationPipeline(COMPLEX_HELLO_INPUT_PATH)

        // Now compile the resulting code
        Reflect.compile("com.plantestic.test.Test", File("$OUTPUT_PATH/scenario.java").readText())
            .create(COMPLEX_HELLO_CONFIG_PATH)
    }

    "End2End test receives request on mock server for complex hello".config(enabled = false) {
        val body = """{
            |"itemA" : "value1",
            |"itemB" : "value2",
            |}""".trimMargin()

        wireMockServer.stubFor(
            WireMock
                .get(WireMock.urlPathMatching("/testReceiver/test/123"))
                .willReturn(WireMock.aResponse().withStatus(200).withBody(body)))

        runTransformationPipeline(COMPLEX_HELLO_INPUT_PATH)

        val generatedCodeText = File("$OUTPUT_PATH/scenario.java").readText()
        val compiledTestClass = Reflect.compile("com.plantestic.test.Test", generatedCodeText)
        val compiledTestClassObject = compiledTestClass.create(COMPLEX_HELLO_CONFIG_PATH)
        compiledTestClassObject.call("test")

        // Check if we received a correct request
        wireMockServer.allServeEvents.size shouldBe 1
        wireMockServer.allServeEvents[0].response.status shouldBe 200
        wireMockServer.allServeEvents.forEach { serveEvent -> println(serveEvent.request) }
    }

    "End2End works for the rerouting example".config(enabled = false) {
        runTransformationPipeline(REROUTE_INPUT_PATH)
    }

    "End2End test produces valid Java code for the rerouting example".config(enabled = false) {
        runTransformationPipeline(REROUTE_INPUT_PATH)

        // Now compile the resulting code
        Reflect.compile("com.plantestic.test.Test", File("$OUTPUT_PATH/scenario.java").readText())
            .create(REROUTE_CONFIG_PATH)
    }

    "End2End test receives request on mock server for the rerouting example".config(enabled = false) {
        wireMockServer.stubFor(get(urlEqualTo("/hello/123")).willReturn(aResponse().withBody("test")))

        runTransformationPipeline(REROUTE_INPUT_PATH)

        // Now compile the resulting code and execute it
        val compiledTest = Reflect.compile("com.plantestic.test.Test", File("$OUTPUT_PATH/scenario.java").readText())
            .create(REROUTE_CONFIG_PATH)
        compiledTest.call("test")

        // Check if we received a correct request
        wireMockServer.allServeEvents.size shouldBe 1
        wireMockServer.allServeEvents[0].response.status shouldBe 200
        wireMockServer.allServeEvents.forEach { serveEvent -> println(serveEvent.request) }
    }

    "End2End test works for the xcall example".config(enabled = false) {
        runTransformationPipeline(XCALL_INPUT_PATH)
    }

    "End2End test produces valid Java code for the xcall example".config(enabled = false) {
        runTransformationPipeline(XCALL_INPUT_PATH)

        // Now compile the resulting code
        Reflect.compile("com.plantestic.test.Test", File("$OUTPUT_PATH/scenario.java").readText())
            .create(XCALL_CONFIG_PATH)
    }

    "End2End test receives request on mock server for the xcall example".config(enabled = false) {
        wireMockServer.stubFor(get(urlEqualTo("/hello/123")).willReturn(aResponse().withBody("test")))

        runTransformationPipeline(XCALL_INPUT_PATH)

        // Now compile the resulting code and execute it
        val compiledTest = Reflect.compile("com.plantestic.test.Test", File("$OUTPUT_PATH/scenario.java").readText())
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

        private val OUTPUT_PATH = Resources.getResource("code-generation").path + "/generatedCode"
    }

    override fun beforeTest(description: Description) {
        wireMockServer.start()
    }

    override fun afterTest(description: Description, result: TestResult) {
        wireMockServer.stop()
    }
}
