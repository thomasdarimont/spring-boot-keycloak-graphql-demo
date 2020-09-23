package demo.winery;

import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLContext;
import io.leangen.graphql.annotations.GraphQLMutation;
import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.spqr.spring.annotations.GraphQLApi;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@GraphQLApi
@RequiredArgsConstructor
public class WineGraphqlService {

    private final RatingService ratingService;

    private final WineRepository wineRepository;

    @GraphQLQuery(name = "wines") // READ ALL
    public List<Wine> getWines() {
        return wineRepository.findAll();
    }

    @GraphQLQuery(name = "wine") // READ BY ID
    public Optional<Wine> getWineById(@GraphQLArgument(name = "id") Long id) {
        return wineRepository.findById(id);
    }

    @GraphQLMutation(name = "saveWine") // CREATE
    public Wine saveWine(@GraphQLArgument(name = "wine") Wine wine) {
        return wineRepository.save(wine);
    }

    @GraphQLMutation(name = "deleteWine") // DELETE
    public void deleteWine(@GraphQLArgument(name = "id") Long id) {
        wineRepository.deleteById(id);
    }

    @GraphQLQuery(name = "rating") // Calculated property of Wine
    public int getRating(@GraphQLContext Wine wine) {
        return ratingService.getRating(wine);
    }
}
