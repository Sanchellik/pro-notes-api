package ru.gozhan.pronotesapi.config.webclient;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import ru.gozhan.pronotesapi.config.prop.GigaChatProperties;

@Configuration
@RequiredArgsConstructor
public class GigaChatWebClientConfig {

    private final GigaChatProperties gigaChatProperties;

    private static final String BASE_URL =
            "https://gigachat.devices.sberbank.ru/api/v1/chat/completions";

    @Bean("webClientGigaChat")
    @SneakyThrows
    public WebClient webClientGigaChat() {
        SslContext sslContext = SslContextBuilder.forClient()
                .trustManager(InsecureTrustManagerFactory.INSTANCE)
                .build();

        HttpClient httpClient = HttpClient.create().secure(t -> t.sslContext(sslContext));

        return WebClient.builder()
                .baseUrl(BASE_URL)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .defaultHeader(
                        HttpHeaders.CONTENT_TYPE,
                        "application/json"
                )
                .defaultHeader(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + gigaChatProperties.getAuthorizationToken()
                )
                .defaultHeader(
                        "X-Client-ID",
                        gigaChatProperties.getXClientId()
                )
                .build();
    }

}
