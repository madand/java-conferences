<%----------------------------------------------------------------------
Single pagination item
----------------------------------------------------------------------%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/tld/mytl.tld" prefix="mytl" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="mytags"%>

<%@ attribute  name="pageNumber" type="java.lang.Integer" required="true" %>
<%@ attribute  name="linkText" required="true" %>
<%@ attribute  name="active" type="java.lang.Boolean" %>
<%@ attribute  name="disabled" type="java.lang.Boolean" %>

<c:choose>
    <c:when test="${disabled}">
        <li class="page-item disabled">
            <a class="page-link" tabindex="-1">${linkText}</a>
        </li>
    </c:when>
    <c:otherwise>
        <c:url var="pageUrl" value="${requestScope['javax.servlet.forward.servlet_path']}">
            <c:param name="page" value="${pageNumber}" />
            <c:if test="${not empty param.sortBy}">
                <c:param name="sortBy" value="${param.sortBy}" />
            </c:if>
            <c:if test="${not empty param.sortDirection}">
                <c:param name="sortDirection" value="${param.sortDirection}" />
            </c:if>
        </c:url>
        <li class="page-item ${active ? 'active' : ''}">
            <a class="page-link" href="${pageUrl}">${linkText}</a>
        </li>
    </c:otherwise>
</c:choose>
