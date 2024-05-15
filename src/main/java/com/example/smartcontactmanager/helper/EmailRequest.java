package com.example.smartcontactmanager.helper;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class EmailRequest {

    private String to;

    private String subject;

    private String message;

}
