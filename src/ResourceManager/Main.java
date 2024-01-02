package ResourceManager;

import Producers.CPUProducer;
import Producers.DiskProducer;
import Producers.RAMProducer;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class Main {
    static AtomicBoolean isRunning = new AtomicBoolean(true);

    public static void main(String[] args) throws InterruptedException {
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
                monitorGUI);

        daemonThread.setDaemon(true);

        executorService.scheduleAtFixedRate(diskProducer, 0, 100, TimeUnit.MILLISECONDS).isDone();
        executorService.scheduleAtFixedRate(cpuProducer, 0, 100, TimeUnit.MILLISECONDS).isDone();
        executorService.scheduleAtFixedRate(ramProducer, 0, 100, TimeUnit.MILLISECONDS).isDone();
        for (Consumer consumer: consumers) {
            executorService.execute(consumer);
        }

        daemonThread.start();
    }
}