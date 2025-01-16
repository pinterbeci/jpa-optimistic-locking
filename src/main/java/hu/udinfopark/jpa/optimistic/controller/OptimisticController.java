package hu.udinfopark.jpa.optimistic.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hu.udinfopark.common.backend.api.entity.Result;
import hu.udinfopark.jpa.optimistic.dto.OptimisticDTO;
import hu.udinfopark.jpa.optimistic.service.OptimisticService;
import lombok.RequiredArgsConstructor;

@RequestMapping("/api/optimistic")
@RestController
@RequiredArgsConstructor
public class OptimisticController {
    private final OptimisticService optimisticService;

    @PostMapping("/create")
    public Result<OptimisticDTO> createOptimistic(@RequestBody final OptimisticDTO optimisticDTO) {
        return this.optimisticService.createOptimistic(optimisticDTO);
    }

    @GetMapping("/{id}")
    public Result<OptimisticDTO> findOptimisticById(@PathVariable final String id) {
        return this.optimisticService.findOptimisticById(id);
    }

    @GetMapping
    public Result<List<OptimisticDTO>> listOptimistic() {
        return this.optimisticService.listOptimistic();
    }

    @PutMapping("/{id}")
    public Result<OptimisticDTO> updateOptimistic(@PathVariable final String id, @RequestBody final OptimisticDTO optimisticDTO) {
        return this.optimisticService.updateOptimistic(id, optimisticDTO);
    }

    @PutMapping("/{id}/soft-delete")
    public Result<String> softDeleteOptimistic(@PathVariable final String id) {
        return this.optimisticService.softDeleteOptimistic(id);
    }

}
