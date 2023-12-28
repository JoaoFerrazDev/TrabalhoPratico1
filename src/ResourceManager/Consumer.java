package ResourceManager;

import Producers.CPUProducer;
import Producers.DiskProducer;
import Producers.RAMProducer;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class Consumer extends Thread{
    protected BlockingQueue<ProducerValue> queue;
    protected ResourceMonitorGUI monitorGUI;

    public Consumer(BlockingQueue<ProducerValue> queue, ResourceMonitorGUI monitorGUI) {
        this.queue = queue;
        this.monitorGUI = monitorGUI;
    }
    @Override
    public void run() {
        try {
            System.out.println(queue.size());
            ProducerValue producerValue = queue.poll(200, TimeUnit.MILLISECONDS);
            if(producerValue.producer instanceof RAMProducer) {
                if(producerValue.producerValue * 100 > 90) {
                    monitorGUI.addAlert("RAM USAGE WARNING : USAGE -> " + String.format("%,.2f", producerValue.producerValue * 100) + "%");
                    System.out.println();
                }
            } else if (producerValue.producer instanceof CPUProducer) {
                if(producerValue.producerValue * 100 > 80) {
                    monitorGUI.addAlert("CPU USAGE WARNING : USAGE -> " + String.format("%,.2f", producerValue.producerValue * 100) + "%");
                }
            }
            else if (producerValue.producer instanceof DiskProducer) {
                if(producerValue.producerValue * 100 > 80) {
                    monitorGUI.addAlert("DISK USAGE WARNING : USAGE -> " + String.format("%,.2f", producerValue.producerValue * 100) + "%");
                }
            }

        } catch (InterruptedException e) {
            System.out.println("Error");

            throw new RuntimeException(e);
        }
    }
}
