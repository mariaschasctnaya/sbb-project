package com.tsystems.timetable.pusher;

import org.omnifaces.cdi.Push;
import org.omnifaces.cdi.PushContext;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
// Annotation serves to be able to address to bean in EL by name.
@Named
//Application scope - is valid for the entire operation of the application.
@ApplicationScoped
public class PushBean implements Serializable {
    @Inject
    @Push(channel = "clock")
    private PushContext push;

    public void clockAction(){
        push.send("Update in database");
    }
}
