package cviko08.uloha1;

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
