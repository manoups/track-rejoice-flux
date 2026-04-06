package com.breece.local.seed;

import com.breece.content.api.model.ContentId;
import com.breece.content.api.model.GenderEnum;
import com.breece.content.api.model.Pet;
import com.breece.content.command.api.CreateContent;
import com.breece.content.command.api.PublishContent;
import com.breece.coreapi.common.SightingDetails;
import com.breece.coreapi.common.SightingEnum;
import com.breece.sighting.api.model.SightingId;
import com.breece.sighting.command.api.CreateSighting;
import io.fluxzero.sdk.Fluxzero;
import org.springframework.boot.SpringApplication;

import java.math.BigDecimal;
import java.time.Duration;

public class SeedPlan {
    public void seed() {
        CreateContent createContent;
        PublishContent publishContent;
        CreateSighting[] sightings;
        final int SIZE = 7;
        sightings = new CreateSighting[SIZE];
        ContentId contentId = new ContentId();
        for (int i = 0; i < SIZE; ++i) {
            sightings[i] = new CreateSighting(new SightingId(), new SightingDetails(BigDecimal.valueOf(i), BigDecimal.valueOf(i * 0.1)), false, SightingEnum.DOG);
        }
        publishContent = new PublishContent(contentId, Duration.ofDays(90));
        createContent = new CreateContent(contentId, new SightingDetails(new BigDecimal("-1"), new BigDecimal("-1")), new Pet("Maya", "Cocker Spaniel", GenderEnum.FEMALE, SightingEnum.DOG));

        Fluxzero.sendCommandAndWait(createContent);
        Fluxzero.sendCommandAndWait(publishContent);
        for (CreateSighting sighting : sightings) {
            Fluxzero.sendCommandAndWait(sighting);
        }
//        Get content and find first association
//        Claim sighting
//        new ClaimSighting(createContent.contentId(), createWeighedAssociations[0].weightedAssociationId()
    }
}
