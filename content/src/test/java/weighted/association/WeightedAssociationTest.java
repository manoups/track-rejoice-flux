package weighted.association;

import com.breece.content.command.api.WeightedAssociationHandler;
import com.breece.coreapi.score.association.GetAllAssociations;
import io.fluxzero.sdk.test.TestFixture;
import org.junit.jupiter.api.Test;
import util.TestUtilities;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;

public class WeightedAssociationTest  extends TestUtilities {
    final TestFixture testFixture = TestFixture.createAsync(new WeightedAssociationHandler());
//            .givenCommands(createUserFromProfile(viewer), createUserFromProfile(user2), createUserFromProfile(Alice));

    @Test
    void givenSightingWithoutContent_whenQuery_thenEmptyList() {
        testFixture.givenCommands("/sighting/create-sighting.json")
                .whenQuery(new GetAllAssociations())
                .expectResult(List::isEmpty);
    }

    @Test
    void givenContentWithoutSighting_whenQuery_thenEmptyList() {
        testFixture.givenCommands("/content/create-content.json")
                .whenQuery(new GetAllAssociations())
                .expectResult(List::isEmpty);
    }

    @Test
    void givenTwoContentsWithoutSighting_whenQuery_thenEmptyList() {
        testFixture.givenCommands("/content/create-content.json", "/content/create-content-keys.json")
                .whenQuery(new GetAllAssociations())
                .expectResult(List::isEmpty);
    }

    @Test
    void givenContentAndSighting_whenQuery_thenOneAssociation() {
        testFixture.givenCommands("/sighting/create-sighting.json", "/content/create-content.json")
                .whenQuery(new GetAllAssociations())
                .expectResult(hasSize(1));
    }

    @Test
    void givenTwoContentsAndSighting_whenQuery_thenTwoAssociations() {
        testFixture.givenCommands("/content/create-content.json", "/content/create-content-keys.json", "/sighting/create-sighting.json")
                .whenQuery(new GetAllAssociations())
                .expectResult(hasSize(2));
    }
}
