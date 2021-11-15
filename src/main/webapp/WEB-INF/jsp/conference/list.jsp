<%@ include file="/WEB-INF/jspf/directive/page.jspf" %>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf" %>

<c:set var="pageTitle" scope="request">
    <fmt:message key="conference.list.title"/>
</c:set>
<%@ include file="/WEB-INF/jspf/head.jspf" %>

<h1>${pageTitle}</h1>

<div class="row">
    <c:forEach items="${entities}" var="entity">
        <div class="col-12 mb-3">
            <div class="card">
                <div class="card-body">
                    <h3 class="card-title"><c:out value="${entity.name}" /></h3>
                    <pre class="card-text"><c:out value="${entity.description}" /></pre>
                    <pre class="card-text location text-muted"><c:out value="${entity.location}" /></pre>
                    <a href="#" class="btn btn-primary">
                        <fmt:message key="conference.list.button.details"/>
                    </a>
                </div>
                <div class="card-footer text-muted">
                    <mytl:formatDate value="${entity.eventDate}" format="FULL"
                                     locale="${currentLanguage.code}" />
                </div>
            </div>
        </div>
    </c:forEach>
</div>

<%@ include file="/WEB-INF/jspf/footer.jspf" %>
