package guru.springframework.spring6restmvc.controllers;

import guru.springframework.spring6restmvc.configuration.NotFoundException;
import guru.springframework.spring6restmvc.model.CustomerDTO;
import guru.springframework.spring6restmvc.repositories.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class CustomerControllerIt {
    @Autowired CustomerController customerController;
    @Autowired
    CustomerRepository customerRepository;

    @Test
    void listCustomers() {
        List<CustomerDTO> customerDTOList = customerController.listCustomers();
        assertThat(customerDTOList.size()).isGreaterThan(0);
    }
    @Rollback
    @Transactional
    @Test
    void testEmpty() {
        customerRepository.deleteAll();
        List<CustomerDTO> customerDTOList = customerController.listCustomers();
        assertThat(customerDTOList.size()).isEqualTo(0);
    }

    @Test
    void getCustomer() {
        CustomerDTO customerDto = customerController.listCustomers().get(0);
        CustomerDTO dto = customerController.getCustomerById(customerDto.getId());
        assertThat(dto).isNotNull();
    }

    @Test
    void getCustomerException() {
        //assert
//        assertThrows(NotFoundException.class, () -> {
//            customerController.getCustomerById(UUID.randomUUID());
//        });

        assertThrows(NotFoundException.class, () -> {
            customerController.getCustomerById(UUID.randomUUID());
        });
    }
}