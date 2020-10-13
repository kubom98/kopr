package cviko07.uloha2_ForJoinPool;

import java.io.File;

public class DirectoryForbiddenException extends RuntimeException {

	private File dir;

	public DirectoryForbiddenException(File dir) {
		this.dir = dir;
	}

	public File getDir() {
		return dir;
	}
}
