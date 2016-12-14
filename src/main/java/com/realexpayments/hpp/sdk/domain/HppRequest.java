package com.realexpayments.hpp.sdk.domain;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.apache.commons.codec.binary.Base64;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.realexpayments.hpp.sdk.utils.GenerationUtils;
import com.realexpayments.hpp.sdk.validators.OtbAmount;

/**
 * <p>
 * Class representing a request to be sent to HPP.
 * </p>
 * <p>
 * Helper methods are provided (prefixed with 'add') for object creation.
 * </p>
 * <p>
 * Example usage:
 * <code><pre>
 * HppRequest request = new HppRequest()
 * 	.addAmount(100)
 * 	.addCurrency("EUR")
 * 	.addMerchantId("merchantId");
 * </pre></code>
 * </p>
 * 
 * @author markstanford
 */
@OtbAmount
public class HppRequest {

	public enum Flag {
		TRUE("1"),
		FALSE("0");

		/**
		 * The flag String value 
		 */
		private final String flag;

		/**
		 * Flag constructor
		 * 
		 * @param type
		 */
		Flag(String flag) {
			this.flag = flag;
		}

		/**
		 * Get the string value of the flag
		 * 
		 * @return String 
		 */
		public String getFlag() {
			return flag;
		}
	}

	/**
	 * The merchant ID supplied by Realex Payments – note this is not the merchant number supplied by your bank.
	 */
	@Size(min = 1, max = 50, message = "{hppRequest.merchantId.size}")
	@Pattern(regexp = "^[a-zA-Z0-9\\.]*$", message = "{hppRequest.merchantId.pattern}")
	@JsonProperty("MERCHANT_ID")
	private String merchantId;

	/**
	 * The sub-account to use for this transaction. If not present, the default sub-account will be used.
	 */
	@Size(min = 0, max = 30, message = "{hppRequest.account.size}")
	@Pattern(regexp = "^[a-zA-Z0-9\\s]*$", message = "{hppRequest.account.pattern}")
	@JsonProperty("ACCOUNT")
	private String account;

	/**
	 * A unique alphanumeric id that’s used to identify the transaction. No spaces are allowed.
	 */
	@Size(min = 0, max = 50, message = "{hppRequest.orderId.size}")
	@Pattern(regexp = "^[a-zA-Z0-9_\\-]*$*$", message = "{hppRequest.orderId.pattern}")
	@JsonProperty("ORDER_ID")
	private String orderId;

	/**
	 * Total amount to authorise in the lowest unit of the currency – i.e. 100 euro would be entered as 10000. 
	 * If there is no decimal in the currency (e.g. JPY Yen) then contact Realex Payments. No decimal points are allowed.
	 * Amount should be set to 0 for OTB transactions (i.e. where validate card only is set to 1).
	 */
	@Size(min = 1, max = 11, message = "{hppRequest.amount.size}")
	@Pattern(regexp = "^[0-9]*$", message = "{hppRequest.amount.pattern}")
	@JsonProperty("AMOUNT")
	private String amount;

	/**
	 * A three-letter currency code (Eg. EUR, GBP). A list of currency codes can be provided by your account manager.
	 */
	@Size(min = 3, max = 3, message = "{hppRequest.currency.size}")
	@Pattern(regexp = "^[a-zA-Z]*$", message = "{hppRequest.currency.pattern}")
	@JsonProperty("CURRENCY")
	private String currency;

	/**
	 * Date and time of the transaction. Entered in the following format: YYYYMMDDHHMMSS. Must be within 24 hours of the current time.
	 */
	@Size(min = 14, max = 14, message = "{hppRequest.timestamp.size}")
	@Pattern(regexp = "^[0-9]*$", message = "{hppRequest.timestamp.pattern}")
	@JsonProperty("TIMESTAMP")
	private String timeStamp;

	/**
	 * A digital signature generated using the SHA-1 algorithm.
	 */
	@Size(min = 40, max = 40, message = "{hppRequest.hash.size}")
	@Pattern(regexp = "^[a-f0-9]*$", message = "{hppRequest.hash.pattern}")
	@JsonProperty("SHA1HASH")
	private String hash;

	/**
	 * Used to signify whether or not you wish the transaction to be captured in the next batch. 
	 * If set to "1" and assuming the transaction is authorised then it will automatically be settled in the next batch. 
	 * If set to "0" then the merchant must use the RealControl application to manually settle the transaction. 
	 * This option can be used if a merchant wishes to delay the payment until after the goods have been shipped. 
	 * Transactions can be settled for up to 115% of the original amount and must be settled within a certain period of time agreed with your issuing bank.
	 */
	@Pattern(regexp = "(?i)^on*|^off$|^*$|^multi$|^1$|^0$", message = "{hppRequest.autoSettleFlag.pattern}")
	@JsonProperty("AUTO_SETTLE_FLAG")
	private String autoSettleFlag;

	/**
	 * A freeform comment to describe the transaction.
	 */
	@Size(min = 0, max = 255, message = "{hppRequest.comment1.size}")
	@Pattern(regexp = "^[\\s \u0020-\u003B \u003D \u003F-\u007E \u00A1-\u00FF\u20AC\u201A\u0192\u201E\u2026\u2020\u2021\u02C6\u2030\u0160\u2039\u0152\u017D\u2018\u2019\u201C\u201D\u2022\u2013\u2014\u02DC\u2122\u0161\u203A\u0153\u017E\u0178]*$", message = "{hppRequest.comment1.pattern}")
	@JsonProperty("COMMENT1")
	private String commentOne;

	/**
	 * A freeform comment to describe the transaction.
	 */
	@Size(min = 0, max = 255, message = "{hppRequest.comment2.size}")
	@Pattern(regexp = "^[\\s \u0020-\u003B \u003D \u003F-\u007E \u00A1-\u00FF\u20AC\u201A\u0192\u201E\u2026\u2020\u2021\u02C6\u2030\u0160\u2039\u0152\u017D\u2018\u2019\u201C\u201D\u2022\u2013\u2014\u02DC\u2122\u0161\u203A\u0153\u017E\u0178]*$", message = "{hppRequest.comment2.pattern}")
	@JsonProperty("COMMENT2")
	private String commentTwo;

	/**
	 * Used to signify whether or not you want a Transaction Suitability Score for this transaction. 
	 * Can be "0" for no and "1" for yes. 
	 */
	@Size(min = 0, max = 1, message = "{hppRequest.returnTss.size}")
	@Pattern(regexp = "^[01]*$", message = "{hppRequest.returnTss.pattern}")
	@JsonProperty("RETURN_TSS")
	private String returnTss;

