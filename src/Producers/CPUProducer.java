package Producers;

import ResourceManager.Producer;
import ResourceManager.ProducerValue;
import ResourceManager.ResourceMonitorUtils;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class CPUProducer extends Producer {

    public CPUProducer(BlockingQueue<ProducerValue> queue) {
        super(queue);
    }
    @Override
    public void run() {
        try {
            this.isProducing = queue.offer(new ProducerValue(this, ResourceMonitorUtils.getCpuLoad()), 10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
