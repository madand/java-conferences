<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/tld/mytl.tld" prefix="mytl" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="mytags"%>

<%@ attribute  name="sortableFields" type="java.util.List" required="true" %>
<%@ attribute  name="sortingOptions" type="net.madand.conferences.db.web.Sorting" required="true" %>
<%@ attribute  name="url" required="true" %>

<div class="mb-3 text-small">
    <fmt:message key="sorting.sortBy"/>:

    <c:set var="sortDrection" value="${sortingOptions.direction}"/>
    <c:forEach items="${sortableFields}" var="sortField">

        <c:set var="sortDirection" value="${sortingOptions.defaultDirection}" />
        <c:set var="btnType" value="default"/>
        <c:set var="icon" value=""/>
        <c:if test="${sortField == sortingOptions.field}">
            <c:set var="sortDirection" value="${sortDrection == 'asc' ? 'desc' : 'asc'}" />
            <c:set var="btnType" value="primary"/>
            <c:set var="icon">
                <i class="bi bi-sort-alpha-${sortDrection == 'asc' ? 'down' : 'up'}"></i>
            </c:set>
        </c:if>

        <c:url var="sortUrl" value="${url}">
            <c:param name="sortBy" value="${sortField}" />
            <c:param name="sortDirection" value="${sortDirection}" />
        </c:url>
        <a href="${sortUrl}" class="btn btn-sm btn-${btnType}">
            <fmt:message key="sorting.field.${sortField}"/>
            ${icon}
        </a>
    </c:forEach>
</div>
