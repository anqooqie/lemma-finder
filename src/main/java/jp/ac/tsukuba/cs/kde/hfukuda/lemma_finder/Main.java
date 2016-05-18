package jp.ac.tsukuba.cs.kde.hfukuda.lemma_finder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import net.didion.jwnl.JWNLException;
import net.didion.jwnl.data.POS;

public final class Main {
	private Main() {}

	private static class CommandLineOption {
		private final Path wordNetDictionaryPath;

		public CommandLineOption(final String[] args) throws IOException {
			if (args.length != 1) throw new IllegalArgumentException();
			this.wordNetDictionaryPath = Paths.get(args[0]).toRealPath();
			if (!Files.isDirectory(this.wordNetDictionaryPath)) throw new IllegalArgumentException();
		}
	}

	public static void main(final String[] args) throws IOException, JWNLException {
		CommandLineOption option;
		try {
			option = new CommandLineOption(args);
		} catch (final IllegalArgumentException e) {
			System.err.println("usage: java -cp <classpath> " + Main.class.getCanonicalName() + " <wordnet_dictionary_path>");
			System.exit(1);
			return;
		}

		LemmaFinder.initialize(option.wordNetDictionaryPath);
		try (final InputStreamReader isr = new InputStreamReader(System.in, StandardCharsets.UTF_8);
				final BufferedReader br = new BufferedReader(isr)) {
			String line;
			while ((line = br.readLine()) != null) {
				final List<String> lemmas = new ArrayList<>();
				for (final POS pos : new POS[]{POS.NOUN, POS.VERB, POS.ADJECTIVE, POS.ADVERB}) {
					lemmas.add(LemmaFinder.findLemma(line, pos).orElse(""));
				}
				System.out.println(String.join("\t", lemmas));
			}
		}
	}
}
