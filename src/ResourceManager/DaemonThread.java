package ResourceManager;

import Producers.CPUProducer;
import Producers.DiskProducer;
import Producers.RAMProducer;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class DaemonThread extends Thread{

    protected AtomicBoolean isRunning;
    protected CPUProducer cpuProducer;
    protected RAMProducer ramProducer;
    protected DiskProducer diskProducer;
    protected ArrayList<Consumer> consumers;
    protected ScheduledExecutorService executorServiceForProducers;
    protected ExecutorService executorServiceForConsumers;
    protected BlockingQueue<ProducerValue> queue;
    protected ResourceMonitorGUI monitorGUI;
    public DaemonThread(CPUProducer cpuProducer,
                        RAMProducer ramProducer,
                        DiskProducer diskProducer,
                        ArrayList<Consumer> consumers,
                        ScheduledExecutorService executorServiceForProducers,
                        ExecutorService executorServiceForConsumers,
                        BlockingQueue<ProducerValue> queue,
                        ResourceMonitorGUI monitorGUI,
                        AtomicBoolean isRunning) {

        this.cpuProducer = cpuProducer;
        this.ramProducer = ramProducer;
        this.diskProducer = diskProducer;
        this.consumers = consumers;
        this.executorServiceForProducers = executorServiceForProducers;
        this.executorServiceForConsumers = executorServiceForConsumers;
        this.queue = queue;
        this.monitorGUI = monitorGUI;
        this.isRunning = isRunning;
    }
    @Override
    public void run() {

        while (true) {
            try {
                if(isRunning.get()) {
                    Thread.sleep(1000);
                    CreateProducerIfNotProducing(cpuProducer);
                    CreateProducerIfNotProducing(ramProducer);
                    CreateProducerIfNotProducing(diskProducer);
                    System.out.println("Here false");
                    for(Consumer consumer : consumers) {
                        CreateConsumerIfNotConsuming(consumer);
                    }
                }
                else {
                    System.out.println(Thread.activeCount());
                    executorServiceForProducers.close();
                    executorServiceForConsumers.close();
                }

            }
            catch (InterruptedException e) {
                throw new RuntimeException("Daemon Parou!");
            }

        }

    }

    public void CreateProducerIfNotProducing(Producer producer) {
        if(producer.lastTimeProduced != null) {
            if(Duration.between(producer.lastTimeProduced, Instant.now()).toMillis() >= 10000) {
                System.out.println("Not Producing");
                switch (producer) {
                    case CPUProducer cpuProducer1 ->
                            this.executorServiceForProducers.scheduleAtFixedRate(new CPUProducer(this.queue), 0, 100, TimeUnit.MILLISECONDS);
                    case RAMProducer ramProducer1 ->
                            this.executorServiceForProducers.scheduleAtFixedRate(new RAMProducer(this.queue), 0, 100, TimeUnit.MILLISECONDS);
                    case DiskProducer diskProducer1 ->
                            this.executorServiceForProducers.scheduleAtFixedRate(new DiskProducer(this.queue), 0, 100, TimeUnit.MILLISECONDS);
                    default -> {
                    }
                }
            }
        }

    }

    public void CreateConsumerIfNotConsuming(Consumer consumer) {
        if(consumer.lastTimeConsumed != null) {
            if(Duration.between(consumer.lastTimeConsumed, Instant.now()).toMillis() >= 30000) {
                System.out.println("Not Consuming");
                Consumer newConsumer = new Consumer(queue, monitorGUI);
                consumers.add(newConsumer);
                executorServiceForConsumers.execute(consumer);
            }
        }

    }


}
