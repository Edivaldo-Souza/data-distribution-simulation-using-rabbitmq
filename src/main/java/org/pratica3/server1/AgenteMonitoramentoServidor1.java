package org.pratica3.server1;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.pratica3.commons.MensagemDeServico;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.concurrent.TimeoutException;

public class AgenteMonitoramentoServidor1 {
    private static final String EXCHANGE = "topic_logs";

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Random random = new Random();

        try(Connection connection =factory.newConnection(); Channel channel = connection.createChannel();) {
            channel.exchangeDeclare(EXCHANGE, BuiltinExchangeType.TOPIC);

            String routingKeyDatabaseService = "servidor1.servico1";
            String routingKeyWebService = "servidor1.servico2";
            MensagemDeServico mensagem = new MensagemDeServico();

            while (true) {
                mensagem.setCpu_usage(random.nextInt(0, 100));
                mensagem.setMemory_usage(random.nextInt(0, 100));
                mensagem.setResponse_time(random.nextInt(500));
                mensagem.setServer("Servidor 1");
                mensagem.setService("Banco de Dados");
                mensagem.setStatus(getStatus(
                        mensagem.getCpu_usage(),
                        mensagem.getMemory_usage(),
                        mensagem.getResponse_time()
                ));
                mensagem.setTimestamp(LocalDateTime.now());

                channel.basicPublish(EXCHANGE, routingKeyDatabaseService, null, mensagem.toString().getBytes(StandardCharsets.UTF_8));
                System.out.println(" [x] Enviando: '" + mensagem.toString() + "'");

                mensagem.setCpu_usage(random.nextInt(0, 100));
                mensagem.setMemory_usage(random.nextInt(0, 100));
                mensagem.setResponse_time(random.nextInt(10000));
                mensagem.setServer("Servidor 1");
                mensagem.setService("Servidor Web");
                mensagem.setStatus(getStatus(
                        mensagem.getCpu_usage(),
                        mensagem.getMemory_usage(),
                        mensagem.getResponse_time()
                ));
                mensagem.setTimestamp(LocalDateTime.now());

                channel.basicPublish(EXCHANGE, routingKeyWebService, null, mensagem.toString().getBytes(StandardCharsets.UTF_8));
                System.out.println(" [x] Enviando: '" + mensagem.toString() + "'");

                Thread.sleep(3000);
            }
        }
    }

    public static String getStatus(Integer cpu, Integer memory, Integer response_time){
        String status = "azul";
        if(cpu<30 || memory<30 || response_time<50){
            status = "azul";
        }
        if((cpu>75 && memory>75) || response_time>7000){
            status = "amarelo";
        }
        if((cpu>90 && memory>90) && response_time>9000){
            status = "vermelho";
        }
        return status;
    }
}
