package fileInterface;

import java.io.File;

import custom.file.exception.*;
public interface FileEditInterface {
	public void fileMaker(String directory, String fileName) throws FileNotCreatedException;
	public void recursiveLoad(File[] arr, int index, int level);
	public void deleteRecursive(File[] arr, int index, int level, String target);
	public void addFileToList(String s);
	public void printFiles();
	public boolean searchFile(String fileName);
}
