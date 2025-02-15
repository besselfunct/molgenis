package org.molgenis.web;

/** Model attributes used in plugin views */
public class PluginAttributes {
  public static final String KEY_CONTEXT_URL = "context_url";
  static final String KEY_PLUGIN_ID = "plugin_id";
  static final String KEY_PLUGIN_ID_WITH_QUERY_STRING = "pluginid_with_query_string";
  static final String KEY_MENU = "menu";
  static final String KEY_AUTHENTICATED = "authenticated";
  /** environment: development, production */
  public static final String KEY_ENVIRONMENT = "environment";

  public static final String KEY_RESOURCE_FINGERPRINT_REGISTRY = "resource_fingerprint_registry";
  public static final String KEY_APP_SETTINGS = "app_settings";
  public static final String KEY_AUTHENTICATION_OIDC_CLIENTS = "authentication_oidc_clients";
  public static final String KEY_AUTHENTICATION_SIGN_UP = "authentication_sign_up";
  public static final String KEY_PLUGIN_SETTINGS = "plugin_settings";
  /** Whether or not the current user can edit settings for the requested plugin */
  static final String KEY_PLUGIN_SHOW_SETTINGS_COG = "plugin_show_settings_cog";

  public static final String KEY_GSON = "gson";
  public static final String KEY_I18N = "i18n";

  private PluginAttributes() {}
}
