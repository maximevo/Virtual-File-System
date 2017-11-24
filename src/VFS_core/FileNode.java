package VFS_core;

import java.io.Serializable;

public class FileNode extends Node implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2098471306122172207L;
	private VFSFile file;
	
	public FileNode(DirectoryNode parent, String name, VFSFile file) throws SameNameException {
		super(parent,name);
		this.file = file;
		//otherwise, the creation of the root which has a null parent will lead to a RuntimeError
		if(!(parent==null)) {
			boolean result = false;
			for(Node node : parent.sonNodes) {
				if (node.getName().equals(name) && node.getType().equals("f")) {
					result = true;
				} 
			}
			if(result) {
				throw new SameNameException();
			}
			parent.sonNodes.add(this);
		}	
	}
	
	public String getType() {
		return "f";
	}
	
	public int getSize(){
		return file.getSize();
		
	}
	
	public VFSFile getVFSFile() {
		return file;
	}
}