package com.example.assignment;

import com.example.assignment.auth.AuthController;
import com.example.assignment.model.RefreshToken;
import com.example.assignment.tokenService.JwtService;
import com.example.assignment.tokenService.RefreshTokenService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @Mock
    private RefreshTokenService refreshTokenService;

    @InjectMocks
    private AuthController authController;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testAuthenticateAndGetToken_Success() throws Exception {
        Authentication authentication = Mockito.mock(Authentication.class);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken("refreshToken");
        when(refreshTokenService.createRefreshToken(any(String.class))).thenReturn(refreshToken);

        when(jwtService.generateToken(any(String.class))).thenReturn("accessToken");

        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(authController).build();

        mockMvc.perform(post("/api/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"dinesh\", \"password\": \"dinesh\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").exists())
                .andExpect(jsonPath("$.refreshToken").exists())
                .andExpect(jsonPath("$.accessToken").value("accessToken"))
                .andExpect(jsonPath("$.refreshToken").value("refreshToken"));
    }

    @Test
    public void testAuthenticateAndGetToken_InvalidUser() throws Exception {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new UsernameNotFoundException("invalid user request!"));

        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(authController).build();

        mockMvc.perform(post("/api/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"dinesh\", \"password\": \"dinesh1\"}"))
                .andExpect(status().isUnauthorized());
    }



    @Test
    public void testRefreshToken_Success() throws Exception {
        String requestBody = "{\"token\": \"valid_refresh_token\"}";

        mockMvc.perform(post("/api/refreshToken")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.accessToken").doesNotExist())
                .andExpect(jsonPath("$.refreshToken").doesNotExist());
    }


    @Test
    public void testRefreshToken_InvalidToken() throws Exception {
        String requestBody = "{\"token\": \"invalid_refresh_token\"}";

        mockMvc.perform(post("/api/refreshToken")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Refresh Token is not valid or has expired"));
    }



}
