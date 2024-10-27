package ru.gozhan.pronotesapi.service;

import ru.gozhan.pronotesapi.web.dto.JwtRequest;
import ru.gozhan.pronotesapi.web.dto.JwtResponse;

public interface AuthService {

    JwtResponse login(JwtRequest loginRequest);

    JwtResponse refresh(String refreshToken);

}
