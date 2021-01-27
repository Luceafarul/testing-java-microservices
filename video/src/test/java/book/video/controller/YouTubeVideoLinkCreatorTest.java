package book.video.controller;

import org.junit.Test;

import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;

// tag::test[]
public class YouTubeVideoLinkCreatorTest {

    @Test
    public void shouldReturnYouTubeEmbeddedUrlForGivenVideoId() {
        final YouTubeVideoLinkCreator youTubeVideoLinkCreator = new YouTubeVideoLinkCreator();

        final URL embeddedUrl = youTubeVideoLinkCreator.createEmbeddedUrl("1234");

        assertThat(embeddedUrl).hasHost("www.youtube.com").hasPath("/embed/1234");
    }
}
// end::test[]