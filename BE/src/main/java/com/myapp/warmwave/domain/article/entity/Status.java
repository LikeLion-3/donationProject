package com.myapp.warmwave.domain.article.entity;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Status {
    DEFAULT("기본"), PROGRESS("진행중"), COMPLETE("완료");

    private final String status;

    public static Status getStatusByString(String input) {
        for (Status status : Status.values()) {
            if (status.getStatus().equals(input)) {
                return status;
            }
        }
        return DEFAULT;
    }
}