	/**
	 * The postcode or ZIP of the shipping address.
	 */
	@Size(min = 0, max = 30, message = "{hppRequest.shippingCode.size}")
	@Pattern(regexp = "^[A-Za-z0-9\\,\\.\\-\\/\\| ]*$", message = "{hppRequest.shippingCode.pattern}")
	@JsonProperty("SHIPPING_CODE")
	private String shippingCode;

	/**
	 * The country of the shipping address.
	 */
	@Size(min = 0, max = 50, message = "{hppRequest.shippingCountry.size}")
	@Pattern(regexp = "^[A-Za-z0-9\\,\\.\\- ]*$", message = "{hppRequest.shippingCountry.pattern}")
	@JsonProperty("SHIPPING_CO")
	private String shippingCountry;

	/**
	 * The postcode or ZIP of the billing address.
	 */
	@Size(min = 0, max = 60, message = "{hppRequest.billingCode.size}")
	@Pattern(regexp = "^[A-Za-z0-9\\,\\.\\-\\/\\|\\* ]*$", message = "{hppRequest.billingCode.pattern}")
	@JsonProperty("BILLING_CODE")
	private String billingCode;

	/**
	 * The country of the billing address.
	 */
	@Size(min = 0, max = 50, message = "{hppRequest.billingCountry.size}")
	@Pattern(regexp = "^[A-Za-z0-9\\,\\.\\- ]*$", message = "{hppRequest.billingCountry.pattern}")
	@JsonProperty("BILLING_CO")
	private String billingCountry;

	/**
	 * The customer number of the customer. You can send in any additional information about the transaction in this field, 
	 * which will be visible under the transaction in the RealControl application.
	 */
	@Size(min = 0, max = 50, message = "{hppRequest.customerNumber.size}")
	@Pattern(regexp = "^[a-zA-Z0-9\\.\\_\\-\\,\\+\\@ \\s]*$", message = "{hppRequest.customerNumber.pattern}")
	@JsonProperty("CUST_NUM")
	private String customerNumber;

	/**
	 * A variable reference also associated with this customer. You can send in any additional information about the transaction in this field, 
	 * which will be visible under the transaction in the RealControl application.
	 */
	@Size(min = 0, max = 50, message = "{hppRequest.variableReference.size}")
	@Pattern(regexp = "^[a-zA-Z0-9\\.\\_\\-\\,\\+\\@ \\s]*$", message = "{hppRequest.variableReference.pattern}")
	@JsonProperty("VAR_REF")
	private String variableReference;

	/**
	 * A product id associated with this product. You can send in any additional information about the transaction in this field, 
	 * which will be visible under the transaction in the RealControl application.
	 */
	@Size(min = 0, max = 50, message = "{hppRequest.productId.size}")
	@Pattern(regexp = "^[a-zA-Z0-9\\.\\_\\-\\,\\+\\@ \\s]*$", message = "{hppRequest.productId.pattern}")
	@JsonProperty("PROD_ID")
	private String productId;

	/**
	 * Used to set what language HPP is displayed in. Currently HPP is available in English, Spanish and German, with other languages to follow. 
	 * If the field is not sent in, the default language is the language that is set in your account configuration. This can be set by your account manager.
	 */
	@Pattern(regexp = "^[a-zA-Z]{2}(_([a-zA-Z]{2}){1})?$|^$", message = "{hppRequest.language.pattern}")
	@JsonProperty("HPP_LANG")
	private String language;

	/**
	 * Used to set what text is displayed on the payment button for card transactions. If this field is not sent in, "Pay Now" is displayed on the button by default.
	 */
	@Size(min = 0, max = 25, message = "{hppRequest.cardPaymentButtonText.size}")
	@Pattern(regexp = "^[ÀÁÂÃÄÅÆÇÈÉÊËÌÍÎÏÐÑÒÓÔÕÖ×ØÙÚÛÜÝÞßàáâãäåæçèéêëìíîïðñòóôõö÷ø¤ùúûüýþÿ\u0152\u017D\u0161\u0153\u017E\u0178¥a-zA-Z0-9\\'\\,\"\\+\\.\\_\\-\\&\\/\\@\\!\\?\\%\\()\\*\\:\\£\\$\\&\\u20AC\\#\\[\\]\\|\\=\\\\\u201C\u201D\u201C ]*$", message = "{hppRequest.cardPaymentButtonText.pattern}")
	@JsonProperty("CARD_PAYMENT_BUTTON")
	private String cardPaymentButtonText;

	/**
	 * Enable card storage.
	 */
	@Size(min = 0, max = 1, message = "{hppRequest.cardStorageEnable.size}")
	@Pattern(regexp = "^[10]*$", message = "{hppRequest.cardStorageEnable.pattern}")
	@JsonProperty("CARD_STORAGE_ENABLE")
	private String cardStorageEnable;

	/**
	 * Offer to save the card.
	 */
	@Size(min = 0, max = 1, message = "{hppRequest.offerSaveCard.size}")
	@Pattern(regexp = "^[01]*$", message = "{hppRequest.offerSaveCard.pattern}")
	@JsonProperty("OFFER_SAVE_CARD")
	private String offerSaveCard;

	/**
	 * The payer reference.
	 */
	@Size(min = 0, max = 50, message = "{hppRequest.payerReference.size}")
	@Pattern(regexp = "^[A-Za-z0-9\\_\\-\\\\ ]*$", message = "{hppRequest.payerReference.pattern}")
	@JsonProperty("PAYER_REF")
	private String payerReference;

	/**
	 * The payment reference.
	 */
	@Size(min = 0, max = 50, message = "{hppRequest.paymentReference.size}")
	@Pattern(regexp = "^[A-Za-z0-9\\_\\-]*$", message = "{hppRequest.paymentReference.pattern}")
	@JsonProperty("PMT_REF")
	private String paymentReference;

	/**
	 * Flag to indicate if the payer exists. 
	 */
	@Size(min = 0, max = 1, message = "{hppRequest.payerExists.size}")
	@Pattern(regexp = "^[102]*$", message = "{hppRequest.payerExists.pattern}")
	@JsonProperty("PAYER_EXIST")
	private String payerExists;

	/**
	 * Supplementary data to be sent to Realex Payments. This will be returned in the HPP response. 
	 */
	private Map<String, String> supplementaryData = new HashMap<String, String>();

	/**
	 * Used to identify an OTB transaction.
	 */
	@Size(min = 0, max = 1, message = "{hppRequest.validateCardOnly.size}")
	@Pattern(regexp = "^[01]*$", message = "{hppRequest.validateCardOnly.pattern}")
	@JsonProperty("VALIDATE_CARD_ONLY")
	private String validateCardOnly;

