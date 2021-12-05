<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@ attribute  name="role" required="true" %>

<c:choose>
    <c:when test="${role == 'MODERATOR'}">
        <c:set var="badgeType" value="danger" />
    </c:when>
    <c:when test="${role == 'SPEAKER'}">
        <c:set var="badgeType" value="success" />
    </c:when>
    <c:otherwise>
        <c:set var="badgeType" value="primary" />
    </c:otherwise>
</c:choose>

<span class="badge bg-${badgeType}">
    <fmt:message key="role.${role}"/>
</span>
