package br.com.lucapp.config;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.web.multipart.MultipartFile;

import com.google.api.client.http.FileContent;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

import br.com.lucapp.model.ArquivoDTO;

@Configuration
public class FileDownloadUtil {

	@Autowired
	public GoogleAuthorizationCodeFlowConfig googleAuthConfig;
	@Autowired
	ServletContext context;

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

	public void uploadArquivo(MultipartFile arquivo, String nomePastaDestino)
			throws IllegalStateException, IOException {

		java.io.File convFile = new java.io.File(System.getProperty("java.io.tmpdir") + "/" + arquivo.getName());
		File fileMetadata = new File();
		fileMetadata.setName(arquivo.getOriginalFilename());
		fileMetadata.setMimeType(arquivo.getContentType());

		arquivo.transferTo(convFile);
		FileContent mediaContent = new FileContent(fileMetadata.getMimeType(), convFile);
		fileMetadata.setParents(Collections.singletonList(pesquisarPasta(nomePastaDestino)));
		googleAuthConfig.getGoogleAuthorizationCondeFlow().files().create(fileMetadata, mediaContent).setFields("id")
				.execute();
	}

	public String setFolder(String nomePasta) throws IOException {

		File fileMetadata = new File();
		fileMetadata.setName(nomePasta);
		fileMetadata.setMimeType("application/vnd.google-apps.folder");
		File pastaCriada = googleAuthConfig.getGoogleAuthorizationCondeFlow().files().create(fileMetadata)
				.setFields("id").execute();
		return pastaCriada.getId();
	}

	public String pesquisarPasta(String nomePasta) throws IOException {
		String pageToken = null;

		do {
			FileList result = googleAuthConfig.getGoogleAuthorizationCondeFlow().files().list().setSpaces("drive")
					.setFields("nextPageToken, files(id, name)").setPageToken(pageToken).execute();
			Optional<File> arquivoLocalizado = result.getFiles().stream()
					.filter(file -> file.getName().startsWith(nomePasta)).findAny();
			if (arquivoLocalizado.isPresent()) {
				return arquivoLocalizado.get().getId();
			}

			pageToken = result.getNextPageToken();
		} while (pageToken != null);

		return null;
	}

}