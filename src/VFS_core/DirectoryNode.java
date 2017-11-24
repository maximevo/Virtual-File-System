package VFS_core;

import java.io.Serializable;
import java.util.ArrayList;

public class DirectoryNode extends Node implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1544769089758445921L;
	public ArrayList<Node> sonNodes;
	
	public DirectoryNode(DirectoryNode parent, String name) throws SameNameException {
		super(parent,name);
		sonNodes = new ArrayList<Node>();
		//otherwise, the creation of the root which has a null parent will lead to a RuntimeError
		if(!(parent==null)) {
			boolean result = false;
			for(Node node : parent.sonNodes) {
				if (node.getName().equals(name) && node.getType().equals("d")) {
					result = true;
				} 
			}
			if(result) {
				throw new SameNameException();
			}
			parent.sonNodes.add(this);
		}	
	}

	public void addNode(Node [] nodes) throws SameNameException{
		for (Node node : nodes){
			addNode(node);			
		}
	}

	public void addNode (Node node) throws SameNameException{
		if(node.checkName(this)) {
			throw new SameNameException();
		}
		node.setParentDirectory(this);
		sonNodes.add(node);
	}
	
	
	public void removeNode(Node [] nodes) {
		for (Node node : nodes){
			sonNodes.remove(node);
		}
	}
	
	public void removeNode(Node node) {
		sonNodes.remove(node);
	}
	
	public String getType() {
		return "d";
	}
	
	public int getSize() {
		int returnValue = 0;
		for (Node node : sonNodes){
			returnValue += node.getSize();  
		}								
		return returnValue;
	}
	
	public ArrayList<FileNode> searchFile (String name){  // sera utilié dans la classe VFS
		ArrayList<FileNode> result = new ArrayList<FileNode>();	// pour implémenter un search
		for (Node node : sonNodes) {
			if  (node instanceof FileNode && node.getName().equals(name)) {
				result.add((FileNode)node);
			}	else if (node instanceof DirectoryNode) {
				result.addAll(((DirectoryNode)node).searchFile(name));
			} // JAVA ne fait rien si c'est un File, mais qui n'a pas le bon nom
		}
		return result;  /// s'il n'a rien trouvé, il rend un tableau vide
	}

	public ArrayList<DirectoryNode> searchDirectory (String name){ 
		ArrayList<DirectoryNode> result = new ArrayList<DirectoryNode>();		
		for (Node node : sonNodes) {
			if  (node instanceof DirectoryNode) {
				if(node.getName().equals(name)) {
					result.add((DirectoryNode)node);
					result.addAll(((DirectoryNode)node).searchDirectory(name));
				} else {
					result.addAll(((DirectoryNode)node).searchDirectory(name));
				}
			}
		}
		return result;
	}
	
	public ArrayList<Node> search (String name) {
		ArrayList<Node> result = new ArrayList<Node>();
		result.addAll(searchFile(name));
		result.addAll(searchDirectory(name));
		return result;
	}
	
}