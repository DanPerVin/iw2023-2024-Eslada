package es.uca.iw.eslada.tarifa;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TarifaService {

    private final TarifaRepository tarifaRepository;

    public TarifaService(TarifaRepository tarifaRepository) {
        this.tarifaRepository = tarifaRepository;
    }

    public List<Tarifa> findAll() {
        return tarifaRepository.findAll();
    }

    //TODO: Editar Tarifa
}
