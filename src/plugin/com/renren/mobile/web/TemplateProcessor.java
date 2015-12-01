package com.renren.mobile.web;


import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.text.TextUtils.isEmpty;

/**
 * at 下午4:32, 12-5-3
 *
 * @author afpro
 */
public class TemplateProcessor {
    private final static Pattern tagPattern = Pattern.compile("(\\\\?)\\$\\{([#/]?)([a-zA-Z0-9_-]+)\\}");

    private final static class TemplateSection {
        final static int TYPE_STRING = 0;
        final static int TYPE_VAR = 1;
        final static int TYPE_SECTION = 2;
        final int type;
        final String name;
        final LinkedList<TemplateSection> children;

        TemplateSection(int $type, String $name) {
            type = $type;
            name = $name;
            if ($type == TYPE_SECTION) {
                children = new LinkedList<TemplateSection>();
            } else {
                children = null;
            }
        }
    }

    public final static class ArgumentSection {
        private final Map<String, String> vars = new TreeMap<String, String>();
        private final Map<String, List<ArgumentSection>> asMap = new TreeMap<String, List<ArgumentSection>>();
        private final ArgumentSection parent;

        public ArgumentSection() {
            this(null);
        }

        public ArgumentSection(ArgumentSection $parent) {
            parent = $parent;
        }

        public ArgumentSection addSection(String name) {
            List<ArgumentSection> asList = asMap.get(name);
            if (asList == null) {
                asMap.put(name, asList = new LinkedList<ArgumentSection>());
            }

            final ArgumentSection as = new ArgumentSection(this);
            asList.add(as);
            return as;
        }

        public void putArgument(String name, Object value) {
            vars.put(name, value.toString());
        }

        public String getArgument(String name) {
            if (vars.containsKey(name)) {
                return vars.get(name);
            } else if (parent != null) {
                return parent.getArgument(name);
            } else {
                return null;
            }
        }

        public List<ArgumentSection> getSections(String name) {
            if (asMap.containsKey(name)) {
                return asMap.get(name);
            } else if (parent != null) {
                return parent.getSections(name);
            } else {
                return null;
            }
        }
    }

    private final TemplateSection rootSection = new TemplateSection(TemplateSection.TYPE_SECTION, "");

    public TemplateProcessor(final String template) {
        assert template != null : "null template";

        final Matcher matcher = tagPattern.matcher(template);
        final Stack<TemplateSection> sectionStack = new Stack<TemplateSection>();
        sectionStack.push(rootSection);
        TemplateSection current = rootSection;
        int pos = 0;
        while (matcher.find()) {
            final String escape = matcher.group(1);
            final String prefix = matcher.group(2);
            final String name = matcher.group(3);
            final int start = matcher.start();
            final int end = matcher.end();

            if (!isEmpty(escape)) {
                continue;
            }

            if (start > pos) {
                current.children.add(new TemplateSection(TemplateSection.TYPE_STRING, template.substring(pos, start)));
            }
            pos = end;

            if (isEmpty(prefix)) {
                current.children.add(new TemplateSection(TemplateSection.TYPE_VAR, name));
            } else {
                switch (prefix.charAt(0)) {
                    case '#':
                        TemplateSection ts = new TemplateSection(TemplateSection.TYPE_SECTION, name);
                        current.children.add(ts);
                        sectionStack.push(current = ts);
                        break;
                    case '/':
                        assert !sectionStack.isEmpty() : String.format("broken section end tag %s", name);
                        final TemplateSection pre = sectionStack.pop();
                        assert pre.name.equals(name) : String.format("section mismatch: %s != %s", name, pre.name);
                        current = sectionStack.peek();
                        break;
                }
            }
        }

        if (pos >= 0 && pos < template.length()) {
            current.children.add(new TemplateSection(TemplateSection.TYPE_STRING, template.substring(pos)));
        }

        assert sectionStack.size()<=1 : "what about left section?";
    }

    private void buildTo(ArgumentSection argSection, TemplateSection tmpSection, StringBuilder sb) {
        switch (tmpSection.type) {
            case TemplateSection.TYPE_STRING:
                sb.append(tmpSection.name);
                break;
            case TemplateSection.TYPE_VAR:
                final String value = argSection.getArgument(tmpSection.name);
                if (value != null) {
                    sb.append(value);
                }
                break;
            case TemplateSection.TYPE_SECTION:
                if (tmpSection.children == null || tmpSection.children.isEmpty()) {
                    break;
                }
                final List<ArgumentSection> asList = argSection.getSections(tmpSection.name);
                if (asList != null) {
                    for (ArgumentSection as : asList) {
                        for (TemplateSection ts : tmpSection.children) {
                            buildTo(as, ts, sb);
                        }
                    }
                }
                break;
        }
    }

    public void buildTo(ArgumentSection argumentSection, StringBuilder sb) {
        if (argumentSection == null || sb == null) {
            return;
        }

        for (TemplateSection tmpSection : rootSection.children) {
            buildTo(argumentSection, tmpSection, sb);
        }
    }

    public String build(ArgumentSection argumentSection) {
        if (argumentSection == null) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        buildTo(argumentSection, sb);
        return sb.toString();
    }
}
