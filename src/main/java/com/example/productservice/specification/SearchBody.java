package com.example.productservice.specification;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SearchBody {
//    private String keyword;
    private int page;
    private int limit;
    private String userId;
    private int status;
}
