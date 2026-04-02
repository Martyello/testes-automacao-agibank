package api.tests;

import api.base.BaseTest;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.*;

@Feature("Imagens por Raça")
@Tag("breeds")
@Owner("Marcelo")
public class BreedImagesTest extends BaseTest {

    private static final String BREED_IMAGES = "/breed/{breed}/images";

    @ParameterizedTest(name = "Validar raça: {0}")
    @ValueSource(strings = {"labrador", "bulldog", "husky", "akita"})
    @Severity(SeverityLevel.BLOCKER)
    @Story("Consulta por Raça")
    @DisplayName("Validar contrato e integridade das imagens")
    void deveValidarContratoEImagensPorRaca(String breed) {
        given()
                .spec(requestSpec)
                .pathParam("breed", breed)
                .when()
                .get(BREED_IMAGES)
                .then()
                .statusCode(200)
                .body("status", equalTo("success"))
                .body("message", not(empty()))
                .body("message[0]", allOf(startsWith("https://"), containsString("images.dog.ceo")))
                .body(matchesJsonSchemaInClasspath("schemas/breed-images-schema.json"));
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Story("Consulta por Sub-raça")
    @DisplayName("Validar imagens de sub-raça (English Bulldog)")
    void deveRetornarImagensParaSubRaca() {
        given()
                .spec(requestSpec)
                .when()
                .get("/breed/bulldog/english/images")
                .then()
                .statusCode(200)
                .body("status", equalTo("success"))
                .body("message", hasSize(greaterThan(0)));
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Story("Fluxo de Exceção")
    @DisplayName("Validar erro 404 para raça inexistente")
    void deveRetornarErroParaRacaInexistente() {
        given()
                .spec(requestSpec)
                .pathParam("breed", "macaco")
                .when()
                .get(BREED_IMAGES)
                .then()
                .statusCode(404)
                .body("status", equalTo("error"))
                .body("message", containsString("Breed not found"));
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Story("Performance")
    @DisplayName("Validar SLA de resposta")
    void deveResponderDentroDoSLA() {
        given()
                .spec(requestSpec)
                .pathParam("breed", "labrador")
                .when()
                .get(BREED_IMAGES)
                .then()
                .time(lessThan(5000L));
    }
}