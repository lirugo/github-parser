package com.lirugo.github.parser.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Configuration
public class AppConfig {

  static final String PREFIX = "ghp_";
  @Value("${github.token}")
  String githubToken;

  @Bean
  RestTemplate restTemplate() {
    var restTemplate = new RestTemplate();

    ClientHttpRequestInterceptor interceptor = (request, body, execution) -> {
      if (StringUtils.hasText(githubToken) && githubToken.startsWith(PREFIX)) {
        request.getHeaders().add(HttpHeaders.AUTHORIZATION, "Bearer " + githubToken);
      }

      log.debug("Request: {}", request.getURI());

      return execution.execute(request, body);
    };

    restTemplate.getInterceptors().add(interceptor);

    var converter = new MappingJackson2HttpMessageConverter();
    converter.setObjectMapper(new ObjectMapper());
    restTemplate.getMessageConverters().add(converter);

    return restTemplate;
  }
}
