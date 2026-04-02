package api.tests;

import api.base.BaseTest;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@Feature("Listagem de Raças")
@Tag("breeds")
@Owner("Marcelo")
@Link(name = "Documentação", url = "https://dog.ceo/dog-api/documentation/")
public class BreedsListTest extends BaseTest {

    private static final String BREEDS_LIST_ALL = "/breeds/list/all";

    @Test
    @Severity(SeverityLevel.BLOCKER)
    @Story("Consulta Geral")
    @DisplayName("Validar estrutura da lista e raças principais")
    void deveValidarEstruturaEConteudoDaLista() {
        given()
                .spec(requestSpec)
                .when()
                .get(BREEDS_LIST_ALL)
                .then()
                .statusCode(200)
                .contentType(containsString("application/json"))
                .body("status", equalTo("success"))
                // Valida que a lista não é vazia
                .body("message", not(anEmptyMap()))
                .body("message.size()", greaterThan(0))
                // Valida raças chaves em uma única passada
                .body("message", hasKey("bulldog"))
                .body("message", hasKey("labrador"))
                .body("message", hasKey("poodle"));
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Story("Consulta Específica")
    @DisplayName("Validar sub-raças do Bulldog")
    void deveRetornarSubRacasParaBulldog() {
        given()
                .spec(requestSpec)
                .when()
                .get(BREEDS_LIST_ALL)
                .then()
                .statusCode(200)
                .body("message.bulldog", hasItems("english", "french", "boston"));
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Story("Performance")
    @DisplayName("Validar SLA de resposta")
    void deveResponderDentroDoSLA() {
        given()
                .spec(requestSpec)
                .when()
                .get(BREEDS_LIST_ALL)
                .then()
                .time(lessThan(5000L));
    }
}