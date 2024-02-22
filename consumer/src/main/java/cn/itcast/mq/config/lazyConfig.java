package cn.itcast.mq.config;


import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class lazyConfig {

    @Bean
    public Queue lazyQueue(){
        return QueueBuilder
                .durable("lazy.queue")
                .lazy()
                .build();
    }


    @Bean
    public Queue normalQueue(){
        return QueueBuilder
                .durable("normal.queue")
                .build();
    }

}
