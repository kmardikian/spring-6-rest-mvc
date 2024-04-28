package guru.springframework.spring6restmvc.bootstrap;

import guru.springframework.spring6restmvc.entities.Beer;
import guru.springframework.spring6restmvc.entities.Customer;
import guru.springframework.spring6restmvc.model.BeerCSVRecord;
import guru.springframework.spring6restmvc.model.BeerStyle;
import guru.springframework.spring6restmvc.repositories.BeerRepository;
import guru.springframework.spring6restmvc.repositories.CustomerRepository;
import guru.springframework.spring6restmvc.services.BeerCsvService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class Bootstrap implements CommandLineRunner {
    private final CustomerRepository customerRepository;
    private final BeerRepository beerRepository;
    private final BeerCsvService beerCsvService;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        Customer customer = Customer.builder()
                .name("Bugs Bunny")
                .modDate(LocalDate.now())
                .crtDate(LocalDate.now())
                .build();
        customerRepository.save(customer);
        //log.info("saving customer name is {}",customer.getName());

        customer = Customer.builder()
                .name("Duffy Duck")
                .modDate(LocalDate.now())
                .crtDate(LocalDate.now())
                .build();
        customerRepository.save(customer);
        customerRepository.save(Customer.builder()
                .id(UUID.randomUUID())
                .name("Richard Jone")
                .version(1)
                .crtDate(LocalDate.now())
                .modDate(LocalDate.now())
                .build());

        loadBeerData();
        if (beerRepository.count() < 10) {
            loadBeerCsv();
        }

    }

    private void loadBeerData() {
        if (beerRepository.count() == 0) {
            Beer beer1 = Beer.builder()
                    .beerName("Galaxy Cat")
                    .beerStyle(BeerStyle.PALE_ALE)
                    .upc("12356")
                    .price(new BigDecimal("12.99"))
                    .quantityOnHand(122)
                    .createdDate(LocalDateTime.now())
                    .updateDate(LocalDateTime.now())
                    .build();

            Beer beer2 = Beer.builder()
                    .beerName("Crank")
                    .beerStyle(BeerStyle.PALE_ALE)
                    .upc("12356222")
                    .price(new BigDecimal("11.99"))
                    .quantityOnHand(392)
                    .createdDate(LocalDateTime.now())
                    .updateDate(LocalDateTime.now())
                    .build();

            Beer beer3 = Beer.builder()
                    .beerName("Sunshine City")
                    .beerStyle(BeerStyle.IPA)
                    .upc("12356")
                    .price(new BigDecimal("13.99"))
                    .quantityOnHand(144)
                    .createdDate(LocalDateTime.now())
                    .updateDate(LocalDateTime.now())
                    .build();

            beerRepository.saveAll(Arrays.asList(beer1, beer2, beer3));

//            beerRepository.save(beer1);
//            beerRepository.save(beer2);
//            beerRepository.save(beer3);
        }

    }

    void loadBeerCsv() {
        File file = null;
        List<BeerCSVRecord> recs = new ArrayList<>();
        try {
            file = ResourceUtils.getFile("classpath:csvdata/beer.csv");
            recs = beerCsvService.convertCsv(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        for (BeerCSVRecord rec : recs) {
            BeerStyle beerStyle = switch (rec.getState()) {
                case "American Pale Lager" -> BeerStyle.LAGER;
                case "American Pale Ale (APA)", "American Black Ale", "Belgian Dark Ale", "American Blonde Ale" ->
                        BeerStyle.ALE;
                case "American IPA", "American Double / Imperial IPA", "Belgian IPA" -> BeerStyle.IPA;
                case "American Porter" -> BeerStyle.PORTER;
                case "Oatmeal Stout", "American Stout" -> BeerStyle.STOUT;
                case "Saison / Farmhouse Ale" -> BeerStyle.SAISON;
                case "Fruit / Vegetable Beer", "Winter Warmer", "Berliner Weissbier" -> BeerStyle.WHEAT;
                case "English Pale Ale" -> BeerStyle.PALE_ALE;
                default -> BeerStyle.PILSNER;
            };
            beerRepository.save(Beer.builder()
                    .beerName(StringUtils.abbreviate(rec.getBeer(), 50))
                    .beerStyle(beerStyle)
                    .price(BigDecimal.TEN)
                    .upc(rec.getRow().toString())
                    .quantityOnHand(rec.getCount())
                    .build());
        }

    }


}
