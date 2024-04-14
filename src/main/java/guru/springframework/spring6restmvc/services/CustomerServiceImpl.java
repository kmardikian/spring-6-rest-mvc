package guru.springframework.spring6restmvc.services;

import guru.springframework.spring6restmvc.entities.Customer;
import guru.springframework.spring6restmvc.model.CustomerDTO;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.*;

@Service

public class CustomerServiceImpl implements CustomerService {
   // private final CustomerRepository customerRepository;
   // private final CustomerMapper customerMapper;
    private Map<UUID, CustomerDTO> customerLongMap = new HashMap<>();

    public CustomerServiceImpl() {
        //this.customerRepository = customerRepository;
        //this.customerMapper =customerMaper;
        // customerLongMap = new HashMap<>();

        CustomerDTO customerDTO = CustomerDTO.builder()
                .id(UUID.randomUUID())
                .name("Joe Smith")
                .version(1)
                .crtDate(LocalDate.now())
                .modDate(LocalDate.now())
                .build();

        customerLongMap.put(customerDTO.getId(), customerDTO);
        customerDTO = CustomerDTO.builder()
                .id(UUID.randomUUID())
                .name("Richard Jone")
                .version(1)
                .crtDate(LocalDate.now())
                .modDate(LocalDate.now())
                .build();
        customerLongMap.put(customerDTO.getId(), customerDTO);


        //customerLongMap.put(new )
    }

    @Override
    public CustomerDTO add(CustomerDTO customerDTO) {

        customerDTO.setId(UUID.randomUUID());
        customerDTO.setVersion(1);
        customerDTO.setCrtDate(LocalDate.now());
        customerDTO.setModDate(LocalDate.now());
        customerLongMap.put(customerDTO.getId(), customerDTO);

        this.customerLongMap.put(customerDTO.getId(), customerDTO);
        return customerDTO;
    }

    @Override
    public Optional<CustomerDTO> get(UUID id) {

        return Optional.of(customerLongMap.get(id));
    }

    @Override
    public List<CustomerDTO> findAll() {
        List<Customer> cus = new ArrayList<>();
        return customerLongMap.values().stream().toList();
//        return new ArrayList<CustomerDTO>(customerLongMap.values());

//        List<CustomerDTO> Dto;
//
//        return customerRepository.findAll().stream()
//                .map(ent ->(customerMapper.getDtoFromEntity(ent))).toList();

    }

    @Override
    public void patchById(UUID id, CustomerDTO customerDTO) {
        CustomerDTO newCustomerDTO = customerLongMap.get(id);
        if (StringUtils.hasText(customerDTO.getName()) ) {
            newCustomerDTO.setName(customerDTO.getName());
        }
        //customerLongMap.put(id, newCustomer);
    }

    @Override
    public void put(UUID id, CustomerDTO customerDTO) {
        CustomerDTO existingCust = customerLongMap.get(id);
        existingCust.setVersion(customerDTO.getVersion());
        existingCust.setModDate(customerDTO.getModDate());
        existingCust.setName(customerDTO.getName());
        existingCust.setCrtDate(customerDTO.getCrtDate());
        customerLongMap.put(id, existingCust);

    }

    @Override
    public void deletebyId(UUID id) {
        customerLongMap.remove(id);
    }
}
