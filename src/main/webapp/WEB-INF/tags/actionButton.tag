<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/tld/mytl.tld" prefix="mytl" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="mytags"%>

<%@ attribute  name="action"  required="true" %>
<%@ attribute  name="entityId"  required="true" %>
<%@ attribute  name="buttonType"  required="true" %>
<%@ attribute  name="messageKey" required="true" %>
<%@ attribute  name="icon" %>

<c:url var="url" value="${action}">
    <c:param name="id" value="${entityId}" />
</c:url>

<c:if test="${not empty icon}">
    <c:set var="icon">
        <i class="bi-${icon}"></i>
    </c:set>
</c:if>

<a href="${url}" class="btn btn-${buttonType}">
    ${icon}
    <fmt:message key="${messageKey}"/>
</a>
