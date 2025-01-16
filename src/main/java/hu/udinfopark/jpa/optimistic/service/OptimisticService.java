package hu.udinfopark.jpa.optimistic.service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hu.udinfopark.common.backend.api.entity.Result;
import hu.udinfopark.jpa.optimistic.repository.OptimisticRepository;
import hu.udinfopark.jpa.optimistic.repository.entity.Optimistic;
import hu.udinfopark.jpa.optimistic.dto.OptimisticDTO;

@Service
public class OptimisticService {

    private static final String ADMIN_USER_ID = UUID.randomUUID().toString();

    private final ModelMapper modelMapper;

    private final OptimisticRepository optimisticRepository;

    @Autowired
    public OptimisticService(final OptimisticRepository optimisticRepository) {
        this.optimisticRepository = optimisticRepository;
        this.modelMapper = new ModelMapper();
    }

    public Result<OptimisticDTO> createOptimistic(final OptimisticDTO optimisticDTO) {
        final Optimistic optimistic = this.modelMapper.map(optimisticDTO, Optimistic.class);
        optimistic.setCreatedAt(Instant.now());
        optimistic.setLastModified(Instant.now());
        optimistic.setCreatorId(ADMIN_USER_ID);
        optimistic.setModifierId(ADMIN_USER_ID);
        return new Result<>(
            modelMapper.map(this.optimisticRepository.saveAndFlush(optimistic), OptimisticDTO.class)
        );
    }

    public Result<OptimisticDTO> findOptimisticById(final String id) {
        return new Result<>(
            this.optimisticRepository.findById(id)
                .map((element) ->
                    modelMapper.map(element, OptimisticDTO.class))
                .orElseThrow(() -> new RuntimeException("Entity not found"))
        );

    }

    @Transactional
    public Result<OptimisticDTO> updateOptimistic(final String id, final OptimisticDTO optimisticDTO) {

        final Optimistic updatedOptimistic = optimisticRepository.findById(id)
            .map(optimistic -> {
                optimistic.setDescripton(optimisticDTO.getDescripton());
                optimistic.setModifierId(ADMIN_USER_ID);
                optimistic.setLastModified(Instant.now());
                return this.optimisticRepository.saveAndFlush(optimistic);
            })
            .orElseThrow(() -> new RuntimeException("Entity not found!"));
        return new Result<>(modelMapper.map(updatedOptimistic, OptimisticDTO.class));
    }

    public Result<String> softDeleteOptimistic(final String id) {
        this.optimisticRepository.findById(id).ifPresent(
            optimistic -> {
                optimistic.setDeleted(true);
                this.optimisticRepository.saveAndFlush(optimistic);
            }
        );
        return Result.ok(String.format("Success delete, deleted entity id = %s", id));
    }

    public Result<List<OptimisticDTO>> listOptimistic() {
        return new Result<>(
            this.optimisticRepository.findAll()
                .stream()
                .map((element) -> modelMapper.map(element, OptimisticDTO.class))
                .collect(Collectors.toList())
        );
    }
}
