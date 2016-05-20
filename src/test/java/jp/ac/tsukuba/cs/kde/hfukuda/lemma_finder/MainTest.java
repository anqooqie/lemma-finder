package jp.ac.tsukuba.cs.kde.hfukuda.lemma_finder;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import org.junit.BeforeClass;
import org.junit.Test;

import net.didion.jwnl.JWNLException;
import net.didion.jwnl.data.POS;

public class MainTest {
	@BeforeClass
	public static void setUpBeforeClass() throws IOException, JWNLException {
		final Path wordNetDictionaryPath = Paths.get("target/lemma-finder/wordnet/dictionary");
		WordNetDictionaryDownloader.downloadDictionary(wordNetDictionaryPath);
		LemmaFinder.initialize(wordNetDictionaryPath);
	}

	@Test
	public void testUsualWord() throws JWNLException {
		assertThat(LemmaFinder.findLemma("Linked", POS.NOUN), is(Optional.empty()));
		assertThat(LemmaFinder.findLemma("Linked", POS.VERB), is(Optional.of("link")));
		assertThat(LemmaFinder.findLemma("Linked", POS.ADJECTIVE), is(Optional.of("linked")));
		assertThat(LemmaFinder.findLemma("Linked", POS.ADVERB), is(Optional.empty()));
	}

	@Test
	public void testTrailingNumbers() throws JWNLException {
		assertThat(LemmaFinder.findLemma("Version4", POS.NOUN), is(Optional.of("version")));
		assertThat(LemmaFinder.findLemma("Version4", POS.VERB), is(Optional.empty()));
		assertThat(LemmaFinder.findLemma("Version4", POS.ADJECTIVE), is(Optional.empty()));
		assertThat(LemmaFinder.findLemma("Version4", POS.ADVERB), is(Optional.empty()));
	}

	@Test
	public void testIllegularInflection() throws JWNLException {
		assertThat(LemmaFinder.findLemma("Women", POS.NOUN), is(Optional.of("woman")));
		assertThat(LemmaFinder.findLemma("Women", POS.VERB), is(Optional.empty()));
		assertThat(LemmaFinder.findLemma("Women", POS.ADJECTIVE), is(Optional.empty()));
		assertThat(LemmaFinder.findLemma("Women", POS.ADVERB), is(Optional.empty()));
	}
}
