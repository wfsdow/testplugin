package com.happy.buriedpoint;


public class EventSender
{
    private EventStatistics statistics;

    private EventStatistics.Event event = new EventStatistics.Event();


    public EventSender(EventStatistics statistics)
    {
        this.statistics = statistics;
        time(System.currentTimeMillis());
    }


    public EventStatistics statistics()
    {
        return statistics;
    }

    public EventSender uid(String uid)
    {
        event.addParameter("uid", uid);

        return this;
    }

    public String uid()
    {
        return event.parameter("uid", "");
    }

    public EventSender name(String type)
    {
        event.addParameter("logId", type);
        return this;
    }

    public EventSender time(long time)
    {
        event.addParameter("logDate", time);

        return this;
    }

    public long time()
    {
        return event.parameter("time", 0L);
    }

    public EventSender network(int type)
    {
        event.addParameter("networkType", type);

        return this;
    }

    public int network()
    {
        return event.parameter("networkType", 0);
    }

    public EventSender operator(int provider)
    {
        event.addParameter("provider", provider);

        return this;
    }

    public int operator()
    {
        return event.parameter("provider", 0);
    }

    public EventSender operator(String imsi)
    {
        event.addParameter("imsi", imsi);

        return this;
    }

    public String getType()
    {
        return event.parameter("logId", "");
    }

    public EventSender f(int index, String value)
    {
        event.addParameter("f" + index, value);
        return this;
    }

    public int appId()
    {
        return event.parameter("appId", 0);
    }

    public EventSender appId(int appId)
    {
        event.addParameter("appId", appId);
        return this;
    }

    public EventSender channel(String channel)
    {
        event.addParameter("appChannel", channel);

        return this;
    }

    public String channel()
    {
        return event.parameter("appChannel", "");
    }

    public String sdkVersion()
    {
        return event.parameter("sdkVersion", "");
    }

    public EventSender sdkVersion(String sdkVersion)
    {
        event.addParameter("sdkVersion", sdkVersion);
        return this;
    }

    public String business()
    {
        return event.parameter("busiId", "");
    }

    public EventSender business(String bizType)
    {
        event.addParameter("busiId", bizType);
        return this;
    }

    public void send()
    {
        statistics().putEvent(event);
    }
}
