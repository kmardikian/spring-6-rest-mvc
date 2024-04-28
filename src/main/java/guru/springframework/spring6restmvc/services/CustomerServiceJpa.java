package guru.springframework.spring6restmvc.services;

import guru.springframework.spring6restmvc.entities.Customer;
import guru.springframework.spring6restmvc.mappers.CustomerMapper;
import guru.springframework.spring6restmvc.model.CustomerDTO;
import guru.springframework.spring6restmvc.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Primary
@RequiredArgsConstructor
@Service
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
        Optional<Customer> customerOp = customerRepository.findById(id);
        customerOp.ifPresentOrElse(customer -> {
           // customer.setName(customerDTO.getName());
           customerRepository.save(customer);
        }, () -> {

        }
                );

    }

    @Override
    public Optional<CustomerDTO> put(UUID id, CustomerDTO customerDTO) {
        AtomicReference<Optional<CustomerDTO>> atomicReference = new AtomicReference<>();
        Optional<Customer> customerOp = customerRepository.findById(id);
        customerOp.ifPresentOrElse(customer ->{
            customer.setName(customerDTO.getName());
            Customer updCust = customerRepository.save(customer);
            atomicReference.set(Optional.of(customerMapper.getDtoFromEntity(updCust)));
        },() ->{
            atomicReference.set(Optional.empty());
        }
        );
        return atomicReference.get();
    }

    @Override
    public boolean deletebyId(UUID id) {
        if (customerRepository.existsById(id)) {
            customerRepository.deleteById(id);
            return true;
        }
        return false;

    }
}
