package com.projeto.projeto.service;


import com.projeto.projeto.entity.ProjetoEntity;
import com.projeto.projeto.repository.ProjetoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjetoService {

    private final ProjetoRepository projetoRepository;

    public ProjetoService(ProjetoRepository projetorepository) {
        this.projetoRepository = projetorepository;
    }

    public ProjetoEntity salvar(ProjetoEntity projeto) {
        return projetoRepository.save(projeto);
    }

    public List<ProjetoEntity> listar() {
        return projetoRepository.findAll();
    }

    public void deleteById(Long id) {
        projetoRepository.deleteById(id);
    }

    public ProjetoEntity editar(Long id,ProjetoEntity projetoAtualizado){
        return projetoRepository.save(projetoAtualizado);
    }




}
