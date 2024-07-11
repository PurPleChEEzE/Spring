package kr.co.bookstore.kpi;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import kr.co.bookstore.model.dto.BookDto;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/kpi")
public class Kpi {
    private static final Logger logger = LogManager.getLogger(Kpi.class);
    private final RestTemplate restTemplate;
    private static final String BASE_URL = "http://localhost/books/list";

    @Autowired
    public Kpi(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    
    @GetMapping("")
    public ResponseEntity<List<BookDto>> getKpiList() {
        logger.info("/kpi 요청 받음");
        ResponseEntity<BookDto[]> response = restTemplate.getForEntity(BASE_URL, BookDto[].class);
        List<BookDto> kpiList = Arrays.asList(response.getBody());
        return ResponseEntity.ok(kpiList);
    }
    
    @GetMapping("/{mainKpi}")
    public ResponseEntity<List<BookDto>> getKpiDetail(@PathVariable String mainKpi) {
        logger.info("/kpi/"+ mainKpi + " 요청 받음");
        ResponseEntity<BookDto[]> response = restTemplate.getForEntity(BASE_URL + "/" + mainKpi, BookDto[].class);
        List<BookDto> kpiDetail = Arrays.asList(response.getBody());
        return ResponseEntity.ok(kpiDetail);
    }
}
