package brukickerleague;

import j2html.tags.DomContent;

import static j2html.TagCreator.*;

public class Page {

    private String title;
    private DomContent[] contents;

    Page(String title, DomContent... contents) {
        this.title = title;
        this.contents = contents;
    }

    String render() {
        return document(html(
            head(
                meta().withCharset("utf-8"),
                meta().attr("name", "viewport").attr("content", "width=device-width, initial-scale=1, shrink-to-fit=no"),
                link().withRel("stylesheet").withHref("https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css").attr("integrity", "sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm").attr("crossorigin", "anonymous"),
                title(title + " - Bru Kicker League")
            ),
            body(
                div(attrs(".container"), contents)
            )
        ));
    }
}
