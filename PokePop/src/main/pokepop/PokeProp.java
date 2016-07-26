package main.pokepop;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class PokeProp {
	String username, password;
	Double lat, lng;

	public PokeProp(String fileName) throws IOException {
		File file = new File(fileName);
		List<String> lines = Files.readAllLines(file.toPath());
		username = lines.get(0);
		password = lines.get(1);
		lat = Double.parseDouble(lines.get(2));
		lng = Double.parseDouble(lines.get(3));
	}
}
