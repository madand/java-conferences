<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/tld/mytl.tld" prefix="mytl" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="mytags"%>

<%@ attribute  name="conference" type="net.madand.conferences.entity.Conference" required="true" %>

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
