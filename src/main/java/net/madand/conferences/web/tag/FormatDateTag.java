package net.madand.conferences.web.tag;

import net.madand.conferences.entity.Language;
import net.madand.conferences.web.scope.ContextScope;
import net.madand.conferences.web.scope.SessionScope;
import org.apache.taglibs.standard.tag.common.core.Util;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.Temporal;
import java.util.Locale;

public class FormatDateTag extends TagSupport {
    protected Temporal value;
    protected String type;
    protected String format;
    protected String locale;
    private String var;
    private int scope;


    public FormatDateTag() {
        super();
        init();
    }

    private void init() {
        this.scope = PageContext.PAGE_SCOPE;

        if (type == null) {
            type = "date";
        }

        if (format == null) {
            format = FormatStyle.SHORT.name();
        }

    }


    public void setVar(final String var) {
        this.var = var;
    }

    public void setScope(final String scope) {
        this.scope = Util.getScope(scope);
    }


    public void setValue(final Temporal value) {
        this.value = value;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override

    public int doEndTag() throws JspException {
        if (value == null) {
            if (var != null) {
                pageContext.removeAttribute(var, scope);
            }
            return EVAL_PAGE;
        }

        if (locale == null) {
            locale = detectLocale();
        }

        DateTimeFormatter formatter = null;
        switch (type) {
            case "both":
                formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.valueOf(format));
                break;
            case "date":
                formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.valueOf(format));
                break;
            case "time":
                formatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.valueOf(format));
                break;
            default:
                throw new InvalidParameterException("Unknown date format type: " + type);
        }

        String formatted = formatter.withLocale(new Locale(locale)).format(value);

        if (var != null) {
            pageContext.setAttribute(var, formatted, scope);
        } else {
            try {
                pageContext.getOut().print(formatted);
            } catch (final IOException ioe) {
                throw new JspTagException(ioe.toString(), ioe);
            }
        }

        return EVAL_PAGE;
    }


    @Override
    public void release() {
        init();
    }

    private String detectLocale() {
        Language sessionLanguage = SessionScope.getCurrentLanguage(pageContext.getSession());
        if (sessionLanguage != null) {
            return sessionLanguage.getCode();
        }

        return ContextScope.getDefaultLanguage(pageContext.getServletContext()).getCode();
    }
}
