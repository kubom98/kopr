package cviko04.uloha3;

import java.io.File;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CountDownLatch;

public class FileAnalyzer implements Runnable {

	private final BlockingQueue<File> filesToAnalyze;
	private final ConcurrentMap<String, Integer> words;
	private CountDownLatch gate;

	public FileAnalyzer(BlockingQueue<File> filesToAnalyze, ConcurrentMap<String, Integer> words, CountDownLatch gate) {
		this.filesToAnalyze = filesToAnalyze;
		this.words = words;
		this.gate = gate;
	}

	public void run() {
		try {
			File file = filesToAnalyze.take();
			while (file != Searcher.POISON_PILL) {
				try (Scanner scanner = new Scanner(file)) {
					while (scanner.hasNext()) {
						String word = scanner.next();
						words.merge(word, 1, (originalValue, newValue) -> originalValue + newValue);

						// synchronized (this) {
						// Integer count = words.get(word);
						// if (count == null) {
						// count = 1;
						// } else {
						// count++;
						// }
						// words.put(word, count);
						// }

					}

				} catch (Exception consumed) {
				}
				file = filesToAnalyze.take();
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			gate.countDown();
		}
	}
}
