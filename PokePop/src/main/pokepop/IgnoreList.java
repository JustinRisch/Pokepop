package main.pokepop;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import com.pokegoapi.api.map.pokemon.CatchablePokemon;

import POGOProtos.Enums.PokemonIdOuterClass.PokemonId;

public class IgnoreList {
	static List<String> ignored;
	static {
		try {
			ignored = Files.readAllLines(new File("ignore.list").toPath());
			System.out.println("Ignoring: " + ignored.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static boolean filterOut(CatchablePokemon pokemon) {
		if (pokemon == null) // always ignore a nullmon!
			return false;
		PokemonId id = pokemon.getPokemonId();
		for (String i : ignored)
			if (i.equalsIgnoreCase(id.name()))
				return false;
		return true;
	}

}
