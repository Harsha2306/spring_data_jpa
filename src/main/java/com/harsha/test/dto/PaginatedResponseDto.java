package com.harsha.test.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaginatedResponseDto<T> {
  private List<T> data;
  private Long totalUsers;
  private int totalPages;
  private int currentPage;
  private int pageSize;
  private Map<String, String> links;
}
