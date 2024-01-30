package uno.crayon.engineer.performanceoptimizationtest;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.Throughput)
@Warmup(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(1)
@State(Scope.Benchmark)
public class KafkaBlockingQueueJmh {

    private static final int QUEUE_CAPACITY = 10000;
    private BlockingQueue<Integer> arrayBlockingQueue;
    private Producer<String, String> kafkaProducer;
    private Consumer<String, String> kafkaConsumer;

    @Setup
    public void setup() {
        // 初始化 ArrayBlockingQueue
        arrayBlockingQueue = new ArrayBlockingQueue<>(QUEUE_CAPACITY);

        // 初始化 Kafka Producer
        Properties producerProps = new Properties();
        producerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        kafkaProducer = new KafkaProducer<>(producerProps);

        // 初始化 Kafka Consumer
        Properties consumerProps = new Properties();
        consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, "test-group");
        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        kafkaConsumer = new KafkaConsumer<>(consumerProps);
        kafkaConsumer.subscribe(Collections.singletonList("test-topic"));
    }

    @Benchmark
    public void testArrayBlockingQueue() throws InterruptedException {
        // 模拟每秒处理 4000 条消息的场景
        for (int i = 0; i < 4000; i++) {
            arrayBlockingQueue.offer(i);
            arrayBlockingQueue.poll();
        }
    }

    @Benchmark
    public void testKafkaQueue() {
        // 模拟每秒处理 4000 条消息的场景
        for (int i = 0; i < 4000; i++) {
            kafkaProducer.send(new ProducerRecord<>("test-topic", Integer.toString(i), Integer.toString(i)));
            ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofMillis(100));
            // 在实际应用中，你可能需要处理消费的 records
        }
    }

    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder()
                .include(KafkaBlockingQueueJmh.class.getSimpleName())
                .build();
        new Runner(options).run();
    }
}
