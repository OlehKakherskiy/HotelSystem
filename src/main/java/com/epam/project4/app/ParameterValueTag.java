package main.java.com.epam.project4.app;

import main.java.com.epam.project4.model.entity.roomParameter.ParameterValue;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.SkipPageException;
import javax.servlet.jsp.jstl.fmt.LocalizationContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;

/**
 * @author Oleh Kakherskyi (olehkakherskiy@gmail.com)
 */
public class ParameterValueTag extends SimpleTagSupport {

    private List<ParameterValue> list;

    private LocalizationContext parameterBundle;

    private LocalizationContext valueBundle;

    @Override
    public void doTag() throws JspException, IOException {
        try {
            JspWriter writer = getJspContext().getOut();
            writer.println("<ul class=\"list-group\">");
            for (ParameterValue parameterValue : list) {
                writer.println(MessageFormat.format("<li class=\"list-group-item\">{0}",
                        parameterBundle.getResourceBundle().getString(parameterValue.getParameter().getParamName())));
                writer.println(MessageFormat.format("<span class=\"pull-right\">{0}</span>",
                        valueBundle.getResourceBundle().getString(parameterValue.getValue().getValue())));
            }
            writer.println("</ul>");
        } catch (Exception e) {
            e.printStackTrace();
            throw new SkipPageException(e);
        }
    }


    public void setList(List<ParameterValue> parameterValueList) {
        this.list = parameterValueList;
    }

    public void setParameterBundle(LocalizationContext parameterBundle) {
        this.parameterBundle = parameterBundle;
    }

    public void setValueBundle(LocalizationContext valueBundle) {
        this.valueBundle = valueBundle;
    }
}
