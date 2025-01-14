package hu.udinfopark.jpa.optimistic.test.repository.entity;

import hu.udinfopark.common.backend.core.repository.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "optimistic")
@Access(value = AccessType.FIELD)
public class Optimistic extends BaseEntity {

    @Column(name = "description")
    private String descripton;
}
