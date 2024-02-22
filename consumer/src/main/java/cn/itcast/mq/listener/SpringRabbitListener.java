package cn.itcast.mq.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SpringRabbitListener {

    @RabbitListener(queues = "simple.queue")
    public void listenSimpleQueue(String msg) {
        log.debug("消费者接收到simple.queue的消息：【" + msg + "】");
        System.out.println(1 / 0);
        log.info("发送消息成功");
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "dl.queue", declare = "true"),
            exchange = @Exchange("dl.direct"),
            key = "dl"
    ))
    public void listenDLQueue(){
        log.info("消费者接受到dl.queue的延迟信息");
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "delay.queue", declare = "true"),
            exchange = @Exchange(value = "delay.direct", delayed = "true"),
            key = "delay"
    ))
    public void listenDelayQueue(){
        log.info("消费者接受到delay.queue的延迟信息");
    }

}
