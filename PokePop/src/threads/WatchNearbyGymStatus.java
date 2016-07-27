package threads;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.RemoteServerException;

import POGOProtos.Map.Fort.FortDataOuterClass.FortData;
import pokepop.PokePop;

public class WatchNearbyGymStatus {
	static SimpleDateFormat df = new SimpleDateFormat("EEE hh:mm");
	static List<FortData> notifiedAlready = new ArrayList<>();

	private static boolean filter(FortData f) {
		return !notifiedAlready.contains(f);
	}

	public static void checkGymStatus() {
		try {
			PokePop.go.getMap().getMapObjects().getGyms().stream().filter(WatchNearbyGymStatus::filter).forEach(Gym -> {
				StringBuilder Message = new StringBuilder("The Gym is owned by ");
				Message.append(Gym.getOwnedByTeam());
				Message.append(" and guarded by a CP ");
				Message.append(Gym.getGuardPokemonCp());
				Message.append(" ");
				Message.append(Gym.getGuardPokemonId().name());
				long modified = (Gym.getLastModifiedTimestampMs() - Instant.now().toEpochMilli());
				Instant timeModified = Instant.now().plusMillis(modified);
				String stringDate = df.format(Date.from(timeModified));
				Message.append(" since ");
				Message.append(stringDate);
				// PokePop.sendMessageToHipchat(Message.toString());
			});
		} catch (LoginFailedException | RemoteServerException e) {
			e.printStackTrace();
		}
	}
}
