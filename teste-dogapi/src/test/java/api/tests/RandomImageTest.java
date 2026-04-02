package api.tests;

import api.base.BaseTest;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.RepeatedTest;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.*;

@Feature("Imagem Aleatória")
@Tag("breeds")
@Owner("Marcelo")
@Link(name = "Documentação", url = "https://dog.ceo/dog-api/documentation/random")
public class RandomImageTest extends BaseTest {

    private static final String RANDOM_IMAGE = "/breeds/image/random";
    private static final String RANDOM_IMAGES_MULTIPLE = "/breeds/image/random/{count}";

    @Test
    @Severity(SeverityLevel.BLOCKER)
    @Story("Consulta Simples")
    @DisplayName("Validar contrato e status success")
    void deveRetornarSucessoEValidarSchema() {
        given()
                .spec(requestSpec)
                .when()
                .get(RANDOM_IMAGE)
                .then()
                .statusCode(200)
                .contentType(containsString("application/json"))
                .body("status", equalTo("success"))
                .body(matchesJsonSchemaInClasspath("schemas/random-image-schema.json"));
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Story("Consulta Simples")
    @DisplayName("Validar integridade da URL da imagem")
    void deveRetornarUrlValidaComExtensao() {
        String imageUrl = given()
                .spec(requestSpec)
                .when()
                .get(RANDOM_IMAGE)
                .then()
                .statusCode(200)
                .body("message", startsWith("https://"))
                .body("message", containsString("images.dog.ceo"))
                .extract().path("message");

        assert imageUrl.matches(".*\\.(jpg|jpeg|png|gif)$") : "Extensão inválida: " + imageUrl;
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Story("Múltiplas Imagens")
    @DisplayName("Validar limites de quantidade (3 e 50)")
    void deveValidarLimitesDeQuantidade() {
        // Teste com 3
        given()
                .spec(requestSpec)
                .pathParam("count", 3)
                .when()
                .get(RANDOM_IMAGES_MULTIPLE)
                .then()
                .statusCode(200)
                .body("message", hasSize(3))
                .body(matchesJsonSchemaInClasspath("schemas/random-images-multiple-schema.json"));

        // Teste com 50
        given()
                .spec(requestSpec)
                .pathParam("count", 50)
                .when()
                .get(RANDOM_IMAGES_MULTIPLE)
                .then()
                .statusCode(200)
                .body("message", hasSize(50));
    }

    @RepeatedTest(value = 3, name = "Validando Aleatoriedade: {currentRepetition}/3")
    @Severity(SeverityLevel.NORMAL)
    void deveRetornarImagensDiferentes() {
        given()
                .spec(requestSpec)
                .when()
                .get(RANDOM_IMAGE)
                .then()
                .statusCode(200)
                .body("message", notNullValue());
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Story("Performance")
    @DisplayName("Validar tempo de resposta")
    void deveResponderEmTempoAceitavel() {
        given()
                .spec(requestSpec)
                .when()
                .get(RANDOM_IMAGE)
                .then()
                .time(lessThan(5000L));
    }

    // --- Bugs Encontrados ---

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Story("Validar parâmetros inválidos")
    @DisplayName("BUG: Erro 400 para input não numérico")
    @Description("A API aceita letras e retorna 200/sucesso, ignorando a tipagem do parâmetro.")
    void deveRetornarErroParaParametroNaoNumerico() {
        given().spec(requestSpec)
                .when().get("/breeds/image/random/b")
                .then().statusCode(400);
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Validar parâmetros inválidos")
    @DisplayName("BUG: Erro 400 para contagem zero")
    void deveRetornarErroParaParametroZero() {
        given().spec(requestSpec)
                .when().get("/breeds/image/random/0")
                .then().statusCode(400);
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Validar parâmetros inválidos")
    @DisplayName("BUG: Erro 400 para contagem negativa")
    void deveRetornarErroParaParametroNegativo() {
        given().spec(requestSpec)
                .when().get("/breeds/image/random/-1")
                .then().statusCode(400);
    }
}