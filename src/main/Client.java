package main;

import guis.ClientGui;

public class Client {
	private final Data data;
	private final FuzzyCartAndPendulumSystem fuzzySystem;
	private final PIDCartAndPendulumSystem pidSystem;
	/*private final ClientGui clientGui;
	private BufferedWriter out;
	private BufferedReader in;*/

	public Client() {
		this.data = new Data(this);
		this.fuzzySystem = new FuzzyCartAndPendulumSystem(this);
		this.pidSystem = new PIDCartAndPendulumSystem(this);
		new ClientGui(this);
	}
	
	public Data getData() {
		return data;
	}
	
	public FuzzyCartAndPendulumSystem getFuzzySystem() {
		return fuzzySystem;
	}

	public PIDCartAndPendulumSystem getPIDSystem() {
		return pidSystem;
	}
	
	/*public void setOutputFile(String filePath, String fileName) throws IOException {
		this.out = new BufferedWriter(new FileWriter(filePath + fileName));
		//the named file either, exists and cannot be opened, does not exist and cannot be created, or exists but is a directory rather than a regular file.
	}
	
	public void outWriteResultsToFile(ArrayList<ResultElement> res) throws IOException {
		for (int i = 0; i < res.size(); i++) {
			out.write(res.get(i).toSavableString());
			out.newLine();
		}
	}*/
	
	public static void main(String[] args) {
		new Client();
	}
}
