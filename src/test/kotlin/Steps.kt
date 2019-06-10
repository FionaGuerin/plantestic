import cucumber.api.java8.En
import io.restassured.RestAssured.given
import io.restassured.response.Response
import io.restassured.specification.RequestSpecification

class Steps : En {

    private var request: RequestSpecification? = null
    private var response: Response? = null

    protected fun RequestSpecification.When(): RequestSpecification {
        return this.`when`()
    }

    init {
        Given("^we read an interaction diagram as XMI$") {
            request = given().basePath("/").baseUri("http://localhost").port(8888)
        }

        When("^an actor sends a request$") {
            response = request!!.When().get("/greeting")
        }

        Then("^another actor sends a response$") {
            response!!.then().statusCode(200)
        }
    }
}