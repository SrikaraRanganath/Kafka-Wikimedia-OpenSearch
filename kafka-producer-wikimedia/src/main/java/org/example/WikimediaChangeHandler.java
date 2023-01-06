package org.example;

import com.launchdarkly.eventsource.EventHandler;
import com.launchdarkly.eventsource.MessageEvent;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WikimediaChangeHandler implements EventHandler {

    KafkaProducer<String,String> kafkaProducer;
    String topic;
    private final Logger log = LoggerFactory.getLogger(WikimediaChangeHandler.class.getName());


    public WikimediaChangeHandler (KafkaProducer<String,String> kafkaProducer, String topic) {
        this.kafkaProducer = kafkaProducer;
        this.topic=topic;
    }

    @Override
    public void onOpen() {
        //nothing here
    }

    @Override
    public void onClosed() {
        kafkaProducer.close();
    }

    @Override
    public void onMessage(String event, MessageEvent messageEvent) {
        //asynchronous
        log.info(messageEvent.getData());
        kafkaProducer.send(new ProducerRecord<String,String>(topic, messageEvent.getData()));
    }

    @Override
    public void onComment(String comment) {
        //nothing here
    }

    @Override
    public void onError(Throwable t) {
        log.error("Error in reading stream");
    }
}
