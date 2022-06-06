package nl.denvelop.stenokhoria;

import static org.eclipse.microprofile.config.ConfigProvider.getConfig;

import jakarta.xml.bind.JAXBException;
import java.io.File;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import nl.denvelop.stenokhoria.parser.Camt053Parser;
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

        val parser = new Camt053Parser(inputFile);
        int numProcessed;
        try {
            numProcessed = parser.parse();
        } catch (final JAXBException e) {
            throw new RuntimeException(e);
        }

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
