package pt.ipleiria.object_detection_autopsy.processors;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import pt.ipleiria.object_detection_autopsy.ObjectDetectionIngestModuleFactory;
import pt.ipleiria.object_detection_autopsy.model.ImageDetection;
import pt.ipleiria.object_detection_autopsy.model.VideoDetection;

public class WebProcessor implements Processor
{

 private final String address;
 private final int port;

 public WebProcessor(String address, int port) throws MalformedURLException
 {
  this.address = address;
  this.port = port;
 }

 @Override
 public ImageDetection processImage(String path)
 {

  return null;
 }

 @Override
 public VideoDetection processVideo(String path)
 {
  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
 }

 @Override
 public boolean isProcessorAvailable() throws IOException, IllegalAccessException
 {
  HttpURLConnection httpConnection = this.buildHttpRequest("GET", RequestsPaths.PING);
  httpConnection.connect();
  int responseCode = httpConnection.getResponseCode();
  httpConnection.disconnect();
  return responseCode >=200 && responseCode < 300;
 }

 @Override
 public void close()
 {
 }

 private HttpURLConnection buildHttpRequest(String method, String endpoint) throws IOException, IllegalAccessException
 {
  URL service = new URL("http", this.address, this.port, endpoint);
  URLConnection connection = service.openConnection();
  if(!(connection instanceof HttpURLConnection))
  {
   throw new IllegalAccessException("URLConnection ins't HttpURLConnection");
  }
  HttpURLConnection httpConnection = (HttpURLConnection) connection;
  httpConnection.setRequestMethod(method);
  httpConnection.setRequestProperty("Accept", "application/json");
  if(!method.contentEquals("GET"))
  {
   httpConnection.setDoOutput(true);
  }
  return httpConnection;
 }
 
 private class RequestsPaths
 {

  private final static String PING = "/ping";
  private final static String PROCESS = "/process";
 }

}
