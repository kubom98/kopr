package cviko07.uloha2_ForJoinPool;

import java.util.List;
import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;

import cviko07.uloha2.DirectoryForbiddenException;

public class SizeSummarizer {

	public static final String START_DIR = "C:\\windows\\";
	public static final int THREAD_COUNT = Runtime.getRuntime().availableProcessors();

	public static void main(String[] args) throws InterruptedException, ExecutionException {
		File rootDir = new File(START_DIR);
		System.out.println("Pocet vlakien: " + THREAD_COUNT);
		ForkJoinPool executor = new ForkJoinPool();

		long start = System.nanoTime();
		File[] files = rootDir.listFiles();
		List<SizeTask> tasks = new ArrayList<>();
		for (int i = 0; i < files.length; i++) {
			if (files[i].isDirectory()) {
				SizeTask task = new SizeTask(files[i]);
				executor.submit(task);
				tasks.add(task);
			}
		}
		for (SizeTask task : tasks) {
			try {
				DirSize dirSize = task.join();
				System.out.println("Čas: " + (System.nanoTime() - start) / 1000000 + " ms   " + dirSize);
			} catch (DirectoryForbiddenException e) {
				System.err.println("Čas: " + (System.nanoTime() - start) / 1000000 + " ms   " + "Adresar "
						+ e.getDir().getAbsolutePath() + "nas nepusti.");
			}
		}
		executor.shutdown();
	}
}
