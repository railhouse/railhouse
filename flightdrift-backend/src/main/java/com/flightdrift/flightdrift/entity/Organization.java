package com.flightdrift.flightdrift.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.util.HashSet;
import java.util.Set;

/*
 * Author: Jamius Siam
 * Since: 27/05/2026
 */
@Entity
@Table(name = "organization")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Organization extends Auditable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(name = "url")
    private String url;

    @Column(name = "icon_url", length = 1024)
    private String iconUrl;

    @Builder.Default
    @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<AccountOrganizationMapping> accounts = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Project> projects = new HashSet<>();
}
