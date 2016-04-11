#!/usr/bin/env bats

task() {
  PATH_SEPARATOR="$(LANG=C java -help 2>&1 | grep -q 'A : separated list' && echo ':' || echo ';')"
  java -cp 'bin'"$PATH_SEPARATOR"'lib/*' jp.ac.tsukuba.cs.kde.hfukuda.lemma_finder.Main | sed 's/\r$//g'
}

@test '単語の原型を取得する' {
  run task <<'_EOT_'
Linked
Version4
Women
_EOT_
  [ "$status" -eq 0 ]
  [ "${lines[0]}" = $'\t'link$'\t'linked$'\t' ]
  [ "${lines[1]}" = version$'\t'$'\t'$'\t' ]
  [ "${lines[2]}" = woman$'\t'$'\t'$'\t' ]
}
