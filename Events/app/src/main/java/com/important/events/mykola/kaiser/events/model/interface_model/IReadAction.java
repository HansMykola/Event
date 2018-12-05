package com.important.events.mykola.kaiser.events.model.interface_model;

import com.important.events.mykola.kaiser.events.model.Event;

public interface IReadAction
{
    void startReadOrUpdate(Event event, int index, boolean can);
}
