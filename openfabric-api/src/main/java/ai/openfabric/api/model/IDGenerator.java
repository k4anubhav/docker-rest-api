package ai.openfabric.api.model;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;

import java.io.Serializable;

public class IDGenerator extends org.hibernate.id.UUIDGenerator {
    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object obj) throws HibernateException {
        if (obj == null) {
            throw new HibernateException(new NullPointerException());
        }
        return super.generate(session, obj);
    }
}
