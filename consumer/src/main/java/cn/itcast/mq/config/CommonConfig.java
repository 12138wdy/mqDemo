package cn.itcast.mq.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommonConfig {

    //声明队列
    @Bean
    public DirectExchange exchange(){
        return new DirectExchange("direct.exchange",true,false);
    }

    @Bean
    public Queue queue(){
        return QueueBuilder.durable("simple.queue").build();
    }
}
