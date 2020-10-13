package cviko06.uloha5;

import java.util.List;
import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class SizeTask implements Callable<DirSize> {

	private File dir;
	private ExecutorService executor;

	public SizeTask(File dir, ExecutorService executor) {
		this.dir = dir;
		this.executor = executor;
	}

	@Override
	public DirSize call() throws Exception {

		return new DirSize(dir, analyzeDir(dir));
	}

	private long analyzeDir(File dir) throws InterruptedException, ExecutionException {
		long sumSize = 0;
		File[] files = dir.listFiles();
		List<SizeTask> tasks = new ArrayList<>();
		for (int i = 0; i < files.length; i++) {
			if (files[i].isFile())
				sumSize += files[i].length();
			if (files[i].isDirectory()) {
				SizeTask task = new SizeTask(files[i], executor);
				tasks.add(task);
			}
		}
		List<Future<DirSize>> all = executor.invokeAll(tasks);
		for (Future<DirSize> future : all) {
			sumSize += future.get().getSize();
		}
		return sumSize;
	}
}
