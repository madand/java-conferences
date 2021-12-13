<%@ include file="/WEB-INF/jspf/directive/page.jspf" %>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf" %>

<c:set var="pageTitle" scope="request">
    <fmt:message key="menu.attendee.myConferences"/>
</c:set>
<%@ include file="/WEB-INF/jspf/head.jspf" %>

<mytags:sorting sortableFields="${sortableFields}"
                sortingOptions="${queryOptions.sorting}"
                url="${requestScope['javax.servlet.forward.servlet_path']}" />

<div class="row row-cols-1">
    <table class="table table-striped">
        <thead>
            <th scope="col"><fmt:message key="table.column.conference"/></th>
            <th scope="col"><fmt:message key="table.column.date"/></th>
            <th scope="col"><fmt:message key="table.column.talksCount"/></th>
            <th scope="col"><fmt:message key="table.column.attendeesCount"/></th>
            <th scope="col"><fmt:message key="table.column.actions"/></th>
        </thead>
        <tbody>
            <c:forEach items="${conferences}" var="conference">
                <tr>
                    <td> <c:out value="${conference.name}"/> </td>
                    <td>
                        <mytl:formatDate value="${conference.eventDate}" format="LONG" />
                    </td>
                    <td> <c:out value="${conference.talksCount}"/> </td>
                    <td> <c:out value="${conference.attendeesCount}"/> </td>
                    <td>
                        <mytags:actionButton action="list-talks"
                                             entityId="${conference.id}"
                                             buttonType="primary"
                                             messageKey="conference.list.button.details"
                                             icon="eye" />

                        <mytags:postActionButton action="cancel-attendance"
                                                 entityId="${conference.id}"
                                                 buttonType="danger"
                                                 messageKey="button.cancelAttendance"
                                                 icon="x-circle" />
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</div>

<mytags:pagination paginationOptions="${queryOptions.pagination}" />

<%@ include file="/WEB-INF/jspf/footer.jspf" %>
