package com.commercel.tshirt.Entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    @Test
    void testUserSettersAndGetters() {
        User user = new User();
        user.setId(1L);
        user.setNom("Doe");
        user.setPrenom("John");
        user.setEmail("john.doe@example.com");
        user.setMotDePasse("secret");

        assertEquals(1L, user.getId());
        assertEquals("Doe", user.getNom());
        assertEquals("John", user.getPrenom());
        assertEquals("john.doe@example.com", user.getEmail());
        assertEquals("secret", user.getMotDePasse());
    }

    @Test
    void testUserConstructor() {
        User user = new User("Doe", "John", "john.doe@example.com", "secret");
        assertEquals("Doe", user.getNom());
        assertEquals("John", user.getPrenom());
        assertEquals("john.doe@example.com", user.getEmail());
        assertEquals("secret", user.getMotDePasse());
    }
}
