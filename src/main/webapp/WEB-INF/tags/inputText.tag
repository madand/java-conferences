<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/tld/mytl.tld" prefix="mytl" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="mytags"%>

<%@ attribute  name="name"  required="true" %>
<%@ attribute  name="labelKey"  required="true" %>
<%@ attribute  name="entity" type="java.lang.Object" %>
<%@ attribute  name="language" type="net.madand.conferences.entity.Language" %>
<%@ attribute  name="required" type="java.lang.Boolean" %>
<%@ attribute  name="value" %>
<%@ attribute  name="type" %>

<c:set var="inputType" value="text" />
<c:if test="${not empty type}">
    <c:set var="inputType" value="${type}" />
</c:if>

<mytags:genericInput name="${name}" labelKey="${labelKey}" entity="${entity}"
                     language="${language}" value="${value}" required="${required}">
    <div class="mb-3">
        <label for="${computedId}" class="form-label ${computedRequired}">
            ${computedLabelText}
        </label>
        <input type="${inputType}" class="form-control" name="${computedName}"
               value="${fn:escapeXml(computedValue)}" id="${computedId}"
               ${computedRequired} />
    </div>
</mytags:genericInput>
