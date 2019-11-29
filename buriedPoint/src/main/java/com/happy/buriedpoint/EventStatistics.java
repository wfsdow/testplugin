package com.happy.buriedpoint;


import android.content.Context;
import android.content.ContextWrapper;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import com.happy.buriedpoint.tool.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

//import shark.lib.utils.LocalBuryingPointUtil;

//数据埋点
public abstract class EventStatistics
{
    public static final int Successed = 0;
    public static final int Failed = 1;
    public static final int Rejected = 2;


    public static Builder builder()
    {
        return new Builder();
    }

    //region Event
    public static class Event implements Serializable
    {
        private static final long serialVersionUID = 1L;

        private Map<String, Object> params;

        private String uuid;
        private int count = 0;

        public Map<String, Object> getParams()
        {
            return params;
        }

        public Event()
        {
            params = new HashMap<>();
            uuid = UUID.randomUUID().toString();
        }

        public String id()
        {
            return uuid;
        }

        @Override
        public boolean equals(Object o)
        {
            if (o == null || !(o instanceof Event))
                return false;

            Event info = (Event) o;

            return getKey().equals(info.getKey());
        }

        public int hashCode()
        {
            return getKey().hashCode();
        }

        private String getKey()
        {
            StringBuffer buffer = new StringBuffer();
            buffer.append(id());
            buffer.append(getParams().toString());

            return buffer.toString();
        }

        public void to(JSONObject json) throws JSONException
        {
            for (String key : params.keySet())
            {
                json.put(key, params.get(key));
            }

            json.put("eid", id());
            json.put("count", count);
        }

        public void from(final JSONObject json)
        {
            for (String key : new Iterable<String>()
            {
                @Override
                public Iterator<String> iterator()
                {
                    return json.keys();
                }
            })
            {
                params.put(key, json.opt(key));
            }

            uuid = json.optString("eid");
            count = json.optInt("count");
        }

        @Override
        public String toString()
        {
            return "[event:" + getKey() + "]";
        }

        public final void addParameter(String name, Object value)
        {
            getParams().put(name, value);
        }

        public <T> T parameter(String name, T def)
        {
            if (getParams().containsKey(name))
                return (T) getParams().get(name);

            return def;
        }

        public void retry()
        {
            count++;
        }

        public int getRetryCount()
        {
            return count;
        }
    }

    public static abstract class EventLocalStore
    {
        public abstract void add(Event event);

        public abstract boolean delete(Iterable<Event> list);

        public abstract boolean update(Iterable<Event> list);

        public abstract Iterable<Event> getEventList();

        public abstract void attach(EventStatistics statistics);
    }

    public static class DatabaseEventLocalStore extends EventLocalStore
    {
        private Context context;
        private SQLiteOpenHelper helper;
        private EventAdapter adapter;

        public DatabaseEventLocalStore(final Context context, final File dir)
        {
            this.context = context;

            helper = new SQLiteOpenHelper(new ContextWrapper(context)
            {
                @Override
                public File getDatabasePath(String name)
                {
                    if (dir != null)
                        return new File(dir, name);
                    else
                        return super.getDatabasePath(name);
                }
            }, "event_store", null, 1)
            {
                @Override
                public void onCreate(SQLiteDatabase db)
                {

                }

                @Override
                public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
                {

                }
            };

            if (!existTable("events"))
                helper.getWritableDatabase().execSQL("CREATE TABLE events (id text PRIMARY KEY,event text)");
        }

        private boolean existTable(String name)
        {
            String sql = String.format("select count(*) as c from sqlite_master where type ='table' and name ='%s'", name);
            Cursor cursor = helper.getWritableDatabase().rawQuery(sql, null);
            if (cursor.moveToNext())
            {
                int count = cursor.getInt(0);
                if (count > 0)
                {
                    return true;
                }
            }

            return false;
        }

        private synchronized boolean insert(String id, String event)
        {
//            LocalBuryingPointUtil.saveDBOperation(context,
//                    LocalBuryingPointUtil.appendTime(Thread.currentThread().getName() + " insert, open db"));
            final SQLiteDatabase writableDatabase = helper.getWritableDatabase();
            final SQLiteStatement sqLiteStatement = writableDatabase.compileStatement("insert into events(id,event) values(?,?)");

            sqLiteStatement.bindString(1, id);
            sqLiteStatement.bindString(2, event);

            final boolean res = sqLiteStatement.executeInsert() > 0;
            writableDatabase.close();
//            LocalBuryingPointUtil.saveDBOperation(context,
//                    LocalBuryingPointUtil.appendTime(Thread.currentThread().getName() +" insert, close db"));
            return res;
        }

