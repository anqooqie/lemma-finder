# lemma-finder

[![Build Status](https://travis-ci.org/anqooqie/lemma-finder.svg)](https://travis-ci.org/anqooqie/lemma-finder)

This program shows lemmas of each word using WordNet.

## Usage
```bash
git clone https://github.com/anqooqie/lemma-finder.git
cd lemma-finder
mvn package dependency:copy-dependencies -Dmaven.test.skip=true
java -jar target/lemma-finder-"$(mvn help:evaluate -Dexpression=project.version | grep -v INFO)".jar /path/to/wordnet/dictionary <input >output
```

## Example of Input
    Linked
    Version4
    Women

## Example of Output
    	link	linked	
    version			
    woman			

The output has four tab-separated fields.
The first field shows lemmas as nouns.
The second field shows lemmas as verbs.
The third field shows lemmas as adjectives.
The fourth field shows lemmas as adverbs.
