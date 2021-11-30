<%----------------------------------------------------------------------
Single pagination item
----------------------------------------------------------------------%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/tld/mytl.tld" prefix="mytl" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="mytags"%>

<%@ attribute  name="labelKey" required="true" %>
<%@ attribute  name="linkUrl" required="true" %>

<c:set var="absUrl" value="/${linkUrl}"/>
<c:if test="${absUrl == requestScope['javax.servlet.forward.servlet_path']}">
    <c:set var="active" value="active"/>
</c:if>

<c:if test="${empty linkUrl}">
    <c:set var="linkUrl" value="${requestScope['javax.servlet.forward.context_path']}/"/>
</c:if>

<li class="nav-item">
    <a class="nav-link ${active}" href="${linkUrl}">
        <fmt:message key="${labelKey}"/>
    </a>
</li>
