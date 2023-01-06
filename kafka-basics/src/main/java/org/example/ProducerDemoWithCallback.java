package org.example;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class ProducerDemoWithCallback {

    private static final Logger log = LoggerFactory.getLogger(ProducerDemoWithCallback.class.getSimpleName());

    public static void main(String[] args) {
        log.info("Kafka Producer");

        //create Producer Properties
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());


        //create a Producer
        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);



        for(int i=0;i<10;i++) {
            //create a Producer Record
            ProducerRecord<String,String> producerRecord = new ProducerRecord<>("demo_java", "hello world "+i);

            //send the data - asynchronous
            producer.send(producerRecord, new Callback() {
                @Override
                public void onCompletion(RecordMetadata metadata, Exception exception) {
                    // method is called after the record has been inserted or something went wrong - synchronous code
                    if (exception == null) {
                        log.info("Received new meta-data / \n" +
                                "Topic: " + metadata.topic() + "\n" +
                                "Partition: " + metadata.partition() + "\n" +
                                "Offset: " + metadata.offset() + "\n" +
                                "Timestamp: " + metadata.timestamp()
                        );
                    } else {
                        log.error("Cannot produce to topic" + exception.getMessage());
                    }
                }
            });
        }

        //flush - synchronous and close - calls flush
        producer.flush();
        producer.close();
    }
}
