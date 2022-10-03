package io.github.davitsjunior;

import io.github.davitsjunior.domain.entity.Cliente;
import io.github.davitsjunior.domain.repository.Clientes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class VendasApplication extends SpringBootServletInitializer {

   /* @Bean
    public CommandLineRunner commandLineRunner (@Autowired Clientes clientes){
        return args -> {
            Cliente cliente = new Cliente();
            cliente.setNome("Davi Junior");
            cliente.setCpf("03903903910");
            clientes.save(cliente);
            Cliente cliente1 = new Cliente();
            cliente1.setNome("David Gabriel");
            cliente1.setCpf("0220220210");
            clientes.save(cliente1);
        };
    }*/

    public static void main(String[] args) {
        SpringApplication.run(VendasApplication.class, args);
    }
}
