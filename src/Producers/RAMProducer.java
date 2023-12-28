package Producers;

import ResourceManager.Producer;
import ResourceManager.ProducerValue;
import ResourceManager.ResourceMonitorUtils;

import java.sql.Time;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class RAMProducer extends Producer {
    public RAMProducer(BlockingQueue<ProducerValue> queue){
        super(queue);
    }
    @Override
    public void run() {
        try {
            this.isProducing = queue.offer(new ProducerValue(this, ResourceMonitorUtils.getFreeRAM()), 10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