	/**
	 * Transaction level configuration to enable/disable a DCC request. (Only if the merchant is configured).
	 */
	@Size(min = 0, max = 1, message = "{hppRequest.dccEnable.size}")
	@Pattern(regexp = "^[01]*$", message = "{hppRequest.dccEnable.pattern}")
	@JsonProperty("DCC_ENABLE")
	private String dccEnable;

	/**
	 * Override merchant configuration for fraud. (Only if the merchant is configured for fraud).
	 */
	@Size(min = 0, max = 7, message = "{hppRequest.hppFraudFilterMode.size}")
	@Pattern(regexp = "^(ACTIVE|PASSIVE|OFF)*$", message = "{hppRequest.hppFraudFilterMode.pattern}")
	@JsonProperty("HPP_FRAUDFILTER_MODE")
	private String hppFraudFilterMode;

	/**
	 * The HPP Version. To use HPP Card Management select HPP_VERSION = 2.
	 */
	@Size(min = 0, max = 1, message = "{hppRequest.hppVersion.size}")
	@Pattern(regexp = "^[1-2]*$", message = "{hppRequest.hppVersion.pattern}")
	@JsonProperty("HPP_VERSION")
	@JsonInclude(Include.NON_EMPTY)
	private String hppVersion;

	/**
	 * The payer reference. If this flag is received, HPP will retrieve a list of the payment methods saved for that payer.
	 */
	@Size(min = 0, max = 50, message = "{hppRequest.hppSelectStoredCard.size}")
	@Pattern(regexp = "^[a-zA-Z0-9\\_\\-.\\s]*$", message = "{hppRequest.hppSelectStoredCard.pattern}")
	@JsonProperty("HPP_SELECT_STORED_CARD")
	@JsonInclude(Include.NON_EMPTY)
	private String hppSelectStoredCard;

	/**
	 * The displayCvn field allows the merchant to not display the CVN field in the payment form
	 */
	@Pattern(regexp = "^(TRUE|FALSE|true|false)*$", message = "{hppRequest.hppDisplayCvn.pattern}")
	@JsonInclude(Include.NON_EMPTY)
	@JsonProperty("HPP_DISPLAY_CVN")
	private String displayCvn;

	/**
	 * The total amount to authorise if the card is a debit card
	 */
	@Size(min = 0, max = 11, message = "{hppRequest.amount.debit.size}")
	@Pattern(regexp = "^[0-9]*$", message = "{hppRequest.amount.debit.pattern}")
	@JsonInclude(Include.NON_EMPTY)
	@JsonProperty("HPP_AMOUNT_DEBIT")
	private String amountDebit;

	/**
	 * The total amount to authorise if the card is a credit card
	 */
	@Size(min = 0, max = 11, message = "{hppRequest.amount.credit.size}")
	@Pattern(regexp = "^[0-9]*$", message = "{hppRequest.amount.credit.pattern}")
	@JsonInclude(Include.NON_EMPTY)
	@JsonProperty("HPP_AMOUNT_CREDIT")
	private String amountCredit;

	/**
	 * The total amount to authorise if the card is a commercial card
	 */
	@Size(min = 0, max = 11, message = "{hppRequest.amount.commercial.size}")
	@Pattern(regexp = "^[0-9]*$", message = "{hppRequest.amount.commercial.pattern}")
	@JsonInclude(Include.NON_EMPTY)
	@JsonProperty("HPP_AMOUNT_COMMERCIAL")
	private String amountCommercial;

	/**
	 * Helper method to add HPP Version flag.
	 * 
	 * @param amountDebit
	 * @return HppRequest
	 */
	public HppRequest addAmountDebit(String amountDebit) {
		this.amountDebit = amountDebit;
		return this;
	}

	/**
	 * Helper method to add HPP Version flag.
	 * 
	 * @param amountCredit
	 * @return HppRequest
	 */
	public HppRequest addAmoutCredit(String amountCredit) {
		this.amountCredit = amountCredit;
		return this;
	}

	/**
	 * Helper method to add HPP Version flag.
	 * 
	 * @param amountCommercial
	 * @return HppRequest
	 */
	public HppRequest addAmoutCommercial(String amountCommercial) {
		this.amountCommercial = amountCommercial;
		return this;
	}

	/**
	 * Getter for displayCvn.
	 * 
	 * @return String
	 */
	public String getDisplayCvn() {
		return displayCvn;
	}

	/**
	 * Helper method to add displayCvn flag.
	 * 
	 * @param displayCvn the HppDisplayCvn field
	 * @return HppRequest
	 */
	public HppRequest addDisplayCvn(String displayCvn) {
		this.displayCvn = displayCvn;
		return this;
	}
	
	/**
	 * Getter for merchant ID.
	 * 
	 * @return String
	 */
	public String getMerchantId() {
		return merchantId;
	}

	/**
	 * Getter for account.
	 * 
	 * @return String
	 */
	public String getAccount() {
		return account;
	}

	/**
	 * Getter for order ID.
	 * 
	 * @return String
	 */
	public String getOrderId() {
		return orderId;
	}

	/**
	 * Getter for amount.
	 * 
	 * @return String
	 */
	public String getAmount() {
		return amount;
	}

	/**
	 * Getter for currency.
	 * 
	 * @return String
	 */
	public String getCurrency() {
		return currency;
	}

	/**
	 * Getter for time stamp.
	 * 
	 * @return String
	 */
	public String getTimeStamp() {
		return timeStamp;
	}

	/**
	 * Getter for hash.
	 * 
	 * @return String
	 */
	public String getHash() {
		return hash;
	}

	/**
	 * Getter for auto settle flag.
	 * 
	 * @return String
	 */
	public String getAutoSettleFlag() {
		return autoSettleFlag;
	}

	/**
	 * Getter for comment one.
	 * 
	 * @return String
	 */
	public String getCommentOne() {
		return commentOne;
	}

	/**
	 * Getter for comment two.
	 * 
	 * @return String
	 */
	public String getCommentTwo() {
		return commentTwo;
	}

	/**
	 * Getter for return TSS flag.
	 * 
	 * @return String
	 */
	public String getReturnTss() {
		return returnTss;
	}

	/**
	 * Getter for shipping code. 
	 * 
	 * @return String
	 */
	public String getShippingCode() {
		return shippingCode;
	}

	/**
	 * Getter for shipping country.
	 * 
	 * @return String
	 */
	public String getShippingCountry() {
		return shippingCountry;
	}

	/**
	 * Getter for billing code. 
	 * 
	 * @return String
	 */
	public String getBillingCode() {
		return billingCode;
	}

	/**
	 * Getter for billing country.
	 * 
	 * @return String
	 */
	public String getBillingCountry() {
		return billingCountry;
	}

	/**
	 * Getter for customer number.
	 * 
	 * @return String
	 */
	public String getCustomerNumber() {
		return customerNumber;
	}

