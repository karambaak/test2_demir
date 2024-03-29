package com.example.firsthttpserver.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Information {
    private Long counter;
    private List<String> json;
    private transient String type;
    private transient String date;
}
