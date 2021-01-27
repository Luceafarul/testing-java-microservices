package book.video.entity;

import book.video.controller.YouTubeVideoLinkCreator;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class YouTubeLinkTest {

    @Test
    public void shouldCalculateEmbedYouTubeLink() {
        final YoutubeLink youtubeLink = new YoutubeLink("12345");
        final YouTubeVideoLinkCreator youTubeVideoLinkCreator = new YouTubeVideoLinkCreator();

        youtubeLink.setYouTubeVideoLinkCreator(youTubeVideoLinkCreator::createEmbeddedUrl);

        assertThat(youtubeLink.getEmbedUrl()).hasHost("www.youtube.com").hasPath("/embed/12345");
    }

}