package com.bus.shell.pubsub

import com.bus.core.DomainError
import com.bus.core.DomainEvent
import com.bus.core.SideEffects

class InMemoryPublisher(
    private val eventHandlers: List<SideEffects.HandleEvent>,
    private val errorHandlers: List<SideEffects.HandleError>
    ) : SideEffects.PublishEvent, SideEffects.PublishError {

    override operator fun invoke(domainEvent: DomainEvent) = eventHandlers.forEach { handle -> handle(domainEvent) }

    override fun invoke(error: DomainError) = errorHandlers.forEach { handle -> handle(error) }
}
