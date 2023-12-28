package Producers;

import ResourceManager.Producer;
import ResourceManager.ProducerValue;
import ResourceManager.ResourceMonitorUtils;

import java.util.concurrent.BlockingQueue;

public class CPUProducer extends Producer {
    public CPUProducer(BlockingQueue<ProducerValue> queue) {
        super(queue);
    }
    @Override
    public void run() {
        try {
            queue.put(new ProducerValue(this, ResourceMonitorUtils.getCpuLoad()));
            System.out.println(queue.take().producer instanceof CPUProducer);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
