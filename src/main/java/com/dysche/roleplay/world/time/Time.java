package com.dysche.roleplay.world.time;

import org.cantaloupe.Cantaloupe;
import org.cantaloupe.service.services.ScheduleService;
import org.cantaloupe.world.World;

public class Time {
    public static void startTask(World world) {
        Cantaloupe.getServiceManager().provide(ScheduleService.class).repeat("timeTask:" + world.getName(), new Runnable() {
            private int ticksPassed = 0;

            @Override
            public void run() {
                if (this.ticksPassed % 3 == 0) {
                    world.toHandle().setTime(world.toHandle().getTime() + 1);
                }

                this.ticksPassed++;
            }
        }, 0L);
    }

    public static void stopTask(World world) {
        Cantaloupe.getServiceManager().provide(ScheduleService.class).cancel("timeTask:" + world.getName());
    }

    public static long getTime(World world) {
        return world.toHandle().getFullTime();
    }

    public static long getSeconds(World world) {
        return getTime(world);
    }

    public static long getMinutes(World world) {
        return getSeconds(world) / 60L;
    }

    public static long getHours(World world) {
        return getMinutes(world) / 60L;
    }

    public static long getDays(World world) {
        return getHours(world) / 24L;
    }

    public static long getWeeks(World world) {
        return getDays(world) / 7L;
    }

    public static long getMonths(World world) {
        return (long) (getDays(world) * 0.0328542094D);
    }

    public static long getYears(World world) {
        return (long) (getDays(world) / 356.25D);
    }
    
    public static int getYear(World world) {
        return (int) (2017 + getYears(world));
    }
    
    public static int getSecondOfMinute(World world) {
        return (int) (getSeconds(world) % 60);
    }
    
    public static int getSecondOfHour(World world) {
        return (int) (getSeconds(world) % 3600);
    }
    
    public static int getMinuteOfHour(World world) {
        return (int) (getMinutes(world) % 60);
    }
    
    public static int getHourOfDay(World world) {
        return (int) (getHours(world) % 24);
    }

    public static int getDayOfYear(World world) {
        return (int) (getDays(world) % 365.25D);
    }

    public static int getDayOfMonth(World world) {
        return (int) (getDays(world) % 30.4368499D);
    }

    public static int getDayOfWeek(World world) {
        return (int) (getDays(world) % 7);
    }

    public static int getWeekOfYear(World world) {
        return (int) (getWeeks(world) % 52);
    }

    public static int getWeekOfMonth(World world) {
        return (int) (getWeeks(world) % 4.34812141D);
    }
    
    public static int getMonthOfYear(World world) {
        return (int) (getMonths(world) % 12);
    }
}