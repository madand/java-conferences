<%@ include file="/WEB-INF/jspf/directive/page.jspf" %>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf" %>

<c:set var="pageTitle" scope="request">
    <fmt:message key="user.moderator.manage.title"/>
</c:set>
<%@ include file="/WEB-INF/jspf/head.jspf" %>

<mytags:sorting sortableFields="${sortableFields}"
                sortingOptions="${queryOptions.sorting}"
                url="${requestScope['javax.servlet.forward.servlet_path']}" />

<div class="row row-cols-1">
    <table class="table table-striped">
        <thead>
            <th scope="col"><fmt:message key="table.column.id"/></th>
            <th scope="col"><fmt:message key="table.column.email"/></th>
            <th scope="col"><fmt:message key="table.column.role"/></th>
            <th scope="col"><fmt:message key="table.column.realName"/></th>
            <th scope="col"><fmt:message key="table.column.createdAt"/></th>
            <th scope="col"><fmt:message key="table.column.actions"/></th>
        </thead>
        <tbody>
            <c:forEach items="${users}" var="user">
                <tr>
                    <td><c:out value="${user.id}"/></td>
                    <td>
                        <a href="mailto: ${user.email}">${user.email}</a>
                    </td>
                    <td>
                        <mytags:roleBadge role="${user.role}" />
                    </td>
                    <td><c:out value="${user.realName}"/></td>
                    <td>
                        <mytl:formatDate value="${user.createdAt}" type="both" format="SHORT"  />
                    </td>
                    <td>
                        <mytags:actionButton action="edit-user"
                                             entityId="${user.id}"
                                             buttonType="primary"
                                             messageKey="button.edit"
                                             icon="pencil" />
                        <mytags:deleteButton action="delete-user"
                                             entityId="${user.id}"
                                             icon="trash" />
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</div>

<mytags:pagination paginationOptions="${queryOptions.pagination}" />

<%@ include file="/WEB-INF/jspf/footer.jspf" %>
