package com.team.common.http;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.apache.http.ConnectionReuseStrategy;
import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.RequestExpectContinue;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.DefaultBHttpClientConnection;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHttpEntityEnclosingRequest;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpCoreContext;
import org.apache.http.protocol.HttpProcessor;
import org.apache.http.protocol.HttpProcessorBuilder;
import org.apache.http.protocol.HttpRequestExecutor;
import org.apache.http.protocol.RequestConnControl;
import org.apache.http.protocol.RequestContent;
import org.apache.http.protocol.RequestTargetHost;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The class HeaderManager.
 * 
 * @author
 */
public class HttpClientManager { 

    /**
     * 
     */
    private static Logger logger = LoggerFactory.getLogger(HttpClientManager.class.getName());
    private CloseableHttpClient client;


    private static HttpClientManager instance = null;

    /**
     * 
     */
    private HttpClientManager() {
        // 初始化http连接池，设置参数、http头等等信息
        PoolingHttpClientConnectionManager httpClientConnectionManager = new PoolingHttpClientConnectionManager();
        // MultiThreadedHttpConnectionManager connectionManager = new
        // MultiThreadedHttpConnectionManager();
        // connectionManager.getParams().setMaxTotalConnections(MAX_TOTAL_CONNECT);
        httpClientConnectionManager.setMaxTotal(HttpConstans.MAX_TOTAL_CONNECTIONS); // 设置连接池线程最大数量
        httpClientConnectionManager.setDefaultMaxPerRoute(HttpConstans.MAX_ROUTE_TOTAL); // 设置单个路由最大的连接线程数量
       /* SocketConfig socketConfig = SocketConfig.custom().setSoTimeout(6000).build();
        httpClientConnectionManager.setDefaultSocketConfig(socketConfig);*/
        // 创建http request的配置信息
        // 设置连接超时和等待超时
        RequestConfig requestConfig = null;
        
             requestConfig = RequestConfig.custom().setConnectionRequestTimeout(HttpConstans.CONNECTION_REQUEST_TIMEOUT).setSocketTimeout(
                    HttpConstans.REQUEST_SOCKET_TIME).setConnectTimeout(
                    HttpConstans.REQUEST_TIMEOUT).setExpectContinueEnabled(false).build();
       
        // http长连接策略：
        // 可以根据须要定制所须要的长连接策略，可根据服务器指定的超时时间，也可根据主机名自己指定超时时间
        ConnectionKeepAliveStrategy longPollingStrategy = new DefaultConnectionKeepAliveStrategy() {
            @Override
            public long getKeepAliveDuration(HttpResponse response, HttpContext context) {

                long keepAlive = super.getKeepAliveDuration(response, context);
                // 如果没有keep-alive这个参数
                if (keepAlive == -1) {
                    // 就自己设置它
                    keepAlive = HttpConstans.HTTP_TARGET_HOST_NAME_KEEP_LIVE_TIME;
                }
                logger.info("keepAlive is :" + keepAlive);
                return keepAlive;
            }
        };

        // add by wangxinyang 20141103
        // HttpProtocolParams.setUseExpectContinue(client.getParams(), false);
        // 增加重试的机制
        HttpRequestRetryHandler myRequestRetry = new HttpRequestRetryHandler() {

            @Override
            public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
                // retry a max of 5 times
                if (executionCount >= 5) {
                    return false;
                }
                if (exception instanceof NoHttpResponseException) {
                    return true;
                } else if (exception instanceof ClientProtocolException) {
                    return true;
                }

                return false;
            }
        };
        // 启动线程，根据配置的时间清空一次失效连接
        new IdleConnectionMonitorThread(httpClientConnectionManager).start();

