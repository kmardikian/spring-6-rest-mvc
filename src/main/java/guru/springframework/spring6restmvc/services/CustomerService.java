package guru.springframework.spring6restmvc.services;

import guru.springframework.spring6restmvc.model.CustomerDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerService {
    public CustomerDTO add(CustomerDTO customerDTO);
    public Optional<CustomerDTO> get(UUID id);
    public List<CustomerDTO> findAll();

    void patchById(UUID id, CustomerDTO customerDTO);

    Optional<CustomerDTO> put(UUID id, CustomerDTO customerDTO);

    boolean deletebyId(UUID id);
}
