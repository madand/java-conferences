<%@ include file="/WEB-INF/jspf/directive/page.jspf" %>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf" %>

<c:set var="pageTitle" scope="request">
    <c:out value="${conference.name}" />
</c:set>
<%@ include file="/WEB-INF/jspf/head.jspf" %>

<div class="actions">
    <mytags:actionButton action="edit-conference"
                         entityId="${conference.id}"
                         buttonType="primary"
                         messageKey="form.button.edit"
                         icon="pencil" />
    <mytags:deleteButton action="delete-conference"
                         entityId="${conference.id}"
                         icon="trash" />
    <mytags:actionButton action="create-talk"
                         entityId="${conference.id}"
                         buttonType="success"
                         messageKey="form.button.createTalk"
                         icon="plus-circle" />
</div>

<div class="row">
    <div class="col mb-4">
        <p class="text-muted event-date">
            <mytl:formatDate value="${conference.eventDate}" format="LONG"
                             locale="${currentLanguage.code}" />
        </p>
        <div class="text-muted event-location">
            ${mytl:linesToParagraphs(conference.location)}
        </div>
        ${mytl:linesToParagraphs(conference.description)}
    </div>
</div>

<div class="row row-cols-1">
    <h2><fmt:message key="talk.list.subtitle"/></h2>
    <c:forEach items="${talks}" var="talk">
        <div class="col mb-3">
            <div class="card">
                <div class="card-body">
                    <h3 class="card-title"><c:out value="${talk.name}" /></h3>
                    <p class="card-subtitle text-muted event-date">
                        <mytl:formatDate value="${talk.startTime}" type="time"
                                         format="SHORT" locale="currentLanguage.code" />
                        (${talk.duration} <fmt:message key="general.minutes"/>)
                        <c:if test="${not empty talk.speaker}">
                            <br/> <b>Speaker: </b>
                            <c:out value="${talk.speaker.realName}" />
                        </c:if>
                    </p>
                    <div class="card-text">
                        ${mytl:linesToParagraphs(talk.description)}
                    </div>
                </div>
                <div class="card-footer">
                    <mytags:actionButton action="edit-talk"
                                         entityId="${talk.id}"
                                         buttonType="primary"
                                         messageKey="form.button.edit"
                                         icon="pencil" />
                    <mytags:deleteButton action="delete-talk"
                                         entityId="${talk.id}"
                                         icon="trash" />
                </div>
            </div>
        </div>
    </c:forEach>
</div>

<%@ include file="/WEB-INF/jspf/footer.jspf" %>
