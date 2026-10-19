import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class AtomicCounter {

	// AtomicInteger даёт атомарные операции.
	private static final AtomicInteger counter = new AtomicInteger();

	public static void main(String[] args) throws Exception {
		int threads = 8;
		int iterations = 1_000_000;
		long expected = (long) threads * iterations;

		ExecutorService pool = Executors.newFixedThreadPool(threads);
		CountDownLatch done = new CountDownLatch(threads);

		for (int t = 0; t < threads; t++) {
			pool.submit(() -> {
				for (int i = 0; i < iterations; i++) {
					// Атомарная операция увеличения.
					counter.incrementAndGet();
				}
				done.countDown();
			});
		}

		done.await();
		pool.shutdown();

		System.out.println("AtomicCounter: safe shared AtomicInteger");
		System.out.println("Expected: " + expected);
		System.out.println("Actual:   " + counter.get());
	}
}