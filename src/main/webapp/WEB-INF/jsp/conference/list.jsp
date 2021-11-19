<%@ include file="/WEB-INF/jspf/directive/page.jspf" %>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf" %>


<%@ page import="net.madand.conferences.web.util.URLManager" %>
<%@ page import="net.madand.conferences.entity.Conference" %>

<c:set var="pageTitle" scope="request">
    <fmt:message key="conference.list.title"/>
</c:set>
<%@ include file="/WEB-INF/jspf/head.jspf" %>

<div class="row row-cols-1 row-cols-xl-2">
    <c:forEach items="${conferences}" var="conference">
        <div class="col mb-4">
            <div class="card h-100">
                <div class="card-body">
                    <h3 class="card-title"><c:out value="${conference.name}" /></h3>
                    <p class="card-subtitle text-muted event-date">
                        <mytl:formatDate value="${conference.eventDate}" format="LONG" />
                    </p>
                    <div class="card-text text-muted event-location">
                        ${mytl:linesToParagraphs(conference.location)}
                    </div>
                    <div class="card-text">
                        ${mytl:truncate(conference.description, 160)}
                    </div>
                </div>
                <div class="card-footer">
                    <a href="<%= URLManager.makeListTalksURL(request, (Conference) pageContext.getAttribute("conference"))  %>"
                       class="btn btn-success">
                        <i class="bi-eye"></i>
                        <fmt:message key="conference.list.button.details"/>
                    </a>
                </div>
            </div>
        </div>
    </c:forEach>
</div>

<%@ include file="/WEB-INF/jspf/footer.jspf" %>
