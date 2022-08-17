package tests;

import core.BaseTest;
import org.junit.Test;
import repositorio.Movimentacao;
import utils.ApiUtils;

import java.util.Random;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class MovimentacaoTest extends BaseTest {

    @Test
    public void deveInserirMovimentacao() {
        given()
                .body(getMovimentacaoValida())
                .when()
                .post("/transacoes")
                .then()
                .statusCode(201)
        ;
    }

    @Test
    public void deveValidarCamposObrigatoriosMovimentacao() {
        String[] param = new String[]{"data_transacao", "data_pagamento", "descricao", "envolvido", "valor", "conta_id", "status"};
        String[] msgObrigatorio = new String[]{"Data da Movimentação é obrigatório", "Data do pagamento é obrigatório", "Descrição é obrigatório", "Interessado é obrigatório", "Valor é obrigatório", "Valor deve ser um número", "Conta é obrigatório", "Situação é obrigatório"};

        given()
                .when()
                .post("/transacoes")
                .then()
                .statusCode(400)
                .body("$", hasSize(8))
                .body("param", hasItems(param))
                .body("msg", hasItems(msgObrigatorio))
        ;
    }

    @Test
    public void naoDeveInserirMovimentacaoComDataFutura() {
        Movimentacao mov = getMovimentacaoValida();
        mov.setData_transacao(ApiUtils.getDataDiferencaDias(3));

        given()
                .body(mov)
                .when()
                .post("/transacoes")
                .then()
                .statusCode(400)
                .body("msg", hasItem("Data da Movimentação deve ser menor ou igual à data atual"))
        ;
    }

    @Test
    public void naoDeveRemoverContaComMovimentacao() {
        given()
                .pathParam("accountId", ApiUtils.getIdContaPeloNome("Conta com movimentacao"))
                .when()
                .delete("/contas/{accountId}")
                .then()
                .statusCode(500)
                .body("constraint", is("transacoes_conta_id_foreign"))
                .body("detail", containsString("is still referenced from table \"transacoes\"."))
        ;
    }

    @Test
    public void deveRemoverMovimentacao() {
        given()
                .pathParam("transactionId", getIdMovimentacaoPelaDescricao("Movimentacao para exclusao"))
                .when()
                .delete("/transacoes/{transactionId}")
                .then()
                .statusCode(204)
        ;
    }

    private Movimentacao getMovimentacaoValida() {
        Movimentacao movimentacao = new Movimentacao();
        Random rand = new Random();

        movimentacao.setConta_id(ApiUtils.getIdContaPeloNome("Conta para movimentacoes"));
        movimentacao.setDescricao("MOVIMENTACAO DO RONALDO");
        movimentacao.setEnvolvido("Envolvido da movimentacao");
        movimentacao.setTipo("REC");
        movimentacao.setData_transacao(ApiUtils.getDataDiferencaDias(-1));
        movimentacao.setData_pagamento(ApiUtils.getDataDiferencaDias(5));
        movimentacao.setValor(rand.nextFloat());
        movimentacao.setStatus(true);
        return movimentacao;
    }

    private Integer getIdMovimentacaoPelaDescricao(String desc) {
        return get("/transacoes?descricao=" + desc).then().extract().path("id[0]");
    }
}
