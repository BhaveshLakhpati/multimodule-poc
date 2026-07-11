package co.ambit.ie.researchportal.authlibrary.properties;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Data
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "shared.security")
public class CustomSecurityProperties {
    private final List<String> excludedPaths;
}
