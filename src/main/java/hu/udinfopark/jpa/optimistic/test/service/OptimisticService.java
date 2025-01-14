package hu.udinfopark.jpa.optimistic.test.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hu.udinfopark.jpa.optimistic.test.repository.OptimisticRepository;
import hu.udinfopark.jpa.optimistic.test.repository.entity.Optimistic;

@Service
public class OptimisticService {
    private final OptimisticRepository optimisticRepository;

    @Autowired
    public OptimisticService(final OptimisticRepository optimisticRepository) {
        this.optimisticRepository = optimisticRepository;
    }

    @Transactional
    public void updateEntity(final String id, final String newDescription) {
        final Optimistic optimistic = optimisticRepository.findById(id).orElseThrow(() -> new RuntimeException("Entity not found"));
        optimistic.setDescripton(newDescription);
        optimisticRepository.saveAndFlush(optimistic);
    }

}
