package io.eddie.backend.global.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties(prefix = "custom.jwt")
public class JwtConfiguration {



}
