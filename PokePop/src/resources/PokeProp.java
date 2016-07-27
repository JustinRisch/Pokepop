package resources;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class PokeProp {
	private String username;
	private String password;
	private Double lat;
	private Double lng;

	public PokeProp(String fileName) throws IOException {
		File file = new File(fileName);
		List<String> lines = Files.readAllLines(file.toPath());
		setUsername(lines.get(0));
		setPassword(lines.get(1));
		setLat(Double.parseDouble(lines.get(2)));
		setLng(Double.parseDouble(lines.get(3)));
	}

	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

	public Double getLng() {
		return lng;
	}

	public void setLng(Double lng) {
		this.lng = lng;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
