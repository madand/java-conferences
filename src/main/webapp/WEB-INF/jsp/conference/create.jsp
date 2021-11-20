<%@ include file="/WEB-INF/jspf/directive/page.jspf" %>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf" %>

<c:set var="pageTitle" scope="request">
    <fmt:message key="conference.create.title"/>
</c:set>
<%@ include file="/WEB-INF/jspf/head.jspf" %>

<c:set var="isNewEntity" value="${true}" scope="request" />
<%@ include file="/WEB-INF/jsp/conference/form.jsp" %>

<%@ include file="/WEB-INF/jspf/footer.jspf" %>
