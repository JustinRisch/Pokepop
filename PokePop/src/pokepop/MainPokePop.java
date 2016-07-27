package pokepop;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.api.map.pokemon.CatchablePokemon;
import com.pokegoapi.auth.PtcLogin;

import POGOProtos.Networking.Envelopes.RequestEnvelopeOuterClass.RequestEnvelope.AuthInfo;
import ch.viascom.hipchat.api.HipChat;
import ch.viascom.hipchat.api.api.RoomsAPI;
import ch.viascom.hipchat.api.request.models.SendMessage;
import okhttp3.OkHttpClient;
import resources.IgnoreList;
import resources.PokeProp;

public class MainPokePop {
	static List<CatchablePokemon> reportedAlready = new ArrayList<>();
	static SimpleDateFormat df = new SimpleDateFormat("hh:mm");
	static HipChat hc = new HipChat("1JyQK24FOvCcR4na5HXTZ0kjdbMxUjVqqGCOm2tN",
			"https://ipponusa.hipchat.com/v2/room/2968214/notification?auth_token=");
	static RoomsAPI rooms = hc.roomsAPI();

	private static void notifyHipchat(CatchablePokemon pokemon) {
		try {
			long expires = (pokemon.getExpirationTimestampMs() - Instant.now().toEpochMilli());
			Instant timeExpires = Instant.now().plusMillis(expires);
			String stringDate = df.format(Date.from(timeExpires));
			String message = pokemon.getPokemonId().name() + " EXPIRES: " + stringDate;
			System.out.println(message);
			SendMessage sendMessage = new SendMessage("PokePop", message);
			rooms.sendRoomMessage(sendMessage);
			reportedAlready.add(pokemon);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static boolean haventNotifiedYet(CatchablePokemon pokemon) {
		return !reportedAlready.contains(pokemon);
	}

	private static PokemonGo go;

	public static boolean login(PokeProp prop) throws InterruptedException {
		try {
			OkHttpClient httpClient = new OkHttpClient();
			AuthInfo auth = new PtcLogin(httpClient).login(prop.getUsername(), prop.getPassword());
			go = new PokemonGo(auth, httpClient);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static void main(String[] args) {
		try {
			PokeProp prop = new PokeProp("PokePop.properties");
			
			while (!login(prop)) {
				System.err.println("Login Failed, Retrying.");
				Thread.sleep(10000);
			}
			System.out.println("Logged in as: " + go.getPlayerProfile().getUsername());
			go.setLatitude(prop.getLat());
			go.setLongitude(prop.getLng());
			System.out.println("Currently located at: " + go.getLatitude() + " " + go.getLongitude());
			while (true) {
				go.getMap().getCatchablePokemon().stream().filter(IgnoreList::filterOut)
						.filter(MainPokePop::haventNotifiedYet).forEach(MainPokePop::notifyHipchat);
				Thread.sleep(10000);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
