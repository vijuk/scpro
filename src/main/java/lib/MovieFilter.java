package lib;

/**
 * Insert the type's description here.
 * Creation date: (11/25/00 9:09:58 PM)
 * @author: 
 */
public class MovieFilter extends javax.swing.filechooser.FileFilter {
/**
 * FileFilter constructor comment.
 */
public MovieFilter() {
	super();
}
/**
 * accept method comment.
 */
public boolean accept(java.io.File file) {
	if (file.isDirectory()) {
		return true;
	}

	if (file.getName().toUpperCase().endsWith(".MOV")) {
		return true;
	}
	return false;
}
	public String getDescription()
	{
		return "MOV files";
	}
}
