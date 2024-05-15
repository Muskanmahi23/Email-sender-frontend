package com.example.smartcontactmanager.helper;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Message {

    private String from;
    private String content;
    private List<String> files;
    private String subjects;
}
