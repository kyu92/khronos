package com.k92.khronos.config

import com.k92.khronos.standard.Logger
import com.k92.khronos.standard.Logger.Companion.log
import com.k92.khronos.task.ClockTask
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.SchedulingConfigurer
import org.springframework.scheduling.config.ScheduledTaskRegistrar

@Configuration
@Logger
class ScheduleConfig(val khronosConfig: KhronosConfig, val clockTask: ClockTask): SchedulingConfigurer {

    override fun configureTasks(taskRegistrar: ScheduledTaskRegistrar) {
        log.info("初始化上午上班打卡定时任务完成, cron表达式: {}", khronosConfig.scheduled.morningStartCron)
        taskRegistrar.addCronTask({ clockTask.runMorningStartClock() }, khronosConfig.scheduled.morningStartCron)
        log.info("初始化上午下班打卡定时任务完成, cron表达式: {}", khronosConfig.scheduled.morningEndCron)
        taskRegistrar.addCronTask({ clockTask.runMorningEndClock() }, khronosConfig.scheduled.morningEndCron)
        log.info("初始化下午上班打卡定时任务完成, cron表达式: {}", khronosConfig.scheduled.afternoonStartCron)
        taskRegistrar.addCronTask({ clockTask.runAfternoonStartClock() }, khronosConfig.scheduled.afternoonStartCron)
        log.info("初始化下午下班打卡定时任务完成, cron表达式: {}", khronosConfig.scheduled.afternoonEndCron)
        taskRegistrar.addCronTask({ clockTask.runAfternoonEndClock() }, khronosConfig.scheduled.afternoonEndCron)
        log.info("初始化晚加班打卡定时任务完成, cron表达式: {}", khronosConfig.scheduled.nightAddCron)
        taskRegistrar.addCronTask({ clockTask.runNightAddClock() }, khronosConfig.scheduled. nightAddCron)
    }
}