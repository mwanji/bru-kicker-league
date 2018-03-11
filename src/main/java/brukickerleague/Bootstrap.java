package brukickerleague;

import j2html.tags.ContainerTag;
import j2html.tags.DomContent;

import static j2html.TagCreator.*;

class Bootstrap {

    static ContainerTag formGroup(ContainerTag label, DomContent input) {
        return div(attrs(".form-group"), label, input);
    }

    static DomContent submit() {
        return button(attrs(".btn.btn-primary"), "Submit").withType("submit");
    }
}
