package cviko07.uloha1;

import java.util.List;
import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.RecursiveTask;

public class SizeTask extends RecursiveTask<DirSize> {

	private File dir;

	public SizeTask(File dir) {
		this.dir = dir;
	}

	@Override
	public DirSize compute() {
		return new DirSize(dir, analyzeDir(dir));
	}

	private long analyzeDir(File dir) {
		long sumSize = 0;
		File[] files = dir.listFiles();
		List<SizeTask> tasks = new ArrayList<>();
		for (int i = 0; i < files.length; i++) {
			if (files[i].isFile())
				sumSize += files[i].length();
			if (files[i].isDirectory()) {
				SizeTask task = new SizeTask(files[i]);
				task.fork();
				tasks.add(task);
			}
		}
		for (SizeTask task : tasks) {
			sumSize += task.join().getSize();
		}
		return sumSize;
	}
}
