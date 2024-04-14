package guru.springframework.spring6restmvc.bootstrap;

import guru.springframework.spring6restmvc.entities.Customer;
import guru.springframework.spring6restmvc.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class Bootstrap implements CommandLineRunner {
    private final CustomerRepository customerRepository;


    @Override
    public void run(String... args) throws Exception {
        Customer customer= Customer.builder()
                .name("Bugs Bunny")
                .modDate(LocalDate.now())
                .crtDate(LocalDate.now())
                .build();
        customerRepository.save(customer);
        log.info("saving customer name is {}",customer.getName());

        customer= Customer.builder()
                .name("Duffy Duck")
                .modDate(LocalDate.now())
                .crtDate(LocalDate.now())
                .build();
        customerRepository.save(customer);
        customerRepository.save(Customer.builder()
                .id(UUID.randomUUID())
                .name("Richard Jone")
                .version(1)
                .crtDate(LocalDate.now())
                .modDate(LocalDate.now())
                .build());

    }
}
