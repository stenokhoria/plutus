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
    assertEquals(109, PLUTUS_LOG.getLogs().size());
    assertEquals("Processed 18 transactions", PLUTUS_LOG.getInfoLogs().get(0));
  }

}
