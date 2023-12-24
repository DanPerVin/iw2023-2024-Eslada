package es.uca.iw.eslada.contrato;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContratoService {

    private final ContratoRepository contratoRepository;

    public ContratoService(ContratoRepository contratoRepository) {
        this.contratoRepository = contratoRepository;
    }

    public List<Contrato> findAll() {
        return contratoRepository.findAll();
    }


    public void delete(Contrato contrato) {
        contratoRepository.deleteById(contrato.getId());
    }
}
