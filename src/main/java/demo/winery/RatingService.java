package demo.winery;

import org.springframework.stereotype.Service;

@Service
public class RatingService {

    public int getRating(Wine wine) {

        switch (wine.getName()) {
            case "Wine 1":
                return 10;
            case "Wine 2":
                return 2;
            case "Wine 3":
                return 7;
        }

        return 0;
    }
}
