package VFS_core;

import java.io.File;
import java.io.Serializable;

public abstract class Node implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3741585595618819900L;
	private DirectoryNode parentDirectory; //null if it's the root
	private String name;
	
	// getters and setters (Name, parentDirectory) 
	
	public String getName(){
		return name;
	}
	
	public void setName(String name) throws SameNameException {
		for(Node node : parentDirectory.sonNodes) {
			if (node.getName().equals(name) && node.getType().equals(getType())) {
				throw new SameNameException();
			} 
		}
		this.name=name;
	}

	public DirectoryNode getParentDirectory() {
		return parentDirectory;
	}

// no need to throw file not found exception as it is the method calling 
// the setParentDirectory that will check if the new directory exists or not
	
	public void setParentDirectory(DirectoryNode parentDirectory) throws SameNameException{
		if(checkName(parentDirectory)) {
				throw new SameNameException();
		}
		this.parentDirectory = parentDirectory;
	}
	
	/////////////////////////////////////////////////////////////////////////
	
	public abstract String getType(); //this just provides a generic method node.getType()
									 // useful when using node arrays

	public Node(DirectoryNode parentDirectory, String name){
		super();
		this.parentDirectory = parentDirectory; // it is a facultative line, as the initialization of this attribute is taken care of in the addNode method
		this.name = name;
	}

	/**
	 * 
	 * @param directory in which you want to verify the existence of a same node
	 * @return true if there is a same Node with same name, false otherwise
	 */
	public boolean checkName(DirectoryNode directory) {
		boolean result = false;
		for(Node node : directory.sonNodes) {
			if (node.getName().equals(name) && node.getType().equals(getType()) ) {
				result = true;
			} 
		}
		return result;
	}
	
	public abstract int getSize();

	public String getPath() {  /// la commande find dans le CLUI retourne le path du file
		String path = name;
		if (parentDirectory != null) {
			path = File.separator+path;
			return parentDirectory.getPath()+path;
		} else {
			return path;
		}



	}
}