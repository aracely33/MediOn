package clinica.medtech.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaginatedResponse<T> {
    private List<T> content;
    private int currentPage;
    private int pageSize;
    private int totalPages;
    private long totalElements;
}