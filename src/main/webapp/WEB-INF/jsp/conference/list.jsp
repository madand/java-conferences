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

<div class="mb-3 text-small">
    <fmt:message key="sorting.sortBy"/>:

    <c:set var="sortDrection" value="${queryOptions.sorting.direction}"/>
    <c:forEach items="${sortableFields}" var="sortField">

        <c:set var="sortDirection" value="desc" />
        <c:set var="btnType" value="default"/>
        <c:set var="icon" value=""/>
        <c:if test="${sortField == queryOptions.sorting.field}">
            <c:set var="sortDirection" value="${sortDrection == 'asc' ? 'desc' : 'asc'}" />
            <c:set var="btnType" value="primary"/>
            <c:set var="icon">
                <i class="bi bi-sort-alpha-${sortDrection == 'asc' ? 'down' : 'up'}"></i>
            </c:set>
        </c:if>

        <c:url var="sortUrl" value="${requestScope['javax.servlet.forward.servlet_path']}">
            <c:param name="sortBy" value="${sortField}" />
            <c:param name="sortDirection" value="${sortDirection}" />
        </c:url>
        <a href="${sortUrl}" class="btn btn-sm btn-${btnType}">
            <fmt:message key="sorting.field.${sortField}"/>
            ${icon}
        </a>
    </c:forEach>
</div>

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

                    <p class="card-text">
                        <fmt:message key="conference.list.attendeesCount"/>:
                        <span class="badge bg-info">${conference.attendeesCount}</span>
                    </p>

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


                    <c:choose>
                        <c:when test="${conference.currentUserAttending}">
                            <mytags:postActionButton action="cancel-attendance"
                                                     entityId="${conference.id}"
                                                     buttonType="danger"
                                                     messageKey="button.cancelAttendance"
                                                     icon="x-circle" />
                        </c:when>
                        <c:otherwise>
                            <mytags:postActionButton action="attend-conference"
                                                     entityId="${conference.id}"
                                                     buttonType="success"
                                                     messageKey="button.attend"
                                                     icon="check-circle" />
                        </c:otherwise>
                    </c:choose>

                </div>
            </div>
        </div>
    </c:forEach>
</div>

<mytags:pagination queryOptions="${queryOptions}" />

<%@ include file="/WEB-INF/jspf/footer.jspf" %>
