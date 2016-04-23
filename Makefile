path_separator := $(shell LANG=C java -help 2>&1 | grep -q 'A : separated list' && echo ':' || echo ';')

bin: files/wordnet/dict lib/jwnl.jar lib/commons-logging.jar $(shell find src -name '*.java')
	rm -rf '$@'
	mkdir '$@'
	find src -name '*.java' -print0 | xargs -0 javac -cp '$@${path_separator}lib/*' -d '$@' -encoding UTF-8
	cat tmp/jwnl14-rc2/config/file_properties.xml | sed 's/<version publisher="Princeton" number="[^"]\+" language="en"\/>/<version publisher="Princeton" number="3.0" language="en"\/>/g' | sed 's/<param name="dictionary_path" value="[^"]\+"\/>/<param name="dictionary_path" value="files\/wordnet\/dict"\/>/g' >bin/jp/ac/tsukuba/cs/kde/hfukuda/lemma_finder/file_properties.xml
	echo 'org.apache.commons.logging.Log=org.apache.commons.logging.impl.NoOpLog' >bin/commons-logging.properties

files/wordnet/dict:
	curl -L -o tmp/WordNet-3.0.tar.gz 'http://wordnetcode.princeton.edu/3.0/WordNet-3.0.tar.gz'
	tar zxvf tmp/WordNet-3.0.tar.gz -C tmp
	rm tmp/WordNet-3.0.tar.gz
	cp -r tmp/WordNet-3.0/dict '$@'
	find '$@' -type f -print0 | xargs -0 chmod 644
	rm -rf tmp/WordNet-3.0

lib/jwnl.jar: tmp/jwnl14-rc2
	cp '$</jwnl.jar' '$@'

lib/commons-logging.jar: tmp/jwnl14-rc2
	cp '$</lib/commons-logging.jar' '$@'

tmp/jwnl14-rc2:
	curl -L -o tmp/jwnl14-rc2.zip 'http://downloads.sourceforge.net/project/jwordnet/jwnl/JWNL%201.4/jwnl14-rc2.zip'
	unzip tmp/jwnl14-rc2.zip -d tmp
	rm tmp/jwnl14-rc2.zip

lemma-finder.jar: bin
	jar cf '$@' -C '$<' .

.PHONY: test
test: bin local/bin/bats
	local/libexec/bats test/run.bats

local/bin/bats:
	git clone 'https://github.com/sstephenson/bats.git' tmp/bats
	tmp/bats/install.sh local
	rm -rf tmp/bats

.PHONY: clean
clean:
	git clean -X -d -f -f

.PHONY: force
force:
	$(MAKE) clean
	$(MAKE)
