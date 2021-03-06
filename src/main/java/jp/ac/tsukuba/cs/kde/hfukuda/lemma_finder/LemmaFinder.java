package jp.ac.tsukuba.cs.kde.hfukuda.lemma_finder;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Optional;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;

import net.didion.jwnl.JWNL;
import net.didion.jwnl.JWNLException;
import net.didion.jwnl.data.IndexWord;
import net.didion.jwnl.data.POS;
import net.didion.jwnl.dictionary.Dictionary;

public class LemmaFinder {
	private LemmaFinder() {}

	private static Dictionary dictionary;
	public static void initialize(final Path wordNetDictionaryPath) throws IOException, JWNLException {
		final InputStream is = LemmaFinder.class.getClassLoader().getResourceAsStream("jp/ac/tsukuba/cs/kde/hfukuda/lemma_finder/file_properties.xml");
		final String xml = IOUtils.toString(is, StandardCharsets.UTF_8).replace("$wordNetDictionaryPath$", wordNetDictionaryPath.toString());
		JWNL.initialize(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));
		dictionary = Dictionary.getInstance();
	}

	private static final Pattern INCLUDING_NUMBER = Pattern.compile("[0-9]");
	public static final Optional<String> findLemma(final String term, final POS pos) throws JWNLException {
		if (dictionary == null) throw new IllegalStateException("LemmaFinderは未初期化です。");

		synchronized (dictionary) {
			IndexWord indexWord = dictionary.lookupIndexWord(pos, term);
			// 見出し語が見つからなかった
			if (indexWord == null) return Optional.empty();

			// 名詞の複数形は独立した見出し語になっている場合があるので、単数形が無いか再検索する
			if (pos == POS.NOUN && !INCLUDING_NUMBER.matcher(indexWord.getLemma()).find()) {
				indexWord = dictionary.getMorphologicalProcessor().lookupBaseForm(pos, indexWord.getLemma());
			}
			return Optional.of(indexWord.getLemma());
		}
	}
}
