import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CounterRace {

	// Специально небезопасный общий изменяемый счётчик.
	// Несколько потоков делают counter++ без синхронизации.
	private static int counter = 0;

	public static void main(String[] args) throws Exception {
		int threads = 8;
		int iterations = 1_000_000;
		long expected = (long) threads * iterations;

		ExecutorService pool = Executors.newFixedThreadPool(threads);
		CountDownLatch done = new CountDownLatch(threads);

		for (int t = 0; t < threads; t++) {
			pool.submit(() -> {
				for (int i = 0; i < iterations; i++) {
					// counter++ выглядит простой операцией,
					// но на самом деле это:
					// 1) прочитать значение;
					// 2) увеличить;
					// 3) записать обратно.
					//
					// Эти шаги не атомарны.
					counter++;
				}
				done.countDown();
			});
		}

		done.await();
		pool.shutdown();

		System.out.println("CounterRace: unsafe shared int counter");
		System.out.println("Expected: " + expected);
		System.out.println("Actual:   " + counter);

		if (counter < expected) {
			System.out.println("Lost updates: " + (expected - counter));
		} else {
			System.out.println("No loss observed this time, but the code is still unsafe.");
		}
	}
}