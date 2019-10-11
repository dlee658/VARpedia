package application;

import java.io.File;
import java.util.Date;

public class Creation {
	private String _term;
	private String _name;
	private File creationFile;
	private File termlessFile;
	private Date createdDate;

	public Creation(String term, String name) {
		_term = term;
		_name = name;
		creationFile = new File("Creation" + File.separatorChar + term + ".mp4");
		termlessFile = new File ("Creation"+ File.separatorChar + term + "NT.mp4");
		createdDate = new Date();
	}
	
	public String getTerm() {
		return _term;
	}
	
	public File getCreation() {
		return creationFile;
	}
	
	@Override
	public String toString() {
		return _name;
	}
	
	
}
