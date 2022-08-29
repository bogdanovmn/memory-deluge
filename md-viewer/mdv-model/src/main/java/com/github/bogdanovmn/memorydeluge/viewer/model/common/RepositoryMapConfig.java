package com.github.bogdanovmn.memorydeluge.viewer.model.common;

import com.github.bogdanovmn.common.spring.jpa.EntityRepositoryMapFactory;
import com.github.bogdanovmn.memorydeluge.viewer.model.entity.UserRole;
import com.github.bogdanovmn.memorydeluge.viewer.model.entity.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "com.github.bogdanovmn.common.spring")
@RequiredArgsConstructor
class RepositoryMapConfig {
    private final UserRoleRepository userRoleRepository;

    @Bean
    EntityRepositoryMapFactory entityRepositoryMapFactory() {
        return new EntityRepositoryMapFactory.Builder()
            .map(UserRole.class, userRoleRepository)
            .build();
    }
}
