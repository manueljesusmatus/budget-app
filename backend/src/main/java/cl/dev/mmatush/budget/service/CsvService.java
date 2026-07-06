package cl.dev.mmatush.budget.service;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.function.ThrowingSupplier;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CsvService {

    private static final CsvMapper CSV_MAPPER;

    static {
        CSV_MAPPER = new CsvMapper();
        CSV_MAPPER.registerModule(new JavaTimeModule());
        CSV_MAPPER.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    public <T> List<T> loadObjectListFromClasspath(Class<T> type, String fileName) {
        return loadObjectList(type, () -> new ClassPathResource(fileName).getInputStream());
    }

    public <T> List<T> loadObjectListFromUpload(Class<T> type, MultipartFile file) {
        return loadObjectList(type, file::getInputStream);
    }

    private <T> List<T> loadObjectList(Class<T> type, ThrowingSupplier<InputStream> source) {
        try (
                InputStream is = source.get();
                MappingIterator<T> readValues = CSV_MAPPER.readerFor(type)
                        .with(CsvSchema.emptySchema().withHeader())
                        .readValues(is)
        ) {
            return readValues.readAll();
        } catch (IOException e) {
            log.error("Error loading object list", e);
            return Collections.emptyList();
        }
    }

}
