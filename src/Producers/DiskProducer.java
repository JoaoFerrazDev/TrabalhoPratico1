package Producers;

import ResourceManager.Producer;
import ResourceManager.ProducerValue;
import ResourceManager.ResourceMonitorUtils;

import java.time.Instant;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class DiskProducer extends Producer implements Runnable {
    public DiskProducer(BlockingQueue<ProducerValue> queue) {
        super(queue);
    }
    @Override
    public void run() {
        try {
            boolean isOffer = queue.offer(new ProducerValue(this, ResourceMonitorUtils.getFreeDiskSpace()), 10, TimeUnit.SECONDS);
            if(!isOffer) Thread.currentThread().interrupt();
            this.lastTimeProduced = Instant.now();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
