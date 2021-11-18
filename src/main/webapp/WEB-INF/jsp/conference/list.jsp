<%@ include file="/WEB-INF/jspf/directive/page.jspf" %>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf" %>

<c:set var="pageTitle" scope="request">
    <fmt:message key="conference.list.title"/>
</c:set>
<%@ include file="/WEB-INF/jspf/head.jspf" %>

<div class="row row-cols-1 row-cols-xl-2">
    <c:forEach items="${entities}" var="entity">
        <div class="col mb-3">
            <div class="card h-100">
                <div class="card-body">
                    <h3 class="card-title"><c:out value="${entity.name}" /></h3>
                    <p class="card-subtitle text-muted event-date">
                        <mytl:formatDate value="${entity.eventDate}" format="LONG"
                                         locale="${currentLanguage.code}" />
                    </p>
                    <div class="card-text text-muted event-location">
                        ${mytl:linesToParagraphs(entity.location)}
                    </div>
                    <div class="card-text">
                        ${mytl:linesToParagraphs(entity.description)}
                    </div>
                </div>
                <div class="card-footer">
                    <a href="#" class="btn btn-primary">
                        <fmt:message key="conference.list.button.details"/>
                    </a>
                </div>
            </div>
        </div>
    </c:forEach>
</div>

<%@ include file="/WEB-INF/jspf/footer.jspf" %>
