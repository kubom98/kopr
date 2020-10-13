package cviko06.uloha2;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class SizeSummarizer {

	public static final String START_DIR = "D:\\UPJS\\Kopr";
	public static final int THREAD_COUNT = Runtime.getRuntime().availableProcessors();

	public static void main(String[] args) throws InterruptedException, ExecutionException {
		File rootDir = new File(START_DIR);
		System.out.println("Pocet vlakien: " + THREAD_COUNT);
		ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);

		long start = System.nanoTime();
		File[] files = rootDir.listFiles();
		List<SizeTask> tasks = new ArrayList<>();
		for (int i = 0; i < files.length; i++) {
			if (files[i].isDirectory()) {
				SizeTask task = new SizeTask(files[i]);
				tasks.add(task);
			}
		}
		System.out.println("Čas: " + (System.nanoTime() - start) / 1000000 + " ms   " + "InvokeAll zacina");
		List<Future<DirSize>> futures = executor.invokeAll(tasks);
		System.out.println("Čas: " + (System.nanoTime() - start) / 1000000 + " ms   " + "InvokeAll skoncil");

		for (Future<DirSize> future : futures) {
			DirSize dirSize = future.get();
			System.out.println("Čas: " + (System.nanoTime() - start) / 1000000 + " ms   " + dirSize);
		}
		executor.shutdown();
	}
}
