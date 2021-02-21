package book.video;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import book.video.boundary.YouTubeGateway;
import book.video.controller.YouTubeVideoLinkCreator;
import book.video.entity.YoutubeLink;
import book.video.entity.YoutubeLinks;
import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class YouTubeFindLinksTest {

    @Mock
    private YouTubeGateway youTubeGateway;

    @Test
    public void shouldReturnFirstThreeLinksForGame() throws IOException {
        String game = "zelda";

        when(youTubeGateway.findYoutubeLinks(game)).thenReturn(zeldaResult());

        YoutubeLinks result = youTubeGateway.findYoutubeLinks(game);

        assertThat(result.getYoutubeLinks()).hasSize(3);
        assertThat(result.getYoutubeLinks().stream().map(YoutubeLink::getVideoId))
            .containsOnly(
                "530E9AwOsO0",
                "gbNitSCkAdY",
                "Ej31TPUzp_Q"
            );
    }


    private YoutubeLinks zeldaResult() {
        final YouTubeVideoLinkCreator youTubeVideoLinkCreator = new YouTubeVideoLinkCreator();

        Set<YoutubeLink> result = Stream.of(
            new YoutubeLink("530E9AwOsO0"),
            new YoutubeLink("gbNitSCkAdY"),
            new YoutubeLink("Ej31TPUzp_Q")
        ).peek(youtubeLink ->
            youtubeLink.setYouTubeVideoLinkCreator(youTubeVideoLinkCreator::createEmbeddedUrl)
        ).collect(Collectors.toSet());


        return new YoutubeLinks(result);
    }
}
