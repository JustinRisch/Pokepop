package pokepop;

import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.auth.PtcLogin;

import POGOProtos.Networking.Envelopes.RequestEnvelopeOuterClass.RequestEnvelope.AuthInfo;
import ch.viascom.hipchat.api.HipChat;
import ch.viascom.hipchat.api.api.RoomsAPI;
import ch.viascom.hipchat.api.exception.APIException;
import ch.viascom.hipchat.api.request.models.SendMessage;
import ch.viascom.hipchat.api.request.models.SetTopic;
import okhttp3.OkHttpClient;
import resources.HipchatProp;
import resources.PokeProp;
import threads.WatchForPokemon;
import threads.WatchNearbyGymStatus;

public class PokePop {
	static HipChat hc = new HipChat(HipchatProp.getApiKey(), HipchatProp.getBaseUrl());
	static RoomsAPI rooms = hc.roomsAPI();
	static {
		System.out.println(HipchatProp.getRoomName() + " " + HipchatProp.getBaseUrl() + HipchatProp.getApiKey());
	}
	public static PokemonGo go;

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

	// TODO fix
	public static void updateRoomStatus(String status) {
		try {
			System.out.println("Setting Topic: " + status);
			SetTopic topic = new SetTopic(HipchatProp.getRoomName(), status);
			rooms.setTopic(topic);
		} catch (APIException e) {
			e.printStackTrace();
		}
	}

	public static void sendMessageToHipchat(String message) {
		try {
			SendMessage sendMessage = new SendMessage(HipchatProp.getRoomName(), message);
			rooms.sendRoomMessage(sendMessage);
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
				WatchNearbyGymStatus.checkGymStatus();
				Thread.sleep(10000);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
