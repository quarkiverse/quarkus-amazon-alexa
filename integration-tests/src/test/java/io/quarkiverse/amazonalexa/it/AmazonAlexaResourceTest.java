package io.quarkiverse.amazonalexa.it;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class AmazonAlexaResourceTest {

    @Test
    public void testHelloEndpoint() {
        given()
                .when().get("/amazon-alexa")
                .then()
                .statusCode(200)
                .body(is("Hello amazon-alexa"));
    }
}
