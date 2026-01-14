package cl.getnet.jsontoapi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.IOException;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JsonData {

  @Schema(description = "Identificador del canal", example = "2")
  @JsonProperty("channel")
  private Integer channel;

  @Schema(description = "Número de sucursal", example = "99")
  @JsonProperty("no_branch")
  private String no_branch;

  @Schema(description = "Código del vendedor", example = "100")
  @JsonProperty("seller_code")
  private String seller_code;

  @Schema(description = "Nombre comercial", example = "Comercio Demo")
  @JsonProperty("trade_name")
  private String trade_name;

  @Schema(description = "RUT del comercio", example = "11111111-1")
  @JsonProperty("rut_commerce")
  private String rut_commerce;

  @Schema(description = "Nombre de la sucursal", example = "Sucursal Centro")
  @JsonProperty("branch_name")
  private String branch_name;

  @Schema(description = "Nombre corto de la sucursal", example = "Centro")
  @JsonProperty("short_branch_name")
  private String short_branch_name;

  @Schema(description = "Código de terminal", example = "61000192")
  @JsonProperty("terminal_code")
  private String terminal_code;

  @Schema(description = "Municipalidad / Comuna", example = "Santiago")
  @JsonProperty("municipality")
  private String municipality;

  @Schema(description = "Dirección", example = "Calle Falsa 123")
  @JsonProperty("address")
  private String address;

  @Schema(description = "Clave del comercio", example = "000000")
  @JsonProperty("key_trade")
  private String key_trade;

  @JsonDeserialize(using = FlexibleBoolean.class)
  @Schema(description = "Bi-moneda", example = "0")
  @JsonProperty("bi_currency")
  private Boolean bi_currency;

  @JsonDeserialize(using = FlexibleBoolean.class)
  @Schema(description = "Recibo / Ticket", example = "0")
  @JsonProperty("ticket_receipt")
  private Boolean ticket_receipt;

  @JsonDeserialize(using = FlexibleBoolean.class)
  @Schema(description = "Vendedor / Salesperson", example = "0")
  @JsonProperty("salesperson")
  private Boolean salesperson;

  @JsonDeserialize(using = FlexibleBoolean.class)
  @Schema(description = "Ticket", example = "0")
  @JsonProperty("ticket")
  private Boolean ticket;

  @JsonDeserialize(using = FlexibleBoolean.class)
  @Schema(description = "Propina", example = "0")
  @JsonProperty("tip_fee")
  private Boolean tip_fee;

  @JsonDeserialize(using = FlexibleBoolean.class)
  @Schema(description = "Cashback", example = "0")
  @JsonProperty("cashback")
  private Boolean cashback;

  @JsonDeserialize(using = FlexibleBoolean.class)
  @Schema(description = "Issuing fees", example = "0")
  @JsonProperty("issuing_fees")
  private Boolean issuing_fees;

  @Schema(description = "Min issuing fees", example = "0")
  @JsonProperty("min_issuing_fees")
  private Integer min_issuing_fees;

  @Schema(description = "Max issuing fees", example = "0")
  @JsonProperty("max_issuing_fees")
  private Integer max_issuing_fees;

  @JsonDeserialize(using = FlexibleBoolean.class)
  @Schema(description = "Fee commerce", example = "0")
  @JsonProperty("fee_commerce")
  private Boolean fee_commerce;

  @Schema(description = "Min fee commerce", example = "0")
  @JsonProperty("min_fee_commerce")
  private Integer min_fee_commerce;

  @Schema(description = "Max fee commerce", example = "0")
  @JsonProperty("max_fee_commerce")
  private Integer max_fee_commerce;

  @JsonDeserialize(using = FlexibleBoolean.class)
  @Schema(description = "Visa crédito", example = "0")
  @JsonProperty("visa_credit")
  private Boolean visa_credit;

  @JsonDeserialize(using = FlexibleBoolean.class)
  @Schema(description = "Visa débito", example = "0")
  @JsonProperty("visa_debit")
  private Boolean visa_debit;

  @JsonDeserialize(using = FlexibleBoolean.class)
  @Schema(description = "Visa débito electron", example = "0")
  @JsonProperty("visa_debit_electron")
  private Boolean visa_debit_electron;

  @JsonDeserialize(using = FlexibleBoolean.class)
  @Schema(description = "Visa prepago", example = "0")
  @JsonProperty("visa_prepago")
  private Boolean visa_prepago;

  @JsonDeserialize(using = FlexibleBoolean.class)
  @Schema(description = "Mastercard crédito", example = "0")
  @JsonProperty("credit_mastercard")
  private Boolean credit_mastercard;

  @JsonDeserialize(using = FlexibleBoolean.class)
  @Schema(description = "Mastercard débito", example = "0")
  @JsonProperty("debit_mastercard")
  private Boolean debit_mastercard;

  @JsonDeserialize(using = FlexibleBoolean.class)
  @Schema(description = "Maestro débito", example = "0")
  @JsonProperty("debit_maestro")
  private Boolean debit_maestro;

  @JsonDeserialize(using = FlexibleBoolean.class)
  @Schema(description = "Mastercard prepago", example = "0")
  @JsonProperty("prepago_mastercard")
  private Boolean prepago_mastercard;

  @JsonDeserialize(using = FlexibleBoolean.class)
  @Schema(description = "Amex crédito", example = "0")
  @JsonProperty("credit_amex")
  private Boolean credit_amex;

  @JsonDeserialize(using = FlexibleBoolean.class)
  @Schema(description = "Amex débito", example = "0")
  @JsonProperty("debit_amex")
  private Boolean debit_amex;

  @JsonDeserialize(using = FlexibleBoolean.class)
  @Schema(description = "Producto Magna", example = "0")
  @JsonProperty("magna_product")
  private Boolean magna_product;

  @Schema(description = "Número de folio", example = "0")
  @JsonProperty("folio_number")
  private Integer folio_number;

  @JsonDeserialize(using = FlexibleBoolean.class)
  @Schema(description = "SEP property", example = "0")
  @JsonProperty("SEP")
  private Boolean SEP;

  @Schema(description = "Credential 01", example = "a")
  @JsonProperty("credential01")
  private String credential01;

  @Schema(description = "Credential 02", example = "b")
  @JsonProperty("credential02")
  private String credential02;

  @Schema(description = "Credential 03", example = "c")
  @JsonProperty("credential03")
  private String credential03;

  @Schema(description = "Credential 04", example = "d")
  @JsonProperty("credential04")
  private String credential04;

  @Schema(description = "Serial Number", example = "")
  @JsonProperty("serialNumber")
  private String serialNumber;

  @Schema(description = "Desactivated terminal", example = "000")
  @JsonProperty("desactivatedterminal")
  private String desactivatedterminal;

  public Integer getChannel() {
    return channel;
  }

  public void setChannel(Integer channel) {
    this.channel = channel;
  }

  public String getNo_branch() {
    return no_branch;
  }

  public void setNo_branch(String no_branch) {
    this.no_branch = no_branch;
  }

  public String getSeller_code() {
    return seller_code;
  }

  public void setSeller_code(String seller_code) {
    this.seller_code = seller_code;
  }

  public String getTrade_name() {
    return trade_name;
  }

  public void setTrade_name(String trade_name) {
    this.trade_name = trade_name;
  }

  public String getRut_commerce() {
    return rut_commerce;
  }

  public void setRut_commerce(String rut_commerce) {
    this.rut_commerce = rut_commerce;
  }

  public String getBranch_name() {
    return branch_name;
  }

  public void setBranch_name(String branch_name) {
    this.branch_name = branch_name;
  }

  public String getShort_branch_name() {
    return short_branch_name;
  }

  public void setShort_branch_name(String short_branch_name) {
    this.short_branch_name = short_branch_name;
  }

  public String getTerminal_code() {
    return terminal_code;
  }

  public void setTerminal_code(String terminal_code) {
    this.terminal_code = terminal_code;
  }

  public String getMunicipality() {
    return municipality;
  }

  public void setMunicipality(String municipality) {
    this.municipality = municipality;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getKey_trade() {
    return key_trade;
  }

  public void setKey_trade(String key_trade) {
    this.key_trade = key_trade;
  }

  public Boolean getBi_currency() {
    return bi_currency;
  }

  public void setBi_currency(Boolean bi_currency) {
    this.bi_currency = bi_currency;
  }

  public Boolean getTicket_receipt() {
    return ticket_receipt;
  }

  public void setTicket_receipt(Boolean ticket_receipt) {
    this.ticket_receipt = ticket_receipt;
  }

  public Boolean getSalesperson() {
    return salesperson;
  }

  public void setSalesperson(Boolean salesperson) {
    this.salesperson = salesperson;
  }

  public Boolean getTicket() {
    return ticket;
  }

  public void setTicket(Boolean ticket) {
    this.ticket = ticket;
  }

  public Boolean getTip_fee() {
    return tip_fee;
  }

  public void setTip_fee(Boolean tip_fee) {
    this.tip_fee = tip_fee;
  }

  public Boolean getCashback() {
    return cashback;
  }

  public void setCashback(Boolean cashback) {
    this.cashback = cashback;
  }

  public Boolean getIssuing_fees() {
    return issuing_fees;
  }

  public void setIssuing_fees(Boolean issuing_fees) {
    this.issuing_fees = issuing_fees;
  }

  public Integer getMin_issuing_fees() {
    return min_issuing_fees;
  }

  public void setMin_issuing_fees(Integer min_issuing_fees) {
    this.min_issuing_fees = min_issuing_fees;
  }

  public Integer getMax_issuing_fees() {
    return max_issuing_fees;
  }

  public void setMax_issuing_fees(Integer max_issuing_fees) {
    this.max_issuing_fees = max_issuing_fees;
  }

  public Boolean getFee_commerce() {
    return fee_commerce;
  }

  public void setFee_commerce(Boolean fee_commerce) {
    this.fee_commerce = fee_commerce;
  }

  public Integer getMin_fee_commerce() {
    return min_fee_commerce;
  }

  public void setMin_fee_commerce(Integer min_fee_commerce) {
    this.min_fee_commerce = min_fee_commerce;
  }

  public Integer getMax_fee_commerce() {
    return max_fee_commerce;
  }

  public void setMax_fee_commerce(Integer max_fee_commerce) {
    this.max_fee_commerce = max_fee_commerce;
  }

  public Boolean getVisa_credit() {
    return visa_credit;
  }

  public void setVisa_credit(Boolean visa_credit) {
    this.visa_credit = visa_credit;
  }

  public Boolean getVisa_debit() {
    return visa_debit;
  }

  public void setVisa_debit(Boolean visa_debit) {
    this.visa_debit = visa_debit;
  }

  public Boolean getVisa_debit_electron() {
    return visa_debit_electron;
  }

  public void setVisa_debit_electron(Boolean visa_debit_electron) {
    this.visa_debit_electron = visa_debit_electron;
  }

  public Boolean getVisa_prepago() {
    return visa_prepago;
  }

  public void setVisa_prepago(Boolean visa_prepago) {
    this.visa_prepago = visa_prepago;
  }

  public Boolean getCredit_mastercard() {
    return credit_mastercard;
  }

  public void setCredit_mastercard(Boolean credit_mastercard) {
    this.credit_mastercard = credit_mastercard;
  }

  public Boolean getDebit_mastercard() {
    return debit_mastercard;
  }

  public void setDebit_mastercard(Boolean debit_mastercard) {
    this.debit_mastercard = debit_mastercard;
  }

  public Boolean getDebit_maestro() {
    return debit_maestro;
  }

  public void setDebit_maestro(Boolean debit_maestro) {
    this.debit_maestro = debit_maestro;
  }

  public Boolean getPrepago_mastercard() {
    return prepago_mastercard;
  }

  public void setPrepago_mastercard(Boolean prepago_mastercard) {
    this.prepago_mastercard = prepago_mastercard;
  }

  public Boolean getCredit_amex() {
    return credit_amex;
  }

  public void setCredit_amex(Boolean credit_amex) {
    this.credit_amex = credit_amex;
  }

  public Boolean getDebit_amex() {
    return debit_amex;
  }

  public void setDebit_amex(Boolean debit_amex) {
    this.debit_amex = debit_amex;
  }

  public Boolean getMagna_product() {
    return magna_product;
  }

  public void setMagna_product(Boolean magna_product) {
    this.magna_product = magna_product;
  }

  public Integer getFolio_number() {
    return folio_number;
  }

  public void setFolio_number(Integer folio_number) {
    this.folio_number = folio_number;
  }

  public Boolean getSep() {
    return SEP;
  }

  public void setSep(Boolean SEP) {
    this.SEP = SEP;
  }

  public String getCredential01() {
    return credential01;
  }

  public void setCredential01(String credential01) {
    this.credential01 = credential01;
  }

  public String getCredential02() {
    return credential02;
  }

  public void setCredential02(String credential02) {
    this.credential02 = credential02;
  }

  public String getCredential03() {
    return credential03;
  }

  public void setCredential03(String credential03) {
    this.credential03 = credential03;
  }

  public String getCredential04() {
    return credential04;
  }

  public void setCredential04(String credential04) {
    this.credential04 = credential04;
  }

  public String getSerialNumber() {
    return serialNumber;
  }

  public void setSerialNumber(String serialNumber) {
    this.serialNumber = serialNumber;
  }

  public String getDesactivatedterminal() {
    return desactivatedterminal;
  }

  public void setDesactivatedterminal(String desactivatedterminal) {
    this.desactivatedterminal = desactivatedterminal;
  }

  public static class FlexibleBoolean extends JsonDeserializer<Boolean> {
    @Override
    public Boolean deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
      if (p.currentToken().isBoolean())
        return p.getBooleanValue();
      if (p.currentToken().isNumeric())
        return p.getIntValue() != 0;

      String s = p.getValueAsString();
      if (s == null)
        return null;
      s = s.trim().toLowerCase();
      switch (s) {
        case "1":
        case "true":
        case "t":
        case "y":
        case "yes":
        case "si":
        case "sí":
          return true;
        case "0":
        case "false":
        case "f":
        case "n":
        case "no":
          return false;
        default:
          return !"0".equals(s) && !"false".equals(s) && !"no".equals(s);
      }
    }
  }
}
