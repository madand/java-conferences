<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@ attribute name="labelKey" required="true" %>
<%@ attribute name="btnClass" %>
<%@ attribute name="extraClasses" %>

<c:if test="${empty btnClass}">
    <c:set var="btnClass"  value="primary" />
</c:if>

<button type="submit" class="btn btn-${btnClass} ${extraClasses}">
    <fmt:message key="${labelKey}"/>
</button>
