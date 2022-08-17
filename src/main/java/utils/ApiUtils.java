package utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static io.restassured.RestAssured.get;

public class ApiUtils {

    public static String getDataDiferencaDias(Integer qtdDias) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, qtdDias);
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        return format.format(cal.getTime());
    }

    public static Integer getIdContaPeloNome(String nome) {
        return get("/contas?nome=" + nome).then().extract().path("id[0]");
    }

}
