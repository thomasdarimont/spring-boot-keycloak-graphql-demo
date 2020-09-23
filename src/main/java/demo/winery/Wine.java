package demo.winery;

import io.leangen.graphql.annotations.GraphQLQuery;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@ToString
public class Wine {

    @Id
    @GeneratedValue
    @GraphQLQuery(name = "id", description = "A wines's id")
    Long id;

    @GraphQLQuery(name = "name", description = "A wines's name")
    String name;

    public static Wine valueOf(String name) {

        Wine wine = new Wine();
        wine.setName(name);

        return wine;
    }
}
