package com.shubhampant.studentDetailsSystem.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
@Setter
public class EmailDetails {

    private String email;

    private String msgSubject;

    private String msgBody;
}
