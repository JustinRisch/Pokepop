package threads;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.pokegoapi.api.map.pokemon.CatchablePokemon;

import pokepop.PokePop;
import resources.IgnoreList;

public class WatchForPokemon {
	static List<CatchablePokemon> reportedAlready = new ArrayList<>();
	static SimpleDateFormat df = new SimpleDateFormat("hh:mm");

	private static void notifyHipchat(CatchablePokemon pokemon) {
		try {
			long expires = (pokemon.getExpirationTimestampMs() - Instant.now().toEpochMilli());
			Instant timeExpires = Instant.now().plusMillis(expires);
			String stringDate = df.format(Date.from(timeExpires));
			String message = pokemon.getPokemonId().name() + " EXPIRES: " + stringDate;
			System.out.println(message);
			PokePop.sendMessageToHipchat(message);
			reportedAlready.add(pokemon);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static boolean haventNotifiedYet(CatchablePokemon pokemon) {
		return !reportedAlready.contains(pokemon);
	}

	public static void lookForPokemon() {
		try {
			PokePop.go.getMap().getCatchablePokemon().stream().filter(IgnoreList::filterOut)
					.filter(WatchForPokemon::haventNotifiedYet).forEach(WatchForPokemon::notifyHipchat);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
