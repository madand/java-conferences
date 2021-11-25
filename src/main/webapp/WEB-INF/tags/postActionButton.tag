<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/tld/mytl.tld" prefix="mytl" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="mytags"%>

<%@ attribute  name="action"  required="true" %>
<%@ attribute  name="entityId"  required="true" %>
<%@ attribute  name="messageKey"  required="true" %>
<%@ attribute  name="buttonType"  required="true" %>
<%@ attribute  name="icon" %>

<c:url var="actionUrl" value="${action}">
    <c:param name="id" value="${entityId}" />
</c:url>

<form method="post" action="${actionUrl}" class="post-action-button">
    <mytags:buttonSubmit labelKey="${messageKey}" btnClass="${buttonType}" icon="${icon}" />
</form>
