package com.example.waytogo.user.service.impl;

import com.example.waytogo.audio.model.entity.Audio;
import com.example.waytogo.audio.repository.AudioRepository;
import com.example.waytogo.route.model.entity.Route;
import com.example.waytogo.route.repository.RouteRepository;
import com.example.waytogo.user.mapper.UserMapper;
import com.example.waytogo.user.model.dto.UserDTO;
import com.example.waytogo.user.model.entity.User;
import com.example.waytogo.user.repository.UserRepository;
import com.example.waytogo.user.service.api.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@Primary
@RequiredArgsConstructor
@Service
@Validated
public class UserServiceJPA implements UserService {
    private final static int DEFAULT_PAGE = 0;
    private final static int DEFAULT_PAGE_SIZE = 25;
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final RouteRepository routeRepository;
    private final AudioRepository audioRepository;

    @Override
    public Page<UserDTO> getAllUsers(Integer pageNumber, Integer pageSize) {
        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize);

        Page<User> userPage;
        userPage = userRepository.findAll(pageRequest);

        return userPage.map(userMapper::userToUserDto);
    }

    @Override
    public Optional<UserDTO> getUserById(UUID userId) {
        return Optional.ofNullable(userMapper.userToUserDto(userRepository.findById(userId)
                .orElse(null)));
    }

    @Override
    public UserDTO saveNewUser(UserDTO userDTO) {
        return userMapper.userToUserDto(userRepository.save(userMapper.userDtoToUser(userDTO)));
    }

    @Override
    public Optional<UserDTO> updateUserById(UUID userId, UserDTO userDTO) {
        AtomicReference<Optional<UserDTO>> atomicReference = new AtomicReference<>();

        userRepository.findById(userId).ifPresentOrElse(found -> {
            userDTO.setId(userId);
            atomicReference.set(Optional.of(userMapper
                    .userToUserDto(userRepository
                            .save(userMapper.userDtoToUser(userDTO)))));
        }, () -> {
            atomicReference.set(Optional.empty());
        });
        userDTO.setId(userId);

        return atomicReference.get();
    }

    @Override
    public boolean deleteUserById(UUID userId) {
        if (userRepository.existsById(userId)) {
            List<Route> routes = routeRepository.findByUser_Id(userId, PageRequest.of(0, Integer.MAX_VALUE)).getContent();
            for (Route r : routes) {
                r.setUser(null);
            }

            List<Audio> audios = audioRepository.findByUser_Id(userId, PageRequest.of(0, Integer.MAX_VALUE)).getContent();
            for (Audio a : audios) {
                a.setUser(null);
            }

            userRepository.deleteById(userId);
            return true;
        }
        return false;
    }

    @Override
    public Optional<UserDTO> patchUserById(UUID userId, UserDTO userDTO) {
        AtomicReference<Optional<UserDTO>> atomicReference = new AtomicReference<>();

        userRepository.findById(userId).ifPresentOrElse(foundUser -> {
            if (StringUtils.hasText(userDTO.getPassword())) {
                foundUser.setPassword(userDTO.getPassword());
            }
            if (StringUtils.hasText(userDTO.getLogin())) {
                foundUser.setLogin(userDTO.getLogin());
            }
            if (StringUtils.hasText(userDTO.getUsername())) {
                foundUser.setUsername(userDTO.getUsername());
            }
            atomicReference.set(Optional.of(userMapper.userToUserDto(userRepository.save(foundUser))));
        }, () -> {
            atomicReference.set(Optional.empty());
        });

        return atomicReference.get();
    }

    private PageRequest buildPageRequest(Integer pageNumber, Integer pageSize) {
        int queryPageNumber;
        int queryPageSize;

        if (pageNumber != null && pageNumber > 0) {
            queryPageNumber = pageNumber - 1;
        } else {
            queryPageNumber = DEFAULT_PAGE;
        }

        if (pageSize == null) {
            queryPageSize = DEFAULT_PAGE_SIZE;
        } else {
            if (pageSize > 1000) {
                queryPageSize = 1000;
            } else {
                queryPageSize = pageSize;
            }
        }

        Sort sort = Sort.by(Sort.Order.asc("username"));

        return PageRequest.of(queryPageNumber, queryPageSize, sort);
    }
}
