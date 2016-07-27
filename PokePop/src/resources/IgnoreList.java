package resources;

import java.util.List;

import com.pokegoapi.api.map.pokemon.CatchablePokemon;

import POGOProtos.Enums.PokemonIdOuterClass.PokemonId;

public class IgnoreList {
	private static List<String> ignored;
	static {
		ignored = ResourceLoader.getResource("ignore.list");
		System.out.println("Ignoring: " + ignored.toString());
	}

	public static boolean filterOut(CatchablePokemon pokemon) {
		if (pokemon == null) // always ignore a nullmon!
			return false;
		PokemonId id = pokemon.getPokemonId();
		for (String i : ignored)
			if (i.equalsIgnoreCase(id.name())) {
				return false;
			}
		return true;
	}

}
