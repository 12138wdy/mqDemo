package cn.itcast.mq.spring;

import com.rabbitmq.client.MessageProperties;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.concurrent.FailureCallback;
import org.springframework.util.concurrent.SuccessCallback;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringAmqpTest {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void testSendMessage2SimpleQueue() throws InterruptedException {

        String message = "hello, spring amqp!";

        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
        correlationData.getFuture().addCallback(
                result -> {
                    if (result.isAck()) {
                        //Ack
                        log.debug("消息成功投递到交换机，ID为：{}", correlationData.getId());
                    } else {
                        //NAck
                        log.error("消息投递到交换机失败，ID为：{}", correlationData.getId());
                    }
                }, err -> {
                    //失败，记录日志
                    log.error("发送消息失败！", err);
                }
        );


        rabbitTemplate.convertAndSend("amq.topic", "simple.test", message, correlationData);
    }

    @Test
    public void testMessage() {
        Message message = MessageBuilder.withBody("hello".getBytes(StandardCharsets.UTF_8))
                .setDeliveryMode(MessageDeliveryMode.PERSISTENT)
                .build();
        //发送信息
        rabbitTemplate.convertAndSend("simple.queue", message);
    }

    @Test
    public void testTTLMessage() {
        Message message = MessageBuilder.withBody("hello".getBytes(StandardCharsets.UTF_8))
                .setDeliveryMode(MessageDeliveryMode.PERSISTENT)
                .setExpiration("5000")
                .build();
        //发送信息
        rabbitTemplate.convertAndSend("ttl.direct", "ttl", message);
        log.info("消息发送成功");
    }

    @Test
    public void testSendMessageDelayQueue() throws InterruptedException {

        Message message = MessageBuilder.withBody("hello, ttl message".getBytes(StandardCharsets.UTF_8))
                .setDeliveryMode(MessageDeliveryMode.PERSISTENT)
                .setHeader("x-delay", 5000)
                .build();;

        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());



        rabbitTemplate.convertAndSend("delay.direct", "delay", message, correlationData);
    }
}
