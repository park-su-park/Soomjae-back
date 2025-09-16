package com.parksupark.soomjae.server.auth.service;

import com.parksupark.soomjae.server.auth.username.dto.UsernamePasswordAuthSuccessResponse;

public interface AuthService {

    UsernamePasswordAuthSuccessResponse refresh(String refreshToken);

}
