package fileInterface;

import custom.file.exception.InvalidInputException;

public interface ChoiceInterface {
	public void select(String userDirectory) throws InvalidInputException ;
	public void inner_select(String userDirectory) throws InvalidInputException;
}
