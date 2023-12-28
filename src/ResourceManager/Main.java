package ResourceManager;

import Producers.CPUProducer;
import Producers.DiskProducer;
import Producers.RAMProducer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<ProducerValue> queue = new ArrayBlockingQueue<>(100);
        ResourceMonitorGUI monitorGUI = ResourceMonitorGUI.newInstance();
        int numberOfConsumers = 1;
        ArrayList<Consumer> consumers = new ArrayList<>();
        consumers.add(new Consumer(queue, monitorGUI));
        consumers.add(new Consumer(queue, monitorGUI));
        consumers.add(new Consumer(queue, monitorGUI));

        ScheduledExecutorService executorServiceForConsumers = Executors.newScheduledThreadPool(numberOfConsumers);
        ScheduledExecutorService executorServiceForProducers = Executors.newScheduledThreadPool(3);

        RAMProducer ramProducer = new RAMProducer(queue);
        CPUProducer cpuProducer = new CPUProducer(queue);
        DiskProducer diskProducer = new DiskProducer(queue);

        DaemonThread daemonThread = new DaemonThread(cpuProducer,
                ramProducer,
                diskProducer,
                consumers,
                executorServiceForProducers,
                executorServiceForConsumers,
                queue,
                monitorGUI);

        daemonThread.setDaemon(true);
        daemonThread.start();

        executorServiceForProducers.scheduleAtFixedRate(ramProducer, 0, 100, TimeUnit.MILLISECONDS);
        executorServiceForProducers.scheduleAtFixedRate(cpuProducer, 0, 100, TimeUnit.MILLISECONDS);
        executorServiceForProducers.scheduleAtFixedRate(diskProducer, 0, 100, TimeUnit.MILLISECONDS);

        for (Consumer consumer: consumers) {
            executorServiceForConsumers.schedule(consumer, 2, TimeUnit.SECONDS);
        }

        try {
            Thread.sleep(Integer.MAX_VALUE);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}