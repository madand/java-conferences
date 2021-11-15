<%@ taglib uri="/WEB-INF/tld/custom.tld" prefix="mytl"%>

<%@ tag import="net.madand.conferences.web.util.WebFormUtil" %>

<%@ attribute  name="name"  required="true" %>
<%@ attribute  name="label"  required="true" %>
<%@ attribute  name="langCode" %>
<%@ attribute  name="required" %>
<%@ attribute  name="value" %>
<%@ attribute  name="rows" %>

<%
String id = WebFormUtil.nameToID((String) jspContext.getAttribute("name"));
%>

<div class="mb-3">
    <label for="<%= id %>" class="form-label ${required != "false" ? "required" : ""}">
        ${label} (${langCode})
    </label>
    <textarea name="${name}" id="<%= id %>" class="form-control" rows="${rows}"
              ${required != "false" ? "required" : ""} >${value}</textarea>
</div>
