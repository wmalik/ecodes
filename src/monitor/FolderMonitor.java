/**
 * 
 */
package monitor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wasif malik
 * 
 *  Thread for monitoring the state of directories
 *
 * Usage: 	FolderMonitor fm = new FolderMonitor();
			fm.start();
 */
public class FolderMonitor extends Thread{
	public FolderMonitor() {
		//Nothing here at the moment
	}

	/* For Testing Only
	public static void main(String[] args) {

		FolderMonitor fm = new FolderMonitor();
		fm.start();
	}
	*/

	public void run() {

		while(true) {

			try {
				Thread.sleep(2*1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			if (getFailedNodes() == null) {
				System.out.println("All nodes are up and running");
			}
			else {
				List<File> failed = getFailedNodes();
				for (int i = 0; i < failed.size(); i++) {
					System.out.println("Failed node: "+failed.get(i).getName());
				}
			}
		}



	}

	/**
	 *  Scans the node directories and returns a list of failed nodes. 
	 *  returns null if all directories are up
	 * 
	 * */
	private List<File> getFailedNodes() {

		File[] nodes = new File[5];
		List<File> failedNodes = new ArrayList<File>(5);

		nodes[0] = new File("bin/nodes/n0");
		nodes[1] = new File("bin/nodes/n1");
		nodes[2] = new File("bin/nodes/n2");
		nodes[3] = new File("bin/nodes/n3");
		nodes[4] = new File("bin/nodes/n4");

		for (int i = 0; i < nodes.length; i++) {
			if(!nodes[i].exists()) {
				failedNodes.add(nodes[i]);
			}
		}
		return failedNodes.size() > 0 ? failedNodes : null;
	}
}
