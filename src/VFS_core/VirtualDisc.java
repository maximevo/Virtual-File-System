package VFS_core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Scanner;

public class VirtualDisc implements Serializable{  

	/**
	 * 
	 */
	private static final long serialVersionUID = 6849119274708712674L;
	private String name;
	private final int size; 
	public String pathName; 
	public DirectoryNode root; // a instancier avec le constructeur, avec
	// un parent node = null


	////////\\\\\\\\
	// constructor
	public VirtualDisc(String name, int size) throws SameNameException{
		super();
		this.name = name;
		this.size = size;
		this.root = new DirectoryNode(null, name);// on crée un objet de type directoryNode qui est la root: parent folder = null, name = nom du vfs
	}

	// getters and setters

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	} 

	public int getSize() {
		return size;
	}
	
	public void setPathName(String newPathName) {
		this.pathName = newPathName;
	}
	
	public String getPathName() {
		return pathName;   //the absolute path in the host file system where is the .ser
	}
	
	//retourne free space sur un vfs. Attention à bien implémenter la méthode free space sur un VD
	
	public int freeSpace() {
		return (this.size - this.root.getSize());
	}
	//
	public ArrayList<FileNode> searchFile (String directoryPath, String name) throws FileNotFoundException{
		DirectoryNode directory = pathToDirectory(directoryPath);
		return directory.searchFile(name);
	}
	
	public ArrayList<DirectoryNode> searchDirectory (String directoryPath, String name) throws FileNotFoundException{
		DirectoryNode directory = pathToDirectory(directoryPath);
		return directory.searchDirectory(name);
	}
	
	public ArrayList<Node> search (String directoryPath, String name) throws FileNotFoundException{
		DirectoryNode directory = pathToDirectory(directoryPath);
		return directory.search(name);
	}
	
	public void renameFile(String path, String name) throws FileNotFoundException,SameNameException{
		FileNode fileNode = pathToFile(path);
		fileNode.setName(name);
	}

	public void renameDirectory(String path, String name) throws FileNotFoundException,SameNameException{
		DirectoryNode directoryNode = pathToDirectory(path);
		directoryNode.setName(name);
	}
	
	public void moveFile (String oldPath, String newPath) throws SameNameException, FileNotFoundException{
		FileNode fileToMove = pathToFile(oldPath);
		DirectoryNode targetDirectory = pathToDirectory(newPath);
		fileToMove.getParentDirectory().removeNode(fileToMove);
		targetDirectory.addNode(fileToMove);
	}
	
	//we need separate method as a file and a directory can share the same name and path
	
	public void moveDirectory (String oldPath, String newPath) throws SameNameException, 
	FileNotFoundException, IllegalArgumentException { 
		DirectoryNode directoryToMove = pathToDirectory(oldPath);
		DirectoryNode targetDirectory = pathToDirectory(newPath);
		
		if (oldPath.equals(newPath)) { throw new IllegalArgumentException();}
		
		directoryToMove.getParentDirectory().removeNode(directoryToMove);
		targetDirectory.addNode(directoryToMove);
	}
		
	public void createDirectory (String targetPath, String name) throws FileNotFoundException, SameNameException {
		DirectoryNode parent = pathToDirectory(targetPath);
		DirectoryNode Dnode = new DirectoryNode(parent,name);
	}
	
	public void copyFile(String path, String targetPath) throws FileNotFoundException, NotEnoughSpaceException, SameNameException{
	
		FileNode FileToCopy = pathToFile(path);
		int sizeToCopy = FileToCopy.getSize();
		DirectoryNode targetParent = pathToDirectory(targetPath);
		
		// on teste s'il y a la place de le copier
		if (sizeToCopy <= this.freeSpace()){
			if(FileToCopy.checkName(targetParent)) {
				throw new SameNameException();
			}
			FileNode copy = new FileNode(targetParent, FileToCopy.getName(), FileToCopy.getVFSFile());
			//targetParent.addNode(copy); would be redundant and even throw a SameNameException because in the constructor of node, the addition into the parent is taken care of
		}else {throw new NotEnoughSpaceException();}
	}

	public void copyDirectory(String path, String targetPath) throws FileNotFoundException, NotEnoughSpaceException, SameNameException{

		DirectoryNode DirectoryToCopy = pathToDirectory(path);
		int sizeToCopy = DirectoryToCopy.getSize();
		DirectoryNode targetParent = pathToDirectory(targetPath);

		// on teste s'il y a la place de le copier
		if (sizeToCopy <= this.freeSpace()){
			if(DirectoryToCopy.checkName(targetParent)) {
				throw new SameNameException();
			}

			DirectoryNode copy = new DirectoryNode(targetParent, DirectoryToCopy.getName());

			// coeur de la récurrence
			if (!DirectoryToCopy.sonNodes.isEmpty()) { 
				for(Node node:DirectoryToCopy.sonNodes) {
					if (node instanceof FileNode) {
						copyFile(node.getPath(), copy.getPath());
					}else { copyDirectory(node.getPath(),copy.getPath());}
				}
			}
		}else {throw new NotEnoughSpaceException();}
	}

	public void removeFile(String path) throws FileNotFoundException {
		FileNode file = pathToFile(path);
		file.getParentDirectory().removeNode(file);
	}
	
	public void removeDirectory (String path) throws FileNotFoundException {
		DirectoryNode directory = pathToDirectory(path);
		directory.getParentDirectory().removeNode(directory);
	}
	
	public FileNode pathToFile(String path) throws FileNotFoundException{
		// WARNING: On Windows, separator of a path is "\". But \ is used to escape a sequence: we cannot use split method with \.
		// So we discuss the cas if File.separator is "\" or not
		String [] nodesName;
		String separator = File.separator;
		if (separator.equals("\\")){
			nodesName = path.split(File.separator + File.separator);
		}else{nodesName = path.split(File.separator);}

		if(nodesName.length <= 1){throw new FileNotFoundException();}
		DirectoryNode currentDirectory = root;
		//we start at 1 as the first element of the split is the VD's name
		for (int i=1; i<nodesName.length-1;i++) {
			boolean ispresent = false;

			//we search until the potential parent directory

			for(Node node : currentDirectory.sonNodes) {
				if (node.getName().equals(nodesName[i]) && node instanceof DirectoryNode ) {
					ispresent = true;
					currentDirectory = (DirectoryNode)node;
					break;
				}
			}		
			if(!ispresent) {throw new FileNotFoundException();}
		}
		// we search now in the sons of the parent directory for the potential file

		for(Node node : currentDirectory.sonNodes) {
			if (node.getName().equals(nodesName[nodesName.length-1]) && node instanceof FileNode ) {
				return (FileNode)node;
			}
		}
		throw new FileNotFoundException();
	}

	public DirectoryNode pathToDirectory(String path) throws FileNotFoundException{
		// WARNING: On Windows, separator of a path is "\". But \ is used to escape a sequence: we cannot use split method with \.
		// So we discuss the cas if File.separator is "\" or not
		String [] nodesName;
		String separator = File.separator;
		if (separator.equals("\\")){
			nodesName = path.split(File.separator + File.separator);
		}else{nodesName = path.split(File.separator);}

		if(nodesName.length == 1 && !nodesName[0].equals(name)){throw new FileNotFoundException();}
		else if (nodesName.length == 0) {throw new FileNotFoundException();}
		if (!nodesName[0].equals(name)) {throw new FileNotFoundException();}
		else {
			DirectoryNode currentDirectory = root;
			for (int i=1; i<nodesName.length;i++) {
				boolean ispresent = false;
				for(Node node : currentDirectory.sonNodes) {
					if (node.getName().equals(nodesName[i]) && node instanceof DirectoryNode ) {
						currentDirectory = (DirectoryNode)node;
						ispresent = true;
						break;
					}
				}		
				if(!ispresent) {throw new FileNotFoundException();}
			}
			return currentDirectory;
		}
	}

