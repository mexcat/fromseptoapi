package cl.getnet.jsontoapi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JsonData {

  private Integer channel;
  private String no_branch;
  private String seller_code;
  private String trade_name;
  private String rut_commerce;
  private String branch_name;
  private String short_branch_name;
  private String terminal_code;
  private String municipality;
  private String address;

  private String key_trade;
  private String min_zero_rate_fees;
  private String max_zero_rate_fees;
  private String min_instalments_Rate;
  private String max_instalments_Rate;

  @JsonDeserialize(using = FlexibleBoolean.class) private Boolean bi_currency;
  @JsonDeserialize(using = FlexibleBoolean.class) private Boolean ticket_receipt;
  @JsonDeserialize(using = FlexibleBoolean.class) private Boolean salesperson;
  @JsonDeserialize(using = FlexibleBoolean.class) private Boolean tip_fee;
  @JsonDeserialize(using = FlexibleBoolean.class) private Boolean cashback;
  @JsonDeserialize(using = FlexibleBoolean.class) private Boolean issuing_fees;
  @JsonDeserialize(using = FlexibleBoolean.class) private Boolean fee_commerce;
  @JsonDeserialize(using = FlexibleBoolean.class) private Boolean zero_rate_fees;
  @JsonDeserialize(using = FlexibleBoolean.class) private Boolean visa_credit;
  @JsonDeserialize(using = FlexibleBoolean.class) private Boolean visa_debit;
  @JsonDeserialize(using = FlexibleBoolean.class) private Boolean visa_debit_electron;
  @JsonDeserialize(using = FlexibleBoolean.class) private Boolean visa_prepago;
  @JsonDeserialize(using = FlexibleBoolean.class) private Boolean credit_mastercard;
  @JsonDeserialize(using = FlexibleBoolean.class) private Boolean debit_mastercard;
  @JsonDeserialize(using = FlexibleBoolean.class) private Boolean debit_maestro;
  @JsonDeserialize(using = FlexibleBoolean.class) private Boolean prepago_mastercard;
  @JsonDeserialize(using = FlexibleBoolean.class) private Boolean credit_amex;
  @JsonDeserialize(using = FlexibleBoolean.class) private Boolean debit_amex;
  @JsonDeserialize(using = FlexibleBoolean.class) private Boolean magna_product;
  @JsonDeserialize(using = FlexibleBoolean.class) private Boolean SEP;
  @JsonDeserialize(using = FlexibleBoolean.class) private Boolean ticket;

  public Integer getChannel() { return channel; }
  public void setChannel(Integer channel) { this.channel = channel; }

  public String getNo_branch() { return no_branch; }
  public void setNo_branch(String no_branch) { this.no_branch = no_branch; }

  public String getSeller_code() { return seller_code; }
  public void setSeller_code(String seller_code) { this.seller_code = seller_code; }

  public String getTrade_name() { return trade_name; }
  public void setTrade_name(String trade_name) { this.trade_name = trade_name; }

  public String getRut_commerce() { return rut_commerce; }
  public void setRut_commerce(String rut_commerce) { this.rut_commerce = rut_commerce; }

  public String getBranch_name() { return branch_name; }
  public void setBranch_name(String branch_name) { this.branch_name = branch_name; }

  public String getShort_branch_name() { return short_branch_name; }
  public void setShort_branch_name(String short_branch_name) { this.short_branch_name = short_branch_name; }

  public String getTerminal_code() { return terminal_code; }
  public void setTerminal_code(String terminal_code) { this.terminal_code = terminal_code; }

  public String getMunicipality() { return municipality; }
  public void setMunicipality(String municipality) { this.municipality = municipality; }

  public String getAddress() { return address; }
  public void setAddress(String address) { this.address = address; }

  public String getKey_trade() { return key_trade; }
  public void setKey_trade(String key_trade) { this.key_trade = key_trade; }

  public String getMin_zero_rate_fees() { return min_zero_rate_fees; }
  public void setMin_zero_rate_fees(String v) { this.min_zero_rate_fees = v; }

  public String getMax_zero_rate_fees() { return max_zero_rate_fees; }
  public void setMax_zero_rate_fees(String v) { this.max_zero_rate_fees = v; }

  public String getMin_instalments_Rate() { return min_instalments_Rate; }
  public void setMin_instalments_Rate(String v) { this.min_instalments_Rate = v; }

  public String getMax_instalments_Rate() { return max_instalments_Rate; }
  public void setMax_instalments_Rate(String v) { this.max_instalments_Rate = v; }

  public Boolean getBi_currency() { return bi_currency; }
  public void setBi_currency(Boolean bi_currency) { this.bi_currency = bi_currency; }

  public Boolean getTicket_receipt() { return ticket_receipt; }
  public void setTicket_receipt(Boolean ticket_receipt) { this.ticket_receipt = ticket_receipt; }

  public Boolean getSalesperson() { return salesperson; }
  public void setSalesperson(Boolean salesperson) { this.salesperson = salesperson; }

  public Boolean getTip_fee() { return tip_fee; }
  public void setTip_fee(Boolean tip_fee) { this.tip_fee = tip_fee; }

  public Boolean getCashback() { return cashback; }
  public void setCashback(Boolean cashback) { this.cashback = cashback; }

  public Boolean getIssuing_fees() { return issuing_fees; }
  public void setIssuing_fees(Boolean issuing_fees) { this.issuing_fees = issuing_fees; }

  public Boolean getFee_commerce() { return fee_commerce; }
  public void setFee_commerce(Boolean fee_commerce) { this.fee_commerce = fee_commerce; }

  public Boolean getZero_rate_fees() { return zero_rate_fees; }
  public void setZero_rate_fees(Boolean zero_rate_fees) { this.zero_rate_fees = zero_rate_fees; }

  public Boolean getVisa_credit() { return visa_credit; }
  public void setVisa_credit(Boolean visa_credit) { this.visa_credit = visa_credit; }

  public Boolean getVisa_debit() { return visa_debit; }
  public void setVisa_debit(Boolean visa_debit) { this.visa_debit = visa_debit; }

  public Boolean getVisa_debit_electron() { return visa_debit_electron; }
  public void setVisa_debit_electron(Boolean visa_debit_electron) { this.visa_debit_electron = visa_debit_electron; }

  public Boolean getVisa_prepago() { return visa_prepago; }
  public void setVisa_prepago(Boolean visa_prepago) { this.visa_prepago = visa_prepago; }

  public Boolean getCredit_mastercard() { return credit_mastercard; }
  public void setCredit_mastercard(Boolean credit_mastercard) { this.credit_mastercard = credit_mastercard; }

  public Boolean getDebit_mastercard() { return debit_mastercard; }
  public void setDebit_mastercard(Boolean debit_mastercard) { this.debit_mastercard = debit_mastercard; }

  public Boolean getDebit_maestro() { return debit_maestro; }
  public void setDebit_maestro(Boolean debit_maestro) { this.debit_maestro = debit_maestro; }

  public Boolean getPrepago_mastercard() { return prepago_mastercard; }
  public void setPrepago_mastercard(Boolean prepago_mastercard) { this.prepago_mastercard = prepago_mastercard; }

  public Boolean getCredit_amex() { return credit_amex; }
  public void setCredit_amex(Boolean credit_amex) { this.credit_amex = credit_amex; }

  public Boolean getDebit_amex() { return debit_amex; }
  public void setDebit_amex(Boolean debit_amex) { this.debit_amex = debit_amex; }

  public Boolean getMagna_product() { return magna_product; }
  public void setMagna_product(Boolean magna_product) { this.magna_product = magna_product; }

  public Boolean getSep() { return SEP; }
  public void setSep(Boolean SEP) { this.SEP = SEP; }

  public Boolean getTicket() { return ticket; }
  public void setTicket(Boolean ticket) { this.ticket = ticket; }


  public static class FlexibleBoolean extends JsonDeserializer<Boolean> {
    @Override
    public Boolean deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
      if (p.currentToken().isBoolean()) return p.getBooleanValue();
      if (p.currentToken().isNumeric()) return p.getIntValue() != 0;

      String s = p.getValueAsString();
      if (s == null) return null;
      s = s.trim().toLowerCase();
      switch (s) {
        case "1": case "true": case "t": case "y": case "yes": case "si": case "sí": return true;
        case "0": case "false": case "f": case "n": case "no": return false;
        default:
          return !"0".equals(s) && !"false".equals(s) && !"no".equals(s);
      }
    }
  }
}
