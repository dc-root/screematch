package br.com.alura.screenmatch.repository;

import br.com.alura.screenmatch.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SerieRepository extends JpaRepository<Serie, Long> {
    Optional<Serie> findByTituloContainingIgnoreCase(String nome);

    @Query("SELECT s FROM Serie s WHERE s.totalTemporadas >= :totalTemporadas AND s.avaliacao >= :avaliacao")
    List<Serie> findSerieByTemporadaAndAvaliacao(int totalTemporadas, double avaliacao);
}
