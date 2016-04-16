package jp.ac.tsukuba.cs.kde.hfukuda.lemma_finder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import net.didion.jwnl.JWNLException;
import net.didion.jwnl.data.POS;

public final class Main {
	private Main() {}

	public static void main(final String[] args) throws IOException, JWNLException {
		try (final InputStreamReader isr = new InputStreamReader(System.in, StandardCharsets.UTF_8);
				final BufferedReader br = new BufferedReader(isr)) {
			String line;
			while ((line = br.readLine()) != null) {
				final List<String> lemmas = new ArrayList<>();
				for (final POS pos : new POS[]{POS.NOUN, POS.VERB, POS.ADJECTIVE, POS.ADVERB}) {
					lemmas.add(Optional.ofNullable(LemmaFinder.findLemma(line, pos)).orElse(""));
				}
				System.out.println(String.join("\t", lemmas));
			}
		}
	}
}
