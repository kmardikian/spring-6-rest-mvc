package guru.springframework.spring6restmvc.controllers;

import guru.springframework.spring6restmvc.configuration.NotFoundException;
import guru.springframework.spring6restmvc.model.CustomerDTO;
import guru.springframework.spring6restmvc.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
//@RequestMapping("/api/v1/customers")
@RequestMapping
public class CustomerController {
    @Autowired
    private final CustomerService customerService;
    //private final String map = "/api/v1/customers";
    public static final String CUSTOMER_PATH = "/api/v1/customers";
    public static final String CUSTOMER_PATH_ID = CUSTOMER_PATH + "/{id}";

    public CustomerController(CustomerService customerService) {

        this.customerService = customerService;
    }


    //@RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @GetMapping(value = CUSTOMER_PATH_ID)
    CustomerDTO getCustomerById(@PathVariable("id") UUID id) {
        //return customerService.get(id).orElseThrow(NotFoundException:: new);
        return customerService.get(id).orElseThrow(NotFoundException::new);
        //return customerService.getCustomerById(id).orElseThrow(NotFoundException::new);
    }

    //@RequestMapping(method = RequestMethod.GET)
    @GetMapping(CUSTOMER_PATH)
    public List<CustomerDTO> listCustomers() {
        return customerService.findAll();
    }

    //@RequestMapping(value="{id}", method=RequestMethod.DELETE)
    //@DeleteMapping(value="{id}")
    @DeleteMapping(CUSTOMER_PATH_ID)
    ResponseEntity deleteById(@PathVariable("id") UUID id) {
        customerService.deletebyId(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    //@RequestMapping(value="/{id}", method = RequestMethod.PUT)
    @PutMapping(CUSTOMER_PATH_ID)
    ResponseEntity putCustomer(@PathVariable("id") UUID id, @RequestBody CustomerDTO customerDTO) {
        customerService.put(id, customerDTO);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PostMapping(CUSTOMER_PATH)
    ResponseEntity postCustomer(@RequestBody CustomerDTO customerDTO) {
        CustomerDTO retCus = customerService.add(customerDTO);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", CUSTOMER_PATH + "/" + retCus.getId());
        return new ResponseEntity(headers, HttpStatus.CREATED);
    }

    //@PatchMapping(value = "/{id}")
    @PatchMapping(CUSTOMER_PATH_ID)
    ResponseEntity patchCustomer(@PathVariable UUID id, @RequestBody CustomerDTO customerDTO) {
        customerService.patchById(id, customerDTO);
        return new ResponseEntity(HttpStatus.NO_CONTENT);

    }
}
