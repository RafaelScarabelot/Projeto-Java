package com.projeto.projeto.service;

import com.projeto.projeto.dtos.MembroDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class MembroService {

    private final RestTemplate restTemplate;
    private final String API_URL = "http://localhost:8080/api/membros"; // URL do mock

    public MembroService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<MembroDTO> listarMembros() {
        MembroDTO[] membros = restTemplate.getForObject(API_URL, MembroDTO[].class);
        return Arrays.asList(membros);
    }

    public MembroDTO buscarPorId(Long id) {
        return restTemplate.getForObject(API_URL + "/" + id, MembroDTO.class);
    }

    public MembroDTO criarMembro(MembroDTO membroDTO) {
        return restTemplate.postForObject(API_URL, membroDTO, MembroDTO.class);
    }

}
