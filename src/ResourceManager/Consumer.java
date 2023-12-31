package ResourceManager;

import Producers.CPUProducer;
import Producers.DiskProducer;
import Producers.RAMProducer;

import java.time.Instant;
import java.time.temporal.Temporal;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class Consumer implements Runnable{
    protected BlockingQueue<ProducerValue> queue;
    protected ResourceMonitorGUI monitorGUI;
    protected Temporal lastTimeConsumed;

    public Consumer(BlockingQueue<ProducerValue> queue, ResourceMonitorGUI monitorGUI) {
        this.queue = queue;
        this.monitorGUI = monitorGUI;
    }
    @Override
    public void run() {
        while(true) {
            try {
                ProducerValue producerValue = queue.poll(10, TimeUnit.SECONDS);
                if (producerValue == null) break;
                this.lastTimeConsumed = Instant.now();
                if(producerValue.producer instanceof RAMProducer) {
                    if(producerValue.producerValue < 10) {
                        monitorGUI.addAlert("RAM USAGE WARNING : USAGE -> " + String.format("%,.2f", producerValue.producerValue * 100) + "%");
                    }
                } else if (producerValue.producer instanceof CPUProducer) {
                    if(producerValue.producerValue > 80) {
                        monitorGUI.addAlert("CPU USAGE WARNING : USAGE -> " + String.format("%,.2f", producerValue.producerValue) + "%");
                    }
                }
                else if (producerValue.producer instanceof DiskProducer) {
                    if(producerValue.producerValue < 20) {
                        monitorGUI.addAlert("DISK USAGE WARNING : USAGE -> " + String.format("%,.2f", producerValue.producerValue) + "%");
                    }
                }

            } catch (InterruptedException e) {
                this.lastTimeConsumed = Instant.now();
                throw new RuntimeException(e);
            }
            System.out.println("Thread count : " + Thread.activeCount());

        }

    }
}
