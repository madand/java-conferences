<%@ include file="/WEB-INF/jspf/directive/page.jspf" %>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf" %>

<c:set var="pageTitle" scope="request">
    <fmt:message key="conference.list.title"/>
</c:set>
<%@ include file="/WEB-INF/jspf/head.jspf" %>

<ul class="nav nav-pills mb-3">
    <mytags:conferenceListModeItem labelKey="conferences.list.mode.upcoming"
                                   linkUrl="" />
    <mytags:conferenceListModeItem labelKey="conferences.list.mode.past"
                                   linkUrl="past" />
    <mytags:conferenceListModeItem labelKey="conferences.list.mode.all"
                                   linkUrl="all" />
</ul>

<mytags:sorting sortableFields="${sortableFields}"
                sortingOptions="${queryOptions.sorting}"
                url="${requestScope['javax.servlet.forward.servlet_path']}" />

<div class="row row-cols-1 row-cols-xl-2">
    <c:forEach items="${conferences}" var="conference">
        <div class="col mb-4">
            <div class="card h-100">
                <div class="card-body">
                    <h3 class="card-title"><c:out value="${conference.name}" /></h3>
                    <div class="card-text row row-cols-2 text-muted mb-1">
                        <div class="col event-date">
                            <mytl:formatDate value="${conference.eventDate}" format="LONG" />
                        </div>
                        <div class="col event-location">
                            ${mytl:linesToParagraphs(conference.location)}
                        </div>
                    </div>
                    <div class="card-text row row-cols-2 mb-2">
                        <div class="col ">
                            <span class="badge badge-pill bg-secondary">${conference.talksCount}</span>
                            <fmt:message key="conference.list.talks"/>
                        </div>
                        <div class="col ">
                            <span class="badge badge-pill bg-info">${conference.attendeesCount}</span>
                            <fmt:message key="conference.list.attendees"/>
                        </div>
                    </div>
                    <div class="card-text">
                        ${mytl:truncate(conference.description, 160)}
                    </div>


                    <c:if test="${conference.currentUserAttending}">
                        <div class="card-text">
                            <span class="badge rounded-pill bg-success">
                                <fmt:message key="conference.status.attending"/>
                            </span>
                            <fmt:message key="conference.status.attendingDescr"/>
                        </div>
                    </c:if>
                </div>
                <div class="card-footer">
                    <mytags:actionButton action="list-talks"
                                         entityId="${conference.id}"
                                         buttonType="primary"
                                         messageKey="conference.list.button.details"
                                         icon="eye" />

                    <mytags:attendConferenceButtons conference="${conference}" />
                </div>
            </div>
        </div>
    </c:forEach>
</div>

<mytags:pagination paginationOptions="${queryOptions.pagination}" />

<%@ include file="/WEB-INF/jspf/footer.jspf" %>
