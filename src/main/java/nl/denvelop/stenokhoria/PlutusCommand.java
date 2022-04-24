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
import nl.denvelop.stenokhoria.parser.Mt940Parser;
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

        val parser = new Mt940Parser(inputFile);
        val numProcessed = parser.parse();

        log.info("Processed %d transactions".formatted(numProcessed));
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
