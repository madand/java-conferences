<%@ include file="/WEB-INF/jspf/directive/page.jspf" %>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf" %>

<html>
    <c:set var="pageTitle" value="test" scope="request" />
    <%@ include file="/WEB-INF/jspf/head.jspf" %>

    <body>
        <c:url value="/rel-path">
            <c:param name="id" value="11" />
        </c:url>

        <%@ include file="/WEB-INF/jspf/footer.jspf" %>
    </body>
</html>