        // client =
        // HttpClients.custom().setKeepAliveStrategy(longPollingStrategy).setDefaultRequestConfig(requestConfig)
        // .setConnectionManager(httpClientConnectionManager).build();
        client = HttpClients.custom().setConnectionManager(httpClientConnectionManager).setDefaultRequestConfig(
                requestConfig).setKeepAliveStrategy(longPollingStrategy).setRetryHandler(myRequestRetry).build();
    }

    /**
     * 获取消息发送客户端实例，目前为单例模式
     * 
     * @return MsgReportService
     */
    public static HttpClientManager getInstance() {
        if (instance == null) {
            syncInit();
        }
        return instance;
    }

    /**
     * 单例对象的初始化同步
     */
    private static synchronized void syncInit() {
        instance = new HttpClientManager();

    }

    /**
     * post方式发送http请求
     * 
     * @param msgreq
     *            消息内容HttpClientReq
     * @return HttpClientResp 接收到的响应内容
     */
    public HttpClientResp sendPostRequst(HttpClientReq msgreq) {

        HttpPost method = preparePost( msgreq);
        return excuteMethod(method);
    }
    public HttpClientResp sendGetRequst(HttpClientReq msgreq) {

    	HttpGet method = prepareGet(msgreq);
        return excuteMethod(method);
    }
    /**
     * 预处理http POST请求
     * @param msgreq 请求信息
     * @return HttpGet
     */
    private HttpPost preparePost( HttpClientReq msgreq) {
        logger.info( "step into prepare method time is ================== "
                + System.currentTimeMillis());
        // 服务端url
        String reqUrl = msgreq.getReqUrl();
        logger.info( "Req http url:" + reqUrl);
        HttpPost method = null;
        method = new HttpPost(reqUrl);
        // 设置body
        StringEntity entity;
        String reqbody = msgreq.getBodyMessage();
        logger.info( "Req http body:" + reqbody);
        if(reqbody!=null&&reqbody.trim().length()>0){
             entity = new StringEntity(reqbody, "UTF-8");
             entity.setContentType("text/xml");
             method.setEntity(entity);
        }
        // 设置header
        this.addRequestHead(method, msgreq);
        return method;

    }
    /**
     * 预处理http GET请求
     * @param msgreq 请求信息
     * @return HttpGet
     */
    private HttpGet prepareGet(HttpClientReq msgreq) {
        logger.info( "step into prepare method time is ================== "
                + System.currentTimeMillis());
        // 服务端url
        String reqUrl = msgreq.getReqUrl();
        logger.info( "Req http url:" + reqUrl);
        HttpGet method = null;
        StringBuffer paraBuffer = new StringBuffer();
        HashMap<String,String> paraMap = msgreq.getParaMap();
        if(paraMap!=null&&!paraMap.isEmpty()){
        	 Set<String> paraName = paraMap.keySet();
             for (Iterator<String> iterator = paraName.iterator(); iterator.hasNext();) {
     			String paraKey =  iterator.next();
     			String paraValue = paraMap.get(paraKey);
     			paraBuffer.append("&").append(paraKey).append("=").append(paraValue);
     		}
             reqUrl = reqUrl+"?"+paraBuffer.substring(1);
        }
        method = new HttpGet(reqUrl);
        // 设置header
        this.addRequestHead(method, msgreq);
        return method;

    }
    private void addRequestHead(HttpRequestBase httpRequestBase,HttpClientReq msgreq){
    	 HashMap<String,String> reqHeadMap = msgreq.getHeaderMap();
         StringBuffer headerlog = new StringBuffer();
         if(reqHeadMap!=null&&!reqHeadMap.isEmpty()){
         	 Set<String> reqHeadKeySet = reqHeadMap.keySet();
              for (Iterator<String> iterator = reqHeadKeySet.iterator(); iterator.hasNext();) {
      			String paraKey =  iterator.next();
      			String paraValue = reqHeadMap.get(paraKey);
      			httpRequestBase.addHeader(paraKey, paraValue);
      	        headerlog.append("[").append(paraKey).append(":").append(paraValue).append("]");
      		}
         }
         logger.info( "Req http header:" + headerlog.toString());
         logger.info( "step out prepare method time is ================== "
                 + System.currentTimeMillis());
    }
    /**
     * 执行method方法，完成后释放 TODO http1.1长连接要求下完成后释放这样处理有什么后续影响？
     * 
     * @param method
     *            HttpMethod
     * @return HttpClientResp
     */
    private HttpClientResp excuteMethod(HttpRequestBase httpRequestBase) {
        HttpClientResp msgresp = new HttpClientResp();
        InputStream in = null;
        ByteArrayOutputStream swapStream = null;
        HashMap<String, String> retheadmap = new HashMap<String, String>();
        CloseableHttpResponse httppHttpResponse = null;
        try {
            httppHttpResponse = client.execute(httpRequestBase);
            in =  httppHttpResponse.getEntity().getContent();
            String bodymessage = null;
            swapStream = new ByteArrayOutputStream(); 
            byte[] buff = new byte[1024]; //buff用于存放循环读取的临时数据 
            int rc = 0; 
            while ((rc = in.read(buff, 0, 1024)) > 0) { 
            swapStream.write(buff, 0, rc); 
            } 
            byte[] in_b = swapStream.toByteArray(); //in_b为转换之后的结果 
            bodymessage = new String(in_b, "UTF-8").trim();
            logger.info("Ack http code:" + httppHttpResponse.getStatusLine().getStatusCode());
            logger.info("Ack http body:" + bodymessage);
            msgresp.setHttpcode(httppHttpResponse.getStatusLine().getStatusCode());
            msgresp.setBodymessage(bodymessage);
            // // 接收的header：
            Header[] retheaders = httppHttpResponse.getAllHeaders();
            StringBuffer retheaderlog = new StringBuffer();
            for (int i = 0; i < retheaders.length; i++) {
            	retheadmap.put(retheaders[i].getName(), retheaders[i].getValue());
                retheaderlog.append("[").append(retheaders[i].getName()).append(":").append(retheaders[i].getValue())
                        .append("]");
            }
            logger.info("receive header info :" + retheaderlog.toString());
            msgresp.setHeaderMap(retheadmap);
        } catch (Exception e) {
        	httpRequestBase.abort();
            logger.error("Ack http error:" + e.getMessage(), e);
            return null;
        } finally {
            /** 回收资源 */
            if (null != swapStream) {
                try {
                	swapStream.close();
                } catch (IOException ioe) {
                    logger.error("br.close() error:" + ioe.getMessage(), ioe);
                }
            }
            if (null != in) {
                try {
                    in.close();
                } catch (IOException ioe) {
                    logger.error( "in.close() error:" + ioe.getMessage(), ioe);
                }
            }
            httpRequestBase.releaseConnection();
        }
        return msgresp;

    }
    public static void main(String[] args) {
      /*  int i = 0;
        while(true){
            HttpClientReq msgreq = new HttpClientReq();
            msgreq.setBodymessage("sdsdsdsdsdsdsd");
            Hashtable<String, String> headertable = new Hashtable<String, String>();
            headertable.put("2121323", "dddd");
            msgreq.setHeadertable(headertable);
            msgreq.setRequrl("http://10.116.43.49:8080/publicaccount/Msgdeliver");
            HttpClientManager.getInstance().sendPostRequst(msgreq);
            i++;
            System.out.println("count:"+i);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }*/
        HttpClientManager.sendMessage();
       
    }

    public static void sendHttpMessage(String test)throws Exception {

        HttpProcessor httpproc = HttpProcessorBuilder.create()
            .add(new RequestContent())
            .add(new RequestTargetHost())
            .add(new RequestConnControl())
            .add(new RequestExpectContinue()).build();

        HttpRequestExecutor httpexecutor = new HttpRequestExecutor();

        HttpCoreContext coreContext = HttpCoreContext.create();
        HttpHost host = new HttpHost("10.116.43.49", 8080);
        coreContext.setTargetHost(host);

        DefaultBHttpClientConnection conn = new DefaultBHttpClientConnection(80 * 1024);
        ConnectionReuseStrategy connStrategy = DefaultConnectionReuseStrategy.INSTANCE;

        try {

            HttpEntity[] requestBodies = {
                    new StringEntity(
                            "This is the first test request",
                            ContentType.create("text/plain", Consts.UTF_8)),
                    new ByteArrayEntity(
                            "This is the second test request".getBytes(Consts.UTF_8),
                            ContentType.APPLICATION_OCTET_STREAM),
                    new InputStreamEntity(
                            new ByteArrayInputStream(
                                    test.getBytes(Consts.UTF_8)),
                            ContentType.APPLICATION_OCTET_STREAM)
            };

            for (int i = 0; i < requestBodies.length; i++) {
                if (!conn.isOpen()) {
                    Socket socket = new Socket(host.getHostName(), host.getPort());
                    conn.bind(socket);
                }
                BasicHttpEntityEnclosingRequest request = new BasicHttpEntityEnclosingRequest("POST",
                        "/publicaccount/Msgdeliver");
                request.setEntity(requestBodies[i]);

                httpexecutor.preProcess(request, httpproc, coreContext);
                HttpResponse response = httpexecutor.execute(request, conn, coreContext);
                httpexecutor.postProcess(response, httpproc, coreContext);
                conn.close();
            }
        } finally {
            conn.close();
        }
    }
    
    public static void sendMessage() {
      /*  int threadcount = Integer.parseInt( HttpConstans.MSGDELIVER_THREADPOOL_MAXSIZE);
        logger.info("threadcount:"+threadcount);
        for (int i = 0; i < threadcount; i++) {
            new SendThread().start();
        }*/
        
      int i = 0;

      HttpClientReq msgreq = new HttpClientReq();
      msgreq.setBodyMessage("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><body><msg_id>79373905</msg_id><sender>sip:+8616002011904@zte.com.cn</sender><receiver><uriinfo><type>0</type><uri>sip:125200024018835834@pia.zte.com.cn</uri></uriinfo></receiver><receivetype>1</receivetype><msg_is_report>1</msg_is_report><msgreport><status>0</status><type>1</type></msgreport><msg_length>0</msg_length></body>");
      HashMap<String, String> headerMap = new HashMap<String, String>();
      headerMap.put("version", "1.0");
      headerMap.put("command_id", "msgdeliver");
      headerMap.put("sequence_id", "37172607");
      headerMap.put("spid", "sip:imas1@public.rcs.chinamobile.com");
      msgreq.setHeaderMap(headerMap);
     
      msgreq.setReqUrl("http://10.116.43.49:8080/publicaccount/Msgdeliver");
      HttpClientManager.getInstance().sendPostRequst(msgreq);
      
      try {
          Thread.sleep(1000);
      } catch (InterruptedException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
      }
  
       
    }
}
