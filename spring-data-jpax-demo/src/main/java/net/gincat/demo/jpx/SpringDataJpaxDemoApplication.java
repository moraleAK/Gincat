package net.gincat.demo.jpx;

import net.gincat.jpax.JpaRepositoryExtendedImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "net.gincat")
@EnableJpaRepositories(repositoryBaseClass = JpaRepositoryExtendedImpl.class)
public class SpringDataJpaxDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringDataJpaxDemoApplication.class, args);
    }

}
