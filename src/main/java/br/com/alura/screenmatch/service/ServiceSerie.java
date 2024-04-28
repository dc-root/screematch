package br.com.alura.screenmatch.service;

import br.com.alura.screenmatch.dto.EpisodioDTO;
import br.com.alura.screenmatch.dto.SerieDTO;
import br.com.alura.screenmatch.model.Serie;
import br.com.alura.screenmatch.repository.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class ServiceSerie {
    @Autowired
    private SerieRepository serieRepository;

    public List<SerieDTO> buscarTodasAsSeries() {
        return converterToSerieDto(
            serieRepository.findAll()
        );
    }

    public List<SerieDTO> busarTopCincoSeries() {
        return converterToSerieDto(
            serieRepository.findTop5ByOrderByAvaliacao()
        );
    }

    public List<SerieDTO> buscarTopCincoLancamentos() {
        return converterToSerieDto(
            serieRepository.encontrarEpisodiosMaisRecentes()
        );
    }

    public SerieDTO buscarSeriePorId(Long id) {
        Optional<Serie> foundSerie = serieRepository.findById(id);

        if(foundSerie.isPresent()) {
            var serie = foundSerie.get();
            return new SerieDTO(
                serie.getId(),
                serie.getTitulo(),
                serie.getTotalTemporadas(),
                serie.getAvaliacao(),
                serie.getSinopse(),
                serie.getAtores(),
                serie.getGeneros(),
                serie.getPoster()
            );
        }

        return null;
    }

    public List<EpisodioDTO> buscarTodasTemporadas(Long id) {
        Optional<Serie> foundSerie = serieRepository.findById(id);

        if(foundSerie.isPresent()) {
            var serie = foundSerie.get();
            return serie.getEpisodios().stream()
                .map(episodio -> new EpisodioDTO(
                    episodio.getTemporada(),
                    episodio.getNumeroEpisodio(),
                    episodio.getTitulo()
                )).toList();
        }

        return null;
    }

    public List<EpisodioDTO> buscarTemporada(Long id, Integer numeroTemporada) {
        return serieRepository.encontrarEpisodiosPorTemporada(id, numeroTemporada).stream()
            .map(episodio -> new EpisodioDTO(
                    episodio.getTemporada(),
                    episodio.getNumeroEpisodio(),
                    episodio.getTitulo()
            )).toList();
    }

    private List<SerieDTO> converterToSerieDto(List<Serie> series) {
        return series.stream()
            .sorted(Comparator.comparing(Serie::getTitulo))
            .map(serie -> new SerieDTO(
                serie.getId(),
                serie.getTitulo(),
                serie.getTotalTemporadas(),
                serie.getAvaliacao(),
                serie.getSinopse(),
                serie.getAtores(),
                serie.getGeneros(),
                serie.getPoster()
            )).toList();
    }
}
