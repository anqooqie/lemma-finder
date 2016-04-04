# lemma-finder

[![Build Status](https://travis-ci.org/anqooqie/lemma-finder.svg)](https://travis-ci.org/anqooqie/lemma-finder)

This program shows lemmas of each word using WordNet.

## Usage
```bash
git clone https://github.com/anqooqie/lemma-finder.git
cd lemma-finder
make
java -cp 'bin:lib/*' jp.ac.tsukuba.cs.kde.hfukuda.lemma_finder.Main <input >output
```

## Example of Input
    Linked
    Version4
    Women

## Example of Output
    	link	linked	
    version			
    woman			

## Note
The following script outputs only lemmas as nouns.

```bash
java -cp 'bin:lib/*' jp.ac.tsukuba.cs.kde.hfukuda.lemma_finder.Main <input | cut -f 1 >output
```

The following script outputs only lemmas as verbs.

```bash
java -cp 'bin:lib/*' jp.ac.tsukuba.cs.kde.hfukuda.lemma_finder.Main <input | cut -f 2 >output
```

The following script outputs only lemmas as adjectives.

```bash
java -cp 'bin:lib/*' jp.ac.tsukuba.cs.kde.hfukuda.lemma_finder.Main <input | cut -f 3 >output
```

The following script outputs only lemmas as adverbs.

```bash
java -cp 'bin:lib/*' jp.ac.tsukuba.cs.kde.hfukuda.lemma_finder.Main <input | cut -f 4 >output
```
