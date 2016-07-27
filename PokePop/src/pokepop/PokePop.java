package pokepop;

import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.auth.PtcLogin;

import POGOProtos.Networking.Envelopes.RequestEnvelopeOuterClass.RequestEnvelope.AuthInfo;
import ch.viascom.hipchat.api.HipChat;
import ch.viascom.hipchat.api.api.RoomsAPI;
import ch.viascom.hipchat.api.models.message.MessageColor;
import ch.viascom.hipchat.api.request.models.SendNotification;
import okhttp3.OkHttpClient;
import resources.HipchatProp;
import resources.PokeProp;
import threads.WatchForPokemon;

public class PokePop {
	static HipChat hc = new HipChat(HipchatProp.getApiKey(), HipchatProp.getBaseUrl());
	static RoomsAPI rooms = hc.roomsAPI();
	static {
		System.out.println(HipchatProp.getRoomName() + " " + HipchatProp.getBaseUrl() + HipchatProp.getApiKey());
	}
	public static PokemonGo go;

	public static boolean login(PokeProp prop) {
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


	public static void sendMessageToHipchat(String message) {
		try {
			SendNotification sendMessage = new SendNotification(HipchatProp.getRoomName(), message, MessageColor.RED,
					true);
			rooms.sendRoomNotification(sendMessage);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		try {
			PokeProp prop = new PokeProp();
			int Attempts = 0;
			while (!login(prop)) {
				Attempts++;
				System.err.println("Login Failed, Retrying.");
				// less likely to get banned this way.
				Thread.sleep(1000 * Attempts);
			}
			System.out.println("Logged in as: " + go.getPlayerProfile().getUsername());
			go.setLatitude(prop.getLat());
			go.setLongitude(prop.getLng());
			System.out.println("Currently located at: " + go.getLatitude() + " " + go.getLongitude());
			while (true) {
				WatchForPokemon.lookForPokemon();
				Thread.sleep(10000);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
