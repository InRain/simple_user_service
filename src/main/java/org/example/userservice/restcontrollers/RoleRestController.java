package org.example.userservice.restcontrollers;

import org.example.userservice.model.Role;
import org.example.userservice.response.ResponseMessage;
import org.example.userservice.service.role.RoleService;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.exception.DataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/roleapi/")
public class RoleRestController {

    private final RoleService roleService;

    @Autowired
    public RoleRestController(RoleService roleService) {
        this.roleService = roleService;
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseMessage<Role>> saveRole(@RequestBody Role role) {
        ResponseMessage<Role> responseMessage = new ResponseMessage<>();
        if (role == null) {
            responseMessage.setSuccess(false);
            responseMessage.putError("Empty body","Empty body");
            return new ResponseEntity<>(responseMessage, HttpStatus.BAD_REQUEST);
        }
        try {
            roleService.add(role);
            responseMessage.setSuccess(true);
            responseMessage.setObject(role);
            return new ResponseEntity<>(responseMessage, HttpStatus.CREATED);
        } catch (DataIntegrityViolationException integrityViolationException) {
            if (integrityViolationException.getCause() instanceof ConstraintViolationException) {
                responseMessage.setSuccess(false);
                responseMessage.putError("Record Exists","Role already exists");
            }
            if (integrityViolationException.getCause() instanceof DataException) {
                responseMessage.setSuccess(false);
                responseMessage.putError("Invalid data", "Invalid data was provided");
            }
            return new ResponseEntity<>(responseMessage, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/getall", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Role>> getAllRoles() {
        List<Role> roles = roleService.getAll();
        if (roles.size() == 0) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(roles, HttpStatus.OK);
    }
}
