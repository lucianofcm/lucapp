package br.com.lucapp.config;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import com.google.api.services.drive.model.File;

import br.com.lucapp.model.ArquivoDTO;

@Configuration
public class FileDownloadUtil {

	@Autowired
	public GoogleAuthorizationCodeFlowConfig googleAuthConfig;

	private Path foundFile;

	public Resource getFileAsResource(String fileCode) throws IOException {
		Path dirPath = Paths.get("Files-Upload");

		Files.list(dirPath).forEach(file -> {
			if (file.getFileName().toString().startsWith(fileCode)) {
				foundFile = file;
				return;
			}
		});

		if (foundFile != null) {
			return new UrlResource(foundFile.toUri());
		}

		return null;
	}

	public ArquivoDTO getFileDrive(String nomeArquivo) throws IOException {
		InputStream inputStream;
		ArquivoDTO arquivo;
		List<File> files = googleAuthConfig.getGoogleAuthorizationCondeFlow().files().list()
				.setFields("nextPageToken, files(id, name)").execute().getFiles();

		Optional<File> arquivoLocalizado = files.stream().filter(file -> file.getName().startsWith(nomeArquivo))
				.findAny();

		if (arquivoLocalizado.isPresent()) {
			inputStream = googleAuthConfig.getGoogleAuthorizationCondeFlow().files()
					.get(arquivoLocalizado.get().getId()).executeMediaAsInputStream();
			arquivo = new ArquivoDTO();
			arquivo.setArquivo(new InputStreamResource(inputStream));
			arquivo.setNomeArquivo(arquivoLocalizado.get().getName());

		} else {
			return null;
		}

		return arquivo;
	}

	public String setFolder(String nomePasta) throws IOException {

		File fileMetadata = new File();
		fileMetadata.setName(nomePasta);
		fileMetadata.setMimeType("application/vnd.google-apps.folder");
		File pastaCriada = googleAuthConfig.getGoogleAuthorizationCondeFlow().files().create(fileMetadata)
				.setFields("id").execute();
		return pastaCriada.getId();
	}
}