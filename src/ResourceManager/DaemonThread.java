package ResourceManager;

import Producers.CPUProducer;
import Producers.DiskProducer;
import Producers.RAMProducer;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DaemonThread extends Thread{

    protected CPUProducer cpuProducer;
    protected RAMProducer ramProducer;
    protected DiskProducer diskProducer;
    protected ArrayList<Consumer> consumers;
    protected ScheduledExecutorService executorServiceForProducers;
    protected ScheduledExecutorService executorServiceForConsumers;
    protected BlockingQueue<ProducerValue> queue;
    protected ResourceMonitorGUI monitorGUI;
    public DaemonThread(CPUProducer cpuProducer,
                        RAMProducer ramProducer,
                        DiskProducer diskProducer,
                        ArrayList<Consumer> consumers,
                        ScheduledExecutorService executorServiceForProducers,
                        ScheduledExecutorService executorServiceForConsumers,
                        BlockingQueue<ProducerValue> queue,
                        ResourceMonitorGUI monitorGUI) {

        this.cpuProducer = cpuProducer;
        this.ramProducer = ramProducer;
        this.diskProducer = diskProducer;
        this.consumers = consumers;
        this.executorServiceForProducers = executorServiceForProducers;
        this.executorServiceForConsumers = executorServiceForConsumers;
        this.queue = queue;
        this.monitorGUI = monitorGUI;
    }
    @Override
    public void run() {

        while (true) {

            CreateProducerIfNotProducing(cpuProducer);
            CreateProducerIfNotProducing(ramProducer);
            CreateProducerIfNotProducing(diskProducer);
            for(Consumer consumer : consumers) {
                CreateConsumerIfNotConsuming(consumer);
            }
            try {
                sleep(1000);
            }
            catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void CreateProducerIfNotProducing(Producer producer) {
        if(!producer.isProducing) {
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

    public void CreateConsumerIfNotConsuming(Consumer consumer) {
        if(!consumer.isConsuming) {
            System.out.println("Not Consuming");
            this.executorServiceForConsumers.schedule(new Consumer(queue, monitorGUI), 2, TimeUnit.SECONDS);
        }
    }


}
