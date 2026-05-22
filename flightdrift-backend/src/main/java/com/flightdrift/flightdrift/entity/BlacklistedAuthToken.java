package com.flightdrift.flightdrift.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import java.io.Serial;

@Entity
@Table(name = "blacklisted_auth_token")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BlacklistedAuthToken extends Auditable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "token", nullable = false)
    private String token;

    public BlacklistedAuthToken(String token) {
        this.token = token;
    }
}
