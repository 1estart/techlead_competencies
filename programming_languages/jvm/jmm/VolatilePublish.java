public class VolatilePublish {

	private static int number;

	// volatile даёт гарантию видимости и создаёт happens-before:
	// запись до volatile-записи видна потоку, который увидел эту volatile-запись.
	private static volatile boolean ready = false;

	public static void main(String[] args) throws Exception {
		Thread reader = new Thread(() -> {
			while (!ready) {
				Thread.yield();
			}

			// Благодаря volatile ready мы ожидаем увидеть number = 42.
			System.out.println("VolatilePublish: number = " + number);
		});

		Thread writer = new Thread(() -> {
			number = 42;
			ready = true;
		});

		reader.start();
		writer.start();

		reader.join();
		writer.join();

		System.out.println("VolatilePublish: finished");
	}
}