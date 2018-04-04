package brukickerleague;

import j2html.tags.DomContent;

import static brukickerleague.Bootstrap.*;
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
        link().withRel("stylesheet").withHref("https://cdnjs.cloudflare.com/ajax/libs/open-iconic/1.1.1/font/css/open-iconic-bootstrap.min.css").attr("integrity", "sha256-BJ/G+e+y7bQdrYkS2RBTyNfBHpA9IuGaPmf9htub5MQ=").attr("crossorigin", "anonymous"),
        title(title + " - Bru Kicker League")
      ),
      body(attrs(".pb-2"),
        nav(attrs(".navbar.navbar-expand.navbar-dark.bg-primary"),
          div(attrs(".container"),
            a(attrs(".navbar-brand.pl-3"), "Bru Kicker League").withHref("/"),
            div(attrs(".collapse.navbar-collapse"),
              div(attrs(".navbar-nav"),
                a(attrs("btn.btn-outline-light.mr-1"), icon("plus"), span(attrs(".d-none.d-md-inline"), " Start Match")).withHref(Urls.match()),
                a(attrs("btn.btn-outline-light"), icon("star"), span(attrs(".d-none.d-md-inline"), " Awards")).withHref("/awards")
              )
            )
          )
        ),
        div(attrs(".container"), contents),
        footer(attrs(".mt-5.text-muted.text-center"),
          p("Created by Moandji Ezana for Bru"),
          p(
            a("crawl").withHref("https://thenounproject.com/term/crawl/485414"), text(" by Marco Fleseri from the Noun Project"),
            br(),
            a("helmet").withHref("https://thenounproject.com/term/helmet/1090140/"), text(" by art shop from the Noun Project"),
            br(),
            a("sword").withHref("https://thenounproject.com/term/sword/25239"), text(" by Geoffrey Joe from the Noun Project"),
            br(),
            a("Rock n Roll").withHref("https://thenounproject.com/term/rock-n-roll/136385"), text(" by julian roman from the Noun Project")
          )
        )
      )
    ));
  }
}
