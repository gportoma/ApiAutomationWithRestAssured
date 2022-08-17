package tests;

import core.BaseTest;
import org.junit.Test;
import utils.ApiUtils;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class SaldoTest extends BaseTest {

    @Test
    public void deveCalcularSaldoContas() {
        given()
                .when()
                .get("/saldo")
                .then()
                .statusCode(200)
                .body("find{it.conta_id == " + ApiUtils.getIdContaPeloNome("Conta para saldo") + "}.saldo", is("534.00"))
        ;
    }

}
