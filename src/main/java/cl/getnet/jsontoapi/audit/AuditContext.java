
package cl.getnet.jsontoapi.audit;

public class AuditContext {
  private static final ThreadLocal<AuditModels.AuditRecord> CTX = new ThreadLocal<>();
  public static void set(AuditModels.AuditRecord rec) { CTX.set(rec); }
  public static AuditModels.AuditRecord get() { return CTX.get(); }
  public static void clear() { CTX.remove(); }
}
