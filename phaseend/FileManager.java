package phaseend;

import java.io.File;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import custom.file.exception.*;
import fileInterface.FileEditInterface;
import fileInterface.ChoiceInterface;

public class FileManager implements FileEditInterface, ChoiceInterface {
	static ArrayList<String> fileList = new ArrayList<>();

	public static void main(String[] args) throws InvalidInputException {

		FileManager fm = new FileManager();
		
		System.out.println("Welcome to the file Manager!");
		System.out.println(
				"This program will allow you to add,delete,search and see the files in your working directory");
		System.out.println("please enter your specified directory to store files");
		System.out.println("if you leave it blank (single space or enter )then it will use java file directory");
		boolean success = false;
		Scanner s = new Scanner(System.in);
		String dir = "";
		while (success == false) {
			dir = s.nextLine();
			if (dir.equals("") || dir.equals(null) || dir.equals(" ")) {
				dir = System.getProperty("user.dir");
				System.out.println("Your working directory is : " + System.getProperty("user.dir"));
				success = true;
			} else {
				try {
					File maindir = new File(dir);
					File filearr[] = maindir.listFiles();
					if (filearr != null) {
						System.out.println("Your working directory is : " + dir);
						success = true;
					} else {
						throw new InvalidInputException("file arrary is null!");
					}

				} catch (Exception e) {
					System.out.println(e.getMessage());
					System.out.println("please enter valid directory");
					System.out.println("please try again");
				}

			}
		}
		
		System.out.println(
				"Keep in mind this program is case insensitive. You cannot make 'Abc.txt' while 'abc.txt' exists in the filelist");

		fm.select(dir);
		s.close();
	}

	public void select(String userDirectory) throws InvalidInputException {
		Scanner s = new Scanner(System.in);

		File maindir = new File(userDirectory);
		File filearr[] = maindir.listFiles();

		int choice = 0;
		System.out
				.println("Enter '1' to display current files or '2' for more file managament(add,delete,search file) ");
		System.out.println("'3' to close application");
		boolean success = false;
		while (success == false) {
			try {
				String uinput = s.nextLine();
				if (uinput.length() == 1) {
					choice = Character.getNumericValue((uinput.charAt(0)));
					success = true;
				
				} else {
					throw new InvalidInputException("invalid input exception thrown!");
				}

			} catch (Exception e) {
				System.out.println(e.getMessage());
				System.out.println("wrong input!");
				System.out.println("try again");
				System.out.println(
						"Enter '1' to display current files or '2' for more file managament(add,delete,search file) ");
				System.out.println("'3' to close application");

			}
		}
		switch (choice) {
		case 1:
			recursiveLoad(filearr, 0, 0);
			printFiles();
			select(userDirectory);
			break;
		case 2:
			inner_select(userDirectory);
			break;
		case 3:
			System.out.println("ending a program");
			System.exit(0);
		default:
			System.out.println("your input is not a valid choice!");
			select(userDirectory);
		}
		s.close();

	}

