package hu.udinfopark.jpa.optimistic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import hu.udinfopark.jpa.optimistic.repository.entity.Optimistic;

@Repository
public interface OptimisticRepository
    extends PagingAndSortingRepository<Optimistic, String>, JpaRepository<Optimistic, String>,
    JpaSpecificationExecutor<Optimistic> {
}
