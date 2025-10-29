package cl.getnet.jsontoapi.dto;

public record StandardResponse(String status, String error) {
  public static StandardResponse success() { return new StandardResponse("success", ""); }
  public static StandardResponse fail(String msg) { return new StandardResponse("fail", msg); }
}
