package tests;

import core.BaseTest;
import io.restassured.RestAssured;
import org.junit.BeforeClass;
import org.junit.Test;
import utils.ApiUtils;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.requestSpecification;
import static org.hamcrest.Matchers.is;

public class ContasTest extends BaseTest {

    @Test
    public void deveIncluirContaComSucesso() {
        Map<String, String> conta = new HashMap<>();
        conta.put("nome", "Conta Inserida");

        given()
                .body(conta)
                .when()
                .post("/contas")
                .then()
                .statusCode(201)
        ;
    }

    @Test
    public void deveAlterarConta() {
        Map<String, String> userAlterado = new HashMap<>();
        userAlterado.put("nome", "Conta Alterada");

        given()
                .body(userAlterado)
                .pathParam("accountId", ApiUtils.getIdContaPeloNome("Conta para alterar"))
                .when()
                .put("/contas/{accountId}")
                .then()
                .statusCode(200)
                .body("nome", is("Conta Alterada"))
        ;
    }

    @Test
    public void naoDeveInserirContaMesmoNome() {
        Map<String, String> conta = new HashMap<>();
        conta.put("nome", "Conta mesmo nome");

        given()
                .body(conta)
                .when()
                .post("/contas")
                .then()
                .statusCode(400)
                .body("error", is("Já existe uma conta com esse nome!"))
        ;
    }

}
