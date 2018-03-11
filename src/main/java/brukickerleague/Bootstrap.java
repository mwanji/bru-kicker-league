package brukickerleague;

import j2html.tags.ContainerTag;
import j2html.tags.DomContent;

import java.util.Map;

import static j2html.TagCreator.*;

class Bootstrap {

    static ContainerTag formGroup(ContainerTag label, DomContent input, DomContent... contents) {
        DomContent[] _contents = new DomContent[contents.length + 2];
        _contents[0] = label;
        _contents[1] = input;
        System.arraycopy(contents, 0, _contents, 2, contents.length);

        return div(attrs(".form-group"), _contents);
    }

    static DomContent submit() {
        return button(attrs(".btn.btn-primary"), "Submit").withType("submit");
    }

  static String ifInvalidInput(String key, Map<String, String> errors) {
    return iff(errors.containsKey("team1Player1"), "is-invalid");
  }

  static DomContent ifInvalidHelpText(String key, Map<String, String> errors) {
    return iff(errors.containsKey(key), div(attrs(".invalid-feedback"), errors.get(key)));
  }
}
