package homework5;

import homework5.security.SecurityConfig;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import java.util.Arrays;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@EnableScheduling
public class BootWebsocketsStompApplication implements ApplicationRunner {

    public static void main(String[] args) {
        SpringApplication.run(BootWebsocketsStompApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) {
        System.out.println("http://localhost:9000/swagger-ui/index.html");
        System.out.println("http://localhost:9000/h2-console \n");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI()
            .info(new Info().title("EIS API")
                .description("Bank Information System sample application")
                .version("v0.0.1")
                .license(new License().name("Apache 2.0").url("http://springdoc.org"))
                .description("SpringShop Wiki Documentation")
                .contact(new Contact().email("test@test.com").url("http://fullstackcode.dev")))
                .components(new Components().addSecuritySchemes("bearer-jwt",
                    new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")
                        .in(SecurityScheme.In.HEADER).name("Authorization")))
                .info(new Info().title("App API").version("snapshot"))
                .addSecurityItem(
                    new SecurityRequirement().addList("bearer-jwt", Arrays.asList("read", "write")));
    }
}