	/**
	 * Getter for variable reference. 
	 * 
	 * @return String
	 */
	public String getVariableReference() {
		return variableReference;
	}

	/**
	 * Getter for product ID.
	 * 
	 * @return String
	 */
	public String getProductId() {
		return productId;
	}

	/**
	 * Getter for language.
	 * 
	 * @return String
	 */
	public String getLanguage() {
		return language;
	}

	/**
	 * Getter for card payment button text.
	 * 
	 * @return String
	 */
	public String getCardPaymentButtonText() {
		return cardPaymentButtonText;
	}

	/**
	 * Getter for card storage enable flag.
	 * 
	 * @return String
	 */
	public String getCardStorageEnable() {
		return cardStorageEnable;
	}

	/**
	 * Getter for offer to save card.
	 * 
	 * @return String
	 */
	public String getOfferSaveCard() {
		return offerSaveCard;
	}

	/**
	 * Getter for payer reference.
	 * 
	 * @return String
	 */
	public String getPayerReference() {
		return payerReference;
	}

	/**
	 * Getter for payment reference.
	 * 
	 * @return String
	 */
	public String getPaymentReference() {
		return paymentReference;
	}

	/**
	 * Getter for payer exists. 
	 * 
	 * @return String
	 */
	public String getPayerExists() {
		return payerExists;
	}

	/**
	 * Getter for validate card only. 
	 * 
	 * @return String
	 */
	public String getValidateCardOnly() {
		return validateCardOnly;
	}

	/**
	 * Getter for DCC enable flag.
	 * 
	 * @return String
	 */
	public String getDccEnable() {
		return dccEnable;
	}

	/**
	 * Getter for HPP fraud filter mode flag.
	 * 
	 * @return String
	 */
	public String getHppFraudFilterMode() {
		return hppFraudFilterMode;
	}

	/**
	 * Getter for amount Debit
	 * @return String
	 */
	public String getAmountDebit() {
		return amountDebit;
	}

	/**
	 * Setter for amount Debit
	 * @param amountDebit
	 */
	public void setAmountDebit(String amountDebit) {
		this.amountDebit = amountDebit;
	}

	/**
	 * Getter for amount Credit
	 * @return String
	 */
	public String getAmountCredit() {
		return amountCredit;
	}

	/**
	 * Setter for amount Credit
	 * @param amountCredit
	 */
	public void setAmountCredit(String amountCredit) {
		this.amountCredit = amountCredit;
	}

	/**
	 * Getter for amount commercial
	 * @return String
	 */
	public String getAmountCommercial() {
		return amountCommercial;
	}

	/**
	 * Setter for amount commercial
	 * @param amountCommercial
	 */
	public void setAmountCommercial(String amountCommercial) {
		this.amountCommercial = amountCommercial;
	}

	/**
	 * Setter for merchant ID.
	 * 
	 * @param merchantId
	 */
	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	/**
	 * Setter for account. 
	 * 
	 * @param account
	 */
	public void setAccount(String account) {
		this.account = account;
	}

	/**
	 * Setter for order ID.
	 * 
	 * @param orderId
	 */
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	/**
	 * Setter for amount. 
	 * 
	 * @param amount
	 */
	public void setAmount(String amount) {
		this.amount = amount;
	}

	/**
	 * Setter for currency.
	 * 
	 * @param currency
	 */
	public void setCurrency(String currency) {
		this.currency = currency;
	}

	/**
	 * Setter for time stamp.
	 * 
	 * @param timeStamp
	 */
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	/**
	 * Setter for hash.
	 * 
	 * @param hash
	 */
	public void setHash(String hash) {
		this.hash = hash;
	}

	/**
	 * Setter for auto settle flag.
	 * 
	 * @param autoSettleFlag
	 */
	public void setAutoSettleFlag(String autoSettleFlag) {
		this.autoSettleFlag = autoSettleFlag;
	}

	/**
	 * Setter for comment one. 
	 * 
	 * @param commentOne
	 */
	public void setCommentOne(String commentOne) {
		this.commentOne = commentOne;
	}

	/**
	 * Setter for comment two. 
	 * 
	 * @param commentTwo
	 */
	public void setCommentTwo(String commentTwo) {
		this.commentTwo = commentTwo;
	}

	/**
	 * Setter for return TSS.
	 * 
	 * @param returnTss
	 */
	public void setReturnTss(String returnTss) {
		this.returnTss = returnTss;
	}

	/**
	 * Setter for shipping code. 
	 * 
	 * @param shippingCode
	 */
	public void setShippingCode(String shippingCode) {
		this.shippingCode = shippingCode;
	}

	/**
	 * Setter for shipping country.
	 * 
	 * @param shippingCountry
	 */
	public void setShippingCountry(String shippingCountry) {
		this.shippingCountry = shippingCountry;
	}

	/**
	 * Setter for billing code. 
	 * 
	 * @param billingCode
	 */
	public void setBillingCode(String billingCode) {
		this.billingCode = billingCode;
	}

	/**
	 * Setter for billing country.
	 * 
	 * @param billingCountry
	 */
	public void setBillingCountry(String billingCountry) {
		this.billingCountry = billingCountry;
	}

	/**
	 * Setter for customer number. 
	 * 
	 * @param customerNumber
	 */
	public void setCustomerNumber(String customerNumber) {
		this.customerNumber = customerNumber;
	}

	/**
	 * Setter for variable reference. 
	 * 
	 * @param variableReference
	 */
	public void setVariableReference(String variableReference) {
		this.variableReference = variableReference;
	}

	/**
	 * Setter for product ID.
	 * 
	 * @param productId
	 */
	public void setProductId(String productId) {
		this.productId = productId;
	}

	/**
	 * Setter for language. 
	 * 
	 * @param language
	 */
	public void setLanguage(String language) {
		this.language = language;
	}

	/**
	 * Setter for card payment button text. 
	 * 
	 * @param cardPaymentButtonText
	 */
	public void setCardPaymentButtonText(String cardPaymentButtonText) {
		this.cardPaymentButtonText = cardPaymentButtonText;
	}

	/**
	 * Setter for card storage enable flag. 
	 * 
	 * @param cardStorageEnable
	 */
	public void setCardStorageEnable(String cardStorageEnable) {
		this.cardStorageEnable = cardStorageEnable;
	}

	/**
	 * Setter for offer to save card. 
	 * 
	 * @param offerSaveCard
	 */
	public void setOfferSaveCard(String offerSaveCard) {
		this.offerSaveCard = offerSaveCard;
	}

	/**
	 * Setter for payer reference. 
	 * 
	 * @param payerReference
	 */
	public void setPayerReference(String payerReference) {
		this.payerReference = payerReference;
	}

