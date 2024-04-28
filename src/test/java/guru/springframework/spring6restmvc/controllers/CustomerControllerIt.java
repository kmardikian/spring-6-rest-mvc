package guru.springframework.spring6restmvc.controllers;

import guru.springframework.spring6restmvc.configuration.NotFoundException;
import guru.springframework.spring6restmvc.entities.Customer;
import guru.springframework.spring6restmvc.mappers.CustomerMapper;
import guru.springframework.spring6restmvc.model.CustomerDTO;
import guru.springframework.spring6restmvc.repositories.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Slf4j
@SpringBootTest
class CustomerControllerIt {
    @Autowired CustomerController customerController;
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    CustomerMapper customerMapper;
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

    @Rollback
    @Transactional
    @Test
    void saveNewCustomer() {
        CustomerDTO customerDto = CustomerDTO.builder()
                .name("Test customer #3")
                .build();
        ResponseEntity responseEntity = customerController.postCustomer(customerDto);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
        assertThat(responseEntity.getHeaders().getLocation()).isNotNull();
        String [] locationUUid = responseEntity.getHeaders().getLocation().getPath().split("/");

        UUID uuid = UUID.fromString(locationUUid[4]);
        Customer customer = customerRepository.findById(uuid).get();
        log.info("uuid = {}", uuid);
        assertThat(customer).isNotNull();
    }

    @Test
    @Transactional
    @Rollback
    void updateExistingCustomer() {
        Customer customer = customerRepository.findAll().get(0);
        CustomerDTO customerDto = customerMapper.getDtoFromEntity(customer);
        //UUID uuid = customerDto.getId();
        customerDto.setId(null);
        customerDto.setVersion(null);
        customerDto.setName("Updated");
        //CustomerDTO customerDTO = ;
        ResponseEntity responseEntity = customerController.putCustomer(customer.getId(),customerDto);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));
    }

    @Test
    void updateNotFound() {
        assertThrows(NotFoundException.class, () -> {
            customerController.putCustomer(UUID.randomUUID(),CustomerDTO.builder().build());
        });
    }

    @Test
    @Transactional
    @Rollback
    void deleteFound() {
        CustomerDTO dto = customerController.listCustomers().get(0);
        ResponseEntity responseEntity= customerController.deleteById(dto.getId());
        assertThat(responseEntity.getStatusCode()  ).isEqualTo(HttpStatusCode.valueOf(204));
        assertThrows(NotFoundException.class, () -> {
            customerController.getCustomerById(dto.getId());
        });
        assertThat(customerRepository.findById(dto.getId())).isEmpty();
        //assertThat(customerController.getCustomerById(dto.getId())).isNull();

        //CustomerDTO dtoFound = customerController.getCustomerById(dto.getId());
        //assertThat(dtoFound).isNull();
    }
    @Test
    void DeleteNotFound() {
        assertThrows(NotFoundException.class, () -> {
            customerController.deleteById(UUID.randomUUID());
        });
    }

}