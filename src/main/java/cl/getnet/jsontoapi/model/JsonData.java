package cl.getnet.jsontoapi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JsonData {
  @NotNull
  private Integer channel;
  private String no_branch;
  private String seller_code;
  private String trade_name;
  private String rut_commerce;
  private String branch_name;
  private String short_branch_name;
  @NotNull
  private String terminal_code;
  private String municipality;
  private String address;
  private Integer bi_currency;
  private Integer ticket_receipt;
  private Integer salesperson;
  private Integer ticket;
  private Integer tip_fee;
  private Integer cashback;
  private String key_trade;
  private Integer issuing_fees;
  private Integer min_issuing_fees;
  private Integer max_issuing_fees;
  private Integer fee_commerce;
  private Integer min_fee_commerce;
  private Integer max_fee_commerce;
  private Integer zero_rate_fees;
  private Integer min_zero_rate_fees;
  private Integer max_zero_rate_fees;
  private Integer instalments_Rate;
  private Integer min_instalments_Rate;
  private Integer max_instalments_Rate;
  private Integer visa_credit;
  private Integer visa_debit;
  private Integer visa_debit_electron;
  private Integer visa_prepago;
  private Integer credit_mastercard;
  private Integer debit_mastercard;
  private Integer debit_maestro;
  private Integer prepago_mastercard;
  private Integer credit_amex;
  private Integer debit_amex;
  private Integer magna_product;
  private Integer folio_number;
  private Integer pos_avance;
  private Integer sep;
  private String credential01;
  private String credential02;
  private String credential03;
  private String credential04;

  public Integer getChannel() { return channel; }
  public void setChannel(Integer channel) { this.channel = channel; }
  public String getNo_branch() { return no_branch; }
  public void setNo_branch(String v) { this.no_branch = v; }
  public String getSeller_code() { return seller_code; }
  public void setSeller_code(String v) { this.seller_code = v; }
  public String getTrade_name() { return trade_name; }
  public void setTrade_name(String v) { this.trade_name = v; }
  public String getRut_commerce() { return rut_commerce; }
  public void setRut_commerce(String v) { this.rut_commerce = v; }
  public String getBranch_name() { return branch_name; }
  public void setBranch_name(String v) { this.branch_name = v; }
  public String getShort_branch_name() { return short_branch_name; }
  public void setShort_branch_name(String v) { this.short_branch_name = v; }
  public String getTerminal_code() { return terminal_code; }
  public void setTerminal_code(String v) { this.terminal_code = v; }
  public String getMunicipality() { return municipality; }
  public void setMunicipality(String v) { this.municipality = v; }
  public String getAddress() { return address; }
  public void setAddress(String v) { this.address = v; }
  public Integer getBi_currency() { return bi_currency; }
  public void setBi_currency(Integer v) { this.bi_currency = v; }
  public Integer getTicket_receipt() { return ticket_receipt; }
  public void setTicket_receipt(Integer v) { this.ticket_receipt = v; }
  public Integer getSalesperson() { return salesperson; }
  public void setSalesperson(Integer v) { this.salesperson = v; }
  public Integer getTicket() { return ticket; }
  public void setTicket(Integer v) { this.ticket = v; }
  public Integer getTip_fee() { return tip_fee; }
  public void setTip_fee(Integer v) { this.tip_fee = v; }
  public Integer getCashback() { return cashback; }
  public void setCashback(Integer v) { this.cashback = v; }
  public String getKey_trade() { return key_trade; }
  public void setKey_trade(String v) { this.key_trade = v; }
  public Integer getIssuing_fees() { return issuing_fees; }
  public void setIssuing_fees(Integer v) { this.issuing_fees = v; }
  public Integer getMin_issuing_fees() { return min_issuing_fees; }
  public void setMin_issuing_fees(Integer v) { this.min_issuing_fees = v; }
  public Integer getMax_issuing_fees() { return max_issuing_fees; }
  public void setMax_issuing_fees(Integer v) { this.max_issuing_fees = v; }
  public Integer getFee_commerce() { return fee_commerce; }
  public void setFee_commerce(Integer v) { this.fee_commerce = v; }
  public Integer getMin_fee_commerce() { return min_fee_commerce; }
  public void setMin_fee_commerce(Integer v) { this.min_fee_commerce = v; }
  public Integer getMax_fee_commerce() { return max_fee_commerce; }
  public void setMax_fee_commerce(Integer v) { this.max_fee_commerce = v; }
  public Integer getZero_rate_fees() { return zero_rate_fees; }
  public void setZero_rate_fees(Integer v) { this.zero_rate_fees = v; }
  public Integer getMin_zero_rate_fees() { return min_zero_rate_fees; }
  public void setMin_zero_rate_fees(Integer v) { this.min_zero_rate_fees = v; }
  public Integer getMax_zero_rate_fees() { return max_zero_rate_fees; }
  public void setMax_zero_rate_fees(Integer v) { this.max_zero_rate_fees = v; }
  public Integer getInstalments_Rate() { return instalments_Rate; }
  public void setInstalments_Rate(Integer v) { this.instalments_Rate = v; }
  public Integer getMin_instalments_Rate() { return min_instalments_Rate; }
  public void setMin_instalments_Rate(Integer v) { this.min_instalments_Rate = v; }
  public Integer getMax_instalments_Rate() { return max_instalments_Rate; }
  public void setMax_instalments_Rate(Integer v) { this.max_instalments_Rate = v; }
  public Integer getVisa_credit() { return visa_credit; }
  public void setVisa_credit(Integer v) { this.visa_credit = v; }
  public Integer getVisa_debit() { return visa_debit; }
  public void setVisa_debit(Integer v) { this.visa_debit = v; }
  public Integer getVisa_debit_electron() { return visa_debit_electron; }
  public void setVisa_debit_electron(Integer v) { this.visa_debit_electron = v; }
  public Integer getVisa_prepago() { return visa_prepago; }
  public void setVisa_prepago(Integer v) { this.visa_prepago = v; }
  public Integer getCredit_mastercard() { return credit_mastercard; }
  public void setCredit_mastercard(Integer v) { this.credit_mastercard = v; }
  public Integer getDebit_mastercard() { return debit_mastercard; }
  public void setDebit_mastercard(Integer v) { this.debit_mastercard = v; }
  public Integer getDebit_maestro() { return debit_maestro; }
  public void setDebit_maestro(Integer v) { this.debit_maestro = v; }
  public Integer getPrepago_mastercard() { return prepago_mastercard; }
  public void setPrepago_mastercard(Integer v) { this.prepago_mastercard = v; }
  public Integer getCredit_amex() { return credit_amex; }
  public void setCredit_amex(Integer v) { this.credit_amex = v; }
  public Integer getDebit_amex() { return debit_amex; }
  public void setDebit_amex(Integer v) { this.debit_amex = v; }
  public Integer getMagna_product() { return magna_product; }
  public void setMagna_product(Integer v) { this.magna_product = v; }
  public Integer getFolio_number() { return folio_number; }
  public void setFolio_number(Integer v) { this.folio_number = v; }
  public Integer getPos_avance() { return pos_avance; }
  public void setPos_avance(Integer v) { this.pos_avance = v; }
  public Integer getSep() { return sep; }
  public void setSep(Integer v) { this.sep = v; }
  public String getCredential01() { return credential01; }
  public void setCredential01(String v) { this.credential01 = v; }
  public String getCredential02() { return credential02; }
  public void setCredential02(String v) { this.credential02 = v; }
  public String getCredential03() { return credential03; }
  public void setCredential03(String v) { this.credential03 = v; }
  public String getCredential04() { return credential04; }
  public void setCredential04(String v) { this.credential04 = v; }
}
