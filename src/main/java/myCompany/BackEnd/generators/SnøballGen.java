package myCompany.BackEnd.generators;

import java.io.Serializable;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

public class SnøballGen implements IdentifierGenerator {

  private SnøBallArbeider worker = new SnøBallArbeider();

  public Serializable generate(SharedSessionContractImplementor session, Object o) throws HibernateException {
    return worker.nextId();
  }

}