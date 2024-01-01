package ResourceManager;

import Producers.CPUProducer;
import Producers.DiskProducer;
import Producers.RAMProducer;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        AtomicBoolean isRunning = new AtomicBoolean(true);
        BlockingQueue<ProducerValue> queue = new ArrayBlockingQueue<>(100);
        ResourceMonitorGUI monitorGUI = ResourceMonitorGUI.newInstance(isRunning);
        int numberOfConsumers = 3;
        ArrayList<Consumer> consumers = new ArrayList<>();
        for (int i = 0; i < numberOfConsumers; i++) {
            consumers.add(new Consumer(queue, monitorGUI));
        }

        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(3 + numberOfConsumers);

        RAMProducer ramProducer = new RAMProducer(queue);
        CPUProducer cpuProducer = new CPUProducer(queue);
        DiskProducer diskProducer = new DiskProducer(queue);

        DaemonThread daemonThread = new DaemonThread(cpuProducer,
                ramProducer,
                diskProducer,
                consumers,
                executorService,
                queue,
                monitorGUI,
                isRunning);

        daemonThread.setDaemon(true);

        executorService.scheduleAtFixedRate(ramProducer, 0, 100, TimeUnit.MILLISECONDS);
        executorService.scheduleAtFixedRate(cpuProducer, 0, 100, TimeUnit.MILLISECONDS);
        executorService.scheduleAtFixedRate(diskProducer, 0, 100, TimeUnit.MILLISECONDS);

        for (Consumer consumer: consumers) {
            executorService.execute(consumer);
        }

        daemonThread.start();
    }
}