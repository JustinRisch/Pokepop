package main.pokepop;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.api.map.pokemon.CatchablePokemon;
import com.pokegoapi.auth.PtcLogin;

import POGOProtos.Networking.Envelopes.RequestEnvelopeOuterClass.RequestEnvelope.AuthInfo;
import okhttp3.OkHttpClient;

public class MainPokePop {
	static List<CatchablePokemon> reportedAlready = new ArrayList<>();
	static SimpleDateFormat df = new SimpleDateFormat("hh:mm");

	private static void notifyHipchat(CatchablePokemon pokemon) {
		long expires = (pokemon.getExpirationTimestampMs() - Instant.now().toEpochMilli());
		Instant timeExpires = Instant.now().plusMillis(expires);
		String stringDate = df.format(Date.from(timeExpires));
		String message = pokemon.getPokemonId().name() + " EXPIRES: " + stringDate;
		// TODO get hipchat to also recieve this message.
		System.out.println(message);
		reportedAlready.add(pokemon);
	}

	private static boolean haventNotifiedYet(CatchablePokemon pokemon) {
		return !reportedAlready.contains(pokemon);
	}

	public static void main(String[] args) {
		try {
			OkHttpClient httpClient = new OkHttpClient();
			PokeProp prop = new PokeProp("PokePop.properties");
			AuthInfo auth = new PtcLogin(httpClient).login(prop.username, prop.password);
			PokemonGo go = new PokemonGo(auth, httpClient);
			System.out.println("Logged in as: " + go.getPlayerProfile().getUsername());
			go.setLatitude(prop.lat);
			go.setLongitude(prop.lng);
			System.out.println("Currently located at: " + go.getLatitude() + " " + go.getLongitude());
			while (true) {
				go.getMap().getCatchablePokemon().stream().filter(MainPokePop::haventNotifiedYet)
						.forEach(MainPokePop::notifyHipchat);
				Thread.sleep(10000);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
