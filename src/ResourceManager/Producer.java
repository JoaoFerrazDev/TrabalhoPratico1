package ResourceManager;
import java.time.temporal.Temporal;
import java.util.concurrent.BlockingQueue;

public abstract class Producer extends Thread{
    protected BlockingQueue<ProducerValue> queue;
    protected Temporal lastTimeProduced;
    public Producer(BlockingQueue<ProducerValue> queue) {
        this.queue = queue;
    }
}
