package com.tinqinacademy.bff.persistence.entities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@Getter
@Setter
@Component
@RequestScope
public class JWTContext {
    private String userId;
}
