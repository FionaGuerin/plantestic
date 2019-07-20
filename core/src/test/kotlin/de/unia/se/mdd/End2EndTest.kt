package de.unia.se.mdd

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo
import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.google.common.io.Resources
import de.unia.se.mdd.Main.runTransformationPipeline
import io.kotlintest.Description
import io.kotlintest.TestResult
import io.kotlintest.shouldNotBe
import io.kotlintest.specs.StringSpec
import org.joor.Reflect
import java.io.File

val wireMockServer = WireMockServer(8080)

class End2EndTest : StringSpec({

    "End2End test produces valid Java code" {
        runTransformationPipeline(INPUT_PATH, OUTPUT_PATH)

        // Now compile the resulting code
        Reflect.compile("com.mdd.test.Test", File(OUTPUT_PATH + "/TestName.java").readText())
            .create(CONFIG_PATH)
    }

    "End2End test receives request on mock server" {
        wireMockServer.stubFor(get(urlEqualTo("/hello/123")).willReturn(aResponse().withBody("test")))

        runTransformationPipeline(INPUT_PATH, OUTPUT_PATH)

        // Now compile the resulting code and execute it
        val compiledTest = Reflect.compile("com.mdd.test.Test", File(OUTPUT_PATH + "/TestName.java").readText())
            .create(CONFIG_PATH)
        compiledTest.call("test")

        // Check if we received at least one request
        wireMockServer.allServeEvents.size shouldNotBe 0
    }
}) {
    companion object {
        private val INPUT_PATH = Resources.getResource("minimal_hello.puml").path
        private val OUTPUT_PATH = Resources.getResource("code-generation").path + "/generatedCode"
        private val CONFIG_PATH = Resources.getResource("test_config.toml").path
    }

    override fun beforeTest(description: Description) {
        wireMockServer.start()
    }

    override fun afterTest(description: Description, result: TestResult) {
        wireMockServer.stop()
    }
}
