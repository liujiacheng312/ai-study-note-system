package com.example.ainote.dto;

import lombok.Data;

@Data
public class NoteQueryRequest {
    private Long pageNo = 1L;
    private Long pageSize = 10L;
    private String keyword;
    private Long categoryId;
    private Long tagId;
    private String status;
}
