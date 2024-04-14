package guru.springframework.spring6restmvc.services;

import guru.springframework.spring6restmvc.entities.Customer;
import guru.springframework.spring6restmvc.mappers.CustomerMapper;
import guru.springframework.spring6restmvc.model.CustomerDTO;
import guru.springframework.spring6restmvc.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Primary
@RequiredArgsConstructor
public class CustomerServiceJpa implements CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    @Override
    public CustomerDTO add(CustomerDTO customerDTO) {
        return customerMapper.getDtoFromEntity(
        customerRepository.save(customerMapper.getEntitFromDto(customerDTO)));
    }

    @Override
    public Optional<CustomerDTO> get(UUID id) {

        return Optional.ofNullable(
                customerMapper.getDtoFromEntity(customerRepository.findById(id).orElse(null))
        );
    }

    @Override
    public List<CustomerDTO> findAll() {

        return customerRepository.findAll().stream()
                .map(customerMapper::getDtoFromEntity)
                .collect(Collectors.toList());
                //.map(ent ->(customerMapper.getDtoFromEntity(ent))) also works
                //.toList();  also works
    }

    @Override
    public void patchById(UUID id, CustomerDTO customerDTO) {
        Optional<Customer> customer = customerRepository.findById(id);
//        if (customer.isPresent()) {
//            customer.
//        }

    }

    @Override
    public void put(UUID id, CustomerDTO customerDTO) {

    }

    @Override
    public void deletebyId(UUID id) {

    }
}
