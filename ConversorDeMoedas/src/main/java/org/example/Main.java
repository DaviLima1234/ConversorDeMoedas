package org.example;

import java.io.IOException;
import java.util.Scanner;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class Main {

    private static final String API_URL = "https://api.exchangerate-api.com/v4/latest/USD";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        JsonObject exchangeRates = null;

        while (true) {
            System.out.println("====== Menu ======");
            System.out.println("1. Exibir taxas de câmbio");
            System.out.println("2. Realizar conversão de moedas");
            System.out.println("3. Sair");
            System.out.print("Escolha uma opção: ");
            int option = scanner.nextInt();

            switch (option) {
                case 1:
                    exchangeRates = obterTaxasDeCambio();
                    if (exchangeRates != null) {
                        exibirTaxasDeCambio(exchangeRates);
                    } else {
                        System.out.println("Erro ao obter taxas de câmbio.");
                    }
                    break;
                case 2:
                    if (exchangeRates == null) {
                        exchangeRates = obterTaxasDeCambio();
                    }
                    if (exchangeRates != null) {
                        realizarConversao(scanner, exchangeRates);
                    } else {
                        System.out.println("Erro ao obter taxas de câmbio.");
                    }
                    break;
                case 3:
                    System.out.println("Saindo...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        }
    }

    private static JsonObject obterTaxasDeCambio() {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(API_URL);
            HttpResponse response = httpClient.execute(request);
            String jsonResponse = EntityUtils.toString(response.getEntity());

            return JsonParser.parseString(jsonResponse).getAsJsonObject().getAsJsonObject("rates");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void exibirTaxasDeCambio(JsonObject exchangeRates) {
        System.out.println("Taxas de câmbio:");
        System.out.println(exchangeRates);
    }

    private static void realizarConversao(Scanner scanner, JsonObject exchangeRates) {
        System.out.print("Digite a moeda de origem (ex: USD, BRL, EUR): ");
        String moedaOrigem = scanner.next().toUpperCase();
        System.out.print("Digite a moeda de destino (ex: USD, BRL, EUR): ");
        String moedaDestino = scanner.next().toUpperCase();
        System.out.print("Digite o valor a ser convertido: ");
        double valor = scanner.nextDouble();

        if (exchangeRates.has(moedaOrigem) && exchangeRates.has(moedaDestino)) {
            double valorConvertido = calcularConversao(exchangeRates, moedaOrigem, moedaDestino, valor);
            System.out.printf("%.2f %s equivale a %.2f %s\n", valor, moedaOrigem, valorConvertido, moedaDestino);
        } else {
            System.out.println("Moeda de origem ou destino inválida.");
        }
    }

    private static double calcularConversao(JsonObject exchangeRates, String moedaOrigem, String moedaDestino, double valor) {
        double taxaOrigem = exchangeRates.get(moedaOrigem).getAsDouble();
        double taxaDestino = exchangeRates.get(moedaDestino).getAsDouble();
        return valor / taxaOrigem * taxaDestino;
    }
}
