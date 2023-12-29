package ResourceManager;

import Producers.CPUProducer;
import Producers.DiskProducer;
import Producers.RAMProducer;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class Consumer extends Thread{
    protected BlockingQueue<ProducerValue> queue;
    protected ResourceMonitorGUI monitorGUI;
    protected boolean isConsuming;

    public Consumer(BlockingQueue<ProducerValue> queue, ResourceMonitorGUI monitorGUI) {
        this.queue = queue;
        this.monitorGUI = monitorGUI;
    }
    @Override
    public void run() {
        //while(true) {
            try {
                ProducerValue producerValue = queue.poll(30, TimeUnit.SECONDS);
                this.isConsuming = producerValue != null;
                if(producerValue != null) {
                    //System.out.println("Producer : " + producerValue.producer.getClass() + ", Producer value : " + producerValue.producerValue);
                    System.out.println(queue.size());
                    if(producerValue.producer instanceof RAMProducer) {
                        if(producerValue.producerValue < 10) {
                            monitorGUI.addAlert("RAM USAGE WARNING : USAGE -> " + String.format("%,.2f", producerValue.producerValue * 100) + "%");
                            System.out.println();
                        }
                    } else if (producerValue.producer instanceof CPUProducer) {
                        if(producerValue.producerValue > 80) {
                            monitorGUI.addAlert("CPU USAGE WARNING : USAGE -> " + String.format("%,.2f", producerValue.producerValue * 100) + "%");
                        }
                    }
                    else if (producerValue.producer instanceof DiskProducer) {
                        if(producerValue.producerValue < 20) {
                            monitorGUI.addAlert("DISK USAGE WARNING : USAGE -> " + String.format("%,.2f", producerValue.producerValue * 100) + "%");
                        }
                    }
                }
                else {
                    interrupt();
                }

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        //}

    }
}
