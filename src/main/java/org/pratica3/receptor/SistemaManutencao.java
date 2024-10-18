package org.pratica3.receptor;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class SistemaManutencao {

    private static String FILA_ORDEM_DE_SERVICOS = "filaOS";

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(FILA_ORDEM_DE_SERVICOS, false, false, false, null);

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
          String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
          String server = message.split(",")[3].split("=")[1];
          String service = message.split(",")[1].split("=")[1];
          String cause = message.split(",")[4].split("=")[1];
          String solution = message.split(",")[5].split("=")[1].split("}")[0];

          System.out.println("Ação necessário no "+service+" do servidor "+server+".\n"+
                  "Motivo: "+ cause + ",\n"+
                  "Recomendação: "+ solution + "\n");
        };

        channel.basicConsume(FILA_ORDEM_DE_SERVICOS, true, deliverCallback, consumerTag -> {});

    }
}