	/**
	 * Setter for payment reference.
	 * 
	 * @param paymentReference
	 */
	public void setPaymentReference(String paymentReference) {
		this.paymentReference = paymentReference;
	}

	/**
	 * Setter for payer exists. 
	 * 
	 * @param payerExists
	 */
	public void setPayerExists(String payerExists) {
		this.payerExists = payerExists;
	}

	/**
	 * Setter for validate card only.
	 * 
	 * @param validateCardOnly
	 */
	public void setValidateCardOnly(String validateCardOnly) {
		this.validateCardOnly = validateCardOnly;
	}

	/**
	 * Setter for DCC enable flag.
	 * 
	 * @param dccEnable
	 */
	public void setDccEnable(String dccEnable) {
		this.dccEnable = dccEnable;
	}

	/**
	 * Setter for HPP fraud filter mode flag.
	 * 
	 * @param hppFraudFilterMode
	 */
	public void setHppFraudFilterMode(String hppFraudFilterMode) {
		this.hppFraudFilterMode = hppFraudFilterMode;
	}

	/**
	 * Setter for displayCvn flag.
	 * 
	 * @param displayCvn
	 */
	public void setDisplayCvn(String displayCvn) {
		this.displayCvn = displayCvn;
	}
	
	/**
	 * Helper method to add merchant ID.
	 * 
	 * @param merchantId
	 * @return HppRequest
	 */
	public HppRequest addMerchantId(String merchantId) {
		this.merchantId = merchantId;
		return this;
	}

	/**
	 * Helper method to add account. 
	 * 
	 * @param account
	 * @return HppRequest
	 */
	public HppRequest addAccount(String account) {
		this.account = account;
		return this;
	}

	/**
	 * Helper method to add order ID.
	 * 
	 * @param orderId
	 * @return HppRequest
	 */
	public HppRequest addOrderId(String orderId) {
		this.orderId = orderId;
		return this;
	}

	/**
	 * Helper method to add amount.
	 * 
	 * @param amount
	 * @return HppRequest
	 */
	public HppRequest addAmount(long amount) {
		this.amount = String.valueOf(amount);
		return this;
	}

	/**
	 * Helper method to add amount.
	 * 
	 * @param amount
	 * @return HppRequest
	 */
	public HppRequest addAmount(String amount) {
		this.amount = amount;
		return this;
	}

	/**
	 * Helper method to add currency.
	 * 
	 * @param currency
	 * @return HppRequest
	 */
	public HppRequest addCurrency(String currency) {
		this.currency = currency;
		return this;
	}

