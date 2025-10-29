package cl.getnet.jsontoapi.client;

public class HttpCallResult<T> {
  private final int status;
  private final String url;
  private final long durationMs;
  private final T body;

  public HttpCallResult(int status, String url, long durationMs, T body) {
    this.status = status;
    this.url = url;
    this.durationMs = durationMs;
    this.body = body;
  }
  public int getStatus() { return status; }
  public String getUrl() { return url; }
  public long getDurationMs() { return durationMs; }
  public T getBody() { return body; }
  public boolean is2xx() { return status >= 200 && status < 300; }
}
