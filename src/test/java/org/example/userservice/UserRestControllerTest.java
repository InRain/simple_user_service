package org.example.userservice;

import org.example.userservice.model.Role;
import org.example.userservice.model.User;
import org.example.userservice.response.ResponseMessage;
import org.example.userservice.restcontrollers.UserRestController;
import org.example.userservice.service.user.UserService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserRestControllerTest {

    private User validUser;

    private User emptyUser = new User();

    @Before
    public void init() {
        validUser = new User();
        validUser.setId(1L);
        validUser.setName("John Smith");
        validUser.setLogin("jSmith");
        validUser.setPassword("1SSdsfq");

        List<Role> roles = new ArrayList<>();
        roles.add(new Role("ADMIN"));

        validUser.setRoles(roles);
    }

    @Test
    public void addNewValidUserTest() {
        UserService mockService = mock(UserService.class);
        when(mockService.findByLogin("login")).thenReturn(null);

        UserRestController controller = new UserRestController(mockService);
        ResponseEntity<ResponseMessage<User>> response = controller.addUser(validUser);

        assertThat(response).isNotNull();
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().isSuccess()).isTrue();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    public void addExistingUser() {
        UserService mockService = mock(UserService.class);
        when(mockService.findByLogin(validUser.getLogin())).thenReturn(validUser);

        UserRestController controller = new UserRestController(mockService);
        ResponseEntity<ResponseMessage<User>> response = controller.addUser(validUser);

        assertThat(response).isNotNull();
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().isSuccess()).isFalse();
        assertThat(response.getBody().getErrors().size()).isEqualTo(1);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void addUserEmptyBody() {
        UserService mockService = mock(UserService.class);
        when(mockService.findByLogin(emptyUser.getLogin())).thenReturn(null);

        UserRestController controller = new UserRestController(mockService);
        ResponseEntity<ResponseMessage<User>> response = controller.addUser(emptyUser);

        assertThat(response).isNotNull();
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().isSuccess()).isFalse();
        assertThat(response.getBody().getErrors().size()).isEqualTo(4);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void addUserRoleConstraintViolation() {
        UserService mockService = mock(UserService.class);
        when(mockService.findByLogin(validUser.getLogin())).thenReturn(null);
        when(mockService.save(validUser)).thenThrow(new DataIntegrityViolationException(""));

        UserRestController controller = new UserRestController(mockService);
        ResponseEntity<ResponseMessage<User>> response = controller.addUser(validUser);

        assertThat(response).isNotNull();
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().isSuccess()).isFalse();
        assertThat(response.getBody().getErrors().size()).isEqualTo(1);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void deleteExistingUserById() {
        UserService mockService = mock(UserService.class);

        UserRestController controller = new UserRestController(mockService);
        ResponseEntity<ResponseMessage<User>> response = controller.deleteUserById(validUser.getId());

        assertThat(response).isNotNull();
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().isSuccess()).isTrue();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void deleteExistingUserByLogin() {
        UserService mockService = mock(UserService.class);

        when(mockService.findByLogin(validUser.getLogin())).thenReturn(validUser);

        UserRestController controller = new UserRestController(mockService);
        ResponseEntity<ResponseMessage<User>> response = controller.deleteUserByLogin(validUser.getLogin());

        assertThat(response).isNotNull();
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().isSuccess()).isTrue();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void deleteUnexistingUserById() {
        UserService mockService = mock(UserService.class);

        Mockito.doThrow(new EmptyResultDataAccessException(1)).when(mockService).delete(validUser.getId());
        UserRestController controller = new UserRestController(mockService);
        ResponseEntity<ResponseMessage<User>> response = controller.deleteUserById(validUser.getId());

        assertThat(response).isNotNull();
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().isSuccess()).isFalse();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().getErrors().size()).isEqualTo(1);
    }

    @Test
    public void deleteUnexistingUserByLogin() {
        UserService mockService = mock(UserService.class);

        when(mockService.findByLogin(validUser.getLogin())).thenReturn(null);

        UserRestController controller = new UserRestController(mockService);
        ResponseEntity<ResponseMessage<User>> response = controller.deleteUserByLogin(validUser.getLogin());

        assertThat(response).isNotNull();
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().isSuccess()).isFalse();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().getErrors().size()).isEqualTo(1);
    }
}
