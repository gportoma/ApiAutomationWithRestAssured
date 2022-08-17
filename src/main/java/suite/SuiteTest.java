package suite;

import core.BaseTest;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import tests.AuthTest;
import tests.ContasTest;
import tests.MovimentacaoTest;
import tests.SaldoTest;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        ContasTest.class,
        MovimentacaoTest.class,
        SaldoTest.class,
        AuthTest.class
})
public class SuiteTest extends BaseTest{

    @BeforeClass
    public static void getToken() {
        Map<String, String> login = new HashMap<>();
        login.put("email", "guilherme@portomalta");
        login.put("senha", "123456");

        String TOKEN;

        TOKEN = given()
                .body(login)
                .when()
                .post("/signin")
                .then()
                .statusCode(200)
                .extract().path("token");

        requestSpecification.header("Authorization", "JWT " + TOKEN);

        get("/reset").then().statusCode(200);
    }
}
