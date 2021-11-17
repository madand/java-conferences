<%@ include file="/WEB-INF/jspf/directive/page.jspf" %>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf" %>

<c:set var="pageTitle" scope="request">
    <fmt:message key="conference.list.title"/>
</c:set>
<%@ include file="/WEB-INF/jspf/head.jspf" %>

<div class="row">
    <c:forEach items="${entities}" var="entity">
        <div class="col-xl-6 col-lg-12 mb-3">
            <div class="card">
                <div class="card-body">
                    <h3 class="card-title"><c:out value="${entity.name}" /></h3>
                    <p class="card-subtitle mb-2 text-muted">
                        <mytl:formatDate value="${entity.eventDate}" format="LONG"
                                         locale="${currentLanguage.code}" />
                    </p>
                    <div class="card-text">
                        ${mytl:linesToParagraphs(entity.description)}
                    </div>
                    <div class="card-text location text-muted">
                        ${mytl:linesToParagraphs(entity.location)}
                    </div>
                    <a href="#" class="btn btn-primary">
                        <fmt:message key="conference.list.button.details"/>
                    </a>
                </div>
            </div>
        </div>
    </c:forEach>
</div>

<%@ include file="/WEB-INF/jspf/footer.jspf" %>
