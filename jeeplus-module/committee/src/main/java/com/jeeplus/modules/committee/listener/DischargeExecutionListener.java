package com.jeeplus.modules.committee.listener;

import com.jeeplus.common.utils.SpringContextHolder;
import com.jeeplus.modules.committee.service.CommitteeIntegralService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class DischargeExecutionListener implements ExecutionListener {

    public void notify(DelegateExecution delegateExecution) {
        if("履职审核".equals(delegateExecution.getCurrentFlowElement().getName()) &&
                "end".equals(delegateExecution.getEventName())){
            Map vars = delegateExecution.getVariables();
            if(vars.containsKey("pass") && (Boolean) vars.get("pass")){
                String committee = vars.get("committee").toString();
                Integer dischargeType = Integer.valueOf(vars.get("discharge_type").toString());
                ((CommitteeIntegralService)SpringContextHolder.getBean("committeeIntegralService"))
                        .saveDetail(committee,dischargeType,1);
            }
        }
    }
}