	/**
	 * Helper method to add time stamp.
	 * 
	 * @param timeStamp
	 * @return HppRequest
	 */
	public HppRequest addTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
		return this;
	}

	/**
	 * Helper method to add hash.
	 * 
	 * @param hash
	 * @return HppRequest
	 */
	public HppRequest addHash(String hash) {
		this.hash = hash;
		return this;
	}

	/**
	 * Helper method to add autop settle flag.
	 * 
	 * @param autoSettleFlag
	 * @return HppRequest
	 */
	public HppRequest addAutoSettleFlag(boolean autoSettleFlag) {
		this.autoSettleFlag = autoSettleFlag ? Flag.TRUE.getFlag() : Flag.FALSE.getFlag();
		return this;
	}

	/**
	 * Helper method to add auto settle flag.
	 * 
	 * @param autoSettleFlag
	 * @return HppRequest
	 */
	public HppRequest addAutoSettleFlag(String autoSettleFlag) {
		this.autoSettleFlag = autoSettleFlag;
		return this;
	}

	/**
	 * Helper method to add comment one. 
	 * 
	 * @param commentOne
	 * @return HppRequest
	 */
	public HppRequest addCommentOne(String commentOne) {
		this.commentOne = commentOne;
		return this;
	}

	/**
	 * Helper method to add comment two.
	 * 
	 * @param commentTwo
	 * @return HppRequest
	 */
	public HppRequest addCommentTwo(String commentTwo) {
		this.commentTwo = commentTwo;
		return this;
	}

	/**
	 * Helper method to add return TSS.
	 * 
	 * @param returnTss
	 * @return HppRequest
	 */
	public HppRequest addReturnTss(boolean returnTss) {
		this.returnTss = returnTss ? Flag.TRUE.getFlag() : Flag.FALSE.getFlag();
		return this;
	}

	/**
	 * Helper method to add return TSS.
	 * 
	 * @param returnTss
	 * @return HppRequest
	 */
	public HppRequest addReturnTss(String returnTss) {
		this.returnTss = returnTss;
		return this;
	}

	/**
	 * Helper method to add shipping code. 
	 * 
	 * @param shippingCode
	 * @return HppRequest
	 */
	public HppRequest addShippingCode(String shippingCode) {
		this.shippingCode = shippingCode;
		return this;
	}

	/**
	 * Helper method to add hipping country.
	 * 
	 * @param shippingCountry
	 * @return HppRequest
	 */
	public HppRequest addShippingCountry(String shippingCountry) {
		this.shippingCountry = shippingCountry;
		return this;
	}

	/**
	 * Helper method to add billing code. 
	 * 
	 * @param billingCode
	 * @return HppRequest
	 */
	public HppRequest addBillingCode(String billingCode) {
		this.billingCode = billingCode;
		return this;
	}

	/**
	 * Helper method to add billing country.
	 * 
	 * @param billingCountry
	 * @return HppRequest
	 */
	public HppRequest addBillingCountry(String billingCountry) {
		this.billingCountry = billingCountry;
		return this;
	}

	/**
	 * Helper method to add customer number. 
	 * 
	 * @param customerNumber
	 * @return HppRequest
	 */
	public HppRequest addCustomerNumber(String customerNumber) {
		this.customerNumber = customerNumber;
		return this;
	}

	/**
	 * Helper method to add variable reference. 
	 * 
	 * @param variableReference
	 * @return HppRequest
	 */
	public HppRequest addVariableReference(String variableReference) {
		this.variableReference = variableReference;
		return this;
	}

	/**
	 * Helper method to add product ID.
	 * 
	 * @param productId
	 * @return HppRequest
	 */
	public HppRequest addProductId(String productId) {
		this.productId = productId;
		return this;
	}

	/**
	 * Helper method to add language.
	 * 
	 * @param language
	 * @return HppRequest
	 */
	public HppRequest addLanguage(String language) {
		this.language = language;
		return this;
	}

	/**
	 * Helper method to add card payment button text.
	 * 
	 * @param cardPaymentButtonText
	 * @return HppRequest
	 */
	public HppRequest addCardPaymentButtonText(String cardPaymentButtonText) {
		this.cardPaymentButtonText = cardPaymentButtonText;
		return this;
	}

	/**
	 * Helper method to add card storage enable flag.
	 * 
	 * @param cardStorageEnable
	 * @return HppRequest
	 */
	public HppRequest addCardStorageEnable(boolean cardStorageEnable) {
		this.cardStorageEnable = cardStorageEnable ? Flag.TRUE.getFlag() : Flag.FALSE.getFlag();
		return this;
	}

	/**
	 * Helper method to add card storage enable flag.
	 * 
	 * @param cardStorageEnable
	 * @return HppRequest
	 */
	public HppRequest addCardStorageEnable(String cardStorageEnable) {
		this.cardStorageEnable = cardStorageEnable;
		return this;
	}

	/**
	 * Helper method to add offer to save card.
	 * 
	 * @param offerSaveCard
	 * @return HppRequest
	 */
	public HppRequest addOfferSaveCard(boolean offerSaveCard) {
		this.offerSaveCard = offerSaveCard ? Flag.TRUE.getFlag() : Flag.FALSE.getFlag();
		return this;
	}

	/**
	 * Helper method to add offer to save card.
	 * 
	 * @param offerSaveCard
	 * @return HppRequest
	 */
	public HppRequest addOfferSaveCard(String offerSaveCard) {
		this.offerSaveCard = offerSaveCard;
		return this;
	}

	/**
	 * Helper method to add payer reference.
	 * 
	 * @param payerReference
	 * @return HppRequest
	 */
	public HppRequest addPayerReference(String payerReference) {
		this.payerReference = payerReference;
		return this;
	}

	/**
	 * Helper method to add payment reference.
	 * 
	 * @param paymentReference
	 * @return HppRequest
	 */
	public HppRequest addPaymentReference(String paymentReference) {
		this.paymentReference = paymentReference;
		return this;
	}

	/**
	 * Helper method to add payer exists flag.
	 * 
	 * @param payerExists
	 * @return HppRequest
	 */
	public HppRequest addPayerExists(boolean payerExists) {
		this.payerExists = payerExists ? Flag.TRUE.getFlag() : Flag.FALSE.getFlag();
		return this;
	}

	/**
	 * Helper method to add payer exists flag.
	 * 
	 * @param payerExists
	 * @return HppRequest
	 */
	public HppRequest addPayerExists(String payerExists) {
		this.payerExists = payerExists;
		return this;
	}

	/**
	 * Helper method to get supplementary data.
	 * 
	 * @return Map<String, String>
	 */
	@JsonAnyGetter
	public Map<String, String> getSupplementaryData() {
		return supplementaryData;
	}

	/**
	 * Setter for supplementary data.
	 * 
	 * @param supplementaryData
	 */
	public void setSupplementaryData(Map<String, String> supplementaryData) {
		this.supplementaryData = supplementaryData;
	}

	/**
	 * Helper method to add supplementary data.
	 * 
	 * @param name
	 * @param value
	 * @return HppRequest
	 */
	@JsonAnySetter
	public HppRequest addSupplementaryDataValue(String name, String value) {
		supplementaryData.put(name, value);
		return this;
	}

	/**
	 * Helper method to add validate card only flag.
	 * 
	 * @param validateCardOnly
	 * @return HppRequest
	 */
	public HppRequest addValidateCardOnly(boolean validateCardOnly) {
		this.validateCardOnly = validateCardOnly ? Flag.TRUE.getFlag() : Flag.FALSE.getFlag();
		return this;
	}

	/**
	 * Helper method to add validate card only flag.
	 * 
	 * @param validateCardOnly
	 * @return HppRequest
	 */
	public HppRequest addValidateCardOnly(String validateCardOnly) {
		this.validateCardOnly = validateCardOnly;
		return this;
	}

	/**
	 * Helper method to add DCC enable flag.
	 * 
	 * @param dccEnable
	 * @return HppRequest
	 */
	public HppRequest addDccEnable(boolean dccEnable) {
		this.dccEnable = dccEnable ? Flag.TRUE.getFlag() : Flag.FALSE.getFlag();
		return this;
	}

	/**
	 * Helper method to add DCC enable flag.
	 * 
	 * @param dccEnable
	 * @return HppRequest
	 */
	public HppRequest addDccEnable(String dccEnable) {
		this.dccEnable = dccEnable;
		return this;
	}

	/**
	 * Helper method to add HPP fraud filter mode flag.
	 * 
	 * @param hppFraudFilterMode
	 * @return HppRequest
	 */
	public HppRequest addHppFraudFilterMode(String hppFraudFilterMode) {
		this.hppFraudFilterMode = hppFraudFilterMode;
		return this;
	}

	/**
	 * Helper method to add HPP Version flag.
	 * 
	 * @param hppVersion
	 * @return HppRequest
	 */
	public HppRequest addHppVersion(String hppVersion) {
		this.hppVersion = hppVersion;
		return this;
	}

	/**
	 * Helper method to add HPP Select stored card.
	 * 
	 * @param hppSelectStoredCard
	 * @return HppRequest
	 */
	public HppRequest addHppSelectStoredCard(String hppSelectStoredCard) {
		this.hppSelectStoredCard = hppSelectStoredCard;
		return this;
	}

	/**
	 * Get hppSelectStoredCard
	 * 
	 * @return String hppSelectStoredCard
	 */
	public String getHppSelectStoredCard() {
		return hppSelectStoredCard;
	}

	/**
	 * Set hppSelectStoredCard
	 * @param String hppSelectStoredCard
	 */
	public void setHppSelectStoredCard(String hppSelectStoredCard) {
		this.hppSelectStoredCard = hppSelectStoredCard;
	}

	/**
	 * Creates the security hash from a number of fields and the shared secret. 
	 * 
	 * @param secret
	 * @return HppRequest
	 */
	public HppRequest hash(String secret) {

		// Override payerRef with hppSelectStoredCard if present.
		if (this.hppSelectStoredCard != null && !"".equalsIgnoreCase(this.hppSelectStoredCard)) {
			this.payerReference = this.hppSelectStoredCard;
		}

		//check for any null values and set them to empty string for hashing
		String timeStamp = null == this.timeStamp ? "" : this.timeStamp;
		String merchantId = null == this.merchantId ? "" : this.merchantId;
		String orderId = null == this.orderId ? "" : this.orderId;
		String amount = null == this.amount ? "" : this.amount;
		String currency = null == this.currency ? "" : this.currency;
		String payerReference = null == this.payerReference ? "" : this.payerReference;
		String paymentReference = null == this.paymentReference ? "" : this.paymentReference;
		String hppFraudFilterMode = null == this.hppFraudFilterMode ? "" : this.hppFraudFilterMode;

		String displayCvn = null == this.displayCvn ? "" : this.displayCvn;
		String amountDebit = null == this.amountDebit ? "" : this.amountDebit;
		String amountCredit = null == this.amountCredit ? "" : this.amountCredit;
		String amountCommercial = null == this.amountCommercial ? "" : this.amountCommercial;

		//create String to hash. Check for card storage enable flag to determine if Real Vault transaction 
		StringBuilder toHash = new StringBuilder();

		toHash.append(timeStamp).append(".").append(merchantId).append(".").append(orderId).append(".").append(amount);

		if (!amountDebit.equals("")) {
			toHash.append(".").append(this.amountDebit);
		}

		if (!amountCredit.equals("")) {
			toHash.append(".").append(this.amountCredit);
		}

		if (!amountCommercial.equals("")) {
			toHash.append(".").append(this.amountCommercial);
		}

		toHash.append(".").append(currency);

		if (Flag.TRUE.getFlag().equals(cardStorageEnable) || (hppSelectStoredCard != null && !hppSelectStoredCard.isEmpty())) {
			toHash.append(".").append(payerReference).append(".").append(paymentReference);
		}

		if (!hppFraudFilterMode.equals("")) {
			toHash.append(".").append(this.hppFraudFilterMode);
		}

		if (!displayCvn.equals("")) {
			toHash.append(".").append(this.displayCvn);
		}

		this.hash = GenerationUtils.generateHash(toHash.toString(), secret);

		return this;
	}

	
	/**
	 * Generates default values for fields such as hash, timestamp and order ID.
	 * 
	 * @param secret
	 * @return HppRequest
	 */
	public HppRequest generateDefaults(String secret) {

		//generate timestamp if not set
		if (null == this.timeStamp || "".equals(this.timeStamp)) {
			this.timeStamp = GenerationUtils.generateTimestamp();
		}

		//generate order ID if not set
		if (null == this.orderId || "".equals(this.orderId)) {
			this.orderId = GenerationUtils.generateOrderId();
		}

		//generate hash
		hash(secret);

		return this;
	}

	/**
	 * Base64 encodes all Hpp Request values.
	 * 
	 * @param charset
	 * @return HppRequest
	 * @throws UnsupportedEncodingException 
	 */
	public HppRequest encode(String charset) throws UnsupportedEncodingException {

		if (null != this.account) {
			this.account = new String(Base64.encodeBase64(this.account.getBytes(charset)), charset);
		}
		if (null != this.amount) {
			this.amount = new String(Base64.encodeBase64(this.amount.getBytes(charset)), charset);
		}
		if (null != this.autoSettleFlag) {
			this.autoSettleFlag = new String(Base64.encodeBase64(this.autoSettleFlag.getBytes(charset)), charset);
		}
		if (null != this.billingCode) {
			this.billingCode = new String(Base64.encodeBase64(this.billingCode.getBytes(charset)), charset);
		}
		if (null != this.billingCountry) {
			this.billingCountry = new String(Base64.encodeBase64(this.billingCountry.getBytes(charset)), charset);
		}
		if (null != this.cardPaymentButtonText) {
			this.cardPaymentButtonText = new String(Base64.encodeBase64(this.cardPaymentButtonText.getBytes(charset)), charset);
		}
		if (null != this.cardStorageEnable) {
			this.cardStorageEnable = new String(Base64.encodeBase64(this.cardStorageEnable.getBytes(charset)), charset);
		}
		if (null != this.commentOne) {
			this.commentOne = new String(Base64.encodeBase64(this.commentOne.getBytes(charset)), charset);
		}
		if (null != this.commentTwo) {
			this.commentTwo = new String(Base64.encodeBase64(this.commentTwo.getBytes(charset)), charset);
		}
		if (null != this.currency) {
			this.currency = new String(Base64.encodeBase64(this.currency.getBytes(charset)), charset);
		}
		if (null != this.customerNumber) {
			this.customerNumber = new String(Base64.encodeBase64(this.customerNumber.getBytes(charset)), charset);
		}
		if (null != this.hash) {
			this.hash = new String(Base64.encodeBase64(this.hash.getBytes(charset)), charset);
		}
		if (null != this.language) {
			this.language = new String(Base64.encodeBase64(this.language.getBytes(charset)), charset);
		}
		if (null != this.merchantId) {
			this.merchantId = new String(Base64.encodeBase64(this.merchantId.getBytes(charset)), charset);
		}
		if (null != this.offerSaveCard) {
			this.offerSaveCard = new String(Base64.encodeBase64(this.offerSaveCard.getBytes(charset)), charset);
		}
		if (null != this.orderId) {
			this.orderId = new String(Base64.encodeBase64(this.orderId.getBytes(charset)), charset);
		}
		if (null != this.payerExists) {
			this.payerExists = new String(Base64.encodeBase64(this.payerExists.getBytes(charset)), charset);
		}
		if (null != this.payerReference) {
			this.payerReference = new String(Base64.encodeBase64(this.payerReference.getBytes(charset)), charset);
		}
		if (null != this.paymentReference) {
			this.paymentReference = new String(Base64.encodeBase64(this.paymentReference.getBytes(charset)), charset);
		}
		if (null != this.productId) {
			this.productId = new String(Base64.encodeBase64(this.productId.getBytes(charset)), charset);
		}
		if (null != this.returnTss) {
			this.returnTss = new String(Base64.encodeBase64(this.returnTss.getBytes(charset)), charset);
		}
		if (null != this.shippingCode) {
			this.shippingCode = new String(Base64.encodeBase64(this.shippingCode.getBytes(charset)), charset);
		}
		if (null != this.shippingCountry) {
			this.shippingCountry = new String(Base64.encodeBase64(this.shippingCountry.getBytes(charset)), charset);
		}
		if (null != this.timeStamp) {
			this.timeStamp = new String(Base64.encodeBase64(this.timeStamp.getBytes(charset)), charset);
		}
		if (null != this.variableReference) {
			this.variableReference = new String(Base64.encodeBase64(this.variableReference.getBytes(charset)));
		}

		if (null != this.supplementaryData) {
			Map<String, String> supplementaryDataMap = new HashMap<String, String>();
			for (String key : supplementaryData.keySet()) {
				supplementaryDataMap.put(key, new String(Base64.encodeBase64(supplementaryData.get(key).getBytes(charset)), charset));
			}
			this.supplementaryData.putAll(supplementaryDataMap);
		}
		if (null != this.validateCardOnly) {
			this.validateCardOnly = new String(Base64.encodeBase64(this.validateCardOnly.getBytes(charset)));
		}
		if (null != this.dccEnable) {
			this.dccEnable = new String(Base64.encodeBase64(this.dccEnable.getBytes(charset)));
		}
		if (null != this.hppFraudFilterMode) {
			this.hppFraudFilterMode = new String(Base64.encodeBase64(this.hppFraudFilterMode.getBytes(charset)));
		}
		if (null != this.hppVersion) {
			this.hppVersion = new String(Base64.encodeBase64(this.hppVersion.getBytes(charset)));
		}
		if (null != this.hppSelectStoredCard) {
			this.hppSelectStoredCard = new String(Base64.encodeBase64(this.hppSelectStoredCard.getBytes(charset)));
		}
		if (null != this.displayCvn) {
			this.displayCvn = new String(Base64.encodeBase64(this.displayCvn.getBytes(charset)));
		}
		if (null != this.amountDebit) {
			this.amountDebit = new String(Base64.encodeBase64(this.amountDebit.getBytes(charset)));
		}
		if (null != this.amountCredit) {
			this.amountCredit = new String(Base64.encodeBase64(this.amountCredit.getBytes(charset)));
		}
		if (null != this.amountCommercial) {
			this.amountCommercial = new String(Base64.encodeBase64(this.amountCommercial.getBytes(charset)));
		}
		return this;
	}

	/**
	 * Base64 decodes all Hpp Request values.
	 * 
	 * @param charset
	 * @return HppRequest
	 * @throws UnsupportedEncodingException 
	 */
	public HppRequest decode(String charset) throws UnsupportedEncodingException {

		if (null != this.account) {
			this.account = new String(Base64.decodeBase64(this.account.getBytes(charset)), charset);
		}
		if (null != this.amount) {
			this.amount = new String(Base64.decodeBase64(this.amount.getBytes(charset)), charset);
		}
		if (null != this.autoSettleFlag) {
			this.autoSettleFlag = new String(Base64.decodeBase64(this.autoSettleFlag.getBytes(charset)), charset);
		}
		if (null != this.billingCode) {
			this.billingCode = new String(Base64.decodeBase64(this.billingCode.getBytes(charset)), charset);
		}
		if (null != this.billingCountry) {
			this.billingCountry = new String(Base64.decodeBase64(this.billingCountry.getBytes(charset)), charset);
		}
		if (null != this.cardPaymentButtonText) {
			this.cardPaymentButtonText = new String(Base64.decodeBase64(this.cardPaymentButtonText.getBytes(charset)), charset);
		}
		if (null != this.cardStorageEnable) {
			this.cardStorageEnable = new String(Base64.decodeBase64(this.cardStorageEnable.getBytes(charset)), charset);
		}
		if (null != this.commentOne) {
			this.commentOne = new String(Base64.decodeBase64(this.commentOne.getBytes(charset)), charset);
		}
		if (null != this.commentTwo) {
			this.commentTwo = new String(Base64.decodeBase64(this.commentTwo.getBytes(charset)), charset);
		}
		if (null != this.currency) {
			this.currency = new String(Base64.decodeBase64(this.currency.getBytes(charset)), charset);
		}
		if (null != this.customerNumber) {
			this.customerNumber = new String(Base64.decodeBase64(this.customerNumber.getBytes(charset)), charset);
		}
		if (null != this.hash) {
			this.hash = new String(Base64.decodeBase64(this.hash.getBytes(charset)), charset);
		}
		if (null != this.language) {
			this.language = new String(Base64.decodeBase64(this.language.getBytes(charset)), charset);
		}
		if (null != this.merchantId) {
			this.merchantId = new String(Base64.decodeBase64(this.merchantId.getBytes(charset)), charset);
		}
		if (null != this.offerSaveCard) {
			this.offerSaveCard = new String(Base64.decodeBase64(this.offerSaveCard.getBytes(charset)), charset);
		}
		if (null != this.orderId) {
			this.orderId = new String(Base64.decodeBase64(this.orderId.getBytes(charset)), charset);
		}
		if (null != this.payerExists) {
			this.payerExists = new String(Base64.decodeBase64(this.payerExists.getBytes(charset)), charset);
		}
		if (null != this.payerReference) {
			this.payerReference = new String(Base64.decodeBase64(this.payerReference.getBytes(charset)), charset);
		}
		if (null != this.paymentReference) {
			this.paymentReference = new String(Base64.decodeBase64(this.paymentReference.getBytes(charset)), charset);
		}
		if (null != this.productId) {
			this.productId = new String(Base64.decodeBase64(this.productId.getBytes(charset)), charset);
		}
		if (null != this.returnTss) {
			this.returnTss = new String(Base64.decodeBase64(this.returnTss.getBytes(charset)), charset);
		}
		if (null != this.shippingCode) {
			this.shippingCode = new String(Base64.decodeBase64(this.shippingCode.getBytes(charset)), charset);
		}
		if (null != this.shippingCountry) {
			this.shippingCountry = new String(Base64.decodeBase64(this.shippingCountry.getBytes(charset)), charset);
		}
		if (null != this.timeStamp) {
			this.timeStamp = new String(Base64.decodeBase64(this.timeStamp.getBytes(charset)), charset);
		}
		if (null != this.variableReference) {
			this.variableReference = new String(Base64.decodeBase64(this.variableReference.getBytes(charset)));
		}

		if (null != this.supplementaryData) {
			Map<String, String> supplementaryDataMap = new HashMap<String, String>();
			for (String key : supplementaryData.keySet()) {
				supplementaryDataMap.put(key, new String(Base64.decodeBase64(supplementaryData.get(key).getBytes(charset)), charset));
			}
			this.supplementaryData.putAll(supplementaryDataMap);
		}
		if (null != this.validateCardOnly) {
			this.validateCardOnly = new String(Base64.decodeBase64(this.validateCardOnly.getBytes(charset)));
		}
		if (null != this.dccEnable) {
			this.dccEnable = new String(Base64.decodeBase64(this.dccEnable.getBytes(charset)));
		}
		if (null != this.hppFraudFilterMode) {
			this.hppFraudFilterMode = new String(Base64.decodeBase64(this.hppFraudFilterMode.getBytes(charset)));
		}
		if (null != this.hppVersion) {
			this.hppVersion = new String(Base64.decodeBase64(this.hppVersion.getBytes(charset)));
		}
		if (null != this.hppSelectStoredCard) {
			this.hppSelectStoredCard = new String(Base64.decodeBase64(this.hppSelectStoredCard.getBytes(charset)));
		}
		if (null != this.displayCvn) {
			this.displayCvn = new String(Base64.decodeBase64(this.displayCvn.getBytes(charset)));
		}
		if (null != this.amountDebit) {
			this.amountDebit = new String(Base64.decodeBase64(this.amountDebit.getBytes(charset)));
		}
		if (null != this.amountCredit) {
			this.amountCredit = new String(Base64.decodeBase64(this.amountCredit.getBytes(charset)));
		}
		if (null != this.amountCommercial) {
			this.amountCommercial = new String(Base64.decodeBase64(this.amountCommercial.getBytes(charset)));
		}
		return this;
	}

}
