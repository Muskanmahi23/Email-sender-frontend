package com.example.smartcontactmanager.helper;

import lombok.*;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@Data
public class CustomResponse {

    private String message;

    private HttpStatus httpStatus;

    private boolean success=false;
}
