package org.example.person;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.model.Address;
import org.example.model.Person;
import org.example.repository.AddressRepository;
import org.example.repository.PersonRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;

import java.util.Arrays;
import java.util.Collections;

@Transactional
@Rollback(false)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@TestPropertySource("classpath:test.properties")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class PersonRepositoryIntegrationTest {
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private AddressRepository addressRepository;


    @Test
    public void whenParentSavedThenChildSaved() {
        Person person = Person.builder().name("myPerson1").build();
        Address address = Address.builder().city("myCity1").houseNumber(89).street("myStreet1").zipCode(1234).build();
        address.setPerson(person);
        person.setAddresses(Arrays.asList(address));
        personRepository.saveAndFlush(person);
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
