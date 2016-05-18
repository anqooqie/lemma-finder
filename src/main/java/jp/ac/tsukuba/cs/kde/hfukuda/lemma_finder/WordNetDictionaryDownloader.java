package jp.ac.tsukuba.cs.kde.hfukuda.lemma_finder;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class WordNetDictionaryDownloader {
	private WordNetDictionaryDownloader() {}

	public static void downloadDictionary(final Path dest) throws IOException {
		final Path tmpDir = Files.createTempDirectory("working");

		try {
			try (final CloseableHttpClient client = HttpClients.createDefault();
					final CloseableHttpResponse response = client.execute(new HttpGet("http://wordnetcode.princeton.edu/3.0/WordNet-3.0.tar.gz"))) {
				final int status = response.getStatusLine().getStatusCode();
				if (status >= 200 && status < 300) {
					final HttpEntity entity = response.getEntity();
					if (entity == null) throw new ClientProtocolException("Entity is empty");
					Files.write(tmpDir.resolve("WordNet-3.0.tar.gz"), EntityUtils.toByteArray(entity));
				} else {
					throw new ClientProtocolException("Unexpected response status: " + status);
				}
			}

			try (final InputStream inputStream = Files.newInputStream(tmpDir.resolve("WordNet-3.0.tar.gz"));
					final InputStream gzipStream = new GzipCompressorInputStream(inputStream);
					final TarArchiveInputStream tarStream = new TarArchiveInputStream(gzipStream)) {
				for (TarArchiveEntry tarEntry = tarStream.getNextTarEntry(); tarEntry != null; tarEntry = tarStream.getNextTarEntry()) {
					final Path path = tmpDir.resolve(tarEntry.getName());
					if (tarEntry.isDirectory()) {
						Files.createDirectories(path);
					} else {
						Files.write(path, IOUtils.toByteArray(tarStream));
					}
				}
			}

			FileUtils.moveDirectory(tmpDir.resolve("WordNet-3.0/dict").toFile(), dest.toFile());
		} finally {
			FileUtils.deleteDirectory(tmpDir.toFile());
		}
	}
}
