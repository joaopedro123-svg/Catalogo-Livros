package com.atividade.catalogo_livros.service;

import com.atividade.catalogo_livros.exception.RecursoNaoEncontradoException;
import com.atividade.catalogo_livros.model.Livro;
import com.atividade.catalogo_livros.repository.LivroRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LivroService {

    private static final Logger log = LoggerFactory.getLogger(LivroService.class);
    private final LivroRepository repository;

    public LivroService(LivroRepository repository) {
        this.repository = repository;
    }

    public Livro cadastrar(Livro livro) {
        if (livro.getPaginas() == null || livro.getPaginas() <= 0) {
            log.warn("Tentativa de cadastro com número de páginas inválido");
            throw new IllegalArgumentException("O número de páginas deve ser maior que zero.");
        }
        if (repository.existsByIsbn(livro.getIsbn())) {
            log.warn("Tentativa de cadastro com ISBN já existente: {}", livro.getIsbn());
            throw new IllegalArgumentException("Já existe um livro cadastrado com este ISBN.");
        }

        Livro salvo = repository.save(livro);
        log.info("Livro cadastrado com sucesso. ID={}", salvo.getId());
        return salvo;
    }

    public List<Livro> listarTodos() {
        log.info("Listando todos os livros cadastrados");
        return repository.findAll();
    }

    public Livro buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Livro não encontrado para o ID={}", id);
                    return new RecursoNaoEncontradoException("Livro não encontrado com ID: " + id);
                });
    }

    public Livro atualizar(Long id, Livro dadosAtualizados) {
        Livro existente = buscarPorId(id);

        if (dadosAtualizados.getPaginas() == null || dadosAtualizados.getPaginas() <= 0) {
            throw new IllegalArgumentException("O número de páginas deve ser maior que zero.");
        }

        existente.setTitulo(dadosAtualizados.getTitulo());
        existente.setAutor(dadosAtualizados.getAutor());
        existente.setIsbn(dadosAtualizados.getIsbn());
        existente.setAnoPublicacao(dadosAtualizados.getAnoPublicacao());
        existente.setPaginas(dadosAtualizados.getPaginas());

        Livro atualizado = repository.save(existente);
        log.info("Livro atualizado com sucesso. ID={}", atualizado.getId());
        return atualizado;
    }

    public void excluir(Long id) {
        Livro existente = buscarPorId(id);
        repository.delete(existente);
        log.info("Livro excluído com sucesso. ID={}", id);
    }
}