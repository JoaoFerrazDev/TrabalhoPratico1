package ResourceManager;
import java.util.concurrent.BlockingQueue;

public abstract class Producer extends Thread{
    protected BlockingQueue<ProducerValue> queue;
    protected boolean isProducing;
    public Producer(BlockingQueue<ProducerValue> queue) {
        this.queue = queue;
    }
}
