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

    protected CPUProducer cpuProducer;
    protected RAMProducer ramProducer;
    protected DiskProducer diskProducer;
    protected ArrayList<Consumer> consumers;
    protected ScheduledExecutorService executorService;
    protected BlockingQueue<ProducerValue> queue;
    protected ResourceMonitorGUI monitorGUI;
    public DaemonThread(CPUProducer cpuProducer,
                        RAMProducer ramProducer,
                        DiskProducer diskProducer,
                        ArrayList<Consumer> consumers,
                        ScheduledExecutorService executorService,
                        BlockingQueue<ProducerValue> queue,
                        ResourceMonitorGUI monitorGUI) {

        this.cpuProducer = cpuProducer;
        this.ramProducer = ramProducer;
        this.diskProducer = diskProducer;
        this.consumers = consumers;
        this.executorService = executorService;
        this.queue = queue;
        this.monitorGUI = monitorGUI;
    }
    @Override
    public void run() {

        while (true) {
            try {
                if(Main.isRunning.get()) {
                    Thread.sleep(1000);
                    CreateProducerIfNotProducing(cpuProducer);
                    CreateProducerIfNotProducing(ramProducer);
                    CreateProducerIfNotProducing(diskProducer);
                    for(Consumer consumer : consumers) {
                        CreateConsumerIfNotConsuming(consumer);
                    }
                }
                else {
                    executorService.shutdown();
                    break;
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
                System.out.println("Not Producing : " + producer.getClass());
                if(producer instanceof CPUProducer) {
                    CPUProducer newCpuProducer = new CPUProducer(this.queue);
                    this.cpuProducer = newCpuProducer;
                    this.executorService.scheduleAtFixedRate(newCpuProducer, 0, 100, TimeUnit.MILLISECONDS);
                }

                if(producer instanceof RAMProducer) {
                    RAMProducer newRamProducer = new RAMProducer(this.queue);
                    this.ramProducer = newRamProducer;
                    this.executorService.scheduleAtFixedRate(newRamProducer, 0, 100, TimeUnit.MILLISECONDS);
                }

                if(producer instanceof DiskProducer) {
                    DiskProducer newDiskProducer = new DiskProducer(this.queue);
                    this.diskProducer = newDiskProducer;
                    this.executorService.scheduleAtFixedRate(newDiskProducer, 0, 100, TimeUnit.MILLISECONDS);
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
                executorService.execute(consumer);
            }
        }

    }


}
