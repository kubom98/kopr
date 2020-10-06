package cviko04.uloha3;

import java.io.File;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Searcher implements Runnable {

	public static final File POISON_PILL = new File("poison.pill");

	private File rootDir;
	private BlockingQueue<File> queue;
	private BlockingQueue<File> unprocessedDirs;
	private AtomicInteger dirCounter;

	public Searcher(File rootDir, BlockingQueue<File> queue) {
		this.rootDir = rootDir;
		this.queue = queue;
		this.unprocessedDirs = new LinkedBlockingQueue<>();
		dirCounter = new AtomicInteger(1);
		unprocessedDirs.offer(rootDir);
	}

	public void run() {
		try {
			while (true) {
				File dir = unprocessedDirs.take();
				if (dir == POISON_PILL) {
					break;
				}
				search(dir.listFiles());
				int count = dirCounter.decrementAndGet();
				if (count == 0) {
					for (int i = 0; i < WordCounter.SEARCHERS_COUNT; i++)
						unprocessedDirs.offer(POISON_PILL);
					for (int i = 0; i < WordCounter.ANALYZERS_COUNT; i++)
						queue.offer(POISON_PILL);
				}
			}

			for (int i = 0; i < WordCounter.ANALYZERS_COUNT; i++) {
				queue.offer(POISON_PILL);
			}

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void search(File[] dir) {
		for (int i = 0; i < dir.length; i++) {
			if (dir[i].isDirectory()) {
				dirCounter.incrementAndGet();
				unprocessedDirs.offer(dir[i]);
			} else {
				if (dir[i].getName().endsWith(".java")) {
					queue.offer(dir[i]);
				}
			}
		}
	}
}
