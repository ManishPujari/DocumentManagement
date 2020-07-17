package com.dt.europe.hal.api.common.templates;

public class HalMessages {

  public static final String POSTMAN_HEADER = "X-Postman-Id";
  public static final String GET_HEADER = "X-HTTP-Method-Override";

  public static final String HTTP_200 = "OK";
  public static final String HTTP_201 = "Created";
  public static final String HTTP_202 = "Accepted";
  public static final String HTTP_204 = "No Content";
  public static final String HTTP_206 = "Partial content";
  public static final String HTTP_207 = "Warning or info";
  public static final String HTTP_400 = "Bad format request";
  public static final String HTTP_401 = "Not authenticated";
  public static final String HTTP_403 = "Not authorized";
  public static final String HTTP_404 = "Not found";
  public static final String HTTP_408 = "Request timeout";
  public static final String HTTP_409 = "Business rules exceptions";
  public static final String HTTP_422 = "Business input parameter validation error";
  public static final String HTTP_429 = "Too many requests";
  public static final String HTTP_500 = "Server error";
  public static final String HTTP_504 = "Backend not available";

  public static final String PARAM_FIELDS = "list of fields returned in response";
  public static final String PARAM_PATCH_FIELDS = "list of fields that needs to be changed in PATCH method";
  public static final String PARAM_BULK_FIELDS = "list of fields that need to be changed in bulkUpdate";
  public static final String PARAM_SEARCH_TERM = "Search term in one line";
  public static final String PARAM_QUERY = "RQL command";
  public static final String PARAM_PERSONAL = "IdentificationId (exact name varies between countries)";
  public static final String PARAM_PAGE = "page, positive integer, starts with 0";
  public static final String PARAM_SIZE = "page size, positive integer greater than 0";
  public static final String PARAM_SORT = "data sort parameter, starts with '+' or '-' prefix; when prefix empty, '+' is default; multiple "
    + "attributes can be used separated by ','";

}
