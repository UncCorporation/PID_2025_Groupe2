package com.commercel.tshirt.Controller;

import com.commercel.tshirt.Entity.User;
import com.commercel.tshirt.Repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void cleanDatabase() {
        userRepository.deleteAll();
    }

    @Test
    void testRegisterUser_success() throws Exception {
        User user = new User();
        user.setNom("Dupont");
        user.setPrenom("Jean");
        user.setEmail("jean.dupont@example.com");
        user.setMotDePasse("password123");

        mockMvc.perform(post("/api/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andExpect(content().string(containsString("inscrit")));
    }

    @Test
    void testRegisterUser_duplicateEmail() throws Exception {
        // Insère un utilisateur existant
        User existing = new User("Dupont", "Jean", "jean.dupont@example.com", "password123");
        userRepository.save(existing);

        User newUser = new User("Martin", "Paul", "jean.dupont@example.com", "newpass");

        mockMvc.perform(post("/api/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("déjà utilisé")));
    }
}
