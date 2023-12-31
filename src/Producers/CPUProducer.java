package Producers;

import ResourceManager.Producer;
import ResourceManager.ProducerValue;
import ResourceManager.ResourceMonitorUtils;

import java.time.Instant;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class CPUProducer extends Producer implements Runnable  {

    public CPUProducer(BlockingQueue<ProducerValue> queue) {
        super(queue);
    }
    @Override
    public void run() {
        try {
            boolean isOffer = queue.offer(new ProducerValue(this, ResourceMonitorUtils.getCpuLoad()), 10, TimeUnit.SECONDS);
            if(!isOffer) Thread.currentThread().interrupt();
            this.lastTimeProduced = Instant.now();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
