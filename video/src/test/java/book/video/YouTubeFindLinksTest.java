package book.video;

import book.video.boundary.YouTubeGateway;
import book.video.entity.YoutubeLinks;
import java.io.IOException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class YouTubeFindLinksTest {
    @Autowired
    private YouTubeGateway youTubeGateway;

    @Test
    public void shouldReturnFirstThreeLinksForGame() throws IOException {
        String game = "zelda";
        YoutubeLinks result = youTubeGateway.findYoutubeLinks(game);

        result.getYoutubeLinks().forEach(System.out::println);
    }
}
