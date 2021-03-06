<%@ include file="/WEB-INF/jspf/directive/page.jspf" %>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf" %>

<c:set var="pageTitle" scope="request">
    <c:out value="${conference.name}" />
</c:set>
<%@ include file="/WEB-INF/jspf/head.jspf" %>

<div class="actions">
    <c:if test="${user.role.moderator}">
        <mytags:actionButton action="edit-conference"
                             entityId="${conference.id}"
                             buttonType="primary"
                             messageKey="button.edit"
                             icon="pencil" />
        <mytags:deleteButton action="delete-conference"
                             entityId="${conference.id}"
                             icon="trash" />
        <mytags:actionButton action="create-talk"
                             entityId="${conference.id}"
                             buttonType="success"
                             messageKey="button.createTalk"
                             icon="plus-circle" />
    </c:if>

    <c:if test="${user.role.speaker}">
        <mytags:actionButton action="create-talk-proposal"
                             entityId="${conference.id}"
                             buttonType="success"
                             messageKey="button.createTalkProposal"
                             icon="plus-circle" />
    </c:if>
</div>

<div class="row">
    <div class="col mb-4">
        <p class="text-muted event-date">
            <mytl:formatDate value="${conference.eventDate}" format="LONG" />
        </p>
        <div class="text-muted event-location">
            ${mytl:linesToParagraphs(conference.location)}
        </div>
        <p>
            <fmt:message key="conference.list.attendeesCount"/>:
            <span class="badge bg-info">${conference.attendeesCount}</span>
        </p>
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
                                         format="SHORT" />
                        (${talk.duration} <fmt:message key="general.minutes"/>)
                    </p>

                    <div class="card-text">
                        <b>Speaker: </b>
                        <c:choose>
                            <c:when test="${not empty talk.speaker}">
                                <c:out value="${talk.speaker.realName}" />
                            </c:when>
                            <c:otherwise>
                                <fmt:message key="talk.speaker.unknown"/>

                                <c:if test="${user.role.speaker}">
                                    <c:choose>
                                        <c:when test="${not empty talkSpeakerRequestsMap[talk.id]}">
                                            <mytags:postActionButton action="delete-talk-speaker-request"
                                                                     entityId="${talkSpeakerRequestsMap[talk.id]}"
                                                                     buttonType="danger"
                                                                     messageKey="talk.speaker.cancelRequest"
                                                                     icon="x-circle"
                                                                     extraClasses="btn-sm" />
                                        </c:when>
                                        <c:otherwise>
                                            <mytags:postActionButton action="request-being-speaker-for-talk"
                                                                     entityId="${talk.id}"
                                                                     buttonType="primary"
                                                                     messageKey="talk.speaker.makeRequest"
                                                                     icon="chat-dots"
                                                                     extraClasses="btn-sm" />
                                        </c:otherwise>
                                    </c:choose>
                                </c:if>

                                <c:if test="${user.role.moderator}">
                                    <mytags:actionButton action="propose-being-speaker-for-talk"
                                                         entityId="${talk.id}"
                                                         buttonType="primary"
                                                         messageKey="button.talkSpeakerProposal.create"
                                                         icon="chat" />
                                </c:if>
                            </c:otherwise>
                        </c:choose>
                    </div>

                    <div class="card-text">
                        ${mytl:linesToParagraphs(talk.description)}
                    </div>
                </div>

                <c:if test="${user.role.moderator}">
                    <div class="card-footer">
                        <mytags:actionButton action="edit-talk"
                                             entityId="${talk.id}"
                                             buttonType="primary"
                                             messageKey="button.edit"
                                             icon="pencil" />
                        <mytags:deleteButton action="delete-talk"
                                             entityId="${talk.id}"
                                             icon="trash" />
                    </div>
                </c:if>
            </div>
        </div>
    </c:forEach>
</div>

<%@ include file="/WEB-INF/jspf/footer.jspf" %>
