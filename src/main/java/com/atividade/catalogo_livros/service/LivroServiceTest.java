package com.atividade.catalogo_livros.service;

import com.atividade.catalogo_livros.exception.RecursoNaoEncontradoException;
import com.atividade.catalogo_livros.model.Livro;
import com.atividade.catalogo_livros.repository.LivroRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LivroServiceTest {

    @Mock
    private LivroRepository repository;

    @InjectMocks
    private LivroService service;

    @Test
    void deveCadastrarLivroValido() {
        // Arrange
        Livro livroNovo = new Livro(null, "Dom Casmurro", "Machado de Assis", "978-8535914849", 1899, 256);
        Livro livroSalvo = new Livro(1L, "Dom Casmurro", "Machado de Assis", "978-8535914849", 1899, 256);

        when(repository.existsByIsbn("978-8535914849")).thenReturn(false);
        when(repository.save(livroNovo)).thenReturn(livroSalvo);

        // Act
        Livro resultado = service.cadastrar(livroNovo);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Dom Casmurro", resultado.getTitulo());
        verify(repository, times(1)).existsByIsbn("978-8535914849");
        verify(repository, times(1)).save(livroNovo);
    }

    @Test
    void deveBuscarLivroPorIdComSucesso() {
        // Arrange
        Livro livro = new Livro(1L, "O Hobbit", "J.R.R. Tolkien", "978-8595084742", 1937, 336);
        when(repository.findById(1L)).thenReturn(Optional.of(livro));

        // Act
        Livro resultado = service.buscarPorId(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals("O Hobbit", resultado.getTitulo());
        assertEquals("J.R.R. Tolkien", resultado.getAutor());
        verify(repository, times(1)).findById(1L);
    }

    @Test
    void deveLancarExcecaoAoCadastrarLivroComPaginasInvalidas() {
        // Arrange
        Livro livroInvalido = new Livro(null, "Livro Sem Páginas", "Autor", "111-1111111111", 2024, 0);

        // Act & Assert
        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> service.cadastrar(livroInvalido)
        );

        assertEquals("O número de páginas deve ser maior que zero.", excecao.getMessage());
        verify(repository, never()).save(any());
    }

    @Test
    void deveLancarExcecaoAoCadastrarLivroComIsbnDuplicado() {
        // Arrange
        Livro livroDuplicado = new Livro(null, "Clean Code", "Robert C. Martin", "978-0132350884", 2008, 464);
        when(repository.existsByIsbn("978-0132350884")).thenReturn(true);

        // Act & Assert
        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> service.cadastrar(livroDuplicado)
        );

        assertEquals("Já existe um livro cadastrado com este ISBN.", excecao.getMessage());
        verify(repository, never()).save(any());
    }

    @Test
    void deveLancarExcecaoAoBuscarLivroInexistente() {
        // Arrange
        when(repository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        RecursoNaoEncontradoException excecao = assertThrows(
                RecursoNaoEncontradoException.class,
                () -> service.buscarPorId(99L)
        );

        assertEquals("Livro não encontrado com ID: 99", excecao.getMessage());
        verify(repository, times(1)).findById(99L);
    }
}