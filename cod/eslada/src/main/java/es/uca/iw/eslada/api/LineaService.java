package es.uca.iw.eslada.api;

import es.uca.iw.eslada.user.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LineaService {
    private LineaRepository lineaRepository;
    private ApiService apiService;

    public LineaService(LineaRepository lineaRepository, ApiService apiService){
        this.lineaRepository = lineaRepository;
        this.apiService = apiService;
    }

    public void addLinea(CustomerLine line, User user){
        Linea linea = new Linea();
        linea.setLine(line.getId());
        linea.setUser(user);
        this.lineaRepository.save(linea);
    }

    public void deleteLinea(CustomerLine line){
        //TODO: añadir borrado de las lineas prestadas
        this.lineaRepository.deleteByLineIs(line.getId());
    }

    public List<Linea> findAll(){
        return this.lineaRepository.findAll();
    }
}
