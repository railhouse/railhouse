package com.flightdrift.flightdrift.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;

/*
 * Author: Jamius Siam
 * Since: 27/05/2026
 */
@Entity
@Table(name = "flag")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Flag extends Auditable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Column(name = "key", nullable = false)
    private String key;

    @Column(name = "value", nullable = false)
    private String value;
}
