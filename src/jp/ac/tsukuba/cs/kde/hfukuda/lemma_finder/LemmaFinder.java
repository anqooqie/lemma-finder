package jp.ac.tsukuba.cs.kde.hfukuda.lemma_finder;

import java.util.regex.Pattern;

import net.didion.jwnl.JWNL;
import net.didion.jwnl.JWNLException;
import net.didion.jwnl.data.IndexWord;
import net.didion.jwnl.data.POS;
import net.didion.jwnl.dictionary.Dictionary;

public class LemmaFinder {
	private LemmaFinder() {}
	private static final Dictionary dictionary;
	static {
		try {
			JWNL.initialize(LemmaFinder.class.getClassLoader().getResourceAsStream("jp/ac/tsukuba/cs/kde/hfukuda/lemma_finder/file_properties.xml"));
		} catch (final JWNLException e) {
			throw new RuntimeException(e);
		}

		dictionary = Dictionary.getInstance();
	}

	private static final Pattern INCLUDING_NUMBER = Pattern.compile("[0-9]");
	public static final String findLemma(final String term, final POS pos) throws JWNLException {
		IndexWord indexWord = dictionary.lookupIndexWord(pos, term);
		// 見出し語が見つからなかった
		if (indexWord == null) return null;

		// 名詞の複数形は独立した見出し語になっている場合があるので、単数形が無いか再検索する
		if (pos == POS.NOUN && !INCLUDING_NUMBER.matcher(indexWord.getLemma()).find()) {
			indexWord = dictionary.getMorphologicalProcessor().lookupBaseForm(pos, indexWord.getLemma());
		}
		return indexWord.getLemma();
	}
}
