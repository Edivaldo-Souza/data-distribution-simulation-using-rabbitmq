package org.pratica3.receptor;

import com.rabbitmq.client.*;
import org.pratica3.commons.OrdemDeServico;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.concurrent.TimeoutException;

public class ServicoMonitoramentoCentralizado {
    private static final String EXCHANGE = "topic_logs";

    private static String[] serverStatus;
    private static String FILA_ORDEM_DE_SERVICOS = "filaOS";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        String routingKey = "#";

        serverStatus = new String[3];
        serverStatus[0] = "azul";
        serverStatus[1] = "azul";
        serverStatus[2] = "azul";
        Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            channel.exchangeDeclare(EXCHANGE, BuiltinExchangeType.TOPIC);
            String queueName = channel.queueDeclare().getQueue();

            channel.queueDeclare(FILA_ORDEM_DE_SERVICOS,false,false,false,null);

            channel.queueBind(queueName, EXCHANGE, routingKey);

            System.out.println("Recebendo mensagens...");

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), "UTF-8");
                //System.out.println("Mensagem: " + message);

                String status = message.split(",")[2].split("=")[1];
                String server = message.split(",")[3].split("=")[1];
                String service = message.split(",")[1].split("=")[1];
                String cpu_usage = message.split(",")[4].split("=")[1];
                String response_time = message.split(",")[6].split("=")[1].split("}")[0];

                OrdemDeServico ordem = new OrdemDeServico();
                if(status.equals("amarelo")) {
                    ordem = new OrdemDeServico();
                    ordem.setTimestamp(LocalDateTime.now());
                    ordem.setServer(server);
                    ordem.setStatus(status);
                    ordem.setService(service);
                    ordem.setProblem("Tempo de resposta está alto: "+response_time+ " milisegundos");
                    ordem.setAction_required("Finalize alguns processos em segundo plano");
                    System.out.println("[x] Enviando: " + ordem.toString());
                    channel.basicPublish("",
                            FILA_ORDEM_DE_SERVICOS,
                            null,
                            ordem.toString().getBytes());
                }
                else if(status.equals("vermelho")) {
                    ordem = new OrdemDeServico();
                    ordem.setTimestamp(LocalDateTime.now());
                    ordem.setServer(server);
                    ordem.setStatus(status);
                    ordem.setService(service);

                    ordem.setProblem("Processador com "+cpu_usage+"% de utilização " +
                            "\ne tempo de resposta: "+response_time+" milisegundos");
                    ordem.setAction_required("Verifique e reinicie o serviço");
                    System.out.println("[x] Enviando: " + ordem.toString());
                    channel.basicPublish("",
                            FILA_ORDEM_DE_SERVICOS,
                            null,
                            ordem.toString().getBytes());

                }
            };

            channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {});

    }
}
