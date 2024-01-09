package es.uca.iw.eslada.api;

import elemental.json.Json;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import java.util.List;
import java.util.Map;

@Service
public class ApiService {
    private final RestTemplate restTemplate;

    @Autowired
    public ApiService(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
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

    public ResponseEntity<CustomerLine> patchInfo(String id, CustomerLineRequest request) {
        String url = "http://omr-simulator.us-east-1.elasticbeanstalk.com/" + id;
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/hal+json");
        headers.set("Content-Type", "application/json");
        HttpEntity<CustomerLineRequest> entity = new HttpEntity<>(request, headers);
        return restTemplate.exchange(url, HttpMethod.PATCH, entity, CustomerLine.class);
    }

    public ResponseEntity<Void> deleteInfo(String id) {
        String url = "http://omr-simulator.us-east-1.elasticbeanstalk.com/" + id + "?carrier=eslada";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "*/*");
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        return restTemplate.exchange(url, HttpMethod.DELETE, entity, Void.class);
    }

    public ResponseEntity<List<DataUsageRecord>> getDataUsageRecord(String id, String startDate, String endDate) {
        StringBuilder url = new StringBuilder("http://omr-simulator.us-east-1.elasticbeanstalk.com/")
                .append(id)
                .append("/datausagerecords?carrier=eslada");

        if (startDate != null && endDate != null) {
            url.append("&startDate=").append(startDate).append("&endDate=").append(endDate);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/hal+json");
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

        return restTemplate.exchange(url.toString(), HttpMethod.GET, entity, new ParameterizedTypeReference<List<DataUsageRecord>>(){});
    }

    public ResponseEntity<List<CallRecord>> getCallRecord(String id, String startDate, String endDate) {
        StringBuilder url = new StringBuilder("http://omr-simulator.us-east-1.elasticbeanstalk.com/")
                .append(id)
                .append("/callrecords?carrier=eslada");

        if (startDate != null && endDate != null) {
            url.append("&startDate=").append(startDate).append("&endDate=").append(endDate);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/hal+json");
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

        return restTemplate.exchange(url.toString(), HttpMethod.GET, entity, new ParameterizedTypeReference<List<CallRecord>>(){});
    }

    public ResponseEntity<CustomerLine> searchByPhoneNumber(String phoneNumber) {
        String url = "http://omr-simulator.us-east-1.elasticbeanstalk.com/search/phoneNumber/" + phoneNumber + "?carrier=eslada";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/hal+json");
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

        try {
            ResponseEntity<CustomerLine> response = restTemplate.exchange(url, HttpMethod.GET, entity, CustomerLine.class);
            return response;
        } catch (HttpClientErrorException.NotFound ex) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }



}