        private synchronized boolean update(String id, String event)
        {
//            LocalBuryingPointUtil.saveDBOperation(context,
//                    LocalBuryingPointUtil.appendTime(Thread.currentThread().getName() + " update, open db"));
            final SQLiteDatabase writableDatabase = helper.getWritableDatabase();
            final SQLiteStatement sqLiteStatement = writableDatabase.compileStatement("update events set event=? where id=?");

            sqLiteStatement.bindString(2, id);
            sqLiteStatement.bindString(1, event);

            final boolean res = sqLiteStatement.executeInsert() > 0;
            writableDatabase.close();
//            LocalBuryingPointUtil.saveDBOperation(context,
//                    LocalBuryingPointUtil.appendTime(Thread.currentThread().getName() + " update, close db"));
            return res;
        }

        private synchronized void delete(String... ids)
        {
            StringBuilder builder = new StringBuilder();

            for (int i = 0; i < ids.length; i++)
            {
                builder.append("'");
                builder.append(ids[i]);
                builder.append("'");

                if (i != ids.length - 1)
                    builder.append(",");
            }
//            LocalBuryingPointUtil.saveDBOperation(context,
//                    LocalBuryingPointUtil.appendTime(Thread.currentThread().getName() + " delete, open db"));
            final SQLiteDatabase writableDatabase = helper.getWritableDatabase();
            writableDatabase.execSQL("delete from events where id in(" + builder.toString() + ")");
            writableDatabase.close();
//            LocalBuryingPointUtil.saveDBOperation(context,
//                    LocalBuryingPointUtil.appendTime(Thread.currentThread().getName() + " delete, close db"));
        }

        @Override
        public boolean update(Iterable<Event> list)
        {
            for (Event event : list)
            {
                JSONObject json = new JSONObject();
                try
                {
                    event.to(json);
                    update(event.id(), json.toString());
                }
                catch (JSONException e)
                {
                    adapter.handle(e);
                }
            }

            return false;
        }


        private synchronized List<String> queryAll()
        {
//            LocalBuryingPointUtil.saveDBOperation(context,
//                    LocalBuryingPointUtil.appendTime(Thread.currentThread().getName() + " query, open db"));
            final SQLiteDatabase readableDatabase = helper.getReadableDatabase();
            final Cursor cursor = readableDatabase.query("events", new String[]{"id", "event"}, null, null, null, null, null);

            List<String> events = new ArrayList<>();
            while (cursor.moveToNext())
            {
                final String event = cursor.getString(cursor.getColumnIndex("event"));

                events.add(event);
            }

            readableDatabase.close();
//            LocalBuryingPointUtil.saveDBOperation(context,
//                    LocalBuryingPointUtil.appendTime(Thread.currentThread().getName() + " query, close db"));
            return events;
        }

        @Override
        public void add(Event event)
        {
            JSONObject json = new JSONObject();

            try
            {
                event.to(json);
                insert(event.id(), json.toString());
                Logger.debug().i("插入事件： " + json.toString());
                //将埋点信息保存在本地文件中
//                LocalBuryingPointUtil.localSave(context,
//                        LocalBuryingPointUtil.setSaveType(json.toString()));
            }
            catch (JSONException e)
            {
                adapter.handle(e);
            }
        }

        @Override
        public boolean delete(Iterable<Event> list)
        {
            List<String> ids = new ArrayList<>();
            for (Event event : list)
            {
                ids.add(event.id());
            }

            delete(ids.toArray(new String[0]));

            return true;
        }

        @Override
        public Iterable<Event> getEventList()
        {
            final List<String> events = queryAll();
            List<Event> eventList = new ArrayList<>();


            for (String e : events)
            {
                final Event event = new Event();
                try
                {
                    event.from(new JSONObject(e));

                    eventList.add(event);
                }
                catch (JSONException e1)
                {
                    adapter.handle(e1);
                }
            }

            return eventList;
        }

        @Override
        public void attach(EventStatistics statistics)
        {
            this.adapter = statistics.getAdapter();
        }
    }
    //endregion

    //region Strategy
    public static abstract class DataEventStrategy
    {
        public abstract void attach(EventStatistics event);

        public abstract void dettach(EventStatistics event);
    }

    public static class TimerDataEventStrategy extends DataEventStrategy
    {
        private Timer timer;

        private EventStatistics dataEvent;

        private long interval;
        private long start;

        public TimerDataEventStrategy(long start, long interval)
        {
            this.start = start;
            this.interval = interval;
        }

        @Override
        public void attach(final EventStatistics event)
        {
            timer = new Timer();
            dataEvent = event;
            timer.schedule(new TimerTask()
            {
                @Override
                public void run()
                {
                    if (dataEvent.sendEvent(0))
                    {

                    }
                }
            }, start, interval);
        }

        @Override
        public void dettach(EventStatistics event)
        {
            if (timer != null)
                timer.cancel();
        }
    }
    //endregion

    public static abstract class LoopEventStatistics extends EventStatistics implements EventAdapter
    {
        private Executor looper = Executors.newFixedThreadPool(1);
        private EventAdapter adapter;

        public LoopEventStatistics(EventAdapter adapter)
        {
            this.adapter = adapter;
        }

