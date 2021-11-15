
<%@ tag import="net.madand.conferences.web.util.WebFormUtil" %>

<%@ attribute  name="name"  required="true" %>
<%@ attribute  name="label"  required="true" %>
<%@ attribute  name="langCode" %>
<%@ attribute  name="required" %>
<%@ attribute  name="value" %>

<% String id = WebFormUtil.nameToID((String) jspContext.getAttribute("name")); %>

<div class="mb-3">
    <label for="<%= id %>" class="form-label ${required == "true" ? "required" : ""}">
        ${label} (${langCode})
    </label>
    <input name="${name}" type="text" value="${value}" id="<%= id %>"
           class="form-control" ${required == "true" ? "required" : ""} />
</div>
