package nl.denvelop.stenokhoria.parser;

import com.softicar.camt053.Document;
import com.softicar.camt053.ObjectFactory;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import java.io.File;
import javax.xml.transform.stream.StreamSource;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

@Slf4j
public class Camt053Parser {

  private final File toParse;

  public Camt053Parser(final File toParse) {
    this.toParse = toParse;
  }

  public int parse() throws JAXBException {
    val cam053Context = JAXBContext.newInstance(ObjectFactory.class);
    val cam053Unmarshaller = cam053Context.createUnmarshaller();
    val root = cam053Unmarshaller.unmarshal(toParse);
    return 0;
  }

}
