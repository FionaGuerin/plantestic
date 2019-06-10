import io.restassured.RestAssured
import io.restassured.specification.RequestSpecification
import org.junit.BeforeClass

open class FunctionalTest {

    companion object {

        @BeforeClass @JvmStatic
        fun setUp() {
            startWireMock()
            setupRestAssuredDefaults()
        }

        private fun startWireMock() {
            Runtime.getRuntime().exec("java -jar wiremock-standalone-2.19.0.jar --port 8888 --root-dir src/test/resources")
        }

        private fun setupRestAssuredDefaults() {
            RestAssured.baseURI = "http://localhost"
            RestAssured.port = 8888
            RestAssured.basePath = "/"
        }
    }
}

fun RequestSpecification.When(): RequestSpecification {
    return this.`when`()
}