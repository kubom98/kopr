package cviko08.uloha1;

import java.util.List;
import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class SizeSummarizerTask implements Runnable {

	public static final String START_DIR = "C:\\windows\\";
	public static final int THREAD_COUNT = Runtime.getRuntime().availableProcessors();

	@Override
	public void run() {
		File rootDir = new File(START_DIR);
		System.out.println("Pocet vlakien: " + THREAD_COUNT);
		ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);

		long start = System.nanoTime();
		File[] files = rootDir.listFiles();
		List<Future<DirSize>> futures = new ArrayList<Future<DirSize>>();
		for (int i = 0; i < files.length; i++) {
			if (files[i].isDirectory()) {
				SizeTask task = new SizeTask(files[i]);
				Future<DirSize> future = executor.submit(task);
				futures.add(future);
			}
		}
		int i = 0;
		try {
			for (; i < futures.size(); i++) {
				Future<DirSize> future = futures.get(i);
				try {
					DirSize dirSize = future.get();
					System.out.println("Čas: " + (System.nanoTime() - start) / 1000000 + " ms   " + dirSize);
				} catch (ExecutionException e) {
					if (e.getCause() instanceof DirectoryForbiddenException) {
						DirectoryForbiddenException dfe = (DirectoryForbiddenException) e.getCause();
						System.err.println("Čas: " + (System.nanoTime() - start) / 1000000 + " ms   " + "Adresar "
								+ dfe.getDir().getAbsolutePath() + "nas nepusti.");
					}
				}
			}
		} catch (InterruptedException e) {
			System.err.println("SizeSummarizer prerusil ulohu SizeSumarizerTask");
			List<Runnable> notStartedTasks = executor.shutdownNow();
			try {
				executor.awaitTermination(1, TimeUnit.DAYS);
			} catch (InterruptedException ee) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for (; i < futures.size(); i++) {
				Future<DirSize> future = futures.get(i);
				try {
					if (future.isDone()) {
						DirSize dirSize = future.get();
						System.out.println("Čas: " + (System.nanoTime() - start) / 1000000 + " ms   " + dirSize);
					} else {
						System.out.println("Uloha nezacata");
					}
				} catch (InterruptedException ignored) {
					// nenastane
				} catch (ExecutionException e1) {
					if (e1.getCause() instanceof DirectoryForbiddenException) {
						DirectoryForbiddenException dfe = (DirectoryForbiddenException) e.getCause();
						System.err.println("Čas: " + (System.nanoTime() - start) / 1000000 + " ms   " + "Adresar "
								+ dfe.getDir().getAbsolutePath() + " nas nepusti.");
					}
					if (e1.getCause() instanceof SizeTaskInterruptedException) {
						SizeTaskInterruptedException stie = (SizeTaskInterruptedException) e1.getCause();
						System.err.println("Čas: " + (System.nanoTime() - start) / 1000000 + " ms   "
								+ "Ciastocny adresar " + stie.getDirSize());
					}
				}
			}
			Thread.interrupted();
		}
		executor.shutdown();
		try {
			executor.awaitTermination(1, TimeUnit.DAYS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