	public void inner_select(String userDirectory) throws InvalidInputException {
		File maindir = new File(userDirectory);
		File filearr[] = maindir.listFiles();
		String directory = userDirectory;

		Scanner s = new Scanner(System.in);
		int choice = 0;

		System.out.println(
				"Enter '1' to add files or '2' to delete specific file or '3' to search for specific file or '4' to go back to main choice");
		boolean success = false;

		while (success == false) {
			try {
				String uinput = s.nextLine();
				if (uinput.length() == 1) {
					choice = Character.getNumericValue(uinput.charAt(0));
					success = true;
		
				} else {
					throw new InvalidInputException("invalid input exception thrown!");
				}
			} catch (Exception e) { // really hard to reach this exception
				System.out.println("wrong input!");
				System.out.println("try again");
				System.out.println(
						"Enter '1' to add files or '2' to delete specific file or '3' to search for specific file or '4' to go back to main choice ");
			}
		}
		switch (choice) {
		case 1:
			recursiveLoad(filearr, 0, 0);
			System.out.println("please enter your file name with extention!");

			String fileName = s.nextLine();
			try {
				fileMaker(directory, fileName);

			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
			select(userDirectory);
			break;
		case 2:
			System.out.println("enter file name (please include extension) to delete file (case insensitive)");
			String del_file = s.nextLine();
			recursiveLoad(filearr, 0, 0);
			if (searchFile(del_file)) {
				System.out.println("file found!");
				System.out.println("deleting file");
				deleteRecursive(filearr, 0, 0, del_file);
			} else {
				System.out.println("file not found!");
				System.out.println("returning to main menu");
				select(userDirectory);
				break;
			}

			select(userDirectory);
			break;
		case 3:
			System.out.println("enter file name (please include extension) to search file (case insensitive)");
			recursiveLoad(filearr, 0, 0);
			String sear_file = s.nextLine();
			if (searchFile(sear_file)) {
				System.out.println("file found!");
			} else {
				System.out.println("file not found!");
			}
			select(userDirectory);
			break;
		case 4:
			System.out.println("going back to main choice");
			select(userDirectory);
			break;
		default:
			System.out.println("invalid choice!");
			System.out.println("returning to prompt!");
			select(userDirectory);
			break;
		}
		s.close();
	}

	public void fileMaker(String directory, String fileName) throws FileNotCreatedException {
		for (String s : fileList) {
			if (fileName.equalsIgnoreCase(s)) {
				System.out.println("file already exists!");
				System.out.println("file not created");
				return;
			}
		}
		File dir = new File(directory);

		if (!dir.exists()) {
			dir.mkdir();
		}
		File tagFile = new File(dir, fileName);
		if (!tagFile.exists()) {
			try {
				if (tagFile.createNewFile()) {
					//
				} else {
					throw new FileNotCreatedException("file not created!");
				}
			} catch (Exception e) {
				System.out.println(e.getMessage());
				return;
			}
		}
		System.out.println("file created : " + fileName);

	}

	public void recursiveLoad(File[] arr, int index, int level) {
		if (index == arr.length)
			return;

		if (arr[index].isFile()) {
	
			addFileToList(arr[index].getName());
		} else if (arr[index].isDirectory()) {

			recursiveLoad(arr[index].listFiles(), 0, level + 1);
		}
		recursiveLoad(arr, ++index, level);

	}

	public void deleteRecursive(File[] arr, int index, int level, String targetName) {
		if (index == arr.length)
			return;
		if (arr[index].isFile()) {
			if (arr[index].getName().equals(targetName)) {
				File targetFile = arr[index];
				if (targetFile.delete()) {
					System.out.println("file deleted");
					if (fileList.contains(targetFile.getName())) {
						fileList.remove(targetFile.getName());
						return;
					} else {
						System.out.println("you should not see this. Possible bug on arraylist");
						return;
					}
				} else {
					System.out.println("file was not deleted");
					return;
				}
			}
		} else if (arr[index].isDirectory()) {

			deleteRecursive(arr[index].listFiles(), 0, level + 1, targetName);
		}
		deleteRecursive(arr, ++index, level, targetName);
	}

	public void addFileToList(String s) {

		for (String fileName : fileList) {
			if (fileName.equalsIgnoreCase(s)) {
				return;
			}
		}
		fileList.add(s);
	}

	public void printFiles() {
		Collections.sort(fileList);

		System.out.println("printing files");
		System.out.println("--------------");
		if (fileList.size() == 0) {
			System.out.println("no files in directory!");
			return;
		}
		for (String s : fileList)
			System.out.println(s);
		System.out.println("--------------");
	}

	public boolean searchFile(String fileName) {
		for (String s : fileList) {
			if (s.equalsIgnoreCase(fileName)) {
				return true;
			}
		}
		return false;

	}

}
