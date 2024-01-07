package es.uca.iw.eslada.api;

import elemental.json.Json;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class ApiService {
    private final RestTemplate restTemplate;

    @Autowired
    public ApiService(RestTemplateBuilder builder){
        this.restTemplate = builder.build();
    }

    public ResponseEntity<List<CustomerLine>> getInfo() { //solo busca eslada.
        String url = "http://omr-simulator.us-east-1.elasticbeanstalk.com/?carrier=eslada";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/hal+json");
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        return restTemplate.exchange(url, HttpMethod.GET, entity, new ParameterizedTypeReference<List<CustomerLine>>(){});
    }

    public ResponseEntity<CustomerLine> postInfo(CustomerLineRequest request) {
        String url = "http://omr-simulator.us-east-1.elasticbeanstalk.com/";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/hal+json");
        headers.set("Content-Type", "application/json");
        HttpEntity<CustomerLineRequest> entity = new HttpEntity<>(request, headers);
        return restTemplate.postForEntity(url, entity, CustomerLine.class);
    }

}
