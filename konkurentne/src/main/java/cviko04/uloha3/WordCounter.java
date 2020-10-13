package cviko04.uloha3;

import java.io.File;
import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;

public class WordCounter {

	public static final String START_DIR = "D:\\UPJS\\2.Rocnik\\ASU";
	public static final int ANALYZERS_COUNT = 4;
	public static final int SEARCHERS_COUNT = 4;

	public static void main(String[] args) throws InterruptedException {
		File dir = new File(START_DIR);
		BlockingQueue<File> filesToAnalyze = new LinkedBlockingQueue<File>();
		ConcurrentMap<String, Integer> words = new ConcurrentHashMap<String, Integer>();
		long start = System.nanoTime();

		Searcher searcher = new Searcher(dir, filesToAnalyze);
		for (int i = 0; i < SEARCHERS_COUNT; i++) {
			Thread searcherWorker = new Thread(searcher);
			searcherWorker.start();
		}

		CountDownLatch gate = new CountDownLatch(ANALYZERS_COUNT);
		FileAnalyzer a = new FileAnalyzer(filesToAnalyze, words, gate);
		for (int i = 0; i < ANALYZERS_COUNT; i++) {
			Thread analyzerWorker = new Thread(a);
			analyzerWorker.start();
		}
		gate.await();
		System.out.println("Running time: " + (System.nanoTime() - start) / 1000000.0 + " ms");
		printTop20Words(words);
	}

	private static void printTop20Words(Map<String, Integer> words) {
		PriorityQueue<Map.Entry<String, Integer>> sortedWords = new PriorityQueue<Map.Entry<String, Integer>>(
				words.size(), new Comparator<Map.Entry<String, Integer>>() {

					public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
						return o2.getValue().compareTo(o1.getValue());
					}
				});

		for (Entry<String, Integer> entry : words.entrySet()) {
			sortedWords.add(entry);
		}
		int min = Math.min(20, sortedWords.size());

		for (int i = 0; i < min; i++) {
			System.out.print(i + ": " + sortedWords.poll() + ", ");
		}
		System.out.println();
	}

}
