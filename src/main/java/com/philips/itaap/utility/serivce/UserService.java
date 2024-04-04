package com.philips.itaap.utility.serivce;

import com.philips.itaap.ms.dev.base.exception.ServiceException;
import com.philips.itaap.utility.dto.UsersDTO;
import com.philips.itaap.utility.entity.User;
import com.philips.itaap.utility.model.enums.Role;
import com.philips.itaap.utility.repository.UserRepo;
import lombok.extern.slf4j.XSlf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Service
@XSlf4j
@SuppressWarnings("CPD-START")
public class UserService {

    @Autowired
    private UserRepo userRepo;

    public Mono<User> createNewUser(UsersDTO dto) {
        if (userRepo.findByEmail(dto.getEmail()).isEmpty()) {
            User user = User.builder()
                    .name(dto.getName())
                    .email(dto.getEmail())
                    .role(Role.valueOf(dto.getRole()).name())
                    .build();
            return Mono
                    .just(userRepo.save(user))
                    .doOnSuccess(t -> {
                        if (log.isInfoEnabled()) {
                            log.info("CREATED : USER -> [{}]", dto.getName());
                        }
                    });
        } else {
            throw new ServiceException(HttpStatus.CONFLICT, 1001, "User " + dto.getName() + " already exists.");
        }
    }

    public Mono<List<User>> getAllUsers() {
        return Mono.just(userRepo.findAll().stream().filter(user -> user.getId() != 0).collect(Collectors.toList()))
                .doOnSuccess(t -> {
                    if (log.isInfoEnabled()) {
                        log.info("FETCHED : USER : ALL");
                    }
                });
    }

    public Mono<List<User>> getAllByRole(String role) {
        return Mono.just(userRepo.findByRole(role))
                .doOnSuccess(t -> {
                    if (log.isInfoEnabled()) {
                        log.info("FETCHED : USER : TECH LEAD");
                    }
                });
    }
}
