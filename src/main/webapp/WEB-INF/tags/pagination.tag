<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/tld/mytl.tld" prefix="mytl" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="mytags"%>

<%@ attribute  name="queryOptions" required="true" type="net.madand.conferences.db.web.QueryOptions" %>

<c:set var="currentPage" value="${queryOptions.pagination.currentPage}" />
<c:set var="totalPages" value="${queryOptions.pagination.totalPages}" />
<c:set var="itemsPerPage" value="${queryOptions.pagination.itemsPerPage}" />
<c:set var="nextText">
    <fmt:message key="pagination.next"/>
</c:set>
<c:set var="prevText">
    <fmt:message key="pagination.previous"/>
</c:set>

<p class="text-center">
    <fmt:message key="pagination.itemsPerPage"/>:
    <c:forEach begin="2" end="10" step="2" var="curItemsPerPage">
        <c:url var="itemsPerPageUrl" value="${originalURIAndQueryString}">
            <c:param name="itemsPerPage" value="${curItemsPerPage}" />
            <c:param name="page" value="1" />
        </c:url>
        <c:choose>
            <c:when test="${curItemsPerPage == itemsPerPage}">
                ${curItemsPerPage}
            </c:when>
            <c:otherwise>
                <a href="${itemsPerPageUrl}">${curItemsPerPage}</a>
            </c:otherwise>
        </c:choose>
    </c:forEach>
</p>

<nav >
    <ul class="pagination justify-content-center">

        <mytags:paginationItem pageNumber="${currentPage - 1}"
                               linkText="${prevText}"
                               disabled="${currentPage <= 1}" />

        <c:forEach begin="1" end="${totalPages}" var="pageNumber">
            <mytags:paginationItem pageNumber="${pageNumber}"
                                   linkText="${pageNumber}"
                                   active="${pageNumber == currentPage}" />
        </c:forEach>

        <mytags:paginationItem pageNumber="${currentPage + 1}"
                               linkText="${nextText}"
                               disabled="${currentPage >= totalPages}" />

    </ul>
</nav>
