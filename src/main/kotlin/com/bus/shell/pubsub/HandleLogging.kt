package com.bus.shell.pubsub

import com.bus.core.DomainError
import com.bus.core.DomainEvent
import com.bus.core.SideEffects
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.lang.invoke.MethodHandles

class HandleLogging(
    private val logger: Logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass()),
) : SideEffects.HandleEvent, SideEffects.HandleError {

    override fun invoke(domainEvent: DomainEvent) {
        logger.info("domain-event: '${domainEvent::class.simpleName}'")
    }

    override fun invoke(error: DomainError) {
        logger.error("domain-error: '${error::class.simpleName}'")
    }
}
