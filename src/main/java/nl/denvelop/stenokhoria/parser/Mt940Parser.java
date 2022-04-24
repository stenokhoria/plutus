package nl.denvelop.stenokhoria.parser;

import static com.prowidesoftware.swift.model.field.Field61.AMOUNT;
import static com.prowidesoftware.swift.model.field.Field61.IDENTIFICATION_CODE;
import static com.prowidesoftware.swift.model.field.Field61.REFERENCE_FOR_THE_ACCOUNT_OWNER;
import static com.prowidesoftware.swift.model.field.Field61.TRANSACTION_TYPE;

import com.prowidesoftware.swift.model.SwiftBlock4;
import com.prowidesoftware.swift.model.SwiftMessage;
import com.prowidesoftware.swift.model.field.Field61;
import com.prowidesoftware.swift.model.mt.mt9xx.MT940;
import java.io.File;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

@Slf4j
public class Mt940Parser {

  private final File toParse;

  public Mt940Parser(final File toParse) {
    this.toParse = toParse;
  }

  public int parse() {
    final SwiftMessage swiftMessage;
    try {
      swiftMessage = MT940.parse(toParse).getSwiftMessage();
    } catch (final IOException ioe) {
      log.error(ioe.getMessage(), ioe);
      return 0;
    }

    var numProcessed = process(swiftMessage.getBlock4());
    for (var i = 0; i < swiftMessage.getUnparsedTextsSize(); ++i) {
      numProcessed += process(swiftMessage.getUnparsedTexts().getTextAsMessage(i).getBlock4());
    }

    return numProcessed;
  }

  private int process(final SwiftBlock4 block4) {
    val loop = block4.getSubBlock(
        block4.getTagByNumber(60), block4.getTagByNumber(62));
    var numProcessed = 0;
    for (var i = 0; i < loop.size(); ++i) {
      val tag = loop.getTag(i);
      if (tag.getNameAsInt() == 61) {
        val field = (Field61) tag.asField();
        log.debug("-----");
        log.debug("amount = %s".formatted(field.getComponent(AMOUNT)));
        log.debug("transaction type = %s".formatted(field.getComponent(TRANSACTION_TYPE)));
        log.debug("ID code = %s".formatted(field.getComponent(IDENTIFICATION_CODE)));
        log.debug("from = %s".formatted(field.getComponent(REFERENCE_FOR_THE_ACCOUNT_OWNER)));
        if (i + 1 < loop.size() && loop.getTag(i + 1).getNameAsInt() == 86) {
          log.debug("description = %s".formatted(loop.getTag(i + 1).getValue()));
        }
        numProcessed++;
      }
    }
    return numProcessed;
  }

}
