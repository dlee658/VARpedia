package helper;

import java.io.IOException;

public class BashCommand {
	
	/**
	 * Method for running a bash command
	 */
	public static Process runCommand(String cmd) {
		ProcessBuilder pb = new ProcessBuilder("bash", "-c", cmd);
		Process process = null;
		try {
			process = pb.start();
			process.waitFor();
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return process;
		
	}
}
