package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.dto.pet.PetDTO;
import com.udacity.jdnd.course3.critter.entity.pet.Pet;
import com.udacity.jdnd.course3.critter.entity.user.Customer;
import com.udacity.jdnd.course3.critter.mapper.PetMapper;
import com.udacity.jdnd.course3.critter.repository.PetRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PetService {

    private final CustomerService customerService;
    private final PetRepository petRepository;

    public PetService(CustomerService customerService, PetRepository petRepository) {
        this.customerService = customerService;
        this.petRepository = petRepository;
    }

    public PetDTO save(PetDTO petDTO) {
        try {
            Pet pet = PetMapper.DtoToEntity(petDTO);
            pet.setCustomer(customerService.findById(petDTO.getOwnerId()));
            pet = petRepository.save(pet);
            customerService.addPetToCustomer(pet);
            petDTO.setId(pet.getId());
        } catch (Exception exception) {
            System.out.println("Error:");
            System.out.println(exception.getMessage());
        }

        return petDTO;
    }

    public PetDTO getByPetId(Long petId) {
        Pet pet = petRepository.findById(petId).orElse(new Pet());
        return PetMapper.entityToDTO(pet);
    }

    public Pet getEntityById(Long petId) {
        return petRepository.findById(petId).orElse(new Pet());
    }

    public List<PetDTO> getPets() {
        List<Pet> pets = petRepository.findAll();
        List<PetDTO> petDTOS = new ArrayList<>();
        for (Pet pet : pets) {
            petDTOS.add(PetMapper.entityToDTO(pet));
        }
        return petDTOS;
    }

    public List<PetDTO> getPetsByOwner(Long ownerId) {
        Customer customer = customerService.findById(ownerId);
        List<Pet> pets = customer.getPets();
        List<PetDTO> petDTOS = new ArrayList<>();
        for (Pet pet : pets) {
            petDTOS.add(PetMapper.entityToDTO(pet));
        }
        return petDTOS;
    }
}
