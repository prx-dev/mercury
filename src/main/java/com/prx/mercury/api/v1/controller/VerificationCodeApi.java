package com.prx.mercury.api.v1.controller;

import com.prx.mercury.api.v1.service.VerificationCodeService;
import com.prx.mercury.api.v1.to.VerificationCodeRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "verification-code", description = "The verification code API")
public interface VerificationCodeApi {

    default VerificationCodeService getService() {
        return new VerificationCodeService() {
        };
    }

    @Operation(description = "Send verification code to user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Verification code sent successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid phone number"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    default ResponseEntity<Void> sendVerificationCode(@RequestBody @Valid VerificationCodeRequest request) {
        return this.getService().confirmCode(request);
    }

}
