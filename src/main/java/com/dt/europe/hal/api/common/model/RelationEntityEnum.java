package com.dt.europe.hal.api.common.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum RelationEntityEnum {
  EXAMPLE("example"),
  REQUEST_EXAMPLE("requestExample"),
  HEADER("header"),
  // modifyOn
  TIME_PERIOD("timePeriod"),
  CHARACTERISTIC("characteristic"),
  VERSION_REFERENCE("versionReference"),
  RELATED_PARTY_REF("relatedPartyRef"),
  RELATED_ENTITY("relatedEntity"),
  RELATED_OBJECT("relatedObject"),
  DOCUMENT_RELATIONSHIP("documentRelationship"),
  DOCUMENT("document"),
  DOCUMENT_SPECIFICATION("documentSpecification"),
  CHANNEL("channel"),
  SPECIFICATION_CHARACTERISTIC("specificationCharacteristic"),
  SPECIFICATION_CHARACTERISTIC_VALUE("specificationCharacteristicValue"),
  ATTACHMENT("attachment"),
  TROUBLE_TICKET("troubleTicket"),
  TICKET_TRACKING("ticketTracking"),
  TICKET_RELATIONSHIP("ticketRelationship"),
  NOTE("note"),
  TROUBLE_TICKET_EVENT("troubleTicketEvent"),
  NOTIFICATION_EVENT("notificationEvent"),
  TROUBLE_TICKET_SPECIFICATION("troubleTicketSpecification"),
  DOCUMENT_REF("documentRef"),
  EVENT_SUBSCRIPTION("eventSubscription"),
  PARTY_PRIVACY_PROFILE("partyPrivacyProfile"),
  REFERENCE("reference"),
  PARTY_PRIVACY_PROFILE_TYPE("partyPrivacyProfileType"),
  PARTY_PRIVACY_PROFILE_CHARACTERISTIC("partyPrivacyProfileCharacteristic"),
  CHAR_VALUE("charValue"),
  PARTY_PRIVACY_PROFILE_TYPE_RELATION("partyPrivacyProfileTypeRelation"),
  AUDIT("audit"),
  EXTERNAL_DOCUMENTATION("externalDocumentation"),
  PARTY_PRIVACY_PROFILE_TYPE_CHARACTERISTIC("partyPrivacyProfileTypeCharacteristic"),
  ADDRESS("address"),
  ADDRESS_ROLE_REFERENCE("addressRoleReference"),
  RECURRING_PRICE("recurringPrice"),
  CONDITIONS_MISSING_REASON("conditionsMissingReason"),
  CREDIT_SCORING_RESULT("creditScoringResult"),
  GEO_CODE("geoCode"),
  PRODUCT_OFFERING_PRICE("productOfferingPrice"),
  PRODUCT_OFFERING_PRICE_CONFIG_REF("productOfferingPriceConfigRef"),
  CONTACT_ROLE_REFERENCE("contactRoleReference"),
  VALIDITY_REFERENCE("validityReference"),
  UNAVAILABILITY_REASON("unavailabilityReason"),
  PRODUCT_SPECIFICATION_CHARACTERISTIC_VALUE("productSpecificationCharacteristicValue"),
  PRODUCT_OFFERING_PRICE_ALTERATION("productOfferingPriceAlteration"),
  CART_TERM_CHARACTERISTIC("cartTermCharacteristic"),
  PRODUCT_ORDER("productOrder"),
  ORDER_ITEM_VALIDATION_STATUS("orderItemValidationStatus"),
  UNIT_OF_MEASURE("unitOfMeasure"),
  PRODUCT_CONFIG_REF("productConfigRef"),
  POINT("point"),
  SUB_ADDRESS("subAddress"),
  ALTERNATE_PRODUCT_OFFERING_PROPOSAL("alternateProductOfferingProposal"),
  VALIDATION_RULE("validationRule"),
  CONTACT("contact"),
  CART_TERM("cartTerm"),
  PRICE("price"),
  STATE_CHANGE("stateChange"),
  MEDIUM("medium"),
  ORDER_ITEM("orderItem"),
  PRICE_CONDITION("priceCondition"),
  ORDER_VALIDATION_STATUS("orderValidationStatus"),
  PAYMENT_METHOD_REFERENCE("paymentMethodReference"),
  ORDER_ITEM_RELATIONSHIP("orderItemRelationship"),
  PRODUCT_OFFERING_CONFIG_REF("productOfferingConfigRef"),
  PRODUCT_SPECIFICATION_CHARACTERISTIC("productSpecificationCharacteristic"),
  UPFRONT_PRICE("upfrontPrice"),
  LOCATION("location"),
  SERVICE_SPECIFICATION("serviceSpecification"),
  RESOURCE_RELATIONSHIP("resourceRelationship"),
  DAY_PERIOD("dayPeriod"),
  DEVICE("device"),
  RESOURCE("resource"),
  SERVICE("service"),
  SUPPORTING_RESOURCE("supportingResource"),
  RESOURCE_SPECIFICATION("resourceSpecification"),
  SUPPORTING_SERVICE("supportingService"),
  RECURRING_PERIOD("recurringPeriod"),
  RELATED_PARTY("relatedParty"),
  RESOURCE_REF("resourceRef"),
  RELATED_PRODUCT("relatedProduct"),
  SERVICE_RELATIONSHIP("serviceRelationship"),
  SERVICE_REF("serviceRef"),
  QUALIFICATION_ITEM_RELATIONSHIP("qualificationItemRelationship"),
  PRODUCT_OFFERING_QUALIFICATION("productOfferingQualification"),
  CREDIT_SCORING_QUALIFICATION("creditScoringQualification"),
  CREDIT_SCORING_QUALIFICATION_ITEM("creditScoringQualificationItem"),
  PRODUCT_OFFERING_QUALIFICATION_ITEM("productOfferingQualificationItem"),
  TERMINATION_ERROR("terminationError"),
  CATEGORY_CONFIG_REF("categoryConfigRef"),
  CART_ITEM("cartItem"),
  CART_ITEM_RELATIONSHIP("cartItemRelationship"),
  PRODUCT_OFFERING_QUALIFICATION_CONTEXT("productOfferingQualificationContext"),
  VALIDATION_CONTEXT("validationContext"),
  PAYMENT_METHOD_TYPE_REFERENCE("paymentMethodTypeReference"),
  CART_ITEM_VALIDATION_STATUS("cartItemValidationStatus"),
  RESERVATION("reservation"),
  RESERVATION_ITEM("reservationItem"),
  CAPACITY("capacity"),
  QUANTITY_TYPE("quantityType"),
  CAPACITY_REF("capacityRef"),
  RESOURCE_POOL("resourcePool"),
  APPLICABLE_TIME_PERIOD("applicableTimePeriod"),
  PRODUCT_SPECIFICATION_RELATION("productSpecificationRelation"),
  OFFERING_ACTION("offeringAction"),
  PRODUCT_OFFERING_CONTEXT("productOfferingContext"),
  DURATION("duration"),
  CATALOG("catalog"),
  PRODUCT_OFFERING_TERM("productOfferingTerm"),
  CATEGORY_CHARACTERISTIC_VALUE("categoryCharacteristicValue"),
  PRODUCT_OFFERING_RELATION("productOfferingRelation"),
  OFFERING_ACTION_RELATION("offeringActionRelation"),
  DEPENDENT_ACTION("dependentActions"),
  CATEGORY("category"),
  CONDITION_VALUE("conditionValue"),
  CONDITION("condition"),
  PRODUCT_SPECIFICATION("productSpecification"),
  PRODUCT_OFFERING("productOffering"),
  CATEGORY_CHARACTERISTIC("categoryCharacteristic"),
  ORDER_REF_TYPE("orderRefType"),
  CHECKPOINT_TYPE("checkpointType"),
  CUSTOMER_REF_TYPE("customerRefType"),
  SHIPMENT_TRACKING("shipmentTracking"),
  ADDRESS_SPECIFICATION("addressSpecification"),
  PRODUCT_OFFERING_REF("productOfferingRef"),
  AGREEMENT_REF("agreementRef"),
  QUOTE_ITEM("quoteItem"),
  PRODUCT("product"),
  CHANNEL_REF("channelRef"),
  AUTHORIZATION("authorization"),
  PRODUCT_RELATIONSHIP("productRelationship"),
  QUOTE_PRICE_ALTERATION("quotePriceAlteration"),
  ADDRESS_SPEC_CHARACTERISTIC_VALUE("addressSpecCharacteristicValue"),
  QUOTE_PRICE("quotePrice"),
  BILLING_ACCOUNT_REF("billingAccountRef"),
  ADDRESS_SPEC_CHARACTERISTIC("addressSpecCharacteristic"),
  QUOTE("quote"),
  QUOTE_ITEM_RELATIONSHIP("quoteItemRelationship"),
  PLACE("place"),
  PRODUCT_SPECIFICATION_REF("productSpecificationRef"),
  RECOMMENDATION_ITEM("recommendationItem"),
  RECOMMENDATION("recommendation"),
  SERVICE_ACCOUNT("serviceAccount"),
  BILL_PRESENTATION_MEDIA("billPresentationMedia"),
  CUSTOMER("customer"),
  BILLING_CYCLE_SPECIFICATION("billingCycleSpecification"),
  SUSPENSION_REASON("suspensionReason"),
  NOTIFICATION_GROUP("notificationGroup"),
  NOTIFICATION("notification"),
  CODEBOOK("codebook"),
  ROLE_REFERENCE("roleReference"),
  BILL_DESTINATION("billDestination"),
  BILLING_ACCOUNT("billingAccount"),
  CURRENCY("currency"),
  BILL_FORMAT("billFormat"),
  BILLING_ACCOUNT_BALANCE("billingAccountBalance"),
  BILL_DELIVERY("billDelivery"),
  BILL_STRUCTURE("billStructure"),
  CUSTOMER_ACCOUNT("customerAccount"),
  CUSTOMER_TYPE("customerType"),
  HOUSEHOLD_ACCOUNT("householdAccount"),
  ACCOUNT_REFERENCE("accountReference"),
  CUSTOMER_ACCOUNT_TAX_EXEMPTION("customerAccountTaxExemption"),
  CUSTOMER_CREDIT_PROFILE("customerCreditProfile"),
  PARTY_SPECIFICATION("partySpecification"),
  PARTY_SPEC_CHARACTERISTIC_VALUE("partySpecCharacteristicValue"),
  OTHER_INDIVIDUAL_NAME("otherIndividualName"),
  INDIVIDUAL("individual"),
  PARTY_SPEC_CHARACTERISTIC("partySpecCharacteristic"),
  OTHER_ORGANIZATION_NAME("otherOrganizationName"),
  PARTY_ROLE_REFERENCE("partyRoleReference"),
  ORGANIZATION("organization"),
  IDENTIFICATION("identification"),
  ORGANIZATION_RELATIONSHIP("organizationRelationship"),
  DISABILITY("disability"),
  PARTY("party"),
  EXTERNAL_REFERENCE("externalReference"),
  AGREEMENT_ITEM("agreementItem"),
  PRODUCT_REFERENCE("productReference"),
  AGREEMENT_TERM_OR_CONDITION("agreementTermOrCondition"),
  PRODUCT_PRICE("productPrice"),
  PRODUCT_TO_ACCOUNT_REFERENCE("productToAccountReference"),
  AGREEMENT("agreement"),
  MANAGEABLE_ASSET_ACCOUNT_REF("manageableAssetAccountRef"),
  CREDENTIAL("credential"),
  CONTACT_MEDIUM("contactMedium"),
  PROFILE("profile"),
  PARTY_TO_PARTY_REFERENCE("partyToPartyReference"),
  PRODUCT_OFFERING_GROUP("productOfferingGroup"),
  PRODUCT_OFFERING_GROUP_REF("productOfferingGroupRef"),
  T_CUSTOMER_REF("manageableAssetCustomerRef"),
  INFO("info"),
  INDIVIDUAL_IDENTIFICATION("individualIdentification"),
  CONSUMPTION_COUNTER("consumptionCounter"),
  BUCKET("bucket"),
  USAGE_CONSUMPTION_REPORT("usageConsumptionReport"),
  PRODUCT_REF("productRef"),
  LIMIT("limit"),
  BALANCE("balance"),
  INVOLVEMENT_IDENTIFICATION_REF("involvementIdentificationRef"),
  ROLE_REF("roleRef"),
  MANAGED_ENTITY_REF("managedEntityRef"),
  PERMISSION("permission"),
  ASSET_INVOLVEMENT_ROLE("assetInvolvementRole"),
  PRIVILEGE("privilege"),
  USAGE_SPEC_CHARACTERISTIC_VALUE("usageSpecCharacteristicValue"),
  USAGE("usage"),
  USAGE_SPEC_CHARACTERISTIC("usageSpecCharacteristic"),
  RATED_PRODUCT_USAGE("ratedProductUsage"),
  USAGE_SPECIFICATION("usageSpecification"),
  USAGE_SPECIFICATION_REF("usageSpecificationRef"),
  RELATED_PARTY_REFERENCE("relatedPartyReference"),
  BILL_REF("billRef"),
  PAYMENT_ORDER("paymentOrder"),
  APPLIED_CUSTOMER_BILLING_RATE("appliedCustomerBillingRate"),
  CUSTOMER_BILL("customerBill"),
  APPLIED_PAYMENT("appliedPayment"),
  APPLIED_BILLING_RATE_CHARACTERISTIC("appliedBillingRateCharacteristic"),
  BANK_ACCOUNT("bankAccount"),
  MONEY("money"),
  TAX_ITEM("taxItem"),
  PAYMENT_METHOD_REF("paymentMethodRef"),
  PAYMENT_REF("paymentRef"),
  FINANCIAL_ACCOUNT_REF("financialAccountRef"),
  PAYEE("payee"),
  JUVO_LOAN_TYPE("juvoLoanType"),
  LOYALTY_REF("loyaltyRef"),
  RELATED_PARTY_REF_TYPE("relatedPartyRefType"),
  CHANNEL_REF_TYPE("channelRefType"),
  ACCOUNT_REF_TYPE("accountRefType"),
  PAY_BY_LINK_TYPE("payByLinkType"),
  PAYMENT_TYPE("paymentType"),
  TOKENIZED_CARD_TYPE("tokenizedCardType"),
  BANK_CARD_TYPE("bankCardType"),
  ENTITY_REF_TYPE("entityRefType"),
  BANK_ACCOUNT_DEBIT_TYPE("bankAccountDebitType"),
  PAYMENT_METHOD_TYPE("paymentMethodType"),
  POSTPAID_TYPE("postpaidType"),
  CASH_TYPE("cashType"),
  DIGITAL_WALLET_TYPE("digitalWalletType"),
  PAYMENT_ITEM_TYPE("paymentItemType"),
  DETAILS("details"),
  PAYMENT_REF_TYPE("paymentRefType"),
  TIME_PERIOD_TYPE("timePeriodType"),
  MONEY_TYPE("moneyType"),
  BANK_ACCOUNT_TRANSFER_TYPE("bankAccountTransferType"),
  TRANSACTION_SUMMARY_INFO_TYPE("transactionSummaryInfoType"),
  VOUCHER_TYPE("voucherType"),
  BUCKET_BALANCE_ACTIVITY("bucketBalanceActivity"),
  BALANCE_TOPUP_STATUS_TYPE("balanceTopupStatusType"),
  PARTY_ACCOUNT_REF_TYPE("partyAccountRefType"),
  PAYMENT_METHOD_REF_OR_VALUE_TYPE("paymentMethodRefOrValueType"),
  BALANCE_TRANSFER_TYPE("balanceTransferType"),
  BUCKET_BALANCE_TYPE("bucketBalanceType"),
  BALANCE_TOPUP_TYPE("balanceTopupType"),
  BALANCE_TRANSFER_ELIGIBILITY("balanceTransferEligibility"),
  PRODUCT_REF_TYPE("productRefType"),
  COMPANY_RECEIPT_INFO_TYPE("companyReceiptInfoType"),
  BUCKET_BALANCE_REF_TYPE("bucketBalanceRefType"),
  BUCKET_BALANCE_TYPE_CHARACTERISTIC("bucketBalanceTypeCharacteristic"),
  COMPANY_RECEIPT_TYPE("companyReceiptType"),
  LOYALTY_EVENT_TYPE_LINK_REF("loyaltyEventTypeLinkRef"),
  RULE_ACTION("ruleAction"),
  LOYALTY_RULE("loyaltyRule"),
  PROGRAM_PRODUCT_SPEC("programProductSpec"),
  UPDATE_PRODUCT_SPEC("updateProductSpec"),
  LOYALTY_EVENT("loyaltyEvent"),
  LOYALTY_CONDITION_LINK_REF("loyaltyConditionLinkRef"),
  PRODUCT_PROGRAM_UPDATE("productProgramUpdate"),
  RULE_EVENT_TYPE("ruleEventType"),
  LOYALTY_ACCOUNT("loyaltyAccount"),
  LOYALTY_MEMBER("loyaltyMember"),
  LOYALTY_ACTION("loyaltyAction"),
  LOYALTY_CONDITION("loyaltyCondition"),
  LOYALTY_BALANCE_UPDATE("loyaltyBalanceUpdate"),
  LOYALTY_BALANCE("loyaltyBalance"),
  LOYALTY_TRANSACTION("loyaltyTransaction"),
  UPDATE_EVENT_TYPES_REQUEST("updateEventTypesRequest"),
  RULE_CONDITION("ruleCondition"),
  PRODUCT_PROGRAM("productProgram"),
  QUANTITY("quantity"),
  LOYALTY_EVENT_TYPE("loyaltyEventType"),
  LOYALTY_ACCOUNT_CREATE("loyaltyAccountCreate"),
  LOYALTY_ACTION_LINK_REF("loyaltyActionLinkRef"),
  PRODUCT_LINK_REF("productLinkRef"),
  CREATE_LOYALTY_BALANCE("createLoyaltyBalance"),
  LOYALTY_MEMBER_LINK_REF("loyaltyMemberLinkRef"),
  EVENT("event"),
  LOYALTY_EXECUTION_POINT("loyaltyExecutionPoint"),
  CREATE_LOYALTY_MEMBER("createLoyaltyMember"),
  UPDATE_LOYALTY_MEMBER("updateLoyaltyMember"),
  SENDER("sender"),
  COMMUNICATION_MESSAGE("communicationMessage"),
  RECEIVER("receiver"),
  COMMUNICATION_REQUEST_CHARACTERISTIC("communicationRequestCharacteristic"),
  INTERACTION_ITEM("interactionItem"),
  RELATED_ENTITY_REF("relatedEntityRef"),
  PARTY_INTERACTION("partyInteraction"),
  EVENT_ATTRIBUTE("eventAttribute"),
  ORGANIZATION_ASSET("organizationAsset"),
  SERVICE_PIN("servicePIN"),
  MAIL_UNIQUE("mailUnique"),
  SERVICES_RESPONSE("servicesResponse"),
  SERVICES_ACCOUNT("servicesAccount"),
  OTP("OTP"),
  TOKEN("token"),
  VERIFICATION_QUALIFICATION("verificationQualification"),
  SERVICES_OTP("servicesOTP"),
  MAIL_REQUEST_RESPONSE("mailRequestResponse"),
  DETAILS_2FA("details2FA"),
  MAIL_REGISTER("mailRegister"),
  REGISTER_ADMIN("registerAdmin"),
  SERVICES_UP("servicesUP"),
  PROVIDER_TOKEN("providerToken"),
  VALIDATION_CONTEXT_REQUEST("validationContextRequest"),
  LOGIN_DATA("loginData"),
  ADDED_SERVICE("addedService"),
  LOGIN("login"),
  PROVIDER("provider"),
  SERVICES_REQUEST("servicesRequest"),
  SERVICES_PIN("servicesPIN"),
  PIN("PIN"),
  PROVIDER_REF("providerRef"),
  ACCOUNT_RECOVERY("accountRecovery"),
  TELEKOM_LOGIN("telekomLogin"),
  SITE_RELATIONSHIP("siteRelationship"),
  CALENDAR_PERIOD("calendarPeriod"),
  HOUR_PERIOD("hourPeriod"),
  GEOGRAPHIC_SITE("geographicSite"),
  ENTITY_CATALOG_ITEM("entityCatalogItem"),
  TRACKING_EVENT("trackingEvent"),
  SERVICE_CATALOG("serviceCatalog"),
  SERVICE_CANDIDATE("serviceCandidate"),
  SERVICE_SPECIFICATION_REF("serviceSpecificationRef"),
  SERVICE_CATEGORY("serviceCategory"),
  SERVICE_SPEC_CHARACTERISTIC("serviceSpecCharacteristic"),
  SERVICE_SPEC_RELATIONSHIP("serviceSpecRelationship"),
  ANY("any"),
  SERVICE_SPEC_CHARACTERISTIC_VALUE("serviceSpecCharacteristicValue"),
  SERVICE_CANDIDATE_REF("serviceCandidateRef"),
  SERVICE_SPEC_CHAR_RELATIONSHIP("serviceSpecCharRelationship"),
  SERVICE_LEVEL_SPECIFICATION_REF("serviceLevelSpecificationRef"),
  SERVICE_CATEGORY_REF("serviceCategoryRef"),
  RESOURCE_SPECIFICATION_REF("resourceSpecificationRef"),
  TARGET_SERVICE_SCHEMA("targetServiceSchema"),
  ATTACHMENT_REF("attachmentRef"),
  CURRENT_STEP("currentStep"),
  FILTER_CHARACTERISTIC_VALUE("filterCharacteristicValue"),
  FILTER_CHARACTERISTIC_CONFIG("filterCharacteristicConfig"),
  SALES_OFFERING("salesOffering"),
  SALES_OFFERING_VALIDATION_STATUS("salesOfferingValidationStatus"),
  FILTER_CHARACTERISTIC("filterCharacteristic"),
  SALES_OFFERING_CONTEXT("salesOfferingContext"),
  SALES_CATALOG_RESPONSE("salesCatalogResponse"),
  CART_VALIDATION_STATUS("cartValidationStatus"),
  SHOPPING_CART_TERM_TYPE_CHARACTERISTIC("shoppingCartTermTypeCharacteristic"),
  SHOPPING_CART("shoppingCart"),
  SHOPPING_CART_TERM_TYPE("shoppingCartTermType"),
  ITEM_VALIDATION_STATUS("itemValidationStatus"),
  VALIDATION_STATUS("validationStatus"),
  APPLIED_CONSEQUENCE("appliedConsequence"),
  DURATION_TYPE_ENUM("durationTypeEnum"),
  MEASURE_THRESHOLD_RULE_VIOLATION("measureThresholdRuleViolation"),
  TEST_MEASURE_DEFINITION("testMeasureDefinition"),
  METRIC_DEF_MEASURE_CONSEQUENCE("metricDefMeasureConsequence"),
  SERVICE_TEST_SPECIFICATION("serviceTestSpecification"),
  METRIC_DEF_MEASURE_THRESHOLD_RULE("metricDefMeasureThresholdRule"),
  TEST_MEASURE("testMeasure"),
  SERVICE_TEST("serviceTest"),
  CHARACTERISTIC_WITH_TYPE("characteristicWithType"),
  APPOINTMENT("appointment"),
  CALENDAR_EVENT_REF("calendarEventRef"),
  MEDIUM_CHARACTERISTIC("mediumCharacteristic"),
  SEARCH_TIME_SLOT("searchTimeSlot"),
  SEARCH_TIME_SLOT_POST_INPUT("searchTimeSlotPostInput"),
  TIME_SLOT("timeSlot"),
  PROMOTION("promotion"),
  PROMOTION_CRITERIA("promotionCriteria"),
  PROMOTION_CRITERIA_GROUP("promotionCriteriaGroup"),
  PROMOTION_PATTERN("promotionPattern"),
  PROMOTION_SPECIFICATION("promotionSpecification"),
  PROMOTION_TARGET_ITEM("promotionTargetItem"),
  PROMOTION_ACTION("promotionAction"),
  SSO_TOKEN("ssoToken"),
  ADDITIONAL_ONE_TIME_USE_REFERENCE("additionalOneTimeUseReference"),
  TICKET_TRACKING_TEST("ticketTrackingTest"),
  PRODUCT_DUMMY("productDummy"),
  PRODUCT_OFFERING_REF_DUMMY("productOfferingRefDummy"),
  INSTALLMENTS_TYPE("installmentsType"),
  CLIENT_CREDENTIALS("clientCredentials"),
  PRODUCT_ORDER_REF("productOrderRef"),
  ORGANIZATION_IDENTIFICATION("organizationIdentification"),
  MANAGEABLE_ASSET("manageableAsset"),
  MANAGEABLE_ASSET_CUSTOMER_REF("manageableAssetCustomerRef"),
  WORKFLOW_STATUS_ENTITY("workflowStatusEntity"),
  PROCESS_STATUS_ITEM("processStatusItem"),
  TOOLTIP("tooltip"),
  COMMUNICATION_MESSAGE_ACTION("communicationMessageAction"),
  PARTY_INTERACTION_CONTEXT("partyInteractionContext"),
  RECOMMENDATION_CONTEXT("recommendationContext"),
  ORDER_TERM("orderTerm"),
  DEPENDENT_ACTIONS("dependentActions"),
  ACTIONS_CONDITIONS("actionsConditions"),
  ACTION_TARGETS("actionTargets"),
  ELIGIBILITY_CONTEXT("eligibilityContext"),
  ELIGIBILITY_ITEM("eligibilityItem"),
  ELIGIBILITY_CHECK_RESULT("eligibilityCheckResult"),
  PRODUCT_PRICE_ALTERATION("productPriceAlteration"),
  // modifyOff
  ERROR("error");
  private String value;

  RelationEntityEnum(String value) {
    this.value = value;
  }

  @JsonCreator
  public static RelationEntityEnum fromValue(String text) {
    for (RelationEntityEnum b : RelationEntityEnum.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }
}
