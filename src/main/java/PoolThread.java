import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class PoolThread {

    private ExecutorService service;
    private static PoolThread poolThread;

    private PoolThread() {

        service = Executors.newCachedThreadPool();
    }

    public static PoolThread instans() {
        if (poolThread == null) {
            poolThread = new PoolThread();
        }
        return poolThread;
    }

    public Future submit(Runnable runnable) {
        return service.submit(runnable);
    }
}