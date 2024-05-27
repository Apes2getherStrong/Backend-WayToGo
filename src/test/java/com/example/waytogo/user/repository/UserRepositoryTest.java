package com.example.waytogo.user.repository;

import com.example.waytogo.initialize.InitializationBasic;
import com.example.waytogo.initialize.csvLoading.repository.CsvConverterGeneric;
import com.example.waytogo.initialize.csvLoading.service.CsvServiceLoader;
import com.example.waytogo.security.config.BeansSecurityConfig;
import com.example.waytogo.security.jwt.JWTService;
import com.example.waytogo.user.model.entity.User;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@Import({InitializationBasic.class, CsvServiceLoader.class, CsvConverterGeneric.class, BeansSecurityConfig.class})
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;


    @Test
    void testGetAllUsers() {
        List<User> list = userRepository.findAll();

        assertThat(list).isNotNull();
        assertThat(list.size()).isEqualTo(500);
    }

    @Test
    void testGetUserById() {
        User user = User.builder()
                .password("p")
                .login("l")
                .username("u")
                .build();

        User savedUser = userRepository.save(user);
        userRepository.flush();

        User foundUser = userRepository.findById(savedUser.getId()).get();

        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getId()).isEqualTo(savedUser.getId());
    }

    @Test
    void testSaveUserUsernameTooLong() {
        assertThrows(ConstraintViolationException.class, () -> {
            User savedUser = userRepository.save(User.builder()
                    .password("haslo")
                    .login("login")
                    .username("usernameusernameusernameusernameusernameusernameusernameusernameusernameusernameusernameusernameusernameusernameusernameusernameusernameusernameusernameusernameusernameusernameusername")
                    .build());

            userRepository.flush();
        });
    }

    @Test
    void testSaveUserLoginTooLong() {
        assertThrows(ConstraintViolationException.class, () -> {
            User savedUser = userRepository.save(User.builder()
                    .password("haslo")
                    .login("loginloginloginloginloginloginloginloginloginloginloginloginloginloginloginloginloginloginloginloginloginloginloginloginloginloginloginloginloginloginloginloginloginloginlogin")
                    .username("username")
                    .build());

            userRepository.flush();
        });
    }

    @Test
    void testSaveUserPasswordTooLong() {
        assertThrows(ConstraintViolationException.class, () -> {
            User savedUser = userRepository.save(User.builder()
                    .password("haslohaslohaslohaslohaslohaslohaslohaslohaslohaslohaslohaslohaslohaslohaslohaslohaslohaslohaslohaslohaslohaslohaslohaslohaslohaslohaslohaslohaslohaslohaslohaslohaslohaslohaslohaslohaslohaslohaslohaslohaslohaslohaslohaslohaslohaslohaslohaslohaslohaslo")
                    .login("login")
                    .username("username")
                    .build());

            userRepository.flush();
        });
    }

    @Test
    void testSaveUserNullPassword() {
        assertThrows(ConstraintViolationException.class, () -> {
            User savedUser = userRepository.save(User.builder()
                    .login("login")
                    .username("username")
                    .build());

            userRepository.flush();
        });
    }

    @Test
    void testSaveUserNullLogin() {
        assertThrows(ConstraintViolationException.class, () -> {
            User savedUser = userRepository.save(User.builder()
                    .password("password")
                    .username("username")
                    .build());

            userRepository.flush();
        });
    }

    @Test
    void testSaveUserNullUsername() {
        assertThrows(ConstraintViolationException.class, () -> {
            User savedUser = userRepository.save(User.builder()
                    .password("password")
                    .login("login")
                    .build());

            userRepository.flush();
        });
    }

    @Test
    void testSaveUser() {
        User savedUser = userRepository.save(User.builder()
                .password("haslo")
                .login("login")
                .username("username")
                .build());

        userRepository.flush();

        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getId().toString()).isGreaterThan("0");
    }

    @Test
    void testUpdateUser() {
        User savedUser = userRepository.save(User.builder()
                .password("haslo")
                .login("login")
                .username("username")
                .build());

        userRepository.flush();

        savedUser.setUsername("Zmienione");
        savedUser.setPassword("Zmienione");
        savedUser.setLogin("Zmienione");

        User updatedUser = userRepository.save(savedUser);

        assertThat(updatedUser.getUsername()).isNotNull();
        assertThat(updatedUser.getUsername()).isEqualTo(savedUser.getUsername());
        assertThat(updatedUser.getPassword()).isNotNull();
        assertThat(updatedUser.getLogin()).isNotNull();
    }

    @Test
    void testDeleteUser() {
        User savedUser = userRepository.save(User.builder()
                .password("haslo")
                .login("login")
                .username("username")
                .build());

        userRepository.deleteById(savedUser.getId());
        Optional<User> returnedUser = userRepository.findById(savedUser.getId());

        assertThat(returnedUser).isEmpty();
    }
}











