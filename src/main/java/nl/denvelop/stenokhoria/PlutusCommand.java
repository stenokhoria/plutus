package nl.denvelop.stenokhoria;

import static com.prowidesoftware.swift.model.field.Field61.AMOUNT;
import static com.prowidesoftware.swift.model.field.Field61.IDENTIFICATION_CODE;
import static com.prowidesoftware.swift.model.field.Field61.REFERENCE_FOR_THE_ACCOUNT_OWNER;
import static com.prowidesoftware.swift.model.field.Field61.TRANSACTION_TYPE;
import static org.eclipse.microprofile.config.ConfigProvider.getConfig;

import com.prowidesoftware.swift.model.SwiftBlock4;
import com.prowidesoftware.swift.model.SwiftMessage;
import com.prowidesoftware.swift.model.field.Field61;
import com.prowidesoftware.swift.model.mt.mt9xx.MT940;
import java.io.File;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import picocli.CommandLine.Command;
import picocli.CommandLine.IDefaultValueProvider;
import picocli.CommandLine.Model.ArgSpec;
import picocli.CommandLine.Parameters;

@Slf4j
@Command(name = "plutus", mixinStandardHelpOptions = true,
    defaultValueProvider = PlutusCommand.DefaultValueProvider.class)
public class PlutusCommand implements Runnable {

    @Parameters(paramLabel = "<file>", description = "Path to input MT940 file to parse.")
    private String path;

    @Override
    public void run() {
        val inputFile = new File(path);
        if (!inputFile.exists() || !inputFile.canRead()) {
            log.error("Cannot open '%s' for reading, does it exist?".formatted(path));
            return;
        }

        final SwiftMessage swiftMessage;
        try {
            swiftMessage = MT940.parse(inputFile).getSwiftMessage();
        } catch (final IOException ioe) {
            log.error(ioe.getMessage(), ioe);
            return;
        }

        var numProcessed = process(swiftMessage.getBlock4());
        for (var i = 0; i < swiftMessage.getUnparsedTextsSize(); ++i) {
            numProcessed += process(swiftMessage.getUnparsedTexts().getTextAsMessage(i).getBlock4());
        }
        log.info("Processed %d transactions".formatted(numProcessed));
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
                    i++;
                }
                numProcessed++;
            }
        }
        return numProcessed;
    }

    protected static class DefaultValueProvider implements IDefaultValueProvider {

        @Override
        public String defaultValue(final ArgSpec argSpec) {
            if ("<file>".equals(argSpec.paramLabel())) {
                return getConfig().getValue("input.path", String.class);
            }
            return null;
        }

    }

}
