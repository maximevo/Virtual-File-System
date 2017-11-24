package VFS_core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;


// all the information regarding the path of the VD are stored in a file called VFSmap.ser
//it is constantly deserialized, updated, and serialized.
//and to find a virtual Disc it is deserialized then we search the value of the keys
// VFSmap = HashMap<key=VDname, value = path>

public class VFS_app {  ///que des méthodes statiques
	/**
	 * this method serves two purposes. The first is to create a VFSmap.ser in the java project in order to store the mapping of the 
	 * different VDs. The second is to enable the user to reset the VFSmap.ser in order to start everything anew.Thus, the import of 
	 * this JavaProject in a new computer will be possible, even if it was previously used on another one.
	 */
	public static void resetVFSmap() {
		HashMap<String,String> VFSmap = new HashMap<String,String>();
		saveVFSmap(VFSmap);
		System.out.println("VFSmap successfully reseted");
	}
	
	public static HashMap<String,String> getVFSmap() {
		HashMap<String,String> VFSmap = null;
		
		//we deserialize the VFSmap
		FileInputStream filein = null;
		ObjectInputStream in = null;

		try {
			filein = new FileInputStream("VFSmap.ser");
			in = new ObjectInputStream(filein);

			VFSmap = (HashMap<String,String>) in.readObject();
			filein.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (ClassNotFoundException e) {
			// if the VFSmap given with the project isn't modified manually or moved, this exception should not arise
			e.printStackTrace();
		} 
	return VFSmap;
	}


	// VD with same name are not authorized
	public static VirtualDisc getVD(String name)  throws VDNotFoundException{
		VirtualDisc VD = null;
		HashMap<String,String> VFSmap = getVFSmap();

		// from the map we get the path of the VFS
		String path=null;
		try {
			if (VFSmap.containsKey(name)) {
				path = VFSmap.get(name);
			}else { throw new VDNotFoundException();}



			// now we can deserialize the vfs
			FileInputStream fileinput = null;
			ObjectInputStream objectin = null;


			fileinput = new FileInputStream(path);
			objectin = new ObjectInputStream(fileinput);

			VD = (VirtualDisc) objectin.readObject();
			fileinput.close();

		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return VD;
	}
	
	public static void saveVD(VirtualDisc VD) {
		FileOutputStream fos = null;
		ObjectOutputStream out = null;

		try {
			fos = new FileOutputStream(VD.getPathName());
			out = new ObjectOutputStream(fos);
			out.writeObject(VD);
			out.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public static void saveVFSmap(HashMap<String,String> VFSmap) {
		FileOutputStream fos = null;
		ObjectOutputStream out = null;

		try {
			fos = new FileOutputStream("VFSmap.ser");
			out = new ObjectOutputStream(fos);
			out.writeObject(VFSmap);
			out.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	/**
	 * 
	 * @param name
	 * @param size
	 * @param pathname is the pathname to the DIRECTORY on the host file system which will store the VD
	 */
	
	public static boolean createVD (String name, int size, String pathname) {
		boolean done = false;
		VirtualDisc VD = null;
		HashMap<String,String> VFSmap = getVFSmap();

		//we check if this VDname is already assigned to an existent VD
		try {
			if (VFSmap.containsKey(name)) {throw new SameNameException();}


			//we create a new VD and then serialize it and update VFSMap

			VD = new VirtualDisc(name,size);
			VD.setPathName(pathname+name+".ser");
			VFSmap.put(name, pathname+name+".ser");
			saveVFSmap(VFSmap);
			saveVD(VD);
			done = true;
		}catch (SameNameException ex) {
			System.out.println("There is already a VD named "+name);
		}
		return done;
	}

	public static boolean deleteVD(String name) {
		boolean done = false;
		HashMap<String,String> VFSmap = getVFSmap();
		try {
			VirtualDisc VD = getVD(name);
			File file = new File(VD.getPathName());
			file.delete();
			VFSmap.remove(name);
			saveVFSmap(VFSmap);
			done = true;
		}catch (VDNotFoundException e) {
			System.out.println("there is no VD named : "+name);
		}
		return done;
	}
	/**
	 * 
	 * @param VDname
	 * @param newPath is the pathname of the new DIRECTORY in which we will store the VD 
	 */
	public static boolean moveVD(String VDname, String newPath) {
		boolean done = false;
		HashMap<String,String> VFSmap = getVFSmap();
		try{
			VirtualDisc VD = getVD(VDname);
			File file = new File(VD.getPathName());
			file.delete();
			VD.setPathName(newPath+VD.getName()+".ser");
			VFSmap.put(VDname,newPath+VD.getName()+".ser");
			saveVD(VD);
			saveVFSmap(VFSmap);
			done = true;
		}catch (VDNotFoundException e) {
			System.out.println("there is no VD named : "+VDname);
		}
		return done;
	}


	public static boolean importFile (String hostpath, String VDname, String VFSPath) {
		boolean done = false;
		try {
			VirtualDisc VD = getVD(VDname);
			VD.importFile(hostpath, VFSPath);
			saveVD(VD);
			done = true;
		}catch (VDNotFoundException e) {
			System.out.println("there is no VD named : "+VDname);
		} catch (FileNotFoundException e) {
			System.out.println("either the path of the to be imported file is wrong, either the path in the VD is wrong or these files don't exist");
			e.printStackTrace();
		} catch (NotEnoughSpaceException e) {
			System.out.println("Not enough space remaining "+VDname+" for the file");
			e.printStackTrace();
		} catch (SameNameException e) {
			System.out.println("a file with the same name already exists at "+VFSPath+" in"+VDname);
			e.printStackTrace();
		}
		return done;
	}

	public static boolean importDirectory (String hostpath, String VDname, String VFSPath) {
		boolean done = false;
		try {
			VirtualDisc VD = getVD(VDname);
			VD.importDirectory(hostpath, VFSPath);
			saveVD(VD);
			done = true;
		}catch (VDNotFoundException e) {
			System.out.println("there is no VD named : "+VDname);
		} catch (FileNotFoundException e) {
			System.out.println("either the path of the to be imported Directory is wrong, either the path in the VD is wrong or these directories don't exist");
			e.printStackTrace();
		} catch (NotEnoughSpaceException e) {
			System.out.println("Not enough space remaining in "+VDname+" for the directory");
			e.printStackTrace();
		} catch (SameNameException e) {
			System.out.println("a directory with the same name already exists at "+VFSPath+" in"+VDname);
			e.printStackTrace();
		}
		return done;
	}
	
	public static boolean exportFile (String hostpath, String VDname, String VFSPath) {
		boolean done = false;
		try {
			VirtualDisc VD = getVD(VDname);
			VD.exportFile(hostpath, VFSPath);
			saveVD(VD);
			done = true;
		}catch (VDNotFoundException e) {
			System.out.println("there is no VD named : "+VDname);
		} catch (FileNotFoundException e) {
			System.out.println("either the path of the to be exported file is wrong, either the path for the host file system is wrong or these files don't exist");
			e.printStackTrace();
		}
		return done;
	}

	public static boolean exportDirectory(String hostpath, String VDname, String VFSPath) {
		boolean done = false;
		try {
			VirtualDisc VD = getVD(VDname);
			VD.exportDirectory(hostpath, VFSPath);
			saveVD(VD);
			done = true;
		}catch (VDNotFoundException e) {
			System.out.println("there is no VD named : "+VDname);
		} catch (FileNotFoundException e) {
			System.out.println("either the path of the to be exported directory is wrong, either the path for the host file system is wrong or these directories don't exist");
			e.printStackTrace();
		}
		return done;
	}

	public static ArrayList<FileNode> searchFile (String VDname, String filename) {
		try{
			VirtualDisc VD = getVD(VDname);
			return VD.root.searchFile(filename); // return an empty array if no match found
		}catch (VDNotFoundException e) {
			System.out.println("there is no VD named : "+VDname);
		}
		return null;
	}
	
	//overloading the searchFile method
	public static ArrayList<FileNode> searchFile (String VDname, String directoryPath, String filename) {
		ArrayList<FileNode> fileList = null;
		try {
			VirtualDisc VD = getVD(VDname);
			fileList = VD.searchFile(directoryPath, filename);
		}catch (VDNotFoundException e) {
			System.out.println("there is no VD named : "+VDname);
		} catch (FileNotFoundException e) {
			System.out.println("the directory "+directoryPath+" does not exist in "+VDname);
			e.printStackTrace();
		}
		return fileList;
	}

	public static ArrayList<DirectoryNode> searchDirectory (String VDname, String Directoryname) {
		try {
			VirtualDisc VD = getVD(VDname);
			return VD.root.searchDirectory(Directoryname); // return an empty array if no match found
		} catch (VDNotFoundException e) {
			System.out.println("there is no VD named : "+VDname);
		}
		return null;
	}

	//overload the searchDirectory method
	public static ArrayList<DirectoryNode> searchDirectory (String VDname, String directoryPath, String filename) {
		ArrayList<DirectoryNode> directoryList = null;
		try {
			VirtualDisc VD = getVD(VDname);
			directoryList = VD.searchDirectory(directoryPath, filename);
		} catch (VDNotFoundException e) {
			System.out.println("there is no VD named : "+VDname);
		} catch (FileNotFoundException e) {
			System.out.println("the directory "+directoryPath+" does not exist in "+VDname);
			e.printStackTrace();
		}
		return directoryList;
	}

	public static ArrayList<Node> search (String VDname, String name) { // if you don't know if you are searching for a file or a directory
		try {
			VirtualDisc VD = getVD(VDname);
			return VD.root.search(name); // return an empty array if no match found
		} catch (VDNotFoundException e) {
			System.out.println("there is no VD named : "+VDname);
		}
		return null;
	}

	//overloading the search method
	public static ArrayList<Node> search (String VDname, String directoryPath, String filename) {
		ArrayList<Node> nodeList = null;
		try {
			VirtualDisc VD = getVD(VDname);
			nodeList = VD.search(directoryPath, filename);
		} catch (VDNotFoundException e) {
			System.out.println("there is no VD named : "+VDname);
		} catch (FileNotFoundException e) {
			System.out.println("the directory "+directoryPath+" does not exist in "+VDname);
			e.printStackTrace();
		}
		return nodeList;
	}

	public static boolean removeFile (String VDname, String filePath) {
		boolean done = false;
		try {
			VirtualDisc VD = getVD(VDname);
			VD.removeFile(filePath);
			saveVD(VD);
			done = true;
		} catch (VDNotFoundException e) {
			System.out.println("there is no VD named : "+VDname);
		} catch (FileNotFoundException e) {
			System.out.println("the file "+filePath+" does not exist in "+VDname);
			e.printStackTrace();
		}
		return done;
	}

	public static boolean removeDirectory (String VDname, String directoryPath) {
		boolean done = false;
		try {
			VirtualDisc VD = getVD(VDname);
			VD.removeDirectory(directoryPath);
			saveVD(VD);
			done = true;
		} catch (VDNotFoundException e) {
			System.out.println("there is no VD named : "+VDname);
		} catch (FileNotFoundException e) {
			System.out.println("the directory "+directoryPath+" does not exist in "+VDname);
			e.printStackTrace();
		}
		return done;
	}
	
	public static boolean createDirectory (String VDname, String parentPath, String name) {
		boolean done = false;
		try {
			VirtualDisc VD = getVD(VDname);
			VD.createDirectory(parentPath, name);
			saveVD(VD);
			done = true;
		} catch (VDNotFoundException e) {
			System.out.println("there is no VD named : "+VDname);
		} catch (FileNotFoundException e) {
			System.out.println("the directory "+parentPath+" does not exist in "+VDname);
			e.printStackTrace();
		} catch (SameNameException e) {
			System.out.println("a directory named "+name+" already exists at "+ parentPath+" in "+VDname );
			e.printStackTrace();
		}
		return done;
	}
	
	//pas de createFile, création de file uniquement par import 

	public static boolean moveFile (String VDname, String oldPath, String newPath) {
		boolean done = false;
		try {
			VirtualDisc VD = getVD(VDname);
			VD.moveFile(oldPath, newPath);
			saveVD(VD);
			done = true;
		} catch (VDNotFoundException e) {
			System.out.println("there is no VD named : "+VDname);
		} catch (FileNotFoundException e) {
			System.out.println("incorrect path given in arguments");
			e.printStackTrace();
		} catch (SameNameException e) {
			System.out.println("there already exists a file with the same name at "+newPath+" in "+VDname);
			e.printStackTrace();
		}
		return done;
	}

	public static boolean moveDirectory (String VDname, String oldPath, String newPath) {
		boolean done = false;
		try {
			VirtualDisc VD = getVD(VDname);
			VD.moveDirectory(oldPath, newPath);
			saveVD(VD);
			done = true;
		} catch (VDNotFoundException e) {
			System.out.println("there is no VD named : "+VDname);
		} catch (FileNotFoundException e) {
			System.out.println("incorrect path given in arguments");
			e.printStackTrace();
		} catch (SameNameException e) {
			System.out.println("there already exists a directory with the same name at "+newPath+" in "+VDname);
			e.printStackTrace();
		}
		return done;
	}

	public static boolean renameFile (String VDname, String path, String name) {
		boolean done = false;
		try {
			VirtualDisc VD = getVD(VDname);
			VD.renameFile(path, name);
			saveVD(VD);
			done = true;
		} catch (VDNotFoundException e) {
			System.out.println("there is no VD named : "+VDname);
		} catch (FileNotFoundException e) {
			System.out.println("the file "+path+" does not exists in "+VDname);
			e.printStackTrace();
		} catch (SameNameException e) {
			System.out.println("a file named “"+name+"” already exists at "+path+" in"+VDname);
			e.printStackTrace();
		}
		return done;
	}

	public static boolean renameDirectory (String VDname, String path, String name) {
		boolean done = false;
		try {
			VirtualDisc VD = getVD(VDname);
			VD.renameDirectory(path, name);
			saveVD(VD);
			done = true;
		} catch (VDNotFoundException e) {
			System.out.println("there is no VD named : "+VDname);
		} catch (FileNotFoundException e) {
			System.out.println("the directory "+path+" does not exists in "+VDname);
			e.printStackTrace();
		} catch (SameNameException e) {
			System.out.println("a diretory named “"+name+"” already exists at "+path+" in"+VDname);
			e.printStackTrace();
		}
		return done;
	}
	
	public static boolean copyFile (String VDname, String filePath, String targetDirectoryPath) {
		boolean done = false;
		try {
			VirtualDisc VD = getVD(VDname);
			VD.copyFile(filePath, targetDirectoryPath);
			saveVD(VD);
			done = true;
		} catch (VDNotFoundException e) {
			System.out.println("there is no VD named : "+VDname);
		} catch (FileNotFoundException e) {
			System.out.println("invalid path given in arguments");
			e.printStackTrace();
		} catch (NotEnoughSpaceException e) {
			System.out.println("Not enough space remaining in "+VDname+" to copy the file "+filePath);
			e.printStackTrace();
		} catch (SameNameException e) {
			System.out.println("there already exists a file with the same name at "+targetDirectoryPath+" in "+VDname);
			e.printStackTrace();
		}
		return done;
	}

	public static boolean copyDirectory (String VDname, String directoryPath, String targetDirectoryPath) {
		boolean done = false;
		try {
			VirtualDisc VD = getVD(VDname);
			VD.copyDirectory(directoryPath, targetDirectoryPath);
			saveVD(VD);
			done = true;
		} catch (VDNotFoundException e) {
			System.out.println("there is no VD named : "+VDname);
		} catch (FileNotFoundException e) {
			System.out.println("invalid path given in arguments");
			e.printStackTrace();
		} catch (NotEnoughSpaceException e) {
			System.out.println("Not enough space remaining in "+VDname+" to copy the directory "+directoryPath);
			e.printStackTrace();
		} catch (SameNameException e) {
			System.out.println("there already exists a directory with the same name at "+targetDirectoryPath+" in "+VDname);
			e.printStackTrace();
		}
		return done;
	}

	public static ArrayList<Node> getSonNodes (String VDname, String directoryPath) {
		ArrayList<Node> nodeList = new ArrayList<Node>();
		try {
			VirtualDisc VD = getVD(VDname);
			DirectoryNode directory = VD.pathToDirectory(directoryPath);
			nodeList = directory.sonNodes;
		} catch (VDNotFoundException e) {
			System.out.println("there is no VD named : "+VDname);
		} catch (FileNotFoundException e) {
			System.out.println("the directory "+directoryPath+" does not exists in "+VDname);
			e.printStackTrace();
		}
		return nodeList;
	}

	public static int freespace(String VDname) {
		try {
			VirtualDisc VD = getVD(VDname);
			return VD.freeSpace();
		}catch (VDNotFoundException e) {
			System.out.println("there is no VD named : "+VDname);
		}
		return -1;
	}









	public static void main(String[] args) throws FileNotFoundException, NotEnoughSpaceException, SameNameException, VDNotFoundException {

		// A main method to test import directory, import file, export directory, export file, moveVD

		VFS_app.resetVFSmap();
		VFS_app.createVD("VDTest",100000000, "");// we create a  VD that is, on the host file system, at the same place as the java project





		Scanner sc0 = new Scanner(System.in);
		System.out.println( "Which method do you want to test?" + "\n" + "1 = importFile"+ "\n" + "2 = importDirectory" + "\n" + "3 = exportFile"+ "\n" + "4 = exportDirectory"+ "\n" + "5 = moveVD"+ "\n" + "Note that ExportDirectory uses ExportFile method" + "\n" + "Note that ImportDirectory uses ImportFile method");
		int n0 = sc0.nextInt();

		switch(n0)

		{case (1): // Test importFile method


			Scanner sc1 = new Scanner(System.in);
		System.out.println("We will test the import file method: we will try to import a file (that you will choose), from your concrete file system, on the root of a virtual disk we have just created");
		System.out.println("Type in the absolute path, on your concrete file system, of the file you want to import on the virtual disk we have created" + "\n"+ "ex: on a Windows OS, the path of the file to import could be: D:\\Maxime\\Programmation\\ReportonPart1.docx "+ "\n" + "ex: on a Mac OS, the path of the file to import could be: /Users/pierrehe/Desktop/rapport AE.docx");
		String targetpath = sc1.nextLine();

		VFS_app.importFile(targetpath, "VDTest", VFS_app.getVD("VDTest").root.getPath());

		System.out.println("Does the root of the virtual disk contain a file?");

		if (VFS_app.getVD("VDTest").root.sonNodes.size() == 1) {
			System.out.println("Yes!" + "\n" +" What is the name of the file contained in the root of the virtual disk?" + "\n" + VFS_app.getVD("VDTest").root.sonNodes.get(0).getName());

		}
		else {System.out.println("No: the import method failed: make sure you gave in a correct path");
		}

		break;



		case (2): // Test importDirectory method



			Scanner sc2 = new Scanner(System.in);
		System.out.println("We will test the import directory method: we will try to import a directory (that you will choose), from your concrete file system, on the root of a virtual disk we have just created");

		System.out.println("Type in the absolute path, on your concrete file system, of the directory you want to import on the root of virtual disk we have created" + "\n" +  " WARNING: End name of directory with \\ on windows or / on mac"+ "\n" + "ex: on a Windows OS, the path of the directory to import could be: D:\\Maxime\\CV\\ "+ "\n" + "ex: on a Mac OS, the path of the directory to import could be: /Users/pierrehe/Desktop/");
		String targetpath2 = sc2.nextLine();

		VFS_app.importDirectory(targetpath2 , "VDTest",VFS_app.getVD("VDTest").root.getPath());

		System.out.println("Does the root of the virtual disk contain a directory?");
		if (VFS_app.getVD("VDTest").root.sonNodes.size() == 1) {
			System.out.println("Yes!" + "\n" +" What is the name of the directory contained in the root of the virtual disk?" + "\n" + VFS_app.getVD("VDTest").root.sonNodes.get(0).getName());

		}
		else {System.out.println("No: the import method failed: make sure you gave in a correct path");
		}


		break;

		case (3): // Test exportFile method

			VFS_app.createDirectory("VDTest", VFS_app.getVD("VDTest").root.getPath(), "1_1");
		// in this test we create two filenodes with very simple files, in the VD

		VirtualDisc vd = VFS_app.getVD("VDTest");
		byte[] array1 = new byte[] {(byte)1, (byte)2};
		FileNode fnodelevel1_1 = new FileNode(vd.root, "f1_1", new VFSFile(array1));
		VFS_app.saveVD(vd);


		System.out.println("We will test the export file method: we will try to export a file (that we have created and named f1_1), from a virtual disk we have just created, to your concrete file system, at a path that you will choose");

		Scanner sc3 = new Scanner(System.in);
		System.out.println("Type in the absolute path, on your concrete file system, of the DIRECTORY which will CONTAIN the exported file"  + "\n" + " WARNING: End name of directory with \\ on windows or / on mac" + "\n" + "ex: on a Windows OS, the path of the directory that will contain the exported file could be: C:\\TestProgrammation\\Example\\ "+ "\n" + "ex: on a Mac OS, the path of the directory to import could be: /Users/pierrehe/Desktop/");
		String targetpath3 = sc3.nextLine();

		VFS_app.exportFile(targetpath3,"VDTest",fnodelevel1_1.getPath());

		System.out.println(" check the directory corresponding to the given adress !" + "\n" + "The file 1_1 has been moved!");
		break;



		case (4): // Test exportDirectory method

			VFS_app.createDirectory("VDTest", VFS_app.getVD("VDTest").root.getPath(), "1_1");
		VFS_app.createDirectory("VDTest", VFS_app.getVD("VDTest").root.getPath() + (String) File.separator + "1_1", "1_2");

		// in this test we create two filenodes with very simple files, in the VD
		VirtualDisc vd4 = VFS_app.getVD("VDTest");
		byte[] array1bis = new byte[] {(byte)1, (byte)2};
		FileNode fnodelevel1_1bis = new FileNode(vd4.pathToDirectory(vd4.root.getPath() + (String) File.separator + "1_1"), "f1_1", new VFSFile(array1bis));
		byte[] array2bis = new byte[] {(byte)3, (byte)4};
		FileNode fnodelevel1_2bis = new FileNode(vd4.pathToDirectory(vd4.root.getPath() + (String) File.separator + "1_1" + File.separator + "1_2"), "f1_2", new VFSFile(array2bis));
		VFS_app.saveVD(vd4);

		System.out.println("We will test the export directory method: we will try to export a directory (that we have created and named 1_1), from a virtual disk we have just created, to your concrete file system, at a path that you will choose");

		Scanner sc4 = new Scanner(System.in);
		System.out.println("Type in the path of the DIRECTORY which will CONTAIN the exported directory" + "\n" + " WARNING: End name of directory with \\ on windows or / on mac" + "\n" + "ex: on a Windows OS, the path of the directory to import could be: C:\\TestProgrammation\\Example\\ "+ "\n" + "ex: on a Mac OS, the path of the directory to import could be: /Users/pierrehe/Desktop/");
		String targetpath4 = sc4.nextLine();

		VFS_app.exportDirectory(targetpath4 , "VDTest", VFS_app.getVD("VDTest").root.getPath());

		System.out.println(" check the directory corresponding to the given adress !" + "\n" + "The file 1_1 has been moved!");
		break;


		case (5):// We test moveVD method
			VFS_app.createDirectory("VDTest", VFS_app.getVD("VDTest").root.getPath(), "1_1");
		// in this test we create two filenodes with very simple files, in the VD

		VirtualDisc vd5 = VFS_app.getVD("VDTest");
		byte[] array11 = new byte[] {(byte)1, (byte)2};
		FileNode fnodelevel1_11 = new FileNode(vd5.root, "f1_1", new VFSFile(array11));
		VFS_app.saveVD(vd5);


		System.out.println("We will test the move virtual disk method: a virtual disk is saved as a concrete file on the concrete file system :  we will try to move a virtual disk (that we have created and saved on the same path as the present java project), from the same path as the present java project, towards a path that you will choose");

		Scanner sc5 = new Scanner(System.in);
		System.out.println("Type in the absolute path, on your concrete file system, of the DIRECTORY which will CONTAIN the concrete file corresponding to the file system" + "\n" +" WARNING: End name of directory with \\ on windows or / on mac" + "\n" + "ex: on a Windows OS, the path of the directory that will contain the exported file could be: C:\\TestProgrammation\\Example\\ "+ "\n" + "ex: on a Mac OS, the path of the directory to import could be: /Users/pierrehe/Desktop/");
		String targetpath5 = sc5.nextLine();

		VFS_app.moveVD("VDTest", targetpath5);

		System.out.println(" check the directory corresponding to the given adress !" + "\n" + "It should contain the concrete file corresponding to the virtual disk (named VDTest.ser)");



		}


	}

}




