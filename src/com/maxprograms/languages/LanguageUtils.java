/*******************************************************************************
 * Copyright (c) 2003-2020 Maxprograms.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 1.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/org/documents/epl-v10.html
 *
 * Contributors:
 *     Maxprograms - initial API and implementation
 *******************************************************************************/
package com.maxprograms.languages;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.maxprograms.xml.Element;
import com.maxprograms.xml.SAXBuilder;

public class LanguageUtils {

	private static List<Language> languages;
	private static RegistryParser registry;

	private LanguageUtils() {
		// do not instantiate
	}

	public static List<Language> getCommonLanguages() throws SAXException, IOException, ParserConfigurationException {
		if (registry == null) {
			registry = new RegistryParser(Language.class.getResource("language-subtag-registry.txt"));
		}
		if (languages == null) {
			languages = new ArrayList<>();
			SAXBuilder builder = new SAXBuilder();
			Element root = builder.build(Language.class.getResource("languageList.xml")).getRootElement();
			List<Element> children = root.getChildren();
			Iterator<Element> it = children.iterator();
			while (it.hasNext()) {
				Element lang = it.next();
				String code = lang.getAttributeValue("code");
				String description = registry.getTagDescription(code);
				languages.add(new Language(code, description));
			}
			Collections.sort(languages);
		}
		return languages;
	}

	public static Language getLanguage(String code) throws IOException {
		if (registry == null) {
			registry = new RegistryParser(Language.class.getResource("language-subtag-registry.txt"));
		}
		String description = registry.getTagDescription(code);
		if (description != null) {
			return new Language(code, description);
		}
		return null;
	}

	public static String normalizeCode(String code) throws IOException {
		if (registry == null) {
			registry = new RegistryParser(Language.class.getResource("language-subtag-registry.txt"));
		}
		return registry.normalizeCode(code);
	}

	public static boolean isBiDi(String code)  {
		return code.startsWith("ar") || code.startsWith("fa") || code.startsWith("az") || code.startsWith("ur")
				|| code.startsWith("pa-PK") || code.startsWith("ps") || code.startsWith("prs") || code.startsWith("ug")
				|| code.startsWith("he") || code.startsWith("ji") || code.startsWith("yi");

	}

	public static boolean isCJK(String code) {
		return code.startsWith("zh") || code.startsWith("ja") || code.startsWith("ko") || code.startsWith("vi")
				|| code.startsWith("ain") || code.startsWith("aib");
	}
}
