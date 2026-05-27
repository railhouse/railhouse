package com.flightdrift.flightdrift.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;

/*
* Author: Jamius Siam
* Since: 05/05/2026
*/
@Entity
@Table(name = "blacklisted_auth_token")
@Getter
@Setter
@NoArgsConstructor
public class BlacklistedAuthToken extends Auditable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Column(name = "token", nullable = false)
    private String token;

    public BlacklistedAuthToken(String token) {
        this.token = token;
    }
}
