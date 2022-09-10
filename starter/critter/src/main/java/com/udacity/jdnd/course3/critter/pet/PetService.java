package com.udacity.jdnd.course3.critter.pet;

import com.udacity.jdnd.course3.critter.user.customer.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PetService {
    @Autowired
    private PetRepository petRepository;

    public Pet savePet(Pet pet) {
        Pet newPet = petRepository.save(pet);
        // due to bidirectional relationship, update customer as well
        Customer owner = pet.getCustomer();
        if (owner != null) {
            List<Pet> ownerPets = owner.getPets(); // pet list is initialized to empty, so will not be null
            if (!ownerPets.contains(pet)) {
                ownerPets.add(newPet);
            }
            owner.setPets(ownerPets); // 'owner' is managed entity, so no need to explicitly save
        }
        return newPet;
    }

    public Pet getPetById(Long petId) {
        Optional<Pet> petOptional = petRepository.findById(petId);
        if (petOptional.isPresent()) {
            return petOptional.get();
        } else {
            throw new PetNotFoundException("Pet Not Found");
        }
    }

    public List<Pet> getAllPets() {
        return petRepository.findAll();
    }

    public List<Pet> getPetsByOwner(long ownerId) {
        return petRepository.findAllByCustomerId(ownerId);
    }
}
