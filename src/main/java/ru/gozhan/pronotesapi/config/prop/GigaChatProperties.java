package ru.gozhan.pronotesapi.config.prop;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "custom.integrations.giga-chat")
public class GigaChatProperties {

    private String xClientId;

    private String authorizationToken;

}