package demo.winery;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
class WineInitializer implements ApplicationRunner {

    private final WineRepository wineRepository;

    public void initializeWines() {
        Stream.of("Wine 1", "Wine 2", "Wine 3")
                .map(Wine::valueOf)
                .forEach(wineRepository::save);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        initializeWines();
    }
}
