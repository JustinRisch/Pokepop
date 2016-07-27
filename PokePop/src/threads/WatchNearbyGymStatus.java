package threads;

import java.text.SimpleDateFormat;

import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.RemoteServerException;

import pokepop.PokePop;


public class WatchNearbyGymStatus extends Thread {
	static SimpleDateFormat df = new SimpleDateFormat("hh:mm");
	@Override
	public void run() {
		while (true) {
			try {
				PokePop.go.getMap().getMapObjects().getGyms().stream().forEach(Gym -> {
					StringBuilder Message = new StringBuilder("The Gym is owned by ");
					Message.append(Gym.getOwnedByTeam());
					Message.append(" and guarded by a CP ");
					Message.append(Gym.getGuardPokemonCp());
					Message.append(" ");
					Message.append(Gym.getGuardPokemonId().name());
					Gym.getLastModifiedTimestampMs();

				});
			} catch (LoginFailedException | RemoteServerException e) {
				e.printStackTrace();
			}
		}
	}
}
