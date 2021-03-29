package com.jeeplus.modules.flowable.common.factory;


import com.jeeplus.modules.flowable.common.handler.ExtUserTaskActivityBehavior;
import org.flowable.bpmn.model.UserTask;
import org.flowable.engine.impl.bpmn.parser.factory.DefaultActivityBehaviorFactory;

public class MyActivityBehaviorFactory extends DefaultActivityBehaviorFactory {
    @Override
    public ExtUserTaskActivityBehavior createUserTaskActivityBehavior(UserTask userTask) {
        return new ExtUserTaskActivityBehavior(userTask);
    }
}
