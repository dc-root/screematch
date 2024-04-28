package br.com.alura.screenmatch.controller;

import br.com.alura.screenmatch.dto.EpisodioDTO;
import br.com.alura.screenmatch.dto.SerieDTO;
import br.com.alura.screenmatch.service.ServiceSerie;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/series")
public class SerieController {
    private final ServiceSerie serviceSerie;

    public SerieController(ServiceSerie serviceSerie) {
        this.serviceSerie = serviceSerie;
    }

    @GetMapping
    public List<SerieDTO> obterSeries() {
        return serviceSerie.buscarTodasAsSeries();
    }

    @GetMapping("/top5")
    public List<SerieDTO> obterTopCincoSeries() {
        return serviceSerie.busarTopCincoSeries();
    }

    @GetMapping("/lancamentos")
    public List<SerieDTO> obterTopCincoLancamentos() {
        return serviceSerie.buscarTopCincoLancamentos();
    }

    @GetMapping("/{id}")
    public SerieDTO obterPorId(@PathVariable Long id) {
        return serviceSerie.buscarSeriePorId(id);
    }

    @GetMapping("/{id}/temporadas/todas")
    public List<EpisodioDTO> obterTodasAsTemporadas(@PathVariable Long id) {
        return serviceSerie.buscarTodasTemporadas(id);
    }

    @GetMapping("/{id}/temporadas/{numero}")
    public List<EpisodioDTO> obterTemporadasPorNumero(@PathVariable Long id, @PathVariable Integer numero) {
        return serviceSerie.buscarTemporada(id, numero);
    }
}
