package edu.neu.husky.wenl.huang.server.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.*;

public class RabbitMQUtils {
    private static Channel channel;
    private static Connection connection;
//    private static final String DOMAIN = "localhost";
//    private static final String USERNAME = "guest";
//    private static final String USERNAME = "guest";
    private static final String DOMAIN = "35.166.195.89";
    private static final String USERNAME = "admin";
    private static final String PASSWORD = "admin";

    private static Channel getChannel() {
        try {
            if (channel == null) {
                ConnectionFactory factory = new ConnectionFactory();

                factory.setHost(DOMAIN);
                factory.setUsername(USERNAME);
                factory.setPassword(PASSWORD);

                connection = factory.newConnection();
                channel = connection.createChannel();
            }
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
        return channel;
    }

    public static void publish(RoutingKeys routingKey, String message) {
        try {
            Channel channel = RabbitMQUtils.getChannel();
            channel.basicPublish("", routingKey.toString(), null, message.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void releaseResources() {
        try {
            if (channel    != null)  channel.close();
            if (connection != null)  connection.close();
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }
}
