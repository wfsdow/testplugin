package com.happy.buriedpoint;

import android.content.Context;

import com.happy.buriedpoint.tool.Logger;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

//import shark.lib.utils.LocalBuryingPointUtil;

public abstract class EventProvider
{
    private EventStatistics statistics;
    private EventInjector injector;

    public EventProvider(EventStatistics statistics)
    {
        this.statistics = statistics;
    }

    public EventStatistics getStatistics()
    {
        return statistics;
    }

    public void setInjector(EventInjector injector)
    {
        this.injector = injector;
    }

    abstract EventSender newInstance();

    public EventSender sender()
    {
        final EventSender eventSender = newInstance();

        if (injector != null)
            injector.inject(eventSender);

        return eventSender;
    }


    public static EventProvider provider(Context context, File dir, EventStatistics.EventAdapter adapter)
    {
        context = context.getApplicationContext();
        final EventStatistics statistics = EventStatistics.builder()
                .database(context, dir)
                .strategy(15 * 1000, 60 * 1000)
                .build(adapter);

        return new EventProvider(statistics)
        {
            @Override
            EventSender newInstance()
            {
                return new EventSender(statistics);
            }
        };
    }

    public interface EventInjector
    {
        void inject(EventSender sender);
    }

    public static abstract class NetEventAdapter implements EventStatistics.EventAdapter
    {
        private static SSLContext sslContext;
        private String uri;
        private Context context;

        public NetEventAdapter(String uri, Context context)
        {
            this.uri = uri;
            this.context = context;
        }

        @Override
        public boolean upload(Iterable<EventStatistics.Event> list)
        {
            return upload(uri, list);
        }

        private boolean upload(String uri, Iterable<EventStatistics.Event> eventList)
        {
            try
            {
                JSONArray ja = new JSONArray();

                StringBuilder builder = new StringBuilder();
                boolean hasEvent = false;
                for (EventStatistics.Event event : eventList)
                {
                    hasEvent = true;
                    JSONObject json = new JSONObject();
                    event.to(json);
                    ja.put(json);

                    builder.append(json + "\n");
                }

                if (!hasEvent)
                    return false;

                log("prepare send events " + builder + "");
                Logger.debug().i("发送事件：" + builder);
                if (ja.length() <= 0)
                {
                    return false;
                }

                return upload(uri, ja);
            }
            catch (Exception e)
            {
                handle(e);

                return false;
            }
        }

        private HttpURLConnection buildConnection(URL url) throws IOException, NoSuchAlgorithmException, KeyManagementException
        {
            if (url.getProtocol().equals("http"))
            {
                return (HttpURLConnection) url.openConnection();
            }
            else
            {
                HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
                httpsURLConnection.setHostnameVerifier((s, sslSession) -> true);

                if (sslContext == null)
                {
                    sslContext = SSLContext.getInstance("TLS");
                    sslContext.init(null, new TrustManager[]{new SdkTrustManager()}, new SecureRandom());
                }

                SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
                httpsURLConnection.setSSLSocketFactory(sslSocketFactory);
                return httpsURLConnection;
            }
        }

        private class SdkTrustManager implements X509TrustManager
        {
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType)
                    throws CertificateException
            {

            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType)
                    throws CertificateException
            {

            }

            @Override
            public X509Certificate[] getAcceptedIssuers()
            {
                return new X509Certificate[0];
            }
        }

        private boolean upload(String uri, JSONArray ja) throws IOException, NoSuchAlgorithmException, KeyManagementException
        {
            URL url = new URL(uri);
            HttpURLConnection connection = buildConnection(url);

            connection.setDoInput(true);
            connection.setConnectTimeout(15 * 1000);
            connection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            connection.getOutputStream().write(ja.toString().getBytes());
            connection.getOutputStream().flush();
            connection.connect();
            int code = connection.getResponseCode();

            if (code == 200)
            {
                connection.disconnect();
//                LocalBuryingPointUtil.localSave(context,
//                        LocalBuryingPointUtil.setSendType(ja.toString()));
                return true;
            }
            else
            {
                connection.disconnect();
                log("send event to server error " + code);
            }

            return false;
        }
    }
}
