package biblioteca.biblioteca.Service;

import biblioteca.biblioteca.Dto.AutorResponseDto;
import biblioteca.biblioteca.Dto.LivroPostPutDto;
import biblioteca.biblioteca.Dto.LivroResponseDto;
import biblioteca.biblioteca.Model.Autor;
import biblioteca.biblioteca.Model.Livro;
import biblioteca.biblioteca.Repository.AutorRepository;
import biblioteca.biblioteca.Repository.LivroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Transactional
public class LivroCrudPadraoService implements LivroCrudService {

    @Autowired
    LivroRepository livroRepository;

    @Autowired
    AutorRepository autorRepository;

    @Override
    public LivroResponseDto criarLivro(LivroPostPutDto livroPostPutDto) {
        Livro livro = new Livro();
        preencherLivro(livro, livroPostPutDto);
        return converterResposta(livroRepository.save(livro));
    }

    @Override
    @Transactional(readOnly = true)
    public List<LivroResponseDto> buscarTodosLivros(String isbn, String titulo) {
        List<Livro> livros;
        if (isbn != null && titulo != null) {
            livros = livroRepository.findByIsbnContainingIgnoreCaseOrTituloContainingIgnoreCase(isbn, titulo);
        } else if (isbn != null) {
            livros = livroRepository.findByIsbnContainingIgnoreCase(isbn);
        } else if (titulo != null) {
            livros = livroRepository.findByTituloContainingIgnoreCase(titulo);
        } else {
            livros = livroRepository.findAll();
        }
        return livros.stream()
                .map(this::converterResposta)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public LivroResponseDto buscarLivro(Long id) {
        return converterResposta(obterLivroExistente(id));
    }

    @Override
    public LivroResponseDto atualizarLivro(Long id, LivroPostPutDto livroPostPutDto) {
        Livro livro = obterLivroExistente(id);
        preencherLivro(livro, livroPostPutDto);
        return converterResposta(livroRepository.save(livro));
    }

    @Override
    public void removerLivro(Long id) {
        livroRepository.delete(obterLivroExistente(id));
    }

    private void preencherLivro(Livro livro, LivroPostPutDto livroPostPutDto) {
        Autor autor = autorRepository.findById(livroPostPutDto.getAutorId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Autor não encontrado"));
        livro.setIsbn(livroPostPutDto.getIsbn());
        livro.setTitulo(livroPostPutDto.getTitulo());
        livro.setAutor(autor);
        livro.setAno(livroPostPutDto.getAno());
        livro.setEdicao(livroPostPutDto.getEdicao());
    }

    private Livro obterLivroExistente(Long id) {
        return livroRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Livro não encontrado"));
    }

    private LivroResponseDto converterResposta(Livro livro) {
        Autor autor = livro.getAutor();
        AutorResponseDto autorResponseDto = AutorResponseDto.builder()
                .id(autor.getId())
                .nomeAutor(autor.getNomeAutor())
                .nacionalidade(autor.getNacionalidade())
                .build();
        return LivroResponseDto.builder()
                .id(livro.getId())
                .isbn(livro.getIsbn())
                .titulo(livro.getTitulo())
                .autor(autorResponseDto)
                .ano(livro.getAno())
                .edicao(livro.getEdicao())
                .build();
    }
}
