package guru.springframework.spring6restmvc.services;

import com.opencsv.bean.CsvToBeanBuilder;
import guru.springframework.spring6restmvc.model.BeerCSVRecord;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.io.FileReader;

@Service
public class BeerCsvServiceImpl implements BeerCsvService {
    @Override
    public List<BeerCSVRecord> convertCsv(File csvFile) {
        List<BeerCSVRecord>  csvList = new ArrayList<>();
        try {
            csvList = new CsvToBeanBuilder(new FileReader(csvFile))
                    .withType(BeerCSVRecord.class).build().parse();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return csvList;
    }
}
