package ResourceManager;

import Producers.CPUProducer;
import Producers.DiskProducer;
import Producers.RAMProducer;
import ResourceManager.ResourceMonitorGUI;

import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<ProducerValue> queue = new ArrayBlockingQueue<>(100);
        ResourceMonitorGUI monitorGUI = ResourceMonitorGUI.newInstance();

        RAMProducer ramProducer = new RAMProducer(queue);
        CPUProducer cpuProducer = new CPUProducer(queue);
        DiskProducer diskProducer = new DiskProducer(queue);
        Consumer consumer1 = new Consumer(queue, monitorGUI);
        Consumer consumer2 = new Consumer(queue, monitorGUI);
        Consumer consumer3 = new Consumer(queue, monitorGUI);

        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();


        // Schedule the task to run every 100 milliseconds
        executorService.scheduleAtFixedRate(ramProducer, 0, 100, TimeUnit.MILLISECONDS);
        executorService.scheduleAtFixedRate(cpuProducer, 0, 100, TimeUnit.MILLISECONDS);
        executorService.scheduleAtFixedRate(diskProducer, 0, 100, TimeUnit.MILLISECONDS);
        executorService.scheduleAtFixedRate(consumer1, 2000, 300, TimeUnit.MILLISECONDS);
        executorService.scheduleAtFixedRate(consumer2, 2000, 300, TimeUnit.MILLISECONDS);
        executorService.scheduleAtFixedRate(consumer3, 2000, 300, TimeUnit.MILLISECONDS);


        // Run the scheduled task for a certain duration (e.g., 5000 milliseconds)
        try {
            Thread.sleep(Integer.MAX_VALUE);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Shut down the executor service
        executorService.shutdown();

        /*ExecutorService executor = Executors.newCachedThreadPool();
        executor.execute(ramProducer);
        executor.execute(cpuProducer);
        executor.execute(diskProducer);

        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);*/

    }
}