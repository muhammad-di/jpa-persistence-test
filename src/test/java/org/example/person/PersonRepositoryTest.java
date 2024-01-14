package org.example.person;

import lombok.RequiredArgsConstructor;
import org.example.model.Address;
import org.example.model.Person;
import org.example.repository.AddressRepository;
import org.example.repository.PersonRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.Collections;

@DataJpaTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class PersonRepositoryTest {
    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private AddressRepository addressRepository;



    @Test
    public void whenParentSavedThenChildSaved() {
        Person person = Person.builder().name("myPerson2").build();
        Address address = Address.builder().city("myCity2").houseNumber(89).street("myStreet2").zipCode(1234).build();
        address.setPerson(person);
        person.setAddresses(Arrays.asList(address));
        personRepository.saveAndFlush(person);
    }

    @Test
    public void whenParentSavedThenChildSavedAndDeleted() {
        Person person = Person.builder().name("myPerson2").build();
        Address address = Address.builder().city("myCity2").houseNumber(89).street("myStreet2").zipCode(1234).build();
        address.setPerson(person);
        person.setAddresses(Arrays.asList(address));
        person = personRepository.saveAndFlush(person);
//        personRepository.deleteById(person.getId());
//        personRepository.flush();
    }

    @Test
    public void whenParentSavedThenMerged() {
        int addressId;
        Person person = Person.builder().name("myPerson2").build();
        Address address = Address.builder()
                .person(person)
                .city("myCity2")
                .houseNumber(89)
                .street("myStreet2")
                .zipCode(1234)
                .build();
        address = addressRepository.save(address);
        person.setAddresses(Arrays.asList(address));
        personRepository.saveAndFlush(person);
        addressId = address.getId();
        personRepository.flush();

        Address savedAddressEntity = addressRepository.findById(addressId).get();
        Person savedPersonEntity = savedAddressEntity.getPerson();
        savedPersonEntity.setName("devender kumar");
        savedAddressEntity.setHouseNumber(24);
        personRepository.save(savedPersonEntity);
    }

    @Test
    public void whenParentRemovedThenChildRemoved() {
        Person person = Person.builder().name("myPerson2").build();
        Address address = Address.builder()
                .city("myCity2")
                .houseNumber(89)
                .street("myStreet2")
                .zipCode(1234)
                .build();
        person.setAddresses(Collections.singletonList(address));
        address.setPerson(person);
        Person personFromDB = personRepository.save(person);
        Address addressFromDB = addressRepository.save(address);


        personRepository.delete(personFromDB);
        personRepository.flush();
    }
}
