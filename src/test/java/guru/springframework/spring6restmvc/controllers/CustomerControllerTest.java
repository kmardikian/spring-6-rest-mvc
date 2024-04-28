package guru.springframework.spring6restmvc.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import guru.springframework.spring6restmvc.model.CustomerDTO;
import guru.springframework.spring6restmvc.services.CustomerService;
import guru.springframework.spring6restmvc.services.CustomerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(CustomerController.class)
class Spring6RestMvcApplicationTests {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
//    @Autowired
//    CustomerRepository customerRepository;
//    @Autowired
//    CustomerMapper customerMapper;

    @MockBean
    CustomerService customerService;
    CustomerServiceImpl customerServiceImpl;
    @Captor
    ArgumentCaptor<UUID> capture;
    @Captor
    ArgumentCaptor<CustomerDTO> customerArgumentCaptor;

    @BeforeEach
    void seUp() {
        customerServiceImpl = new CustomerServiceImpl(  );
    }

    @Test
    void patchCustomer() throws Exception {
        CustomerDTO customerDTO = customerServiceImpl.findAll().get(0);
        Map<String, Object> cusMap = new HashMap<>();
        cusMap.put("name","changed customer");
        mockMvc.perform(patch(CustomerController.CUSTOMER_PATH_ID, customerDTO.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cusMap))
        ) .andExpect(status().isNoContent());

        verify(customerService).patchById(capture.capture(),customerArgumentCaptor.capture());
        assertThat(customerDTO.getId().equals(capture.getValue()));
        assertThat(cusMap.get("name")).isEqualTo(customerArgumentCaptor.getValue().getName());
    }

    @Test
    void updateCustomer() throws Exception {
        CustomerDTO customerDTO = customerServiceImpl.findAll().get(0);

        given(customerService.put(any(),any())).willReturn(Optional.of(customerDTO));

        mockMvc.perform(put(CustomerController.CUSTOMER_PATH_ID, customerDTO.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerDTO)))
                .andExpect(status().isNoContent());

        verify(customerService).put(any(UUID.class),any(CustomerDTO.class));

    }

    @Test
    void deleteCustomer() throws Exception {
        CustomerDTO customerDTO = customerServiceImpl.findAll().get(0);

        given(customerService.deletebyId(any())).willReturn(true);

        mockMvc.perform(delete(CustomerController.CUSTOMER_PATH_ID, customerDTO.getId())
                        .accept(MediaType.asMediaType(MediaType.APPLICATION_JSON)))
                .andExpect(status().isNoContent());

        // ArgumentCaptor<UUID> capture = ArgumentCaptor.forClass(UUID.class);
        verify(customerService).deletebyId(capture.capture());
        assertThat(customerDTO.getId().equals(capture.getValue()));
    }
    @Test
    void createNewCustomer() throws Exception {
        CustomerDTO customerDTO = customerServiceImpl.findAll().get(0);
        customerDTO.setId(null);
        customerDTO.setVersion(null);
        customerDTO.setName("Russ new customer");
        given(customerService.add(any(CustomerDTO.class)))
                .willReturn(customerServiceImpl.findAll().get(0));
        mockMvc.
                perform(post(CustomerController.CUSTOMER_PATH)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerDTO)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
        ;
    }

    @Test
    void listCustomers() throws Exception {
        given(customerService.findAll())
                .willReturn(customerServiceImpl.findAll());

        mockMvc.perform(get(CustomerController.CUSTOMER_PATH)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(2)))
        ;
    }

    @Test
    void getCustomerById() throws Exception {
        CustomerDTO customerDTO = customerServiceImpl.findAll().get(0);

        given(customerService.get(customerDTO.getId())).willReturn(Optional.of(customerDTO));
        mockMvc.perform(get(CustomerController.CUSTOMER_PATH_ID, customerDTO.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is(customerDTO.getName())))
        // .andExpect(jsonPath("$.id",is(customer.getId())))
        ;

    }
    @Test
    void getCustomerByIdException() throws Exception {
        CustomerDTO customerDTO = customerServiceImpl.findAll().get(0);
        given(customerService.get(customerDTO.getId())).willReturn(Optional.of(customerDTO));
        mockMvc.perform(get(CustomerController.CUSTOMER_PATH_ID, UUID.randomUUID()))
                .andExpect(status().isNotFound())
        ;
    }

}
