package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.dto.user.CustomerDTO;
import com.udacity.jdnd.course3.critter.entity.pet.Pet;
import com.udacity.jdnd.course3.critter.entity.user.Customer;
import com.udacity.jdnd.course3.critter.mapper.CustomerMapper;
import com.udacity.jdnd.course3.critter.repository.CustomerRepository;
import com.udacity.jdnd.course3.critter.repository.PetRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final PetRepository petRepository;

    public CustomerService(CustomerRepository customerRepository, PetRepository petRepository) {
        this.customerRepository = customerRepository;
        this.petRepository = petRepository;
    }

    public Customer findById(long ownerId) {
        return customerRepository.findById(ownerId).orElse(new Customer());
    }

    public Customer addPetToCustomer(Pet pet) throws Exception {
        if (pet.getCustomer() == null) {
            throw new Exception("Can not add pet to customer because customer_id is null");
        }

        Customer customer = pet.getCustomer();
        if (customer.getPets() != null) {
            customer.getPets().add(pet);
        } else {
            List<Pet> pets = new ArrayList<>();
            pets.add(pet);
            customer.setPets(pets);
        }

        return customerRepository.save(customer);
    }

    public CustomerDTO save(CustomerDTO customerDTO) {
        Customer customer = customerRepository.save(CustomerMapper.DtdoToEntity(customerDTO));
        customerDTO.setId(customer.getId());
        return customerDTO;
    }

    public List<CustomerDTO> getCustomers() {
        List<Customer> customers = customerRepository.findAll();
        List<CustomerDTO> customerDTOS = new ArrayList<>();
        for (Customer customer : customers) {
            customerDTOS.add(CustomerMapper.entityToDTO(customer));
        }
        return customerDTOS;
    }

    public CustomerDTO getOwnerByPetId(long petId) {
        Pet pet = petRepository.findById(petId).get();
        Customer customer = pet.getCustomer();
        return CustomerMapper.entityToDTO(customer);
    }

}
