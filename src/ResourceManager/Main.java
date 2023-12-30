package ResourceManager;

import Producers.CPUProducer;
import Producers.DiskProducer;
import Producers.RAMProducer;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        AtomicBoolean isRunning = new AtomicBoolean(true);
        BlockingQueue<ProducerValue> queue = new ArrayBlockingQueue<>(100);
        ResourceMonitorGUI monitorGUI = ResourceMonitorGUI.newInstance(isRunning);
        Scanner in = new Scanner(System.in);
        int numberOfConsumers = 1;
        ArrayList<Consumer> consumers = new ArrayList<>();
        for (int i = 0; i < numberOfConsumers; i++) {
            consumers.add(new Consumer(queue, monitorGUI));
        }

        ExecutorService executorServiceForConsumers = Executors.newCachedThreadPool();
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
                monitorGUI,
                isRunning);

        daemonThread.setDaemon(true);

        executorServiceForProducers.scheduleAtFixedRate(ramProducer, 0, 100, TimeUnit.MILLISECONDS);
        executorServiceForProducers.scheduleAtFixedRate(cpuProducer, 0, 100, TimeUnit.MILLISECONDS);
        executorServiceForProducers.scheduleAtFixedRate(diskProducer, 0, 100, TimeUnit.MILLISECONDS);

        for (Consumer consumer: consumers) {
            executorServiceForConsumers.execute(consumer);
        }

        daemonThread.start();

    }
}