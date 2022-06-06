package nl.denvelop.stenokhoria;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.quarkus.test.junit.main.Launch;
import io.quarkus.test.junit.main.LaunchResult;
import io.quarkus.test.junit.main.QuarkusMainTest;
import nl.altindag.log.LogCaptor;
import org.junit.jupiter.api.Test;

@QuarkusMainTest
class PlutusCommandQuarkusTest {

  private static final LogCaptor PLUTUS_LOG = LogCaptor.forClass(PlutusCommand.class);

  @Test
  @Launch({})
  void testRunWithoutArguments(final LaunchResult launchResult) {
    assertEquals(0, launchResult.exitCode());
    assertEquals(1, PLUTUS_LOG.getLogs().size());
    assertEquals(
        "Cannot open './src/test/resources/input.940' for reading, does it exist?",
        PLUTUS_LOG.getErrorLogs().get(0)
    );
  }

}
