package br.com.alura.screenmatch.principal;

import br.com.alura.screenmatch.model.*;
import br.com.alura.screenmatch.repository.SerieRepository;
import br.com.alura.screenmatch.service.ConsumoApi;
import br.com.alura.screenmatch.service.ConverteDados;

import java.util.*;

public class Principal {

    private Scanner leitura = new Scanner(System.in);
    private ConsumoApi consumo = new ConsumoApi();
    private ConverteDados conversor = new ConverteDados();

    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=6585022c";

    private SerieRepository serieRepository;

    private List<Serie> series = new ArrayList<>();

    public Principal(SerieRepository serieRepository) {
        this.serieRepository = serieRepository;
    }

    public void exibeMenu() {
        int option = -1;

        do {
            System.out.println("""
                    1 - Buscar e registrar série
                    2 - Buscar episódios de série
                    3 - Listar series registradas
                    4 - Buscar serie por titulo
                    5 - Buscar series por temporada e avaliação
                    0 - Sair
                    """);
            System.out.print(": ");

            option = leitura.nextInt();
            leitura.nextLine();

            switch (option) {
                case 1:
                    registrarSerie(
                        buscarDadosSerieNaWeb()
                    );
                    break;
                case 2:
                    listarEpisodiosDeSerie();
                    break;
                case 3:
                    listarSeriesBuscadas();
                    break;
                case 4:
                    buscarSeriePorTitulo();
                    break;
                case 5:
                    listarSeriesPorTemporadasEAvaliacao();
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção invalida!");
            }
        } while(option != 0);
    }

    private void registrarSerie(DadosSerie dados) {
        Serie newSerie = new Serie(dados);

        List<Episodio> episodios = buscarEpisodios(
                buscarTemporadas(dados)
        );

        newSerie.setEpisodios(episodios);

        serieRepository.save(newSerie);
        System.out.println(dados);
    }

    private DadosSerie buscarDadosSerieNaWeb() {
        System.out.println("Digite o nome da série para buscar: ");
        var nomeSerie = leitura.nextLine();

        var json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);

        return dados;
    }

    private void listarSeriesBuscadas() {
        series = serieRepository.findAll();
        series.stream()
                .sorted(Comparator.comparing(Serie::getTitulo))
                .forEach(System.out::println);
    }

    private void listarEpisodiosDeSerie() {
        listarSeriesBuscadas();

        System.out.println("De qual serie?");
        var nomeSerie = leitura.nextLine();

        Optional<Serie> foundSerie = serieRepository.findByTituloContainingIgnoreCase(nomeSerie);

        if(foundSerie.isPresent()) {
            Serie dadoSerie = foundSerie.get();

            dadoSerie.getEpisodios().forEach(System.out::println);
        } else {
            System.out.println("Série não encontrada!!");
        }
    }

    private void buscarSeriePorTitulo() {
        System.out.println("De qual série?");
        var nomeSerie = leitura.nextLine();

        Optional<Serie> foundSerie = serieRepository.findByTituloContainingIgnoreCase(nomeSerie);

        if(foundSerie.isPresent()) {
            var serie = foundSerie.get();
            System.out.println("Série encontrada: "+ serie);
        } else {
            System.out.println("Série não encontrada!!");
        }
    }

    private List<DadosTemporada> buscarTemporadas(DadosSerie dados) {
        List<DadosTemporada> temporadas = new ArrayList<>();

        for (int i = 1; i<=dados.totalTemporadas(); i++) {
            var json = consumo.obterDados(ENDERECO + dados.titulo().replace(" ", "+") +"&season=" + i + API_KEY);
            DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
            temporadas.add(dadosTemporada);
        }

        return temporadas;
    }
    private List<Episodio> buscarEpisodios(List<DadosTemporada> temporadas) {
        List<Episodio> episodios = temporadas.stream()
                .flatMap(temporada -> temporada.episodios().stream()
                        .map(dadosEpisodio -> new Episodio(temporada.numero(), dadosEpisodio)))
                .toList();

        return episodios;
    }

    private void listarSeriesPorTemporadasEAvaliacao() {
        System.out.println("Qual é o total de temporadas? ");
        var totalTemporada = leitura.nextInt();

        System.out.println("Qual é a avaliação minima da serie? ");
        var serieAvaliacao = leitura.nextDouble();

        List<Serie> series = serieRepository.findSerieByTemporadaAndAvaliacao(totalTemporada, serieAvaliacao);

        series.forEach(serie -> {
            System.out.println(serie.getTitulo()+" | "+serie.getTotalTemporadas()+" | "+serie.getAvaliacao());
        });
    }
}