        public abstract EventLocalStore getLocalList();

        @Override
        public int getRetryCount()
        {
            return adapter.getRetryCount();
        }

        @Override
        public boolean sendEvent(int floor)
        {
            final Iterable<Event> list = getLocalList().getEventList();

            beforeUpload(list);

            List<Event> uploadList = new ArrayList<>();
            List<Event> deleteList = new ArrayList<>();
            int count = 0;
            for (Event event : list)
            {
                if (event.getRetryCount() <= adapter.getRetryCount())
                {
                    uploadList.add(event);
                    count++;
                }
                else
                    deleteList.add(event);
            }

            if (count < floor)
            {
                afterUpload(uploadList, Rejected);
                return false;
            }

            boolean res;

            if (upload(uploadList))
            {
                uploadSuccess(list, uploadList);
                res = true;
            }
            else
            {
                uploadFail(uploadList);
                res = false;
            }

            if (deleteList.size() > 0)
                getLocalList().delete(deleteList);

            return res;
        }

        private void uploadFail(Iterable<Event> list)
        {
            if (list.iterator().hasNext())
            {
                for (Event event : list)
                {
                    event.retry();
                }

                getLocalList().update(list);
            }

            afterUpload(list, Failed);
        }

        private void uploadSuccess(Iterable<Event> list, List<Event> uploadList)
        {
            int c = 0;
            StringBuffer buffer = new StringBuffer();

            c = buildInfo(list, c, buffer);

            if (c > 0)
            {
                log(buffer.toString());

                if (getLocalList().delete(uploadList))
                    log("delete upload data events");
            }

            afterUpload(list, Successed);
        }

        private int buildInfo(Iterable<Event> list, int c, StringBuffer buffer)
        {
            buffer.append("dataevent upload suc [");

            for (Event info : list)
            {
                buffer.append(info.toString());
                buffer.append("\n");
                buffer.append(" ");
                c++;
            }

            buffer.append("]");
            buffer.append("{" + c + "}");
            return c;
        }

        @Override
        public boolean putEvent(final Event event)
        {
            looper.execute(new Runnable()
            {
                @Override
                public void run()
                {
                    getLocalList().add(event);
                }
            });

            return true;
        }
    }

    public interface EventAdapter
    {
        boolean upload(Iterable<Event> list);

        void log(String msg);

        void handle(Throwable e);

        int getRetryCount();
    }

    public interface EventListener
    {
        void beforeUpload(Iterable<Event> list);

        void afterUpload(Iterable<Event> list, int state);
    }

    public static class Builder
    {
        private DataEventStrategy _strategy;
        private EventLocalStore _localList;

        public Builder strategy(DataEventStrategy strategy)
        {
            _strategy = strategy;
            return this;
        }

        public Builder strategy(long start, long span)
        {
            return strategy(new TimerDataEventStrategy(start, span));
        }

        public Builder database(Context context, File path)
        {
            return localList(new DatabaseEventLocalStore(context, path));
        }

        public Builder localList(EventLocalStore list)
        {
            _localList = list;

            return this;
        }

        /***
         * build DataEvent
         *
         * localList is must
         * uploader is must
         * strategy is must
         * @return
         */
        public EventStatistics build( final EventAdapter adapter)
        {
            EventStatistics dataEvent = new LoopEventStatistics(adapter)
            {
                @Override
                public EventLocalStore getLocalList()
                {
                    return _localList;
                }

                @Override
                public boolean upload(Iterable<Event> list)
                {
                    return adapter.upload(list);
                }

                @Override
                public void log(String msg)
                {
                    adapter.log(msg);
                }

                @Override
                public void handle(Throwable e)
                {
                    adapter.handle(e);
                }

                @Override
                public EventAdapter getAdapter()
                {
                    return adapter;
                }
            };

            _localList.attach(dataEvent);

            dataEvent.strategy(_strategy);

            return dataEvent;
        }
    }

    private DataEventStrategy strategy;
    private List<EventListener> listeners = new ArrayList<EventListener>();

    void beforeUpload(Iterable<Event> list)
    {
        for (EventListener listener : listeners)
            listener.beforeUpload(list);
    }

    void afterUpload(Iterable<Event> list, int state)
    {
        for (EventListener listener : listeners)
            listener.afterUpload(list, state);
    }

    void strategy(DataEventStrategy strategy)
    {
        if (this.strategy != null)
            this.strategy.dettach(this);

        this.strategy = strategy;

        this.strategy.attach(this);
    }

    public void addListener(EventListener listener)
    {
        listeners.add(listener);
    }

    public void removeListener(EventListener listener)
    {
        listeners.remove(listener);
    }

    public abstract EventAdapter getAdapter();

    public abstract boolean putEvent(Event event);

    /***
     * 发送埋点
     * @param floor 埋点数量的下限,只有大于等于这个数量才会发送
     * @return
     */
    public abstract boolean sendEvent(int floor);
}