/**
 * 
 * @param hostpath the absolute path into the host file system of the file to import 
 * @param VFSPath the path of the DIRECTORY you want to export the file into.
 * @throws FileNotFoundException
 * @throws NotEnoughSpaceException
 * @throws SameNameException
 */

	public void importFile (String hostpath, String VFSPath) throws FileNotFoundException, NotEnoughSpaceException, SameNameException{
		DirectoryNode targetDirectory = pathToDirectory(VFSPath);
		FileInputStream reader = null;
		File hostFile = new File(hostpath);

		if (hostFile.length() > freeSpace()) {
			throw new NotEnoughSpaceException();
		}
		try {
			//this throws FileNotFoundExcception if hostpath is not a file or doesn't exists
			reader = new FileInputStream(hostpath); 
			int i = 0;
			int currentByte;
			byte [] file = new byte[(int) hostFile.length()];
			//reading the file byte by byte
			while ((currentByte=reader.read())!=-1) {
				file[i] = (byte)currentByte;
				i++;
			}
			VFSFile vfsfile = new VFSFile(file);
			FileNode fn = new FileNode(targetDirectory,hostFile.getName(),vfsfile);
			// by creating the filenode, it is already added into the targetDirectory, with everything taken care of. No need for further use of the created file node
		}  catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (reader != null)
					reader.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

	}

	/**
	 * 
	 * @param hostpath the absolute path into the host file system of the directory to import 
	 * @param VFSPath the path of the DIRECTORY you want to export the directory into.
	 * @throws FileNotFoundException
	 * @throws NotEnoughSpaceException
	 * @throws SameNameException
	 */	
	public void importDirectory (String hostpath, String VFSPath) throws FileNotFoundException, NotEnoughSpaceException, SameNameException {
		DirectoryNode targetDirectory = pathToDirectory(VFSPath);
		File hostDirectory = new File(hostpath);

		if (!(hostDirectory.exists())) {
			throw new FileNotFoundException();
		}
		if (!(hostDirectory.isDirectory())) {
			throw new FileNotFoundException();
		}
		if (hostDirectory.length() > freeSpace()) {
			throw new NotEnoughSpaceException();
		}	
		//constructor of class node can throw SameNameException
		DirectoryNode importedDirectory = new DirectoryNode(targetDirectory, hostDirectory.getName());
		for (File file : hostDirectory.listFiles()) {
			if (file.isFile()) {
				importFile(file.getAbsolutePath(),importedDirectory.getPath());
			}
			else if (file.isDirectory()) {
				importDirectory(file.getAbsolutePath(),importedDirectory.getPath());
			}
		}
	}
	/**
	 * 
	 * @param targetPath is the absolute path of the parent directory where you wish to create or overwrite (exportFile handle the creation of a new file if it does'nt exists) the file
	 * @param VFSpath is the path in the VFS of the file you wish to export
	 * @throws FileNotFoundException if either the VFSpath doe'nt lead to a file or targetpath doesn't lead to a file but a directory in the host file system or path doesn't exists
	 */

	public void exportFile(String targetPath, String VFSpath) throws FileNotFoundException{
		FileNode fileToExport = pathToFile(VFSpath);
		FileOutputStream output = null;
		File targetFile = new File(targetPath+fileToExport.getName());

		try {
			targetFile.createNewFile();		//does nothing if the file already exists
			output = new FileOutputStream (targetPath+fileToExport.getName());  // throws fileNotFoundException if the given path doesn"t lead to a file
			output.write(fileToExport.getVFSFile().getFile());
			output.close();
		}catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (output != null) {
					output.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void exportDirectory(String targetpath, String VFSpath) throws FileNotFoundException{
		DirectoryNode directoryToExport = pathToDirectory(VFSpath);
		File hostDirectory = new File(targetpath+directoryToExport.getName()+File.separator);
		hostDirectory.mkdir(); // create the folder, but return false if the folder wasn't created either because it couldn't or either because the directory already exists. It cannot serve as a test for the rightfulness of the given pass

		if (!hostDirectory.exists()){
			throw new FileNotFoundException();
		}

		for (Node node : directoryToExport.sonNodes) {
			if (node instanceof FileNode) {
				exportFile(targetpath+directoryToExport.getName()+File.separator,node.getPath());
			} else {
				exportDirectory(targetpath+directoryToExport.getName()+File.separator,node.getPath());
			}
		}
	}
	
	
	@Override
	public String toString() {
		return "VirtualDisc [name=" + name + ", size=" + size + ", pathName="
				+ pathName + ", root=" + root + "]";
	}

	public static void main(String[] args) throws FileNotFoundException, NotEnoughSpaceException, SameNameException {

		// A main method to test import directory, import filen export directory, export file


		
		
		// we create a VD on which we will do our tests
		VirtualDisc vd1 = new VirtualDisc("vd1",100000000);
		

		Scanner sc0 = new Scanner(System.in);
		System.out.println( "Which method do you want to test?" + "\n" + "1 = importFile"+ "\n" + "2 = importDirectory" + "\n" + "3 = exportFile"+ "\n" + "4 = exportDirectory"+ "\n" + "Note that ExportDirectory uses ExportFile method" + "\n" + "Note that ImportDirectory uses ImportFile method");
		int n0 = sc0.nextInt();

		switch(n0)

		{case (1): // Test File method


			Scanner sc1 = new Scanner(System.in);
		System.out.println("We will test the import file method: we will try to import a file (that you will choose), from your concrete file system, on the root of a virtual disk we have just created");
		System.out.println("Type in the absolute path, on your concrete file system, of the file you want to import on the virtual disk we have created" +"\n" + "ex: on a Windows OS, the path of the file to import could be: D:\\Maxime\\Programmation\\ReportonPart1.docx "+ "\n" + "ex: on a Mac OS, the path of the file to import could be: /Users/pierrehe/Desktop/rapport AE.docx");
		String targetpath = sc1.nextLine();

		vd1.importFile(targetpath,vd1.root.getPath());

		System.out.println("Does the root of the virtual disk contain a file?");
		if (vd1.root.sonNodes.size() == 1) {
			System.out.println("Yes!" + "\n" +" What is the name of the file contained in the root of the virtual disk?" + "\n" + vd1.root.sonNodes.get(0).getName());
			
		}
		else {System.out.println("No: the import method failed: make sure you gave in a correct path");
		}
		
		break;



		case (2): // Test importDirectory method

				

		Scanner sc2 = new Scanner(System.in);
		System.out.println("We will test the import directory method: we will try to import a directory (that you will choose), from your concrete file system, on the root of a virtual disk we have just created");

		System.out.println("Type in the absolute path, on your concrete file system, of the directory you want to import on the root of virtual disk we have created"+ "\n" + " WARNING: End name of directory with \\ on windows or / on mac"+ "\n" + "ex: on a Windows OS, the path of the directory to import could be: D:\\Maxime\\CV\\ "+ "\n" + "ex: on a Mac OS, the path of the directory to import could be: /Users/pierrehe/Desktop/");
		String targetpath2 = sc2.nextLine();

		vd1.importDirectory(targetpath2 , vd1.root.getPath());

		System.out.println("Does the root of the virtual disk contain a directory?");
		if (vd1.root.sonNodes.size() == 1) {
			System.out.println("Yes!" + "\n" +" What is the name of the directory contained in the root of the virtual disk?" + "\n" + vd1.root.sonNodes.get(0).getName());
			
		}
		else {System.out.println("No: the import method failed: make sure you gave in a correct path");
		}
		
		
		break;
		
		case (3): // Test File method

			DirectoryNode dnode1_1 = new DirectoryNode(vd1.root, "1_1");
		// in this test we create two filenodes with very simple files, in the VD

		byte[] array1 = new byte[] {(byte)1, (byte)2};
		FileNode fnodelevel1_1 = new FileNode(vd1.root, "f1_1", new VFSFile(array1));
		byte[] array2 = new byte[] {(byte)3, (byte)4};
		FileNode fnodelevel1_2 = new FileNode(dnode1_1, "f1_2", new VFSFile(array2));
		
		
		System.out.println("We will test the export file method: we will try to export a file (that we have created and named f1_1), from a virtual disk we have just created, to your concrete file system, at a path that you will choose");

		Scanner sc3 = new Scanner(System.in);
		System.out.println("Type in the absolute path, on your concrete file system, of the DIRECTORY which will CONTAIN the exported file"+ "\n" + " WARNING: End name of directory with \\ on windows or / on mac" + "\n" + "ex: on a Windows OS, the path of the directory that will contain the exported file could be: C:\\TestProgrammation\\Example\\ "+ "\n" + "ex: on a Mac OS, the path of the directory to import could be: /Users/pierrehe/Desktop/");
		String targetpath3 = sc3.nextLine();

		vd1.exportFile(targetpath3,fnodelevel1_1.getPath());

		System.out.println(" check the directory corresponding to the given adress !" + "\n" + "The file 1_1 has been moved!");
		break;



		case (4): // Test exportDirectory method
			
			DirectoryNode dnode1_1bis = new DirectoryNode(vd1.root, "1_1");
		DirectoryNode dnode1_2bis = new DirectoryNode(dnode1_1bis, "1_2");
		// in this test we create two filenodes with very simple files, in the VD

		byte[] array1bis = new byte[] {(byte)1, (byte)2};
		FileNode fnodelevel1_1bis = new FileNode(dnode1_1bis, "f1_1", new VFSFile(array1bis));
		byte[] array2bis = new byte[] {(byte)3, (byte)4};
		FileNode fnodelevel1_2bis = new FileNode(dnode1_2bis, "f1_2", new VFSFile(array2bis));

		System.out.println("We will test the export directory method: we will try to export a directory (that we have created and named 1_1), from a virtual disk we have just created, to your concrete file system, at a path that you will choose");

			Scanner sc4 = new Scanner(System.in);
		System.out.println("Type in the path of the DIRECTORY which will CONTAIN the exported directory" + "\n" + " WARNING: End name of directory with \\ on windows or / on mac" + "\n" + "ex: on a Windows OS, the path of the file to import could be: C:\\TestProgrammation\\Example\\ "+ "\n" + "ex: on a Mac OS, the path of the directory to import could be: /Users/pierrehe/Desktop/");
		String targetpath4 = sc4.nextLine();

		vd1.exportDirectory(targetpath4 , dnode1_1bis.getPath());

		System.out.println(" check the directory corresponding to the given adress !" + "\n" + "The file 1_1 has been moved!");
		break;




		}
		

	}

}


