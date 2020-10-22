package org.example.userservice.restcontrollers;

import org.example.userservice.model.User;
import org.example.userservice.response.ResponseMessage;
import org.example.userservice.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/userapi/")
public class UserRestController {

    private final UserService userService;

    @Autowired
    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/getall", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseMessage<List<User>>> getAllUsers() {
        ResponseMessage<List<User>> responseMessage = new ResponseMessage<>();
        responseMessage.setSuccess(true);
        List<User> users = userService.getAll();
        if (users.size() > 0) {
            users.forEach((u) -> u.setRoles(null));
            responseMessage.setObject(users);
        }
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    @RequestMapping(value = "/id/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseMessage<User>> getUser(@PathVariable("id") long id) {
        try {
            ResponseMessage<User> responseMessage = new ResponseMessage<>();
            User u = userService.findById(id);
            if (u != null) {
                responseMessage.setSuccess(true);
                responseMessage.setObject(u);
                return new ResponseEntity<>(responseMessage, HttpStatus.OK);
            }
            responseMessage.setSuccess(false);
            responseMessage.putError("No user", "No user with id= " + id + " not found");
            return new ResponseEntity<>(responseMessage, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            ResponseMessage<User> responseMessage = new ResponseMessage<>();
            responseMessage.setSuccess(false);
            return new ResponseEntity<>(responseMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/login/{login}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseMessage<User>> getUser(@PathVariable String login) {
        try {
            ResponseMessage<User> responseMessage = new ResponseMessage<>();
            User u = userService.findByLogin(login);
            if (u != null) {
                responseMessage.setSuccess(true);
                responseMessage.setObject(u);
                return new ResponseEntity<>(responseMessage, HttpStatus.OK);
            }
            responseMessage.setSuccess(false);
            responseMessage.putError("No user", "No user with login = " + login + " not found");
            return new ResponseEntity<>(responseMessage, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            ResponseMessage<User> responseMessage = new ResponseMessage<>();
            responseMessage.setSuccess(false);
            return new ResponseEntity<>(responseMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseMessage<User>> addUser(@RequestBody User user) {
        ResponseMessage<User> responseMessage = new ResponseMessage<>();
        User u = userService.findByLogin(user.getLogin());

        if (u != null) {
            responseMessage.setSuccess(false);
            responseMessage.putError("User exists", "User with login = " + user.getLogin() + " exists");
            return new ResponseEntity<>(responseMessage, HttpStatus.BAD_REQUEST);
        } else {
            return saveUser(user);
        }
    }

    @RequestMapping(value = "/update", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseMessage<User>> updateUser(@RequestBody User user) {
        ResponseMessage<User> responseMessage = new ResponseMessage<>();

        User u = userService.findByLogin(user.getLogin());
        if (u != null) {
            user.setId(u.getId());
            return saveUser(user);
        } else {
            responseMessage.setSuccess(false);
            responseMessage.putError("Nothing to update", "No user found with login = " + user.getLogin());
            return new ResponseEntity<>(responseMessage, HttpStatus.NO_CONTENT);
        }
    }

    @RequestMapping(value = "/delete/id/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseMessage<User>> deleteUserById(@PathVariable long id) {
        ResponseMessage<User> responseMessage = new ResponseMessage<>();
        try {
            userService.delete(id);
            responseMessage.setSuccess(true);
            return new ResponseEntity<>(responseMessage, HttpStatus.OK);
        } catch (EmptyResultDataAccessException noData) {
            responseMessage.setSuccess(false);
            responseMessage.putError("User not found", "No user with id = " + id);
            return new ResponseEntity<>(responseMessage, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/delete/login/{login}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseMessage<User>> deleteUserByLogin(@PathVariable String login) {
        ResponseMessage<User> responseMessage = new ResponseMessage<>();
        User userToDelete = userService.findByLogin(login);
        if (userToDelete == null) {
            responseMessage.setSuccess(false);
            responseMessage.putError("User not found", "No user with login = " + login);
            return new ResponseEntity<>(responseMessage, HttpStatus.BAD_REQUEST);
        }
        try {
            userService.delete(userToDelete);
            responseMessage.setSuccess(true);
            return new ResponseEntity<>(responseMessage, HttpStatus.OK);
        } catch (Exception e) {
            responseMessage.setSuccess(false);
            responseMessage.putError("Internal error", e.getMessage());
            return new ResponseEntity<>(responseMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    private ResponseEntity<ResponseMessage<User>> saveUser(User user) {
        ResponseMessage<User> responseMessage = new ResponseMessage<>();
        responseMessage.setSuccess(true);
        try {
            if (!user.isPasswordValid()) {
                responseMessage.setSuccess(false);
                responseMessage.putError("Bad password", "Please provide password contains one uppercase letter, one lowercase letter and one number");
            }
            if (!user.isLoginValid()) {
                responseMessage.setSuccess(false);
                responseMessage.putError("Bad login", "Login was not provided");
            }
            if (!user.isNameValid()) {
                responseMessage.setSuccess(false);
                responseMessage.putError("Bad name", "Name was not provided");
            }
            if (!user.isRolesValid()) {
                responseMessage.setSuccess(false);
                responseMessage.putError("No roles", "Roles was not provided");
            }

            if (responseMessage.isSuccess()) {
                userService.save(user);
                responseMessage.setSuccess(true);
                responseMessage.setObject(user);
                return new ResponseEntity<>(responseMessage, HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(responseMessage, HttpStatus.BAD_REQUEST);
            }

        } catch (DataIntegrityViolationException integrityViolationException) {
            responseMessage.setSuccess(false);
            responseMessage.putError("Invalid data", "Invalid data was provided");
            return new ResponseEntity<>(responseMessage, HttpStatus.BAD_REQUEST);
        } catch (JpaObjectRetrievalFailureException jpaException) {
            responseMessage.setSuccess(false);
            responseMessage.putError("Entity not found", jpaException.getMessage());
            return new ResponseEntity<>(responseMessage, HttpStatus.BAD_REQUEST);
        }
    }
}
