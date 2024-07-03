package org.example;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

public class ClienteApiConversorMoedas {

    private static final String BASE_URL = "https://api.exchangerate-api.com/v4/latest/";

    private static final List<String> MOEDAS_INTERESSE = Arrays.asList(
            "ARS", "BOB", "BRL", "CLP", "COP", "USD"
    );

    public TaxasDeCambio obterTaxasDeCambio(String base) throws IOException, URISyntaxException {
        HttpClient httpClient = HttpClients.createDefault();

        URI uri = new URI(BASE_URL + base);
        HttpGet httpGet = new HttpGet(uri);

        HttpResponse response = httpClient.execute(httpGet);

        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode != 200) {
            throw new IOException("Falha na requisição HTTP: Código de status " + statusCode);
        }

        HttpEntity entity = response.getEntity();
        if (entity == null) {
            throw new IOException("Resposta vazia recebida da API");
        }

        String jsonResposta = EntityUtils.toString(entity);


        JsonParser parser = new JsonParser();
        JsonObject json = parser.parse(jsonResposta).getAsJsonObject();


        JsonObject rates = json.getAsJsonObject("rates");
        JsonObject ratesFiltradas = new JsonObject();
        for (String moeda : MOEDAS_INTERESSE) {
            if (rates.has(moeda)) {
                ratesFiltradas.add(moeda, rates.get(moeda));
            }
        }


        TaxasDeCambio taxasDeCambio = new TaxasDeCambio();
        taxasDeCambio.setProvider(json.get("provider").getAsString());
        taxasDeCambio.setBase(json.get("base").getAsString());
        taxasDeCambio.setDate(json.get("date").getAsString());
        taxasDeCambio.setRates(ratesFiltradas);

        return taxasDeCambio;
    }
}
