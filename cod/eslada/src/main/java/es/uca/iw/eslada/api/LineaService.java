package es.uca.iw.eslada.api;

import es.uca.iw.eslada.servicio.Servicio;
import es.uca.iw.eslada.servicio.ServicioType;
import es.uca.iw.eslada.user.User;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        linea.setBlock(false);
        linea.setRoaming(false);
        this.lineaRepository.save(linea);
    }
    @Transactional
    public void deleteLinea(CustomerLine line){
        //TODO: añadir borrado de las lineas prestadas
        apiService.deleteInfo(line.getId());
        this.lineaRepository.deleteAllByLine(line.getId());
    }

    public List<Linea> findAll(){
        return this.lineaRepository.findAll();
    }

    public List<Linea> findAllByUser(User user){
        return this.lineaRepository.findAllByUser(user);
    }

    public  Linea findByLine(String line){
        return this.lineaRepository.findByLineIs(line);
    }

    public boolean saveLinea(Linea linea) {
        try {
            lineaRepository.save(linea);
            return true;
        } catch (DataIntegrityViolationException e) {
            return false;
        }
    }
}
