package Producers;

import ResourceManager.Producer;
import ResourceManager.ProducerValue;
import ResourceManager.ResourceMonitorUtils;

import java.time.Instant;
import java.util.concurrent.BlockingQueue;

public class DiskProducer extends Producer {
    public DiskProducer(BlockingQueue<ProducerValue> queue) {
        super(queue);
    }
    @Override
    public void run() {
        try {
            queue.put(new ProducerValue(this, ResourceMonitorUtils.getFreeDiskSpace()));
            this.lastTimeProduced = Instant.now();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
