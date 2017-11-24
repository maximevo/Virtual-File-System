package VFS_core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class CLUI {
	private static HashMap<String,String> commandList = new HashMap<String, String>();
	
	private static String clearHelp = "clear"+"\n"+"No argument is expected for this command. It clears clears the map that saves the information about where the VD's are stored in the host file system. Use this command if you have manually moved or removed VD's in your host file system. Under normal use circumstancies, you should not need to use this command";
	private static String lsHelp = "ls;<vfsname>;<args>;<pathname>"+"\n"+"To list the information concerning files and directories contained in absolute position corresponding to pathname. The command lsbehave differently, depending on the optional argument arg:"+"\n"+"– args='' (i.e. no args is given): in this case ls simply displays the list of names of files and directories (together with an indication of their “file” or “dir” nature) contained in the directory which absolute path in the vfs is pathname."+"\n"+"– args='-l': in this case, the command also displays the size of the files and directories"+"\n"+"Path Syntax in the VFS : directories' path don't end with a separator (/ on mac, \\ on Windows)";;
	private static String mvfHelp = "mvf;<vfsname>;<oldpath>;<newpath>"+"\n"+"to move a file with absolute positionning oldpath into a target directory with aboslute positionning newpath in the vfs vfsname"+"\n"+"Path Syntax in the VFS : directories' path don't end with a separator (/ on mac, \\ on Windows)";
	private static String mvdHelp = "mvd;<vfsname>;<oldpath>;<newpath>"+"\n"+"to move a directory with absolute positionning oldpath into a target directory with aboslute positionning newpath in the vfs vfsname"+"\n"+"Path Syntax in the VFS : directories' path don't end with a separator (/ on mac, \\ on Windows)";
	private static String cpfHelp = "cpf;<vfsname>;<sourcepath>;<targetpath>"+"\n"+"to copy, within the VFS named vfsname, the content of a file whose absolute name is source path into a target directory whose absolute name is targetpath."+"\n"+"Path Syntax in the VFS : directories' path don't end with a separator (/ on mac, \\ on Windows)";
	private static String cpdHelp = "cpd;<vfsname>;<sourcepath>;<targetpath>"+"\n"+"to copy, within the VFS named vfsname, the content of a directory whose absolute name is source path into a target directory whose absolute name is targetpath."+"\n"+"Path Syntax in the VFS : directories' path don't end with a separator (/ on mac, \\ on Windows)";
	private static String rmfHelp = "rmf;<vfsname>;<pathname>"+"\n"+"to remove a file with absolute name pathname from the VFS named vfsname.";
	private static String rmdHelp = "rmd;<vfsname>;<pathname>"+"\n"+"to remove a directory with absolute name pathname from the VFS named vfsname."+"\n"+"Path Syntax in the VFS : directories' path don't end with a separator (/ on mac, \\ on Windows)";
	private static String crvfsHelp = "crvfs;<vfsname>;<dim>;<pathname>"+"\n"+"to create a new VFS with name vfsname and maximal dimension dim bytes into the target directory whose absolute path is pathname in the host file system"+"\n"+"WARNING: End name of directory with a separator (\\ on windows or / on mac)";
	private static String rmvfsHelp = "rmvfs;<vfsname>"+"\n"+"to delete a VFS named vfsname";
	private static String impfvfsHelp = "impfvfs;<hostpath>;<vfsname>;<vfspath>"+"\n"+"to import the content of the file corresponding to absolute name hostpath on the host file system into the position vfspath of a target directory on an existing VFS named vfsname."+"\n"+"Path Syntax in the VFS : directories' path don't end with a separator (/ on mac, \\ on Windows)";
	private static String impdvfsHelp = "impdvfs;<hostpath>;<vfsname>;<vfspath>"+"\n"+"to import the content of the directory corresponding to absolute name hostpath on the host file system into the position vfspath of a target directory on an existing VFS named vfsname."+"\n"+"WARNING: End name of directory of the host file system with a separator (\\ on windows or / on mac)"+"\n"+"Path Syntax in the VFS : directories' path don't end with a separator (/ on mac, \\ on Windows)";
	private static String expfvfsHelp = "expfvfs;<vfspath>;<vfsname>;<hostpath>"+"\n"+"to export the content of the file corresponding to absolute name vfspath on the VFS named vfsname into the directory corresponding to absolute path named hostpath of the host file system"+"\n"+"WARNING: End name of directory of the host file system with a separator (\\ on windows or / on mac)";
	private static String expdvfsHelp = "expdvfs;<vfspath>;<vfsname>;<hostpath>"+"\n"+"to export the directory corresponding to absolute name vfspath on the VFS named vfsname into the directory corresponding to absolute path named hostpath of the host file system"+"\n"+"WARNING: End name of directory of the host file system with a separator (\\ on windows or / on mac)"+"\n"+"Path Syntax in the VFS : directories' path don't end with a separator (/ on mac, \\ on Windows)";
	private static String expvfsHelp = "expvfs;<vfsname>;<hostpath>"+"\n"+"to export the whole vfs named vfsname into the directory corresponding to absolute path named hostpath on the host file system"+"\n"+"WARNING: End name of directory of the host file system with a separator (\\ on windows or / on mac)";
	private static String freeHelp = "free;<vfsname>"+"\n"+"to display the quantity of free space for VFS named vfsname";
	private static String findfHelp = "findf;<vfsname>;<directorypaht>;<filename>"+"\n"+"<directorypath> is an optionnal argument. The command behaves differently depending on this argument : "+"\n"+"if <directorypath> is given : the command searchs if a file named filename is stored in the directory corresponding to absolute path directorypath in the VFS named vfsname or in its son directories, returns the absolute path of the sought file if it is present. Can return multiple results if there are more than one file with this name in the son directories."+"\n"+"if no <directorypath> given : apply the search on the whole vfs named vfsname."+"\n"+"Path Syntax in the VFS : directories' path don't end with a separator (/ on mac, \\ on Windows)";
	private static String finddHelp = "findd;<vfsname>;<directorypaht>;<directoryname>"+"\n"+"<directorypath> is an optionnal argument. The command behaves differently depending on this argument : "+"\n"+"if <directorypath> is given : the command searchs if a directory named directoryname is stored in the directory corresponding to absolute path directorypath in the VFS named vfsname or in its son directories, returns the absolute path of the sought directory if it is present. Can return multiple results if there are more than one directory with this name in the son directories."+"\n"+"if no <directorypath> given : apply the search on the whole vfs named vfsname."+"\n"+"Path Syntax in the VFS : directories' path don't end with a separator (/ on mac, \\ on Windows)";
	private static String findHelp = "find;<vfsname>;<directorypaht>;<name>"+"\n"+"<directorypath> is an optionnal argument. The command behaves differently depending on this argument : "+"\n"+"if <directorypath> is given : the command searchs if a directory/file named name is stored in the directory corresponding to absolute path directorypath in the VFS named vfsname or in its son directories, returns the absolute path of the sought directory/file if it is present. Can return multiple results if there are more than one directory/file with this name in the son directories."+"\n"+"if no <directorypath> given : apply the search on the whole vfs named vfsname."+"\n"+"Path Syntax in the VFS : directories' path don't end with a separator (/ on mac, \\ on Windows)";
	private static String helpHelp ="help;<command-name>"+"\n"+"displays the help of the command named command-name";
	private static String generalHelp = "enter a command in the console to act on your different vfs'. Type 'help;<command-name>' to see what does the command do.";
	
	public static void getCommand(String [] input) {
		String command = input[0];
		
		if (command.equals("clear")) {
			clearCommand(input);
		}
		
		else if (command.equals("ls")) {
			lsCommand(input);
		}

		else if (command.equals("mvf")) {
			mvfCommand(input);
		}
		else if (command.equals("mvd")) {
			mvdCommand(input);
		}
		else if (command.equals("cpf")) {
			cpfCommand(input);
		}
		else if (command.equals("cpd")) {
			cpdCommand(input);
		}
		else if (command.equals("rmf")) {
			rmfCommand(input);
		}
		else if (command.equals("rmd")) {
			rmdCommand(input);
		}
		else if (command.equals("crvfs")) {
			crvfsCommand(input);
		}
		else if (command.equals("rmvfs")) {
			rmvvfsCommand(input);
		}
		else if (command.equals("impfvfs")) {
			impfvfsCommand(input);
		}
		else if (command.equals("impdvfs")) {
			impdvfsCommand(input);
		}
		else if (command.equals("expfvfs")) {
			expfvfsCommand(input);
		}
		else if (command.equals("expdvfs")) {
			expdvfsCommand(input);
		}
		else if (command.equals("expvfs")) {
			expvfsCommand(input);
		}
		else if (command.equals("free")) {
			freeCommand(input);
		}
		else if (command.equals("findf")) {
			findfCommand(input);
		}
		else if (command.equals("findd")) {
			finddCommand(input);
		}
		else if (command.equals("find")) {
			findCommand(input);
		}
		else if (command.equals("help")) {
			helpCommand(input);
		}
		else { System.out.println("unknown command, type 'help' to see a list of the available commands"); }
	}

	public static void clearCommand(String [] input) {
		if (input.length == 1) { 
			VFS_app.resetVFSmap();
		}
		else {System.out.println("no argument expected with reset command");}
	}
	
	
	//note that in our implementation, you must always specify the absolute path of the directory you're seeking to obtain information on
	public static void lsCommand (String [] input) {
		if (input.length == 3) { // case when no args is given
			ArrayList<Node> list = VFS_app.getSonNodes(input[1], input[2]);
			if (list.size() == 0) {
				System.out.println("the directory is empty");
			}
			else {
				for (Node node : list) {
					System.out.println(node.getName()+" "+node.getType());
				}
			}
		}
		else if (input.length == 4) {
			if (input[2].equals("-l")) {
				ArrayList<Node> list = VFS_app.getSonNodes(input[1], input[3]);
				if (list.size() == 0) {
					System.out.println("the directory is empty");
				}
				else {
					for (Node node : list) {
						System.out.println(node.getName()+" "+node.getSize()+" "+node.getType());
					}
				}
			}
			else {System.out.println("wrong optionnal argument given");}
		}
		else { System.out.println("wrong arguments"); }
	} 

	public static void mvfCommand (String [] input) {
		if (input.length == 4) {
			boolean done = VFS_app.moveFile(input[1],input[2],input[3]);
			if (done) { System.out.println("file successfully moved");}
		}
		else { System.out.println("wrong arguments"); }
	}

	public static void mvdCommand (String [] input) {
		if (input.length == 4) {
			boolean done = VFS_app.moveDirectory(input[1],input[2],input[3]);
			if (done) {System.out.println("directory successfully moved");}
		}
		else { System.out.println("wrong arguments"); }
	}

	public static void cpfCommand (String [] input) {
		if (input.length == 4) {
			boolean done = VFS_app.copyFile(input[1],input[2],input[3]);
			if (done) {System.out.println("file successfully copied");}
		}
		else { System.out.println("wrong arguments"); }
	}

	public static void cpdCommand (String [] input) {
		if (input.length == 4) {
			boolean done = VFS_app.copyDirectory(input[1],input[2],input[3]);
			if (done) {System.out.println("directory successfully copied");}
		}
		else { System.out.println("wrong arguments"); }
	}

	public static void rmfCommand (String [] input) {
		if (input.length == 3) {
			boolean done = VFS_app.removeFile(input[1],input[2]);
			if (done) {System.out.println("file successfully removed");}
		}
		else { System.out.println("wrong arguments"); }
	}

	public static void rmdCommand (String [] input) {
		if (input.length == 3) {
			boolean done = VFS_app.removeDirectory(input[1],input[2]);
			if (done) {System.out.println("directory successfully removed");}
		}
		else { System.out.println("wrong arguments"); }
	}

	//note that in our implementation, this method also needs the user to enter the pathname of the directory where he wants to store the new VD
	public static void crvfsCommand (String [] input) {
		if (input.length == 4) {
			try {
				int size = Integer.parseInt(input[2]);
				boolean done = VFS_app.createVD(input[1],size,input[3]);
				if (done) {System.out.println("virtual disc successfully created");}
			} catch (NumberFormatException e) {
				System.out.println("wrong argument given for size");
			}
		}
		else { System.out.println("wrong arguments"); }
	}

	public static void rmvvfsCommand(String [] input) {
		if (input.length == 2) {
			boolean done = VFS_app.deleteVD(input[1]);
			if (done) {System.out.println("virtual disc successfully deleted");}
		}
		else { System.out.println("wrong arguments"); }
	}
	
	public static void impfvfsCommand(String [] input) {
		if (input.length == 4) {
			boolean done = VFS_app.importFile(input[1], input[2], input[3]);
			if (done) {System.out.println("file successfully imported");}
		}
		else { System.out.println("wrong arguments"); }
	}
	
	public static void impdvfsCommand(String [] input) {
		if (input.length == 4) {
			boolean done = VFS_app.importDirectory(input[1], input[2], input[3]);
			if (done) {System.out.println("directory successfully imported");}
		}
		else { System.out.println("wrong arguments"); }
	}
	
	public static void expfvfsCommand(String [] input) {
		if (input.length == 4) {
			boolean done = VFS_app.exportFile(input[3], input[2], input[1]);
			if (done) {System.out.println("file successfully exported");}
		}
		else { System.out.println("wrong arguments"); }
	}
	
	public static void expdvfsCommand(String [] input) {
		if (input.length == 4) {
			boolean done = VFS_app.exportDirectory(input[3], input[2], input[1]);
			if (done) {System.out.println("directory successfully exported");}
		}
		else { System.out.println("wrong arguments"); }
	}
	
	public static void expvfsCommand(String [] input) {
		if (input.length == 3) {
			boolean done = VFS_app.exportDirectory(input[2], input[1], input[1]);  //this will export the root, whose name is the same as the vfs
			if (done) {System.out.println(input[1]+" successfully exported");}
		}
		else { System.out.println("wrong arguments"); }
	}
	
	public static void freeCommand(String [] input) {
		if (input.length == 2) {
			System.out.println("remaining space in "+input[1]+" : "+VFS_app.freespace(input[1]));
		}
		else { System.out.println("wrong arguments"); }
	}
	
	public static void findfCommand(String [] input) {
		if (input.length == 4) { //case when you want to search from a directory
			ArrayList<FileNode> result = VFS_app.searchFile(input[1], input[2], input[3]);
			if (result == null) {
				System.out.println("no matching file found");
			}
			else {
				System.out.println(result.size()+" result(s) found : ");
				for (FileNode file : result) {
					System.out.println(file.getPath());
				}
			}
		}
		else if (input.length == 3) { //general search in the Virtual Disc
			ArrayList<FileNode> result = VFS_app.searchFile(input[1], input[2]);
			if (result == null) {
				System.out.println("no matching file found");
			}
			else {
				System.out.println(result.size()+" result(s) found : ");
				for (FileNode file : result) {
					System.out.println(file.getPath());
				}
			}
		}
		else { System.out.println("wrong arguments"); }
	}

	public static void finddCommand(String [] input) {
		if (input.length == 4) { //case when you want to search from a directory
			ArrayList<DirectoryNode> result = VFS_app.searchDirectory(input[1], input[2], input[3]);
			if (result == null) {
				System.out.println("no matching directory found");
			}
			else {
				System.out.println(result.size()+" result(s) found : ");
				for (DirectoryNode directory : result) {
					System.out.println(directory.getPath());
				}
			}
		}
		else if (input.length == 3) { //general search in the Virtual Disc
			ArrayList<DirectoryNode> result = VFS_app.searchDirectory(input[1], input[2]);
			if (result == null) {
				System.out.println("no matching directory found");
			}
			else {
				System.out.println(result.size()+" result(s) found : ");
				for (DirectoryNode directory : result) {
					System.out.println(directory.getPath());
				}
			}
		}
		else { System.out.println("wrong arguments"); }
	}

	public static void findCommand(String [] input) {
		if (input.length == 4) { //case when you want to search from a directory
			ArrayList<Node> result = VFS_app.search(input[1], input[2], input[3]);
			if (result == null) {
				System.out.println("no match found");
			}
			else {
				System.out.println(result.size()+" result(s) found : ");
				for (Node node : result) {
					System.out.println(node.getType()+"   "+node.getPath());
				}
			}
		}
		else if (input.length == 3) { //general search in the Virtual Disc
			ArrayList<Node> result = VFS_app.search(input[1], input[2]);
			if (result == null) {
				System.out.println("no match found");
			}
			else {
				System.out.println(result.size()+" result(s) found : ");
				for (Node node : result) {
					System.out.println(node.getType()+"   "+node.getPath());
				}
			}
		}
		else { System.out.println("wrong arguments"); }
	}

	public static void helpCommand(String [] input) {
		if (input.length == 1) {
			System.out.println(generalHelp);
			System.out.println("these are the available commands : ");
			for (String key : commandList.keySet()) {
				System.out.println(key);
			}
		}
		else if (input.length == 2) {
			if (commandList.containsKey(input[1])) {
				System.out.println(commandList.get(input[1]));
			}
			else { System.out.println("the requested command doesn't exist, type 'help' to see a list of the available commands");}
		}
		else { System.out.println("wrong arguments"); }
	}
	
	public static void main(String[] args) {
		commandList.put("clear",clearHelp);
		commandList.put("ls", lsHelp);
		commandList.put("mvf", mvfHelp);
		commandList.put("mvd", mvdHelp);
		commandList.put("cpf", cpfHelp);
		commandList.put("cpd", cpdHelp);
		commandList.put("rmf", rmfHelp);
		commandList.put("rmd", rmdHelp);
		commandList.put("crvfs", crvfsHelp);
		commandList.put("rmvfs", rmvfsHelp);
		commandList.put("impfvfs", impfvfsHelp);
		commandList.put("impdvfs", impdvfsHelp);
		commandList.put("expfvfs", expfvfsHelp);
		commandList.put("expdvfs", expdvfsHelp);
		commandList.put("expvfs", expvfsHelp);
		commandList.put("free", freeHelp);
		commandList.put("findf", findfHelp);
		commandList.put("findd", finddHelp);
		commandList.put("find", findHelp);
		commandList.put("help", helpHelp);
		
		System.out.println("Dear master, your wish is my command");
		
		while (true) {
			Scanner sc = new Scanner(System.in);
			String [] input = sc.nextLine().split(";");
			getCommand(input);
		}
	}

}
