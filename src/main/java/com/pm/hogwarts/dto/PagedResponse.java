package com.pm.hogwarts.dto;

import java.util.List;

public record PagedResponse<T>(int page, int size, int total, List<T> items) {
}
