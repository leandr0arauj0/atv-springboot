package biblioteca.biblioteca.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LivroPostPutDto {
    @NotBlank(message = "ISBN não pode ser vazio")
    private String isbn;

    @NotBlank(message = "Título não pode ser vazio")
    private String titulo;

    @NotNull(message = "Autor deve ser informado")
    private Long autorId;

    @NotNull(message = "Ano deve ser informado")
    private Integer ano;

    @NotNull(message = "Edição deve ser informada")
    private Integer edicao;
}
