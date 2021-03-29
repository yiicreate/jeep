package com.jeeplus.modules.monitor.entity;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.jeeplus.common.websocket.service.system.SystemInfoSocketHandler;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.annotation.Bean;


/**
 * 定时任务工作类
 *
 * @author ty
 * @date 2015年1月13日
 */
@DisallowConcurrentExecution
public abstract class Task implements Job {

    /**
     * 系统通知bean
     *
     * @return
     */
    @Bean
    public SystemInfoSocketHandler systemInfoSocketHandler() {
        return new SystemInfoSocketHandler();
    }

    public void execute(JobExecutionContext context) throws JobExecutionException {
        Object scheduleJob = context.getMergedJobDataMap().get("scheduleJob");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
        run();
        try {
            String isInfo = scheduleJob.getClass().getMethod("getIsInfo").invoke(scheduleJob).toString();
            if (isInfo.equals("1")) {
                systemInfoSocketHandler().sendMessageToUser("admin", "任务名称 = [" + scheduleJob.toString() + "]" + " 在 " + dateFormat.format(new Date()) + " 时运行");
            } else if (isInfo.equals("2")) {

                systemInfoSocketHandler().sendMessageToAllUsers("任务名称 = [" + scheduleJob.toString() + "]" + " 在 " + dateFormat.format(new Date()) + " 时运行");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("任务名称 = [" + scheduleJob.toString() + "]" + " 在 " + dateFormat.format(new Date()) + " 时运行");
    }

    public abstract void run();
}
