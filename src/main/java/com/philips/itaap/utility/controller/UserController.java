package com.philips.itaap.utility.controller;

import com.philips.itaap.utility.dto.UsersDTO;
import com.philips.itaap.utility.entity.User;
import com.philips.itaap.utility.exception.ServiceException;
import com.philips.itaap.utility.model.enums.Role;
import com.philips.itaap.utility.serivce.UserService;
import lombok.extern.slf4j.XSlf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@XSlf4j
@SuppressWarnings({"CPD-START"})
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(value = "/poc/create/user", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<User> createNewUser(@RequestBody @Valid UsersDTO usersDTO) {
        return userService.createNewUser(usersDTO);
    }

    @GetMapping(value = "/poc/fetch/users/all")
    public Mono<List<User>> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping(value = "/poc/fetch/tech-leads")
    public Mono<List<User>> getAllTechLeads() {
        return userService.getAllByRole(Role.T.name());
    }

    @GetMapping(value = "/poc/fetch/developers")
    public Mono<List<User>> getAllDevelopers() {
        return userService.getAllByRole(Role.D.name());
    }

    @GetMapping(value = "/poc/fetch/scrum-masters")
    public Mono<List<User>> getAllScrumMasters() {
        return userService.getAllByRole(Role.M.name());
    }

    @GetMapping(value = "/poc/fetch/clients")
    public Mono<List<User>> getAllClients() {
        return userService.getAllByRole(Role.C.name());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(WebExchangeBindException.class)
    public final @ResponseBody ServiceException handleValidationExceptions(WebExchangeBindException ex) {
        List<String> errorDesc = ex.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.toList());
        if (log.isErrorEnabled()) {
            log.error("handleValidationExceptions() : errorDesc -> {}", errorDesc);
        }
        throw new ServiceException(ex.getStatusCode(), ex.getStatusCode().value(), errorDesc.toString());
    }

}
