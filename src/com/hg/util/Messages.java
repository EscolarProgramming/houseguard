package com.hg.util;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;

public class Messages {

	// property file is: name/messages.properties
	private static final String BUNDLE_NAME = "com.hg.i18n.messages";

	private static Locale LOCALE;
	private static ResourceBundle RESOURCE_BUNDLE;


	private Messages() {
	}


	public static void enable(String language) {
		try {
			String[] list = StringUtils.split(language, "_");

			LOCALE = new Locale(list[0], list[1]);
			RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME, LOCALE);
		} catch (Exception e) {
			RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);
			e.printStackTrace();
			System.out.println(getString("hg.i18n_string_error"));
		}

	}


	public static String getString(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}


	public static String getString(String key, Object... params) {
		try {
			return MessageFormat.format(RESOURCE_BUNDLE.getString(key), params);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}
}