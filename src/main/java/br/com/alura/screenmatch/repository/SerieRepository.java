package br.com.alura.screenmatch.repository;

import br.com.alura.screenmatch.model.Episodio;
import br.com.alura.screenmatch.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SerieRepository extends JpaRepository<Serie, Long> {
    Optional<Serie> findByTituloContainingIgnoreCase(String nome);

    @Query("SELECT s FROM Serie s WHERE s.totalTemporadas >= :totalTemporadas AND s.avaliacao >= :avaliacao")
    List<Serie> findSerieByTemporadaAndAvaliacao(int totalTemporadas, double avaliacao);

    @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE LOWER(e.titulo) = LOWER(:trechoDoEp)")
    List<Episodio> findEpisodiosPorTrecho(String trechoDoEp);

    @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE s.titulo = :nomeSerie ORDER BY e.avaliacao DESC LIMIT 5")
    List<Episodio> findTopEpisodiosPorSerie(String nomeSerie);

    @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE s.titulo = :nomeSerie AND YEAR(e.dataLancamento) >= :anoLancamento")
    List<Episodio> findEpisodiosPorSereEAno(String nomeSerie, int anoLancamento);
}