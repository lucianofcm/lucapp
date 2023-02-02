package br.com.lucapp.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.api.services.drive.model.File;

import br.com.lucapp.config.FileDownloadUtil;
import br.com.lucapp.config.GoogleAuthorizationCodeFlowConfig;
import br.com.lucapp.model.ArquivoDTO;

@RestController
@RequestMapping("drive")
public class DriveController {

	@Autowired
	public GoogleAuthorizationCodeFlowConfig googleAuthConfig;

	@Autowired
	private FileDownloadUtil fileDownloadUtil;

	@GetMapping("/helloMessage")
	public String greetingMessage() throws IOException {
		return "Welcome to Hello Spring Boot Application!";
	}

	@GetMapping("/download/{nomeArquivo}")
	public ResponseEntity<?> download(@PathVariable("nomeArquivo") String nomeArquivo) throws IOException {

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.IMAGE_JPEG);
		ArquivoDTO arquivo = fileDownloadUtil.getFileDrive(nomeArquivo);

		if (arquivo == null) {
			return new ResponseEntity<>("Arquivo não encontrado", HttpStatus.NOT_FOUND);
		}

		// String headerValue = "attachment; filename=\"" + arquivo.getNomeArquivo() +
		// "\"";
		// headers.set(HttpHeaders.CONTENT_DISPOSITION, headerValue);

		return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).headers(headers).body(arquivo.getArquivo());

	}

	@GetMapping("/pasta/{nomePasta}")
	public ResponseEntity<?> criarPasta(@PathVariable("nomePasta") String nomePasta) throws IOException {

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.TEXT_PLAIN);
		String arquivo = fileDownloadUtil.setFolder(nomePasta);

		if (arquivo == null) {
			return new ResponseEntity<>("Pasta não criada", HttpStatus.METHOD_NOT_ALLOWED);
		}

		return ResponseEntity.ok().contentType(MediaType.TEXT_PLAIN).headers(headers).body(arquivo);

	}

	@PostMapping(value="/upload", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
	public String handleFileUpload( @RequestBody MultipartFile[] file,
			@RequestParam("pastaDestino") String pastaDestino) throws IllegalStateException, IOException {

		System.out.println("ENTROU");
		//fileDownloadUtil.uploadArquivo(uploadFile,pastaDestino);

		return "redirect:/";
	}

	@GetMapping("/pasta/get/{nomePasta}")
	public ResponseEntity<?> pesquisarPasta(@PathVariable("nomePasta") String nomePasta) throws IOException {

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.TEXT_PLAIN);
		String files = fileDownloadUtil.pesquisarPasta(nomePasta);

		if (files.isEmpty()) {
			return new ResponseEntity<>("Pasta não criada", HttpStatus.METHOD_NOT_ALLOWED);
		}

		return ResponseEntity.ok().contentType(MediaType.TEXT_PLAIN).headers(headers).body("");

	}

}
