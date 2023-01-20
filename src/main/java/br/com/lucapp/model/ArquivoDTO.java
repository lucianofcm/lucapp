package br.com.lucapp.model;

import org.springframework.core.io.Resource;

public class ArquivoDTO {
	private Resource arquivo;
	private String nomeArquivo;

	public Resource getArquivo() {
		return arquivo;
	}

	public void setArquivo(Resource arquivo) {
		this.arquivo = arquivo;
	}

	public String getNomeArquivo() {
		return nomeArquivo;
	}

	public void setNomeArquivo(String nomeArquivo) {
		this.nomeArquivo = nomeArquivo;
	}
}
