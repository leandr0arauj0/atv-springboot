package biblioteca.biblioteca.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LivroResponseDto {
    private Long id;
    private String isbn;
    private String titulo;
    private AutorResponseDto autor;
    private Integer ano;
    private Integer edicao;
}
