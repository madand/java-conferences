<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/tld/mytl.tld" prefix="mytl" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="mytags"%>

<%@ attribute  name="name"  required="true" %>
<%@ attribute  name="labelKey"  required="true" %>
<%@ attribute  name="required" type="java.lang.Boolean" %>
<%@ attribute  name="value" type="java.lang.Integer" required="true" %>

<c:set var="computedRequired" value="" />
<c:if test="${required}">
    <c:set var="computedRequired"  value="required" />
</c:if>

<div>
    <label for="${name}" class="form-label ${computedRequired}">
        <fmt:message key="${labelKey}"/>
    </label>

    <select id="${name}" name="${name}" class="form-select"
            ${computedRequired}>

        <c:forEach begin="10" end="300" step="10" var="minutes">
            <option value="${minutes}"
                    ${minutes == value ? " selected" : ""}>
                ${minutes}
                <fmt:message key="general.minutes"/>
            </option>
        </c:forEach>
    </select>
</div>
