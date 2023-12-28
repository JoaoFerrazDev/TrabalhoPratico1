package ResourceManager;

import Producers.CPUProducer;
import Producers.RAMProducer;

import java.util.concurrent.BlockingQueue;

public class Consumer extends Thread{
    protected BlockingQueue<ProducerValue> queue;

    public Consumer(BlockingQueue<ProducerValue> queue) {
        this.queue = queue;
    }
    @Override
    public void run() {
        try {
            System.out.println(queue.size());
            ProducerValue producerValue = queue.take();
            if(producerValue.producer instanceof RAMProducer) {
                System.out.println("Here RAM");
                if(producerValue.producerValue * 100 > 90) {
                    System.out.println("here entrei");
                    ResourceMonitorGUI.newInstance().addAlert("RAM USAGE WARNING");
                }
            } else if (producerValue.producer instanceof CPUProducer) {
                System.out.println("Here CPU");
                if(producerValue.producerValue * 100 > 80) {
                    ResourceMonitorGUI.newInstance().addAlert("CPU USAGE WARNING");
                }
            }
            else {
                System.out.println("Here Disk");
                if(producerValue.producerValue * 100 > 80) {
                    ResourceMonitorGUI.newInstance().addAlert("DISK USAGE WARNING");
                }
            }

        } catch (InterruptedException e) {
            System.out.println("Error");

            throw new RuntimeException(e);
        }
    }
}
