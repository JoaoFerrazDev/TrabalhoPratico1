package Producers;

import ResourceManager.Producer;
import ResourceManager.ProducerValue;
import ResourceManager.ResourceMonitorUtils;

import java.util.concurrent.BlockingQueue;

public class RAMProducer extends Producer {
    public RAMProducer(BlockingQueue<ProducerValue> queue){
        super(queue);
    }
    @Override
    public void run() {
        try {
            queue.put(new ProducerValue(this, ResourceMonitorUtils.getFreeRAM()));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
