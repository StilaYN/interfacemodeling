package interfacemodeling.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class SwaggerConfig {

    private static final String SWAGGER_API_VERSION = "1.0";

    private static final String TITLE = "analisis REST API";
    @Bean
    public OpenAPI openAPI(){
        return new OpenAPI().info(new Info()
                .title(TITLE)
                .version(SWAGGER_API_VERSION)
        );
    }

}
