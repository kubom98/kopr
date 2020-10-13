package cviko06.uloha3;

import java.util.List;
import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
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
		CompletionService<DirSize> completionService = new ExecutorCompletionService<>(executor);

		long start = System.nanoTime();
		File[] files = rootDir.listFiles();
		int dirCount = 0;
		for (int i = 0; i < files.length; i++) {
			if (files[i].isDirectory()) {
				SizeTask task = new SizeTask(files[i]);
				completionService.submit(task);
				dirCount++;
			}
		}
		for (int i = 0; i < dirCount; i++) {
			DirSize dirSize = completionService.take().get();
			System.out.println("Čas: " + (System.nanoTime() - start) / 1000000 + " ms   " + dirSize);
		}
		executor.shutdown();
	}
}
