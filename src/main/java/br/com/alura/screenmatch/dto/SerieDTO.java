package br.com.alura.screenmatch.dto;

import java.util.List;

public record SerieDTO(
    Long id,
    String titulo,
    Integer totalTemporadas,
    Double avaliacao,
    String sinopse,
    List<String> atores,
    List<String> generos,
    String poster
) {
}
