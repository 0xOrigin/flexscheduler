package io.github._0xorigin.flexscheduler.base.generators.uuid;

import com.fasterxml.uuid.Generators;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.generator.BeforeExecutionGenerator;
import org.hibernate.generator.EventType;

import java.util.EnumSet;

public class UuidV7Generator implements BeforeExecutionGenerator {
    public static final long serialVersionUID = 1L;

    @Override
    public Object generate(SharedSessionContractImplementor sharedSessionContractImplementor, Object owner, Object currentValue, EventType eventType) {
        return Generators.timeBasedEpochGenerator().generate();
    }

    @Override
    public EnumSet<EventType> getEventTypes() {
        return EnumSet.of(EventType.INSERT);
    }
}
