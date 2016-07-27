package resources;

import java.io.IOException;
import java.util.List;

public class PokeProp {
	private String username;
	private String password;
	private Double lat;
	private Double lng;

	/*
	 * In the resource folder, you must create a "PokePop.properties" which
	 * includes, on separate lines, your: 
	 * 	1. PTC Username 
	 * 	2. PTC Password 
	 * 	3. Latitude 
	 * 	4. Longitude
	 */
	public PokeProp() throws IOException {
		List<String> lines = ResourceLoader.getResource("PokePop.properties");
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
